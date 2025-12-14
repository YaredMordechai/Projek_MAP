package com.example.projek_map.ui.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0002J\u0014\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0002J&\u0010\r\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\u001a\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\n2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J \u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bH\u0002J\u0018\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u0019H\u0002J\u0018\u0010$\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u00192\u0006\u0010%\u001a\u00020!H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/example/projek_map/ui/fragments/DashboardFragment;", "Landroidx/fragment/app/Fragment;", "()V", "chartKeuangan", "Lcom/github/mikephil/charting/charts/LineChart;", "isAdmin", "", "findFirstImageView", "Landroid/widget/ImageView;", "root", "Landroid/view/View;", "findFirstTextView", "Landroid/widget/TextView;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "view", "scheduleDailyJatuhTempo", "context", "Landroid/content/Context;", "hour", "", "minute", "setCardTitle", "card", "Landroidx/cardview/widget/CardView;", "newTitle", "", "setupChartKeuanganAdmin", "appContext", "setupChartKeuanganUser", "kodePegawai", "app_debug"})
public final class DashboardFragment extends androidx.fragment.app.Fragment {
    private boolean isAdmin = false;
    private com.github.mikephil.charting.charts.LineChart chartKeuangan;
    
    public DashboardFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupChartKeuanganUser(android.content.Context appContext, java.lang.String kodePegawai) {
    }
    
    private final void setupChartKeuanganAdmin(android.content.Context appContext) {
    }
    
    private final void setCardTitle(androidx.cardview.widget.CardView card, java.lang.String newTitle) {
    }
    
    private final android.widget.TextView findFirstTextView(android.view.View root) {
        return null;
    }
    
    private final android.widget.ImageView findFirstImageView(android.view.View root) {
        return null;
    }
    
    private final void scheduleDailyJatuhTempo(android.content.Context context, int hour, int minute) {
    }
}