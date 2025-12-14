package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0014"}, d2 = {"Lcom/example/projek_map/data/TransaksiSimpananDao;", "", "deleteTransaksi", "", "item", "Lcom/example/projek_map/data/TransaksiSimpanan;", "(Lcom/example/projek_map/data/TransaksiSimpanan;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTransaksi", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMaxId", "", "getTransaksiForUser", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAllTransaksi", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTransaksi", "app_debug"})
@androidx.room.Dao()
public abstract interface TransaksiSimpananDao {
    
    @androidx.room.Query(value = "SELECT * FROM transaksi_simpanan ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllTransaksi(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.TransaksiSimpanan>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM transaksi_simpanan WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransaksiForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.TransaksiSimpanan>> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM transaksi_simpanan")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllTransaksi(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.TransaksiSimpanan> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTransaksi(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.TransaksiSimpanan item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTransaksi(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.TransaksiSimpanan item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}