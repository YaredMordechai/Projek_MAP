<?php
require_once "config.php";

try {
    $sql = "SELECT id, DATE_FORMAT(tanggal, '%Y-%m-%d') AS tanggal,
                   jenis, kategori, deskripsi, jumlah
            FROM kas_transaksi
            ORDER BY tanggal DESC, id DESC";
    $res = $conn->query($sql);
    if (!$res) send_json(["success"=>false,"message"=>$conn->error], 500);

    $rows = [];
    while ($r = $res->fetch_assoc()) {
        // jumlah decimal → pastikan numeric (Android Double)
        $r["jumlah"] = (double)$r["jumlah"];
        $rows[] = $r;
    }

    send_json(["success"=>true,"data"=>$rows], 200);
} catch (Throwable $e) {
    send_json(["success"=>false,"message"=>$e->getMessage()], 500);
}
