package com.example.projek_map.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000bJ\u0006\u0010\u001d\u001a\u00020\u001bJ\u0016\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\tJ\u000e\u0010!\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000bJ&\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\t2\u0006\u0010 \u001a\u00020\t2\u0006\u0010%\u001a\u00020\tJ\u0016\u0010&\u001a\u00020\u001b2\u0006\u0010%\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tJ\u001e\u0010\'\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\t2\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\tR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0019\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0019\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011\u00a8\u0006*"}, d2 = {"Lcom/example/projek_map/viewmodel/KoperasiViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_loginState", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/projek_map/viewmodel/LoginState;", "_registerError", "", "_registerResult", "Lcom/example/projek_map/data/User;", "_users", "", "loginState", "Landroidx/lifecycle/LiveData;", "getLoginState", "()Landroidx/lifecycle/LiveData;", "registerError", "getRegisterError", "registerResult", "getRegisterResult", "repo", "Lcom/example/projek_map/data/KoperasiRepository;", "users", "getUsers", "hapusAnggota", "", "user", "loadUsers", "login", "identifier", "password", "nonaktifkanAnggota", "register", "kodePegawai", "email", "nama", "tambahAnggota", "updateAnggota", "namaBaru", "emailBaru", "app_debug"})
public final class KoperasiViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.projek_map.data.KoperasiRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.projek_map.data.User>> _users = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.projek_map.data.User>> users = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.projek_map.viewmodel.LoginState> _loginState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.projek_map.viewmodel.LoginState> loginState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.projek_map.data.User> _registerResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.projek_map.data.User> registerResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _registerError = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> registerError = null;
    
    public KoperasiViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.projek_map.data.User>> getUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.projek_map.viewmodel.LoginState> getLoginState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.projek_map.data.User> getRegisterResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getRegisterError() {
        return null;
    }
    
    public final void loadUsers() {
    }
    
    public final void tambahAnggota(@org.jetbrains.annotations.NotNull()
    java.lang.String nama, @org.jetbrains.annotations.NotNull()
    java.lang.String email) {
    }
    
    public final void updateAnggota(@org.jetbrains.annotations.NotNull()
    java.lang.String kodePegawai, @org.jetbrains.annotations.NotNull()
    java.lang.String namaBaru, @org.jetbrains.annotations.NotNull()
    java.lang.String emailBaru) {
    }
    
    public final void nonaktifkanAnggota(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.User user) {
    }
    
    public final void hapusAnggota(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.User user) {
    }
    
    public final void login(@org.jetbrains.annotations.NotNull()
    java.lang.String identifier, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
    }
    
    public final void register(@org.jetbrains.annotations.NotNull()
    java.lang.String kodePegawai, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String nama) {
    }
}