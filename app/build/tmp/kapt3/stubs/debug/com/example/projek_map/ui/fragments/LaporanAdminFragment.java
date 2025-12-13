package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J$\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0017H\u0002J\u0016\u0010!\u001a\u00020\"*\u0004\u0018\u00010\"2\u0006\u0010#\u001a\u00020\"H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/example/projek_map/ui/fragments/LaporanAdminFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/example/projek_map/ui/adapters/LaporanAdminAdapter;", "btnFilter", "Landroid/widget/Button;", "btnReset", "etCari", "Landroid/widget/EditText;", "rupiah", "Ljava/text/NumberFormat;", "rvAnggota", "Landroidx/recyclerview/widget/RecyclerView;", "spStatus", "Landroid/widget/Spinner;", "tvEmpty", "Landroid/widget/TextView;", "tvJumlahAnggota", "tvSaldoKas", "tvTotalPinjamanAll", "tvTotalSimpananAll", "applyFilter", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "renderSummary", "orElse", "", "def", "UserSummary", "app_debug"})
public final class LaporanAdminFragment extends androidx.fragment.app.Fragment {
    private android.widget.TextView tvSaldoKas;
    private android.widget.TextView tvTotalSimpananAll;
    private android.widget.TextView tvTotalPinjamanAll;
    private android.widget.TextView tvJumlahAnggota;
    private android.widget.Spinner spStatus;
    private android.widget.EditText etCari;
    private android.widget.Button btnFilter;
    private android.widget.Button btnReset;
    private androidx.recyclerview.widget.RecyclerView rvAnggota;
    private com.example.projek_map.ui.adapters.LaporanAdminAdapter adapter;
    private android.widget.TextView tvEmpty;
    @org.jetbrains.annotations.NotNull()
    private final java.text.NumberFormat rupiah = null;
    
    public LaporanAdminFragment() {
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
    
    private final void renderSummary() {
    }
    
    private final void applyFilter() {
    }
    
    private final java.lang.String orElse(java.lang.String $this$orElse, java.lang.String def) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0005H\u00c6\u0003J\'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0018"}, d2 = {"Lcom/example/projek_map/ui/fragments/LaporanAdminFragment$UserSummary;", "", "user", "Lcom/example/projek_map/data/User;", "totalSimpanan", "", "totalPinjamanAktif", "(Lcom/example/projek_map/data/User;DD)V", "getTotalPinjamanAktif", "()D", "getTotalSimpanan", "getUser", "()Lcom/example/projek_map/data/User;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class UserSummary {
        @org.jetbrains.annotations.NotNull()
        private final com.example.projek_map.data.User user = null;
        private final double totalSimpanan = 0.0;
        private final double totalPinjamanAktif = 0.0;
        
        public UserSummary(@org.jetbrains.annotations.NotNull()
        com.example.projek_map.data.User user, double totalSimpanan, double totalPinjamanAktif) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.projek_map.data.User getUser() {
            return null;
        }
        
        public final double getTotalSimpanan() {
            return 0.0;
        }
        
        public final double getTotalPinjamanAktif() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.projek_map.data.User component1() {
            return null;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.projek_map.ui.fragments.LaporanAdminFragment.UserSummary copy(@org.jetbrains.annotations.NotNull()
        com.example.projek_map.data.User user, double totalSimpanan, double totalPinjamanAktif) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}