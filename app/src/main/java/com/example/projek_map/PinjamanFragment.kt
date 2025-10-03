package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_map.model.Pinjaman
import com.example.projek_map.model.PinjamanResponse
import com.example.projek_map.api.ApiClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinjamanFragment : Fragment() {

    private lateinit var rvPinjaman: RecyclerView
    private lateinit var btnAjukan: Button
    private lateinit var adapter: PinjamanAdapter
    private val data = mutableListOf<Pinjaman>()

    // Input field dari layout
    private lateinit var inputNominal: TextInputEditText
    private lateinit var inputTenor: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pinjaman, container, false)

        rvPinjaman = view.findViewById(R.id.rvRiwayatPinjaman)
        btnAjukan = view.findViewById(R.id.btnAjukanPinjaman)
        inputNominal = view.findViewById(R.id.inputNominalPinjaman)
        inputTenor = view.findViewById(R.id.inputTenor)

        // Setup RecyclerView
        rvPinjaman.layoutManager = LinearLayoutManager(requireContext())
        adapter = PinjamanAdapter(data) { pinjaman ->
            Toast.makeText(
                requireContext(),
                "Rp ${pinjaman.jumlah} — tenor ${pinjaman.jenis}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvPinjaman.adapter = adapter

        // Load data pertama kali
        loadPinjamanFromApi()

        // Tombol Ajukan Pinjaman
        btnAjukan.setOnClickListener {
            val nominal = inputNominal.text.toString().trim()
            val tenor = inputTenor.text.toString().trim()

            if (nominal.isEmpty() || tenor.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                // langsung kirim nominal + tenor
                ajukanPinjamanBaru(nominal, tenor)
            }
        }

        return view
    }

    private fun loadPinjamanFromApi() {
        ApiClient.instance.getPinjaman().enqueue(object : Callback<List<Pinjaman>> {
            override fun onResponse(call: Call<List<Pinjaman>>, response: Response<List<Pinjaman>>) {
                if (response.isSuccessful && response.body() != null) {
                    data.clear()
                    data.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Gagal ambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pinjaman>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ajukanPinjamanBaru(jumlah: String, tenor: String) {
        ApiClient.instance.insertPinjaman(jumlah, tenor)
            .enqueue(object : Callback<PinjamanResponse> {
                override fun onResponse(
                    call: Call<PinjamanResponse>,
                    response: Response<PinjamanResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            requireContext(),
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        loadPinjamanFromApi() // Refresh list setelah berhasil
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengajukan pinjaman", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PinjamanResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
