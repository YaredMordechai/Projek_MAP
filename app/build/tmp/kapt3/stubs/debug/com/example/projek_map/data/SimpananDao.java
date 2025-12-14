package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/example/projek_map/data/SimpananDao;", "", "getAllSimpanan", "", "Lcom/example/projek_map/data/Simpanan;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSimpananByKode", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAllSimpanan", "", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface SimpananDao {
    
    @androidx.room.Query(value = "SELECT * FROM simpanan")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllSimpanan(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Simpanan>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM simpanan WHERE kodePegawai = :kode LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSimpananByKode(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.projek_map.data.Simpanan> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllSimpanan(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.Simpanan> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}