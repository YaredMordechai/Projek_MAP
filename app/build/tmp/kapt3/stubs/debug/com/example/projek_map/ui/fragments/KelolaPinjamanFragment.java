package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J$\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u000e2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u001aH\u0016J\u000e\u0010#\u001a\b\u0012\u0004\u0012\u00020%0$H\u0002J\b\u0010&\u001a\u00020\u001aH\u0002J\u0018\u0010\'\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020%2\u0006\u0010)\u001a\u00020\u0010H\u0002J\u0018\u0010*\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020%2\u0006\u0010)\u001a\u00020\u0010H\u0002J\b\u0010+\u001a\u00020\u001aH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/example/projek_map/ui/fragments/KelolaPinjamanFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapterHistori", "Lcom/example/projek_map/ui/adapters/HistoriPembayaranAdapter;", "adapterPinjaman", "Lcom/example/projek_map/ui/adapters/KelolaPinjamanAdapter;", "btnCatat", "Lcom/google/android/material/button/MaterialButton;", "btnKelolaPembayaran", "displayHistori", "", "Lcom/example/projek_map/data/HistoriPembayaran;", "layoutPembayaran", "Landroid/view/ViewGroup;", "modePembayaran", "", "rupiah", "Ljava/text/NumberFormat;", "kotlin.jvm.PlatformType", "rvHistori", "Landroidx/recyclerview/widget/RecyclerView;", "rvPinjaman", "tvEmpty", "Landroid/widget/TextView;", "applyMode", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "pendingOnly", "", "Lcom/example/projek_map/data/Pinjaman;", "refreshHistori", "sendDecisionBroadcast", "pinjaman", "approve", "showApprovalDialog", "showCatatDialog", "app_debug"})
public final class KelolaPinjamanFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvPinjaman;
    private com.example.projek_map.ui.adapters.KelolaPinjamanAdapter adapterPinjaman;
    private android.view.ViewGroup layoutPembayaran;
    private androidx.recyclerview.widget.RecyclerView rvHistori;
    private com.google.android.material.button.MaterialButton btnCatat;
    private android.widget.TextView tvEmpty;
    private com.example.projek_map.ui.adapters.HistoriPembayaranAdapter adapterHistori;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.projek_map.data.HistoriPembayaran> displayHistori = null;
    private final java.text.NumberFormat rupiah = null;
    private com.google.android.material.button.MaterialButton btnKelolaPembayaran;
    private boolean modePembayaran = false;
    
    public KelolaPinjamanFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void applyMode() {
    }
    
    private final void refreshHistori() {
    }
    
    private final void showCatatDialog() {
    }
    
    private final void showApprovalDialog(com.example.projek_map.data.Pinjaman pinjaman, boolean approve) {
    }
    
    private final void sendDecisionBroadcast(com.example.projek_map.data.Pinjaman pinjaman, boolean approve) {
    }
    
    private final java.util.List<com.example.projek_map.data.Pinjaman> pendingOnly() {
        return null;
    }
}