package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0012H&J\b\u0010\u0013\u001a\u00020\u0014H&J\b\u0010\u0015\u001a\u00020\u0016H&\u00a8\u0006\u0018"}, d2 = {"Lcom/example/projek_map/data/KoperasiDatabase;", "Landroidx/room/RoomDatabase;", "()V", "adminDao", "Lcom/example/projek_map/data/AdminDao;", "buktiPembayaranAnggotaDao", "Lcom/example/projek_map/data/BuktiPembayaranAnggotaDao;", "historiPembayaranDao", "Lcom/example/projek_map/data/HistoriPembayaranDao;", "historiSimpananDao", "Lcom/example/projek_map/data/HistoriSimpananDao;", "kasTransaksiDao", "Lcom/example/projek_map/data/KasTransaksiDao;", "pengumumanDao", "Lcom/example/projek_map/data/PengumumanDao;", "pinjamanDao", "Lcom/example/projek_map/data/PinjamanDao;", "simpananDao", "Lcom/example/projek_map/data/SimpananDao;", "transaksiSimpananDao", "Lcom/example/projek_map/data/TransaksiSimpananDao;", "userDao", "Lcom/example/projek_map/data/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.projek_map.data.User.class, com.example.projek_map.data.Admin.class, com.example.projek_map.data.Pinjaman.class, com.example.projek_map.data.Simpanan.class, com.example.projek_map.data.HistoriPembayaran.class, com.example.projek_map.data.HistoriSimpanan.class, com.example.projek_map.data.BuktiPembayaranAnggota.class, com.example.projek_map.data.Pengumuman.class, com.example.projek_map.data.TransaksiSimpanan.class, com.example.projek_map.data.KasTransaksi.class}, version = 1, exportSchema = false)
public abstract class KoperasiDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.projek_map.data.KoperasiDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.projek_map.data.KoperasiDatabase.Companion Companion = null;
    
    public KoperasiDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.AdminDao adminDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.PinjamanDao pinjamanDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.SimpananDao simpananDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.HistoriPembayaranDao historiPembayaranDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.HistoriSimpananDao historiSimpananDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.BuktiPembayaranAnggotaDao buktiPembayaranAnggotaDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.TransaksiSimpananDao transaksiSimpananDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.KasTransaksiDao kasTransaksiDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.projek_map.data.PengumumanDao pengumumanDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/projek_map/data/KoperasiDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/projek_map/data/KoperasiDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.projek_map.data.KoperasiDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}