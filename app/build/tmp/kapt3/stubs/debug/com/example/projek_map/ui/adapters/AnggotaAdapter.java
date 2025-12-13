package com.example.projek_map.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0019BU\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\b0\u000b\u00a2\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\fH\u0016J\u001c\u0010\u000f\u001a\u00020\b2\n\u0010\u0010\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\fH\u0016J\u001c\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0016J\u0014\u0010\u0016\u001a\u00020\b2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\b0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/example/projek_map/ui/adapters/AnggotaAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/projek_map/ui/adapters/AnggotaAdapter$AnggotaViewHolder;", "anggotaList", "", "Lcom/example/projek_map/data/User;", "onEdit", "Lkotlin/Function1;", "", "onDeactivate", "onDelete", "Lkotlin/Function2;", "", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "refreshData", "newList", "", "AnggotaViewHolder", "app_debug"})
public final class AnggotaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.projek_map.ui.adapters.AnggotaAdapter.AnggotaViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.projek_map.data.User> anggotaList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.projek_map.data.User, kotlin.Unit> onEdit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.projek_map.data.User, kotlin.Unit> onDeactivate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.example.projek_map.data.User, java.lang.Integer, kotlin.Unit> onDelete = null;
    
    public AnggotaAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.User> anggotaList, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.projek_map.data.User, kotlin.Unit> onEdit, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.projek_map.data.User, kotlin.Unit> onDeactivate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.projek_map.data.User, ? super java.lang.Integer, kotlin.Unit> onDelete) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.projek_map.ui.adapters.AnggotaAdapter.AnggotaViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.projek_map.ui.adapters.AnggotaAdapter.AnggotaViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void refreshData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.projek_map.data.User> newList) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0011\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/example/projek_map/ui/adapters/AnggotaAdapter$AnggotaViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/example/projek_map/ui/adapters/AnggotaAdapter;Landroid/view/View;)V", "btnDeactivate", "Landroid/widget/ImageButton;", "getBtnDeactivate", "()Landroid/widget/ImageButton;", "btnEdit", "getBtnEdit", "tvEmail", "Landroid/widget/TextView;", "getTvEmail", "()Landroid/widget/TextView;", "tvNama", "getTvNama", "tvStatus", "getTvStatus", "app_debug"})
    public final class AnggotaViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvNama = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvEmail = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView tvStatus = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageButton btnEdit = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageButton btnDeactivate = null;
        
        public AnggotaViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvNama() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvEmail() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTvStatus() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageButton getBtnEdit() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageButton getBtnDeactivate() {
            return null;
        }
    }
}