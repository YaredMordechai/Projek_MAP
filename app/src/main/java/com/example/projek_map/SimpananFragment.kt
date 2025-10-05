package com.example.projek_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_map.databinding.FragmentSimpananBinding

class SimpananFragment : Fragment() {

    private var _binding: FragmentSimpananBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSimpanan = listOf(
            Simpanan("Simpanan Pokok", "Rp 500.000", "01-01-2025"),
            Simpanan("Simpanan Wajib", "Rp 100.000", "10-01-2025"),
            Simpanan("Simpanan Sukarela", "Rp 50.000", "15-01-2025")
        )


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
