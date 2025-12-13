package com.example.projek_map.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\"\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00032\b\b\u0001\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\tH\'\u00a8\u0006\u000b"}, d2 = {"Lcom/example/projek_map/api/ApiService;", "", "getPinjaman", "Lretrofit2/Call;", "", "Lcom/example/projek_map/data/Pinjaman;", "insertPinjaman", "Lcom/example/projek_map/model/PinjamanResponse;", "jumlah", "", "tenor", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.GET(value = "get_pinjaman.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<java.util.List<com.example.projek_map.data.Pinjaman>> getPinjaman();
    
    @retrofit2.http.FormUrlEncoded()
    @retrofit2.http.POST(value = "insert_pinjaman.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.projek_map.model.PinjamanResponse> insertPinjaman(@retrofit2.http.Field(value = "jumlah")
    @org.jetbrains.annotations.NotNull()
    java.lang.String jumlah, @retrofit2.http.Field(value = "tenor")
    @org.jetbrains.annotations.NotNull()
    java.lang.String tenor);
}