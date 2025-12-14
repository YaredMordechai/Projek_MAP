package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u0011\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u0015\u001a\u00020\u00032\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010 \u00a8\u0006!"}, d2 = {"Lcom/example/projek_map/data/PinjamanDao;", "", "deletePinjaman", "", "pinjaman", "Lcom/example/projek_map/data/Pinjaman;", "(Lcom/example/projek_map/data/Pinjaman;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPinjaman", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMaxId", "", "getPinjamanAktif", "getPinjamanById", "id", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPinjamanForUser", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPinjamanLunas", "insertAllPinjaman", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPinjaman", "updateBungaUntukPinjamanAktif", "bunga", "", "(DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updatePinjaman", "updateStatus", "status", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface PinjamanDao {
    
    @androidx.room.Query(value = "SELECT * FROM pinjaman ORDER BY id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllPinjaman(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Pinjaman>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM pinjaman WHERE kodePegawai = :kode ORDER BY id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPinjamanForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Pinjaman>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM pinjaman WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPinjamanById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.projek_map.data.Pinjaman> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM pinjaman WHERE status = \'Disetujui\' OR status = \'Aktif\'")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPinjamanAktif(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Pinjaman>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM pinjaman WHERE status = \'Lunas\'")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPinjamanLunas(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Pinjaman>> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM pinjaman")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertPinjaman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pinjaman pinjaman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllPinjaman(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.Pinjaman> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updatePinjaman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pinjaman pinjaman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePinjaman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pinjaman pinjaman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE pinjaman SET status = :status WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateStatus(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE pinjaman SET bunga = :bunga WHERE status = \'Proses\' OR status = \'Disetujui\'")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBungaUntukPinjamanAktif(double bunga, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}