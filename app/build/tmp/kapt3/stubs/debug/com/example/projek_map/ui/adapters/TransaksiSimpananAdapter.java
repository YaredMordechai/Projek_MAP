package com.example.projek_map.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0005\u0018\u0000 \u001d2\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u001d\u001eBK\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u001a\b\u0002\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007\u0012\u001a\b\u0002\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000f\u001a\u00020\bJ\b\u0010\u0010\u001a\u00020\bH\u0016J\u001c\u0010\u0011\u001a\u00020\t2\n\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000f\u001a\u00020\bH\u0016J\u001c\u0010\u0013\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\bH\u0016J\u000e\u0010\u0017\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\bJ\u0014\u0010\u0018\u001a\u00020\t2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aJ\u0016\u0010\u001b\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u0005R \u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter$ViewHolder;", "transaksiList", "", "Lcom/example/projek_map/data/TransaksiSimpanan;", "onEdit", "Lkotlin/Function2;", "", "", "onDelete", "(Ljava/util/List;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "rupiah", "Ljava/text/NumberFormat;", "getItemAt", "position", "getItemCount", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "removeAt", "replaceAll", "newList", "", "updateAt", "newItem", "Companion", "ViewHolder", "app_debug"})
public final class TransaksiSimpananAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.projek_map.ui.adapters.TransaksiSimpananAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.projek_map.data.TransaksiSimpanan> transaksiList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.example.projek_map.data.TransaksiSimpanan, java.lang.Integer, kotlin.Unit> onEdit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.example.projek_map.data.TransaksiSimpanan, java.lang.Integer, kotlin.Unit> onDelete = null;
    private static final int MENU_EDIT = 1001;
    private static final int MENU_DELETE = 1002;
    @org.jetbrains.annotations.NotNull()
    private final java.text.NumberFormat rupiah = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.projek_map.ui.adapters.TransaksiSimpananAdapter.Companion Companion = null;
    
    public TransaksiSimpananAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.TransaksiSimpanan> transaksiList, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.projek_map.data.TransaksiSimpanan, ? super java.lang.Integer, kotlin.Unit> onEdit, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.projek_map.data.TransaksiSimpanan, ? super java.lang.Integer, kotlin.Unit> onDelete) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.projek_map.ui.adapters.TransaksiSimpananAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.ui.adapters.TransaksiSimpananAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void removeAt(int position) {
    }
    
    public final void updateAt(int position, @org.jetbrains.annotations.NotNull()
    com.example.projek_map.data.TransaksiSimpanan newItem) {
    }
    
    public final void replaceAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.TransaksiSimpanan> newList) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.projek_map.data.TransaksiSimpanan getItemAt(int position) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter$Companion;", "", "()V", "MENU_DELETE", "", "MENU_EDIT", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\bR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\b\u00a8\u0006\u000f"}, d2 = {"Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/example/projek_map/ui/adapters/TransaksiSimpananAdapter;Landroid/view/View;)V", "tvJenis", "Landroid/widget/TextView;", "getTvJenis", "()Landroid/widget/TextView;", "tvJumlah", "getTvJumlah", "tvKode", "getTvKode", "tvTanggal", "getTvTanggal", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvKode = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvJenis = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvJumlah = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvTanggal = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvKode() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvJenis() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvJumlah() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvTanggal() {
            return null;
        }
    }
}