package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u001c\u0010\u000f\u001a\u00020\u00032\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0014"}, d2 = {"Lcom/example/projek_map/data/PengumumanDao;", "", "deletePengumuman", "", "pengumuman", "Lcom/example/projek_map/data/Pengumuman;", "(Lcom/example/projek_map/data/Pengumuman;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPengumuman", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMaxId", "", "getPengumumanById", "id", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAllPengumuman", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPengumuman", "updatePengumuman", "app_debug"})
@androidx.room.Dao()
public abstract interface PengumumanDao {
    
    @androidx.room.Query(value = "SELECT * FROM pengumuman ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllPengumuman(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Pengumuman>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM pengumuman WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPengumumanById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.projek_map.data.Pengumuman> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM pengumuman")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertPengumuman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pengumuman pengumuman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAllPengumuman(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.Pengumuman> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updatePengumuman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pengumuman pengumuman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePengumuman(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Pengumuman pengumuman, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}