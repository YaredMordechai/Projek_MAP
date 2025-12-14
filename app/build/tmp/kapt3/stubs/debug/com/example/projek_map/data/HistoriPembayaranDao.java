package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\t\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\fH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u0019\u001a\u00020\u00032\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001e"}, d2 = {"Lcom/example/projek_map/data/HistoriPembayaranDao;", "", "deleteHistori", "", "histori", "Lcom/example/projek_map/data/HistoriPembayaran;", "(Lcom/example/projek_map/data/HistoriPembayaran;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllHistori", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHistoriByPinjaman", "pinjamanId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHistoriByUser", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHistoriWithBuktiByUser", "getMaxId", "getTotalAngsuranBulanan", "", "bulanPattern", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalTerbayarForPinjaman", "insertAllHistori", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertHistori", "updateHistori", "app_debug"})
@androidx.room.Dao()
public abstract interface HistoriPembayaranDao {
    
    @androidx.room.Query(value = "SELECT * FROM histori_pembayaran ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllHistori(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriPembayaran>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM histori_pembayaran WHERE pinjamanId = :pinjamanId ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHistoriByPinjaman(int pinjamanId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriPembayaran>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM histori_pembayaran WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHistoriByUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriPembayaran>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM histori_pembayaran WHERE kodePegawai = :kode AND buktiPembayaranUri IS NOT NULL ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHistoriWithBuktiByUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriPembayaran>> $completion);
    
    @androidx.room.Query(value = "SELECT COALESCE(SUM(jumlah), 0) FROM histori_pembayaran WHERE pinjamanId = :pinjamanId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalTerbayarForPinjaman(int pinjamanId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COALESCE(CAST(SUM(jumlah) AS REAL), 0.0) \n        FROM histori_pembayaran \n        WHERE kodePegawai = :kode AND tanggal LIKE :bulanPattern\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalAngsuranBulanan(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    java.lang.String bulanPattern, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM histori_pembayaran")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertHistori(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.HistoriPembayaran histori, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllHistori(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.HistoriPembayaran> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateHistori(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.HistoriPembayaran histori, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteHistori(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.HistoriPembayaran histori, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}