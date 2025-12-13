package com.example.projek_map.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u0004H\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\nJ \u0010\u0010\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u001e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004J\u001e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004J2\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/projek_map/utils/NotificationHelper;", "", "()V", "CHANNEL_DESC", "", "CHANNEL_ID", "CHANNEL_NAME", "baseBuilder", "Landroidx/core/app/NotificationCompat$Builder;", "context", "Landroid/content/Context;", "title", "message", "dest", "createNotificationChannel", "", "notifyIfAllowed", "id", "", "builder", "showAnnouncementNotification", "showDueNotification", "showNotification", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "koperasi_channel_01";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_NAME = "Pengumuman Koperasi";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_DESC = "Notifikasi pengumuman dan pengingat jatuh tempo";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.projek_map.utils.NotificationHelper INSTANCE = null;
    
    private NotificationHelper() {
        super();
    }
    
    public final void createNotificationChannel(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void showNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String dest) {
    }
    
    public final void showDueNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    public final void showAnnouncementNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    private final androidx.core.app.NotificationCompat.Builder baseBuilder(android.content.Context context, java.lang.String title, java.lang.String message, java.lang.String dest) {
        return null;
    }
    
    private final void notifyIfAllowed(android.content.Context context, int id, androidx.core.app.NotificationCompat.Builder builder) {
    }
}