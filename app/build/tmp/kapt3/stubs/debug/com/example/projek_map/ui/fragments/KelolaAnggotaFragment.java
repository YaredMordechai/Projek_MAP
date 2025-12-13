package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J$\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0010H\u0016J\b\u0010\u001c\u001a\u00020\u0010H\u0002J\u0010\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001f"}, d2 = {"Lcom/example/projek_map/ui/fragments/KelolaAnggotaFragment;", "Landroidx/fragment/app/Fragment;", "()V", "adapter", "Lcom/example/projek_map/ui/adapters/AnggotaAdapter;", "btnTambahAnggota", "Lcom/google/android/material/button/MaterialButton;", "rvAnggota", "Landroidx/recyclerview/widget/RecyclerView;", "viewModel", "Lcom/example/projek_map/viewmodel/KoperasiViewModel;", "getViewModel", "()Lcom/example/projek_map/viewmodel/KoperasiViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "deactivateUser", "", "user", "Lcom/example/projek_map/data/User;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "showAddDialog", "showDeleteOptions", "showEditDialog", "app_debug"})
public final class KelolaAnggotaFragment extends androidx.fragment.app.Fragment {
    private androidx.recyclerview.widget.RecyclerView rvAnggota;
    private com.google.android.material.button.MaterialButton btnTambahAnggota;
    private com.example.projek_map.ui.adapters.AnggotaAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    
    public KelolaAnggotaFragment() {
        super();
    }
    
    private final com.example.projek_map.viewmodel.KoperasiViewModel getViewModel() {
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
    
    private final void showEditDialog(com.example.projek_map.data.User user) {
    }
    
    private final void showDeleteOptions(com.example.projek_map.data.User user) {
    }
    
    private final void deactivateUser(com.example.projek_map.data.User user) {
    }
}