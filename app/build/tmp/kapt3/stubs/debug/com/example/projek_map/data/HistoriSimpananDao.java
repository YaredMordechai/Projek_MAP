package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001c\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/example/projek_map/data/HistoriSimpananDao;", "", "getAllHistori", "", "Lcom/example/projek_map/data/HistoriSimpanan;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHistoriForUser", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMaxId", "", "insertAllHistori", "", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertHistori", "item", "(Lcom/example/projek_map/data/HistoriSimpanan;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface HistoriSimpananDao {
    
    @androidx.room.Query(value = "SELECT * FROM histori_simpanan")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllHistori(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriSimpanan>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM histori_simpanan WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHistoriForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.HistoriSimpanan>> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM histori_simpanan")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertHistori(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.HistoriSimpanan item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllHistori(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.HistoriSimpanan> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}