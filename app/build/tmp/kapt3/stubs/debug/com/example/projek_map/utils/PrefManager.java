package com.example.projek_map.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0012\u001a\u0004\u0018\u00010\u0006J\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0015\u001a\u0004\u0018\u00010\u0006J\b\u0010\u0016\u001a\u0004\u0018\u00010\u0006J\b\u0010\u0017\u001a\u0004\u0018\u00010\u0006J\u0006\u0010\u0018\u001a\u00020\u0014J\u0006\u0010\u0019\u001a\u00020\u0014J\u0006\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006J\u000e\u0010 \u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u0014J\u000e\u0010!\u001a\u00020\u001b2\u0006\u0010\"\u001a\u00020\u0006R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n \u000f*\u0004\u0018\u00010\u00110\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/projek_map/utils/PrefManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "KEY_EMAIL", "", "KEY_IS_ADMIN", "KEY_IS_LOGGED_IN", "KEY_KODE_PEGAWAI", "KEY_LAST_SEEN_ANNOUNCE", "KEY_USER_NAME", "PREF_NAME", "editor", "Landroid/content/SharedPreferences$Editor;", "kotlin.jvm.PlatformType", "pref", "Landroid/content/SharedPreferences;", "getEmail", "getIsAdmin", "", "getKodePegawai", "getLastSeenAnnouncementDate", "getUserName", "isAdmin", "isLoggedIn", "logout", "", "saveLogin", "userName", "email", "kodePegawai", "setIsAdmin", "setLastSeenAnnouncementDate", "date", "app_debug"})
public final class PrefManager {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String PREF_NAME = "koperasi_pref";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_IS_LOGGED_IN = "is_logged_in";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_USER_NAME = "user_name";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_EMAIL = "user_email";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_KODE_PEGAWAI = "kode_pegawai";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_LAST_SEEN_ANNOUNCE = "last_seen_announce";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_IS_ADMIN = "is_admin";
    private final android.content.SharedPreferences pref = null;
    private final android.content.SharedPreferences.Editor editor = null;
    
    public PrefManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void saveLogin(@org.jetbrains.annotations.NotNull()
    java.lang.String userName, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String kodePegawai) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getKodePegawai() {
        return null;
    }
    
    public final void logout() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastSeenAnnouncementDate() {
        return null;
    }
    
    public final void setLastSeenAnnouncementDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    public final void setIsAdmin(boolean isAdmin) {
    }
    
    public final boolean isAdmin() {
        return false;
    }
    
    public final boolean getIsAdmin() {
        return false;
    }
}