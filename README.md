**README – Aplikasi SimPin (Koperasi Simpan Pinjam)**

**NAMA, NIM, DAN TUGAS ANGGOTA KELOMPOK:**
1.	David Lin	(00000078135) :	Mengerjakan fitur User/Anggota, Mengimplementasikan native Android features (kamera/galeri dan internal storage), dan membantu Menerapkan arsitektur Model–View–ViewModel (MVVM).
   
3.	Hito Kawiswara (00000068541) :	Mengerjakan fitur Admin/Pengurus, membantu pengembangan bagian User, membantu mengimplementasikan native Android features (kamera/galeri dan internal storage).serta membuat Video Demo Aplikasi untuk UAS.
   
4.	Tommy Viriya Irawan	(00000089265) :	Mengerjakan fitur User/Anggota, membantu pengembangan Admin, Melakukan Migrasi dari data dummy ke Database MySQL online yang diakses melalui PHP REST API, Menerapkan arsitektur Model–View–ViewModel (MVVM) secara menyeluruh, dan menyusun Laporan.
   
6.	Yared Agustinus Mordechai	(00000089287) :	Mengerjakan fitur Admin/Pengurus, serta membantu bagian User, Membantu Migrasi dari data dummy ke MySQL Database dan Mengintegrasikan Machine Learning (Natural Language Processing) untuk klasifikasi teks transaksi.


**LOGIN CREDENTIALS (DUMMY ACCOUNT)**
Untuk memudahkan pengujian aplikasi tanpa perlu melakukan registrasi, berikut akun dummy yang telah disediakan:
- Admin / Pengurus:
1. Username: ADM001
   Password: admin123

2. Username: ADM002
   Password: admin123

- User / Anggota
1. Username: EMP001
   Password: 1234

2. Username: EMP002
   Password: 1234


**SPECIAL INSTRUCTIONS (PETUNJUK PENGUJIAN APLIKASI)**
1. Pastikan perangkat Android terhubung ke internet karena aplikasi menggunakan database online.
   
2. Gunakan akun dummy yang telah disediakan untuk langsung masuk ke aplikasi.
   
3. Untuk menguji fitur unggah bukti pembayaran:
   - Berikan izin kamera dan penyimpanan saat diminta.
   - Gunakan tombol Ambil Foto (Kamera) atau Pilih Gambar Bukti (Galeri).
     
4. Untuk menguji fitur Machine Learning:
   - Buka menu Smart ML SimPin.
   - Masukkan teks transaksi (contoh: “beli bakso bang udin”).
   - Tekan tombol Prediksi Sekarang dan lihat hasil klasifikasi.
     
5. Jika aplikasi pertama kali dijalankan dan permission belum diberikan, pastikan semua permission yang diminta diizinkan agar fitur berjalan dengan baik.


**BACKEND DAN DATABASE**
Aplikasi SimPin menggunakan backend berbasis PHP REST API dengan database MySQL yang di-host secara online.
- Base URL API:
   http://koperasidbmapkelompok6.cyou/koperasi_api
- Cek Koneksi API:
   http://koperasidbmapkelompok6.cyou/koperasi_api/ping.php
- Cek Koneksi Database:
   http://koperasidbmapkelompok6.cyou/koperasi_api/db_test.php

Total terdapat 18 tabel pada database yang digunakan untuk mendukung seluruh fitur aplikasi.


**DESKRIPSI SINGKAT APLIKASI**
  SimPin adalah aplikasi mobile berbasis Android yang dirancang untuk mensimulasikan sistem koperasi simpan pinjam secara digital.
Aplikasi ini dikembangkan sebagai proyek mata kuliah Mobile Application Programming, dengan tujuan untuk memberikan gambaran bagaimana pengelolaan data anggota,
transaksi simpanan, pengajuan pinjaman, serta laporan keuangan dapat dilakukan secara efisien dan transparan melalui perangkat mobile.

  Pada versi ini, SimPin menggunakan Database MySQL online yang diakses melalui PHP REST API.
Aplikasi memiliki dua role utama:
- Anggota Koperasi: dapat melihat simpanan, mengajukan pinjaman, melihat riwayat transaksi, dan menerima pengumuman.
- Pengurus Koperasi: dapat mengelola data anggota, mencatat simpanan, memproses pinjaman, mengirim pengumuman, serta menampilkan laporan keuangan koperasi.


**FITUR UTAMA APLIKASI
FITUR ANGGOTA KOPERASI (USER)**
- Login anggota
- Melihat saldo simpanan (pokok, wajib, sukarela)
- Melakukan transaksi simpanan
- Mengajukan pinjaman
- Melihat status dan rincian pinjaman
- Melihat riwayat transaksi simpanan dan angsuran
- Mengunggah bukti pembayaran menggunakan kamera atau galeri
- Menggunakan fitur Machine Learning untuk klasifikasi teks transaksi
- Menerima notifikasi pengumuman dan keputusan pinjaman

**FITUR PENGURUS KOPERASI (ADMIN)**
- Login admin
- Manajemen data anggota
- Verifikasi dan pengelolaan transaksi simpanan
- Persetujuan atau penolakan pengajuan pinjaman
- Pencatatan pembayaran angsuran
- Manajemen kas koperasi
- Pengelolaan pengumuman
- Melihat laporan keuangan koperasi

