package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u0004\u0018\u00010\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/example/projek_map/data/BuktiPembayaranAnggotaDao;", "", "getBuktiForUser", "", "Lcom/example/projek_map/data/BuktiPembayaranAnggota;", "kode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMaxId", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertBukti", "", "item", "(Lcom/example/projek_map/data/BuktiPembayaranAnggota;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface BuktiPembayaranAnggotaDao {
    
    @androidx.room.Query(value = "SELECT * FROM bukti_pembayaran_anggota WHERE kodePegawai = :kode ORDER BY tanggal DESC, id DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBuktiForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.BuktiPembayaranAnggota>> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(id) FROM bukti_pembayaran_anggota")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBukti(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.BuktiPembayaranAnggota item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}