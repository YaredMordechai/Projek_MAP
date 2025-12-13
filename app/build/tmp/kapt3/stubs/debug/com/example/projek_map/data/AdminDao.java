package com.example.projek_map.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000f\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\b0\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u0013\u001a\u00020\u00062\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u0017"}, d2 = {"Lcom/example/projek_map/data/AdminDao;", "", "countAdmins", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAdmin", "", "admin", "Lcom/example/projek_map/data/Admin;", "(Lcom/example/projek_map/data/Admin;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAdminByEmail", "email", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAdminByKode", "kode", "getAllAdmins", "", "insertAdmin", "insertAdmins", "admins", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateAdmin", "app_debug"})
@androidx.room.Dao()
public abstract interface AdminDao {
    
    @androidx.room.Query(value = "SELECT * FROM admins")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllAdmins(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.projek_map.data.Admin>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM admins WHERE kodePegawai = :kode LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAdminByKode(@org.jetbrains.annotations.NotNull()
    java.lang.String kode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.projek_map.data.Admin> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM admins WHERE email = :email LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAdminByEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.projek_map.data.Admin> $completion);
    
    @androidx.room.Insert(onConflict = 3)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAdmin(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Admin admin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 3)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAdmins(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.Admin> admins, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateAdmin(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Admin admin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAdmin(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.Admin admin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM admins")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object countAdmins(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}