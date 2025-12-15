-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 15 Des 2025 pada 04.52
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
(1, 'EMP001', 2000000, 12, 'Ditolak', 'ADM001', '2025-12-15 04:31:40', NULL, 0.1000, 0),
(2, 'EMP002', 1500000, 6, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(3, 'EMP001', 3000000, 12, 'Disetujui', NULL, NULL, NULL, 0.1000, 0),
(4, 'EMP002', 5500000, 6, 'Disetujui', NULL, NULL, NULL, 0.1000, 0),
(5, 'EMP003', 1000000, 12, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(6, 'EMP001', 10000000, 12, 'Ditolak', 'ADM001', '2025-12-15 04:41:41', NULL, 0.1000, 0),
(7, 'EMP001', 123456, 0, 'Proses', NULL, NULL, NULL, 0.1000, 0),
(8, 'EMP001', 123456, 0, 'Proses', NULL, NULL, NULL, 0.1000, 0);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pinjaman_kodePegawai` (`kodePegawai`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `pinjaman`
--
ALTER TABLE `pinjaman`
  ADD CONSTRAINT `fk_pinjaman_user` FOREIGN KEY (`kodePegawai`) REFERENCES `users` (`kodePegawai`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
