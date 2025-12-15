-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 14 Des 2025 pada 16.57
-- Versi server: 8.0.30
-- Versi PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `koperasi_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admins`
--

CREATE TABLE `admins` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Admin'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admins`
--

INSERT INTO `admins` (`kodePegawai`, `email`, `password`, `nama`, `role`) VALUES
('ADM001', 'admin@koperasi.com', 'admin123', 'Admin Utama', 'Admin'),
('ADM002', 'supervisor@koperasi.com', 'admin123', 'Supervisor Koperasi', 'Admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `bukti_pembayaran_anggota`
--

CREATE TABLE `bukti_pembayaran_anggota` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `uri` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `decision_notifications`
--

CREATE TABLE `decision_notifications` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pinjamanId` int NOT NULL,
  `decision` enum('disetujui','ditolak') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` int NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `due_reminders`
--

CREATE TABLE `due_reminders` (
  `id` int NOT NULL,
  `pinjamanId` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dueDate` date NOT NULL,
  `nominalCicilan` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `histori_pembayaran`
--

CREATE TABLE `histori_pembayaran` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pinjamanId` int NOT NULL,
  `tanggal` date NOT NULL,
  `jumlah` int NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `buktiPembayaranUri` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `histori_pembayaran`
--

INSERT INTO `histori_pembayaran` (`id`, `kodePegawai`, `pinjamanId`, `tanggal`, `jumlah`, `status`, `buktiPembayaranUri`) VALUES
(1, 'EMP001', 3, '2025-07-10', 200000, 'Lunas', NULL),
(2, 'EMP002', 4, '2025-08-10', 200000, 'Lunas', NULL),
(3, 'EMP001', 3, '2025-09-10', 200000, 'Belum Lunas', NULL),
(4, 'EMP002', 2, '2025-07-05', 250000, 'Belum Lunas', NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `histori_simpanan`
--

CREATE TABLE `histori_simpanan` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal` date NOT NULL,
  `jenis` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` decimal(15,2) NOT NULL,
  `keterangan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `histori_simpanan`
--

INSERT INTO `histori_simpanan` (`id`, `kodePegawai`, `tanggal`, `jenis`, `jumlah`, `keterangan`) VALUES
(1, 'EMP001', '2025-07-01', 'Setoran Wajib', 200000.00, 'Setoran rutin Juli'),
(2, 'EMP001', '2025-08-01', 'Setoran Sukarela', 300000.00, 'Setoran tambahan Agustus'),
(3, 'EMP002', '2025-09-01', 'Penarikan Sukarela', -150000.00, 'Penarikan keperluan pribadi'),
(4, 'EMP001', '2025-12-14', 'Setoran Sukarela', 100000.00, '-'),
(5, 'EMP001', '2025-12-14', 'Setoran Sukarela', 15000.00, '-');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kas_transaksi`
--

CREATE TABLE `kas_transaksi` (
  `id` int NOT NULL,
  `tanggal` date NOT NULL,
  `jenis` enum('Masuk','Keluar') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `kategori` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `deskripsi` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kas_transaksi`
--

INSERT INTO `kas_transaksi` (`id`, `tanggal`, `jenis`, `kategori`, `deskripsi`, `jumlah`) VALUES
(1, '2025-10-01', 'Masuk', 'Iuran Anggota', 'Setoran wajib Oktober', 500000.00),
(2, '2025-10-03', 'Keluar', 'Operasional', 'Beli ATK kantor', 120000.00),
(3, '2025-10-07', 'Masuk', 'Bunga Pinjaman', 'Bunga angsuran minggu 1', 300000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengaturan_koperasi`
--

CREATE TABLE `pengaturan_koperasi` (
  `id` tinyint NOT NULL DEFAULT '1',
  `sukuBungaPinjamanGlobal` decimal(6,4) NOT NULL DEFAULT '0.1000',
  `dendaKeterlambatanHarian` decimal(6,4) NOT NULL DEFAULT '0.0100'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengaturan_koperasi`
--

INSERT INTO `pengaturan_koperasi` (`id`, `sukuBungaPinjamanGlobal`, `dendaKeterlambatanHarian`) VALUES
(1, 0.1000, 0.0100);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengumuman`
--

CREATE TABLE `pengumuman` (
  `id` int NOT NULL,
  `judul` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `isi` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengumuman`
--

INSERT INTO `pengumuman` (`id`, `judul`, `isi`, `tanggal`) VALUES
(1, 'Rapat Anggota Tahunan', 'RAT akan dilaksanakan di aula kantor pukul 09.00 WIB.', '2025-10-18'),
(2, 'Update Suku Bunga', 'Suku bunga pinjaman ditetapkan 1.2% flat efektif.', '2025-10-10'),
(3, 'Libur Operasional', 'Koperasi libur 28-29 Oktober untuk maintenance sistem.', '2025-10-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pinjaman`
--

CREATE TABLE `pinjaman` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` int NOT NULL,
  `tenor` int NOT NULL,
  `status` enum('Proses','Disetujui','Ditolak','Lunas','Aktif') COLLATE utf8mb4_general_ci DEFAULT 'Proses',
  `approved_by` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `approved_at` datetime DEFAULT NULL,
  `reject_reason` text COLLATE utf8mb4_general_ci,
  `bunga` decimal(6,4) NOT NULL DEFAULT '0.1000',
  `angsuranTerbayar` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pinjaman`
--

INSERT INTO `pinjaman` (`id`, `kodePegawai`, `jumlah`, `tenor`, `status`, `approved_by`, `approved_at`, `reject_reason`, `bunga`, `angsuranTerbayar`) VALUES
(1, 'EMP001', 2000000, 12, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(2, 'EMP002', 1500000, 6, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(3, 'EMP001', 3000000, 12, 'Disetujui', NULL, NULL, NULL, 0.1000, 0),
(4, 'EMP002', 5500000, 6, 'Disetujui', NULL, NULL, NULL, 0.1000, 0),
(5, 'EMP003', 1000000, 12, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(6, 'EMP001', 10000000, 12, 'Proses', NULL, NULL, NULL, 0.1000, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pinjaman_angsuran_jadwal`
--

CREATE TABLE `pinjaman_angsuran_jadwal` (
  `id` int NOT NULL,
  `pinjamanId` int NOT NULL,
  `periode` int NOT NULL,
  `pokok` int NOT NULL,
  `bunga` int NOT NULL,
  `total` int NOT NULL,
  `sisaPokok` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pinjaman_rincian`
--

CREATE TABLE `pinjaman_rincian` (
  `pinjamanId` int NOT NULL,
  `cicilanPerBulan` int NOT NULL,
  `totalBunga` int NOT NULL,
  `totalBayar` int NOT NULL,
  `totalPokok` int NOT NULL,
  `terbayar` int NOT NULL,
  `sisaBayar` int NOT NULL,
  `sisaPokok` int NOT NULL,
  `angsuranDibayar` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `simpanan`
--

CREATE TABLE `simpanan` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `simpananPokok` decimal(15,2) NOT NULL DEFAULT '0.00',
  `simpananWajib` decimal(15,2) NOT NULL DEFAULT '0.00',
  `simpananSukarela` decimal(15,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `simpanan`
--

INSERT INTO `simpanan` (`kodePegawai`, `simpananPokok`, `simpananWajib`, `simpananSukarela`) VALUES
('EMP001', 500000.00, 200000.00, 415000.00),
('EMP002', 500000.00, 250000.00, 150000.00),
('EMP003', 0.00, 0.00, 0.00),
('EMP004', 0.00, 0.00, 0.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi_simpanan`
--

CREATE TABLE `transaksi_simpanan` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jenis` enum('Pokok','Wajib','Sukarela') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` decimal(15,2) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksi_simpanan`
--

INSERT INTO `transaksi_simpanan` (`id`, `kodePegawai`, `jenis`, `jumlah`, `tanggal`) VALUES
(1, 'EMP001', 'Pokok', 500000.00, '2025-06-01'),
(2, 'EMP001', 'Wajib', 200000.00, '2025-07-01'),
(3, 'EMP001', 'Sukarela', 300000.00, '2025-08-01'),
(4, 'EMP002', 'Pokok', 500000.00, '2025-06-02'),
(5, 'EMP002', 'Wajib', 250000.00, '2025-08-05'),
(6, 'EMP002', 'Sukarela', 300000.00, '2025-08-10');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `statusKeanggotaan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`kodePegawai`, `email`, `password`, `nama`, `statusKeanggotaan`) VALUES
('EMP001', 'pegawai1@koperasi.com', '1234', 'Budi Santoso', 'Anggota Aktif'),
('EMP002', 'pegawai2@koperasi.com', '1234', 'Siti Aminah', 'Anggota Tidak Aktif'),
('EMP003', 'tommy@example.com', '1234', 'Tommy Viriya Irawan', 'Anggota Aktif'),
('EMP004', 'david@example.com', '1234', 'David Lin', 'Anggota Aktif');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`kodePegawai`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bukti_kodePegawai` (`kodePegawai`);

--
-- Indeks untuk tabel `decision_notifications`
--
ALTER TABLE `decision_notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_decision_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_decision_pinjamanId` (`pinjamanId`);

--
-- Indeks untuk tabel `due_reminders`
--
ALTER TABLE `due_reminders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_due_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_due_pinjamanId` (`pinjamanId`),
  ADD KEY `idx_due_dueDate` (`dueDate`);

--
-- Indeks untuk tabel `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_histpay_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_histpay_pinjamanId` (`pinjamanId`);

--
-- Indeks untuk tabel `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_histsimp_kodePegawai` (`kodePegawai`);

--
-- Indeks untuk tabel `kas_transaksi`
--
ALTER TABLE `kas_transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_kas_tanggal` (`tanggal`);

--
-- Indeks untuk tabel `pengaturan_koperasi`
--
ALTER TABLE `pengaturan_koperasi`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `pengumuman`
--
ALTER TABLE `pengumuman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pengumuman_tanggal` (`tanggal`);

--
-- Indeks untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pinjaman_kodePegawai` (`kodePegawai`);

--
-- Indeks untuk tabel `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_jadwal_pinjamanId` (`pinjamanId`);

--
-- Indeks untuk tabel `pinjaman_rincian`
--
ALTER TABLE `pinjaman_rincian`
  ADD PRIMARY KEY (`pinjamanId`);

--
-- Indeks untuk tabel `simpanan`
--
ALTER TABLE `simpanan`
  ADD PRIMARY KEY (`kodePegawai`);

--
-- Indeks untuk tabel `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_transsimp_kodePegawai` (`kodePegawai`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`kodePegawai`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `decision_notifications`
--
ALTER TABLE `decision_notifications`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `due_reminders`
--
ALTER TABLE `due_reminders`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `kas_transaksi`
--
ALTER TABLE `kas_transaksi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `pengumuman`
--
ALTER TABLE `pengumuman`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  ADD CONSTRAINT `fk_bukti_anggota_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `decision_notifications`
--
ALTER TABLE `decision_notifications`
  ADD CONSTRAINT `fk_decision_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_decision_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `due_reminders`
--
ALTER TABLE `due_reminders`
  ADD CONSTRAINT `fk_due_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_due_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  ADD CONSTRAINT `fk_histpay_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_histpay_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  ADD CONSTRAINT `fk_histsimp_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD CONSTRAINT `fk_pinjaman_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  ADD CONSTRAINT `fk_jadwal_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pinjaman_rincian`
--
ALTER TABLE `pinjaman_rincian`
  ADD CONSTRAINT `fk_rincian_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `simpanan`
--
ALTER TABLE `simpanan`
  ADD CONSTRAINT `fk_simpanan_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  ADD CONSTRAINT `fk_transsimp_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
