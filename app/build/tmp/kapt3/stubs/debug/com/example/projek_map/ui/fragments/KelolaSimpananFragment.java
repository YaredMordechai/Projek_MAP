package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J$\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010#\u001a\u00020\u001aH\u0016J\b\u0010$\u001a\u00020\u001aH\u0002J\u0018\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\t2\u0006\u0010\'\u001a\u00020(H\u0002J\u0018\u0010)\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\t2\u0006\u0010\'\u001a\u00020(H\u0002J.\u0010*\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u000201H\u0082@\u00a2\u0006\u0002\u00102J\u0010\u00103\u001a\u00020\u001a2\u0006\u00104\u001a\u000205H\u0002J \u00106\u001a\u00020\u001a2\u0006\u00107\u001a\u0002012\u0006\u00108\u001a\u0002012\u0006\u00109\u001a\u000201H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcom/example/projek_map/ui/fragments/KelolaSimpananFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter;", "btnTambah", "Lcom/google/android/material/button/MaterialButton;", "displayList", "", "Lcom/example/projek_map/data/TransaksiSimpanan;", "rupiah", "Ljava/text/NumberFormat;", "getRupiah", "()Ljava/text/NumberFormat;", "rupiah$delegate", "Lkotlin/Lazy;", "rvTransaksi", "Landroidx/recyclerview/widget/RecyclerView;", "spinnerFilterJenis", "Landroid/widget/Spinner;", "tvEmptyState", "Landroid/widget/TextView;", "tvTotalPokok", "tvTotalSukarela", "tvTotalWajib", "applyFilterAndRefresh", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "showAddDialog", "showDeleteDialog", "item", "pos", "", "showEditDialog", "tambahTransaksiSimpananDiDb", "db", "Lcom/example/projek_map/data/KoperasiDatabase;", "kodePegawai", "", "jenis", "jumlah", "", "(Lcom/example/projek_map/data/KoperasiDatabase;Ljava/lang/String;Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmptyState", "isEmpty", "", "updateSummary", "totalPokok", "totalWajib", "totalSukarela", "app_debug"})
public final class KelolaSimpananFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvTransaksi;
    private com.google.android.material.button.MaterialButton btnTambah;
    private com.example.projek_map.ui.adapters.TransaksiSimpananAdapter adapter;
    private android.widget.Spinner spinnerFilterJenis;
    private android.widget.TextView tvTotalPokok;
    private android.widget.TextView tvTotalWajib;
    private android.widget.TextView tvTotalSukarela;
    private android.widget.TextView tvEmptyState;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.projek_map.data.TransaksiSimpanan> displayList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy rupiah$delegate = null;
    
    public KelolaSimpananFragment() {
        super();
    }
    
    private final java.text.NumberFormat getRupiah() {
        return null;
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
    
    private final void showAddDialog() {
    }
    
    private final void showEditDialog(com.example.projek_map.data.TransaksiSimpanan item, int pos) {
    }
    
    private final void showDeleteDialog(com.example.projek_map.data.TransaksiSimpanan item, int pos) {
    }
    
    private final java.lang.Object tambahTransaksiSimpananDiDb(com.example.projek_map.data.KoperasiDatabase db, java.lang.String kodePegawai, java.lang.String jenis, double jumlah, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void applyFilterAndRefresh() {
    }
    
    private final void updateSummary(double totalPokok, double totalWajib, double totalSukarela) {
    }
    
    private final void updateEmptyState(boolean isEmpty) {
    }
}