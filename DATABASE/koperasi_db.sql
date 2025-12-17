-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 17, 2025 at 11:47 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

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
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Admin'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`kodePegawai`, `email`, `password`, `nama`, `role`) VALUES
('ADM001', 'admin@koperasi.com', 'admin123', 'Admin Utama', 'Admin'),
('ADM002', 'supervisor@koperasi.com', 'admin123', 'Supervisor Koperasi', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `bukti_pembayaran_anggota`
--

CREATE TABLE `bukti_pembayaran_anggota` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `uri` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bukti_pembayaran_anggota`
--

INSERT INTO `bukti_pembayaran_anggota` (`id`, `kodePegawai`, `uri`, `tanggal`) VALUES
(1, 'EMP002', 'content://com.example.projek_map.fileprovider/cache_images/bukti_6519085491984063085.jpg', '2025-12-17');

-- --------------------------------------------------------

--
-- Table structure for table `decision_notifications`
--

CREATE TABLE `decision_notifications` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pinjamanId` int NOT NULL,
  `decision` enum('disetujui','ditolak') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` int NOT NULL,
  `tanggal` date NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `decision_notifications`
--

INSERT INTO `decision_notifications` (`id`, `kodePegawai`, `pinjamanId`, `decision`, `jumlah`, `tanggal`, `is_read`) VALUES
(1, 'EMP001', 1, 'ditolak', 2000000, '2025-12-15', 1),
(2, 'EMP001', 6, 'ditolak', 10000000, '2025-12-15', 1),
(3, 'EMP001', 8, 'disetujui', 123456, '2025-12-15', 1),
(4, 'EMP001', 7, 'ditolak', 123456, '2025-12-15', 1),
(5, 'EMP002', 2, 'ditolak', 1500000, '2025-12-15', 1),
(6, 'EMP001', 9, 'disetujui', 410000, '2025-12-15', 1),
(7, 'EMP003', 5, 'ditolak', 1000000, '2025-12-15', 1),
(8, 'EMP003', 10, 'disetujui', 5000000, '2025-12-15', 1),
(9, 'EMP003', 11, 'disetujui', 1000000000, '2025-12-16', 1),
(10, 'EMP002', 15, 'disetujui', 1400000, '2025-12-17', 0),
(11, 'EMP003', 14, 'ditolak', 1100000, '2025-12-17', 0),
(12, 'EMP003', 13, 'disetujui', 11000000, '2025-12-17', 0),
(13, 'EMP001', 19, 'disetujui', 100000, '2025-12-17', 1),
(14, 'EMP001', 18, 'disetujui', 5000000, '2025-12-17', 1),
(15, 'EMP001', 17, 'ditolak', 10000000, '2025-12-17', 1);

-- --------------------------------------------------------

--
-- Table structure for table `due_reminders`
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
-- Table structure for table `histori_pembayaran`
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
-- Dumping data for table `histori_pembayaran`
--

INSERT INTO `histori_pembayaran` (`id`, `kodePegawai`, `pinjamanId`, `tanggal`, `jumlah`, `status`, `buktiPembayaranUri`) VALUES
(1, 'EMP001', 3, '2025-07-10', 200000, 'Lunas', NULL),
(2, 'EMP002', 4, '2025-08-10', 200000, 'Lunas', NULL),
(3, 'EMP001', 3, '2025-09-10', 200000, 'Belum Lunas', NULL),
(4, 'EMP002', 2, '2025-07-05', 250000, 'Belum Lunas', NULL),
(5, 'EMP001', 3, '2025-12-15', 120000, 'Dibayar (Admin)', NULL),
(6, 'EMP001', 9, '2025-12-16', 70340, 'Menunggu Verifikasi', 'content://com.example.projek_map.fileprovider/pictures/IMG_BUKTI_20251216_211842.jpg'),
(7, 'EMP003', 10, '2025-12-17', 15000, 'Dibayar (User)', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `histori_simpanan`
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
-- Dumping data for table `histori_simpanan`
--

INSERT INTO `histori_simpanan` (`id`, `kodePegawai`, `tanggal`, `jenis`, `jumlah`, `keterangan`) VALUES
(1, 'EMP001', '2025-07-01', 'Setoran Wajib', 200000.00, 'Setoran rutin Juli'),
(2, 'EMP001', '2025-08-01', 'Setoran Sukarela', 300000.00, 'Setoran tambahan Agustus'),
(3, 'EMP002', '2025-09-01', 'Penarikan Sukarela', -150000.00, 'Penarikan keperluan pribadi'),
(4, 'EMP001', '2025-12-14', 'Setoran Sukarela', 100000.00, '-'),
(5, 'EMP001', '2025-12-14', 'Setoran Sukarela', 15000.00, '-'),
(6, 'EMP001', '2025-12-15', 'Setoran Pokok', 100000.00, '-'),
(7, 'EMP002', '2025-12-15', 'Setoran Sukarela', 321123.00, '-'),
(8, 'EMP004', '2025-12-16', 'Setoran Pokok', 100000.00, '-');

-- --------------------------------------------------------

--
-- Table structure for table `kas_transaksi`
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
-- Dumping data for table `kas_transaksi`
--

INSERT INTO `kas_transaksi` (`id`, `tanggal`, `jenis`, `kategori`, `deskripsi`, `jumlah`) VALUES
(1, '2025-10-01', 'Masuk', 'Iuran Anggota', 'Setoran wajib Oktober', 500000.00),
(2, '2025-10-03', 'Keluar', 'Operasional', 'Beli ATK kantor', 120000.00),
(3, '2025-10-07', 'Masuk', 'Bunga Pinjaman', 'Bunga angsuran minggu 1', 300000.00),
(4, '2025-12-15', 'Keluar', 'Operasional', '', 51000.00);

-- --------------------------------------------------------

--
-- Table structure for table `pengaturan_koperasi`
--

CREATE TABLE `pengaturan_koperasi` (
  `id` tinyint NOT NULL DEFAULT '1',
  `sukuBungaPinjamanGlobal` decimal(6,4) NOT NULL DEFAULT '0.1000',
  `dendaKeterlambatanHarian` decimal(6,4) NOT NULL DEFAULT '0.0100'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pengumuman`
--

CREATE TABLE `pengumuman` (
  `id` int NOT NULL,
  `judul` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `isi` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengumuman`
--

INSERT INTO `pengumuman` (`id`, `judul`, `isi`, `tanggal`) VALUES
(1, 'Rapat Anggota Tahunan', 'RAT akan dilaksanakan di aula kantor pukul 09.00 WIB.', '2025-10-18'),
(2, 'Update Suku Bunga', 'Suku bunga pinjaman ditetapkan 1.2% flat efektif.', '2025-10-10'),
(3, 'Libur Operasional', 'Koperasi libur 28-29 Oktober untuk maintenance sistem.', '2025-10-28'),
(4, 'Tes', 'tes narik pengumuman database', '2025-12-15');

-- --------------------------------------------------------

--
-- Table structure for table `pinjaman`
--

CREATE TABLE `pinjaman` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` int NOT NULL,
  `tenor` int NOT NULL,
  `status` enum('Proses','Disetujui','Ditolak','Lunas','Aktif') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'Proses',
  `approved_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `approved_at` datetime DEFAULT NULL,
  `reject_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `bunga` decimal(6,4) NOT NULL DEFAULT '0.1000',
  `angsuranTerbayar` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pinjaman`
--

INSERT INTO `pinjaman` (`id`, `kodePegawai`, `jumlah`, `tenor`, `status`, `approved_by`, `approved_at`, `reject_reason`, `bunga`, `angsuranTerbayar`) VALUES
(1, 'EMP001', 2000000, 12, 'Ditolak', 'ADM001', '2025-12-15 04:31:40', NULL, 0.1000, 0),
(2, 'EMP002', 1500000, 6, 'Ditolak', 'ADM001', '2025-12-15 05:05:56', NULL, 0.1000, 0),
(3, 'EMP001', 3000000, 12, 'Disetujui', NULL, NULL, NULL, 0.1000, 100000),
(4, 'EMP002', 5500000, 6, 'Disetujui', NULL, NULL, NULL, 0.1000, 0),
(5, 'EMP003', 1000000, 12, 'Ditolak', 'ADM001', '2025-12-15 13:45:11', NULL, 0.1000, 0),
(6, 'EMP001', 10000000, 12, 'Ditolak', 'ADM001', '2025-12-15 04:41:41', NULL, 0.1000, 0),
(7, 'EMP001', 123456, 0, 'Ditolak', 'ADM001', '2025-12-15 05:02:47', NULL, 0.1000, 0),
(8, 'EMP001', 123456, 0, 'Disetujui', 'ADM001', '2025-12-15 05:02:45', NULL, 0.1000, 0),
(9, 'EMP001', 410000, 6, 'Disetujui', 'ADM001', '2025-12-15 13:45:07', NULL, 0.1000, 70340),
(10, 'EMP003', 5000000, 12, 'Disetujui', 'ADM001', '2025-12-15 14:47:19', NULL, 0.1000, 15000),
(11, 'EMP003', 1000000000, 12, 'Disetujui', 'ADM001', '2025-12-16 10:01:11', NULL, 0.1000, 0),
(12, 'EMP002', 1100000, 3, 'Disetujui', 'ADM001', '2025-12-17 08:16:25', NULL, 0.1000, 0),
(13, 'EMP003', 11000000, 2, 'Disetujui', 'ADM001', '2025-12-17 11:19:01', NULL, 0.1000, 0),
(14, 'EMP003', 1100000, 6, 'Ditolak', 'ADM001', '2025-12-17 11:18:48', NULL, 0.1000, 0),
(15, 'EMP002', 1400000, 7, 'Disetujui', 'ADM001', '2025-12-17 11:18:44', NULL, 0.1000, 0),
(16, 'EMP001', 1999000, 3, 'Ditolak', 'ADM001', '2025-12-17 11:01:46', NULL, 0.1000, 0),
(17, 'EMP001', 10000000, 2, 'Ditolak', 'ADM001', '2025-12-17 11:43:12', NULL, 0.1000, 0),
(18, 'EMP001', 5000000, 4, 'Disetujui', 'ADM001', '2025-12-17 11:43:09', NULL, 0.1000, 0),
(19, 'EMP001', 100000, 7, 'Disetujui', 'ADM001', '2025-12-17 11:43:04', NULL, 0.1000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pinjaman_angsuran_jadwal`
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
-- Table structure for table `pinjaman_jadwal`
--

CREATE TABLE `pinjaman_jadwal` (
  `id` int NOT NULL,
  `pinjaman_id` int NOT NULL,
  `periode_ke` int NOT NULL,
  `due_date` date NOT NULL,
  `jumlah` int NOT NULL,
  `status_bayar` enum('BELUM','LUNAS') DEFAULT 'BELUM',
  `tanggal_bayar` date DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pinjaman_rincian`
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

--
-- Dumping data for table `pinjaman_rincian`
--

INSERT INTO `pinjaman_rincian` (`pinjamanId`, `cicilanPerBulan`, `totalBunga`, `totalBayar`, `totalPokok`, `terbayar`, `sisaBayar`, `sisaPokok`, `angsuranDibayar`) VALUES
(12, 403333, 110000, 1210000, 1100000, 0, 1210000, 1100000, 0),
(16, 732967, 199900, 2198900, 1999000, 0, 2198900, 1999000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `id` int NOT NULL,
  `bungaPersen` double NOT NULL DEFAULT '10',
  `dendaPersenPerHari` double NOT NULL DEFAULT '1',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `bungaPersen`, `dendaPersenPerHari`, `updated_at`) VALUES
(1, 20, 2, '2025-12-15 11:49:55');

-- --------------------------------------------------------

--
-- Table structure for table `simpanan`
--

CREATE TABLE `simpanan` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `simpananPokok` decimal(15,2) NOT NULL DEFAULT '0.00',
  `simpananWajib` decimal(15,2) NOT NULL DEFAULT '0.00',
  `simpananSukarela` decimal(15,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `simpanan`
--

INSERT INTO `simpanan` (`kodePegawai`, `simpananPokok`, `simpananWajib`, `simpananSukarela`) VALUES
('EMP001', 600000.00, 200000.00, 415000.00),
('EMP002', 500000.00, 250000.00, 471123.00),
('EMP003', 0.00, 0.00, 0.00),
('EMP004', 100000.00, 0.00, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `simpanan_pending`
--

CREATE TABLE `simpanan_pending` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) NOT NULL,
  `jenisInput` varchar(100) NOT NULL,
  `jumlah` decimal(15,2) NOT NULL,
  `keterangan` text,
  `tanggal` date NOT NULL,
  `statusVerifikasi` varchar(50) NOT NULL DEFAULT 'Menunggu Verifikasi',
  `buktiUrl` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `simpanan_pending`
--

INSERT INTO `simpanan_pending` (`id`, `kodePegawai`, `jenisInput`, `jumlah`, `keterangan`, `tanggal`, `statusVerifikasi`, `buktiUrl`) VALUES
(1, 'EMP001', 'Simpanan Sukarela', 100000.00, '-', '2025-12-17', 'Menunggu Verifikasi', 'http://10.0.2.2/koperasi_api/uploads/simpanan_20251216_181442_55b53553.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_simpanan`
--

CREATE TABLE `transaksi_simpanan` (
  `id` int NOT NULL,
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jenis` enum('Pokok','Wajib','Sukarela') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah` decimal(15,2) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_simpanan`
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
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `kodePegawai` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `statusKeanggotaan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`kodePegawai`, `email`, `password`, `nama`, `statusKeanggotaan`) VALUES
('EMP001', 'pegawai1@koperasi.com', '1234', 'Budi Santos', 'Anggota Aktif'),
('EMP002', 'pegawai2@koperasi.com', '1234', 'Siti Aminah', 'Anggota Aktif'),
('EMP003', 'tommy@example.com', '1234', 'Tommy Viriya Irawan', 'Anggota Aktif'),
('EMP004', 'david@example.com', '1234', 'David Lin', 'Anggota Aktif'),
('EMP005', 'kevynsus@gmail.com', '1234', 'Kevyn Susanto', 'Anggota Aktif');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`kodePegawai`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bukti_kodePegawai` (`kodePegawai`);

--
-- Indexes for table `decision_notifications`
--
ALTER TABLE `decision_notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_decision_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_decision_pinjamanId` (`pinjamanId`);

--
-- Indexes for table `due_reminders`
--
ALTER TABLE `due_reminders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_due_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_due_pinjamanId` (`pinjamanId`),
  ADD KEY `idx_due_dueDate` (`dueDate`);

--
-- Indexes for table `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_histpay_kodePegawai` (`kodePegawai`),
  ADD KEY `idx_histpay_pinjamanId` (`pinjamanId`);

--
-- Indexes for table `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_histsimp_kodePegawai` (`kodePegawai`);

--
-- Indexes for table `kas_transaksi`
--
ALTER TABLE `kas_transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_kas_tanggal` (`tanggal`);

--
-- Indexes for table `pengaturan_koperasi`
--
ALTER TABLE `pengaturan_koperasi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pengumuman`
--
ALTER TABLE `pengumuman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pengumuman_tanggal` (`tanggal`);

--
-- Indexes for table `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pinjaman_kodePegawai` (`kodePegawai`);

--
-- Indexes for table `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_jadwal_pinjamanId` (`pinjamanId`);

--
-- Indexes for table `pinjaman_jadwal`
--
ALTER TABLE `pinjaman_jadwal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pinjaman_id` (`pinjaman_id`);

--
-- Indexes for table `pinjaman_rincian`
--
ALTER TABLE `pinjaman_rincian`
  ADD PRIMARY KEY (`pinjamanId`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `simpanan`
--
ALTER TABLE `simpanan`
  ADD PRIMARY KEY (`kodePegawai`);

--
-- Indexes for table `simpanan_pending`
--
ALTER TABLE `simpanan_pending`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_transsimp_kodePegawai` (`kodePegawai`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`kodePegawai`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `decision_notifications`
--
ALTER TABLE `decision_notifications`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `due_reminders`
--
ALTER TABLE `due_reminders`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `kas_transaksi`
--
ALTER TABLE `kas_transaksi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pengumuman`
--
ALTER TABLE `pengumuman`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pinjaman`
--
ALTER TABLE `pinjaman`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pinjaman_jadwal`
--
ALTER TABLE `pinjaman_jadwal`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `simpanan_pending`
--
ALTER TABLE `simpanan_pending`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bukti_pembayaran_anggota`
--
ALTER TABLE `bukti_pembayaran_anggota`
  ADD CONSTRAINT `fk_bukti_anggota_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `decision_notifications`
--
ALTER TABLE `decision_notifications`
  ADD CONSTRAINT `fk_decision_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_decision_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `due_reminders`
--
ALTER TABLE `due_reminders`
  ADD CONSTRAINT `fk_due_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_due_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `histori_pembayaran`
--
ALTER TABLE `histori_pembayaran`
  ADD CONSTRAINT `fk_histpay_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_histpay_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `histori_simpanan`
--
ALTER TABLE `histori_simpanan`
  ADD CONSTRAINT `fk_histsimp_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD CONSTRAINT `fk_pinjaman_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `pinjaman_angsuran_jadwal`
--
ALTER TABLE `pinjaman_angsuran_jadwal`
  ADD CONSTRAINT `fk_jadwal_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `pinjaman_jadwal`
--
ALTER TABLE `pinjaman_jadwal`
  ADD CONSTRAINT `pinjaman_jadwal_ibfk_1` FOREIGN KEY (`pinjaman_id`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pinjaman_rincian`
--
ALTER TABLE `pinjaman_rincian`
  ADD CONSTRAINT `fk_rincian_pinjaman` FOREIGN KEY (`pinjamanId`) REFERENCES `pinjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `simpanan`
--
ALTER TABLE `simpanan`
  ADD CONSTRAINT `fk_simpanan_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaksi_simpanan`
--
ALTER TABLE `transaksi_simpanan`
  ADD CONSTRAINT `fk_transsimp_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
