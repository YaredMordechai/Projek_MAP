<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$raw = file_get_contents("php://input");
$body = json_decode($raw, true);

if (!$body) {
  echo json_encode(["success" => false, "message" => "Invalid JSON body"]);
  exit;
}

$pinjamanId = isset($body["pinjamanId"]) ? intval($body["pinjamanId"]) : 0;
$decision   = isset($body["decision"]) ? strtolower(trim($body["decision"])) : "";
$adminKode  = isset($body["adminKode"]) ? trim($body["adminKode"]) : "";

if ($pinjamanId <= 0 || $adminKode === "" || !in_array($decision, ["approve", "reject"])) {
  echo json_encode(["success" => false, "message" => "Missing/invalid fields"]);
  exit;
}

$newStatus = ($decision === "approve") ? "Disetujui" : "Ditolak";
$now = date("Y-m-d H:i:s");

try {
  $dbRes = $conn->query("SELECT DATABASE() AS db");
  $dbRow = $dbRes->fetch_assoc();
  $activeDb = $dbRow["db"] ?? "(unknown)";

  // ambil data pinjaman
  $check = $conn->prepare("SELECT id, kodePegawai, jumlah, tenor, status, bunga, angsuranTerbayar FROM pinjaman WHERE id = ?");
  $check->bind_param("i", $pinjamanId);
  $check->execute();
  $res = $check->get_result();

  if ($res->num_rows === 0) {
    echo json_encode(["success" => false, "message" => "Pinjaman tidak ditemukan", "db" => $activeDb]);
    exit;
  }

  $row = $res->fetch_assoc();
  $kodePegawaiUser = $row["kodePegawai"];
  $jumlahPinjaman  = intval($row["jumlah"]);
  $tenor           = intval($row["tenor"]);
  $statusLama      = $row["status"];
  $bungaRate       = floatval($row["bunga"]);
  $terbayarNow     = intval($row["angsuranTerbayar"]);

  // update status + audit
  $stmt = $conn->prepare("
    UPDATE pinjaman
    SET status = ?, approved_by = ?, approved_at = ?
    WHERE id = ?
  ");
  $stmt->bind_param("sssi", $newStatus, $adminKode, $now, $pinjamanId);
  $stmt->execute();

  // kalau approve dan sebelumnya belum Disetujui -> generate rincian & jadwal
  $shouldGenerate = ($decision === "approve" && strtolower($statusLama) !== "disetujui");

  if ($shouldGenerate) {
    if ($tenor <= 0) {
      echo json_encode(["success"=>false, "message"=>"Tenor pinjaman tidak valid (<=0). Tidak bisa generate jadwal."]);
      exit;
    }

    if ($bungaRate <= 0) {
      // fallback ke settings (bungaPersen)
      $s = $conn->query("SELECT bungaPersen FROM settings WHERE id=1 LIMIT 1");
      if ($s && ($sr = $s->fetch_assoc())) {
        $bungaRate = floatval($sr["bungaPersen"]) / 100.0;
      } else {
        $bungaRate = 0.10; // fallback terakhir
      }
    }

    $totalBunga   = (int) round($jumlahPinjaman * $bungaRate);
    $totalBayar   = (int) round($jumlahPinjaman + $totalBunga);
    $cicilanBulan = (int) max(1, round($totalBayar / $tenor));

    $angsuranDibayar = (int) floor($terbayarNow / $cicilanBulan);
    if ($angsuranDibayar > $tenor) $angsuranDibayar = $tenor;

    $sisaBayar = $totalBayar - $terbayarNow;
    if ($sisaBayar < 0) $sisaBayar = 0;

    // Simpan/replace rincian
    $stmtR = $conn->prepare("
      INSERT INTO pinjaman_rincian
      (pinjamanId, cicilanPerBulan, totalBunga, totalBayar, totalPokok, terbayar, sisaBayar, sisaPokok, angsuranDibayar)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
      ON DUPLICATE KEY UPDATE
        cicilanPerBulan=VALUES(cicilanPerBulan),
        totalBunga=VALUES(totalBunga),
        totalBayar=VALUES(totalBayar),
        totalPokok=VALUES(totalPokok),
        terbayar=VALUES(terbayar),
        sisaBayar=VALUES(sisaBayar),
        sisaPokok=VALUES(sisaPokok),
        angsuranDibayar=VALUES(angsuranDibayar)
    ");

    // sisaPokok dihitung sederhana dari proporsi pokok (bukan amortisasi kompleks)
    $pokokTerbayar = (int) min($jumlahPinjaman, round($terbayarNow * ($jumlahPinjaman / max(1, $totalBayar))));
    $sisaPokok = $jumlahPinjaman - $pokokTerbayar;
    if ($sisaPokok < 0) $sisaPokok = 0;

    $stmtR->bind_param(
      "iiiiiiiii",
      $pinjamanId,
      $cicilanBulan,
      $totalBunga,
      $totalBayar,
      $jumlahPinjaman,
      $terbayarNow,
      $sisaBayar,
      $sisaPokok,
      $angsuranDibayar
    );
    $stmtR->execute();

    // Bersihkan jadwal lama lalu generate ulang
    $del = $conn->prepare("DELETE FROM pinjaman_angsuran_jadwal WHERE pinjamanId=?");
    $del->bind_param("i", $pinjamanId);
    $del->execute();

    $startDate = date("Y-m-d"); // base dueDate: hari approve
    $sisaPokokRun = $jumlahPinjaman;

    // Bagi totalBunga rata per bulan (biar totalnya pas, sisanya taruh di bulan terakhir)
    $bungaPerBulan = (int) floor($totalBunga / $tenor);
    $sisaBunga = $totalBunga - ($bungaPerBulan * $tenor);

    // Bagi pokok rata per bulan (sisanya taruh di bulan terakhir)
    $pokokPerBulan = (int) floor($jumlahPinjaman / $tenor);
    $sisaPokokRemainder = $jumlahPinjaman - ($pokokPerBulan * $tenor);

    $ins = $conn->prepare("
      INSERT INTO pinjaman_angsuran_jadwal (pinjamanId, periode, dueDate, pokok, bunga, total, sisaPokok, isPaid, paidAt)
      VALUES (?, ?, ?, ?, ?, ?, ?, 0, NULL)
    ");

    for ($p = 1; $p <= $tenor; $p++) {
      $pokokP = $pokokPerBulan + (($p === $tenor) ? $sisaPokokRemainder : 0);
      $bungaP = $bungaPerBulan + (($p === $tenor) ? $sisaBunga : 0);
      $totalP = $pokokP + $bungaP;

      $sisaPokokRun -= $pokokP;
      if ($sisaPokokRun < 0) $sisaPokokRun = 0;

      // dueDate tiap bulan (p bulan dari startDate)
      $due = date("Y-m-d", strtotime("+".($p)." month", strtotime($startDate)));

      $ins->bind_param("isiiiii", $pinjamanId, $p, $due, $pokokP, $bungaP, $totalP, $sisaPokokRun);
      $ins->execute();
    }
  }

  echo json_encode([
    "success" => true,
    "message" => "Decision updated",
    "data" => [
      "pinjamanId" => $pinjamanId,
      "kodePegawai" => $kodePegawaiUser,
      "statusLama" => $statusLama,
      "statusBaru" => $newStatus,
      "generated" => $shouldGenerate
    ],
    "db" => $activeDb
  ]);

} catch (Throwable $e) {
  echo json_encode(["success" => false, "message" => $e->getMessage()]);
}
