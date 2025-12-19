  <?php
  require 'config.php';
  header('Content-Type: application/json; charset=UTF-8');

  $raw = file_get_contents("php://input");
  $body = json_decode($raw, true);
  if (!is_array($body)) send_json(["success"=>false,"message"=>"Body JSON tidak valid"], 200);

  $id = (int)($body["id"] ?? 0);
  $action = strtolower(trim($body["action"] ?? "")); // "approve" / "reject"

  if ($id <= 0 || !in_array($action, ["approve","reject"])) {
    send_json(["success"=>false,"message"=>"Parameter tidak valid"], 200);
  }

  try {
    $conn->begin_transaction();

    $stmt = $conn->prepare("SELECT * FROM simpanan_pending WHERE id=? FOR UPDATE");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $p = $stmt->get_result()->fetch_assoc();

    if (!$p) {
      $conn->rollback();
      send_json(["success"=>false,"message"=>"Data pending tidak ditemukan"], 200);
    }

    if ($p["statusVerifikasi"] !== "Menunggu Verifikasi") {
      $conn->rollback();
      send_json(["success"=>false,"message"=>"Data ini sudah diproses"], 200);
    }

    if ($action === "reject") {
      $stmt = $conn->prepare("UPDATE simpanan_pending SET statusVerifikasi='Ditolak' WHERE id=?");
      $stmt->bind_param("i", $id);
      $stmt->execute();
      $conn->commit();
      send_json(["success"=>true,"message"=>"Ditolak","data"=>true], 200);
    }

    // APPROVE → update saldo + insert histori_simpanan + set status
    $kodePegawai = $p["kodePegawai"];
    $jenisInput  = $p["jenisInput"];
    $jumlah      = (float)$p["jumlah"];
    $keterangan  = $p["keterangan"] ?? "-";

    $col = "simpananSukarela";
    $label = "Setoran Sukarela";
    if (stripos($jenisInput, "Pokok") !== false) { $col = "simpananPokok"; $label = "Setoran Pokok"; }
    else if (stripos($jenisInput, "Wajib") !== false) { $col = "simpananWajib"; $label = "Setoran Wajib"; }

    // pastikan row simpanan ada
    $stmt = $conn->prepare("SELECT kodePegawai FROM simpanan WHERE kodePegawai=? LIMIT 1");
    $stmt->bind_param("s", $kodePegawai);
    $stmt->execute();
    $exists = $stmt->get_result()->fetch_assoc();

    if (!$exists) {
      $stmt = $conn->prepare("INSERT INTO simpanan (kodePegawai, simpananPokok, simpananWajib, simpananSukarela) VALUES (?,0,0,0)");
      $stmt->bind_param("s", $kodePegawai);
      $stmt->execute();
    }

    $stmt = $conn->prepare("UPDATE simpanan SET $col = $col + ? WHERE kodePegawai=?");
    $stmt->bind_param("ds", $jumlah, $kodePegawai);
    $stmt->execute();

    $stmt = $conn->prepare("
      INSERT INTO histori_simpanan (kodePegawai, tanggal, jenis, jumlah, keterangan)
      VALUES (?, CURDATE(), ?, ?, ?)
    ");
    $stmt->bind_param("ssds", $kodePegawai, $label, $jumlah, $keterangan);
    $stmt->execute();

    $stmt = $conn->prepare("UPDATE simpanan_pending SET statusVerifikasi='Terverifikasi' WHERE id=?");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    $conn->commit();
    send_json(["success"=>true,"message"=>"Terverifikasi","data"=>true], 200);

  } catch (Exception $e) {
    $conn->rollback();
    send_json(["success"=>false,"message"=>$e->getMessage()], 200);
  }
