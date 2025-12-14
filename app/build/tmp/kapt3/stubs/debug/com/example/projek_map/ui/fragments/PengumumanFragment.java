package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0018B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/projek_map/ui/fragments/PengumumanFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/example/projek_map/ui/fragments/PengumumanFragment$PengumumanAdapter;", "emptyState", "Landroid/widget/TextView;", "fab", "Lcom/google/android/material/floatingactionbutton/FloatingActionButton;", "isAdmin", "", "rv", "Landroidx/recyclerview/widget/RecyclerView;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "showAddAnnouncementDialog", "", "updateEmptyState", "PengumumanAdapter", "app_debug"})
public final class PengumumanFragment extends androidx.fragment.app.Fragment {
    private boolean isAdmin = false;
    @org.jetbrains.annotations.Nullable()
    private androidx.recyclerview.widget.RecyclerView rv;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView emptyState;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    private com.example.projek_map.ui.fragments.PengumumanFragment.PengumumanAdapter adapter;
    
    public PengumumanFragment() {
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
    
    private final void updateEmptyState() {
    }
    
    private final void showAddAnnouncementDialog() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0011B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u001c\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\f\u001a\u00020\bH\u0016J\u001c\u0010\r\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/example/projek_map/ui/fragments/PengumumanFragment$PengumumanAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/projek_map/ui/fragments/PengumumanFragment$PengumumanAdapter$VH;", "items", "", "Lcom/example/projek_map/data/Pengumuman;", "(Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "h", "pos", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "VH", "app_debug"})
    static final class PengumumanAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.projek_map.ui.fragments.PengumumanFragment.PengumumanAdapter.VH> {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.example.projek_map.data.Pengumuman> items = null;
        
        public PengumumanAdapter(@org.jetbrains.annotations.NotNull()
        java.util.List<com.example.projek_map.data.Pengumuman> items) {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.example.projek_map.ui.fragments.PengumumanFragment.PengumumanAdapter.VH onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.projek_map.ui.fragments.PengumumanFragment.PengumumanAdapter.VH h, int pos) {
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\b\u00a8\u0006\r"}, d2 = {"Lcom/example/projek_map/ui/fragments/PengumumanFragment$PengumumanAdapter$VH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "v", "Landroid/view/View;", "(Lcom/example/projek_map/ui/fragments/PengumumanFragment$PengumumanAdapter;Landroid/view/View;)V", "tIsi", "Landroid/widget/TextView;", "getTIsi", "()Landroid/widget/TextView;", "tJudul", "getTJudul", "tTanggal", "getTTanggal", "app_debug"})
        public final class VH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull()
            private final android.widget.TextView tJudul = null;
            @org.jetbrains.annotations.NotNull()
            private final android.widget.TextView tTanggal = null;
            @org.jetbrains.annotations.NotNull()
            private final android.widget.TextView tIsi = null;
            
            public VH(@org.jetbrains.annotations.NotNull()
            android.view.View v) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.widget.TextView getTJudul() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.widget.TextView getTTanggal() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final android.widget.TextView getTIsi() {
                return null;
            }
        }
    }
}