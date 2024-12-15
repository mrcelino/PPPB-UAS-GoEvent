package com.example.uaspppb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uaspppb.database.AppDatabase
import com.example.uaspppb.database.EventDao
import com.example.uaspppb.database.EventEntity
import com.example.uaspppb.databinding.FragmentUserHomeBinding
import com.example.uaspppb.model.Event
import com.example.uaspppb.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fungsi logout pada btnBack
        binding.btnBack.setOnClickListener {
            logoutUser()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val db = AppDatabase.getDatabase(requireContext())
        val eventDao = db.eventDao()
        val apiService = ApiClient.getInstance()

        // Panggil API untuk mendapatkan semua event
        apiService.getAllEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (!isAdded || _binding == null) return // Fragment tidak aktif, batalkan operasi

                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()!!

                    // Atur adapter RecyclerView
                    val adapter = UserEventAdapter(
                        events,
                        onloveClick = { event ->
                            lifecycleScope.launch {
                                val eventEntity = EventEntity(
                                    id = event.id!!,
                                    judul = event.judul,
                                    lokasi = event.lokasi,
                                    tanggal = event.tanggal,
                                    deskripsi = event.deskripsi,
                                    harga = event.harga,
                                    foto = event.foto
                                )
                                eventDao.insertEvent(eventEntity)
                                if (isAdded && _binding != null) {
                                    Toast.makeText(
                                        requireContext(),
                                        "${event.judul} ditambahkan ke favorit!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )

                    adapter.setOnItemClickListener { event ->
                        if (isAdded && _binding != null) {
                            val intent = Intent(requireContext(), UserDetailAcara::class.java)
                            intent.putExtra("judul", event.judul)
                            intent.putExtra("lokasi", event.lokasi)
                            intent.putExtra("harga", event.harga)
                            intent.putExtra("tanggal", event.tanggal)
                            intent.putExtra("deskripsi", event.deskripsi)
                            intent.putExtra("foto", event.foto)
                            startActivity(intent)
                        }
                    }

                    binding.cardUser.apply {
                        layoutManager = GridLayoutManager(requireContext(), 2)
                        this.adapter = adapter
                    }
                } else {
                    if (isAdded && _binding != null) {
                        Toast.makeText(requireContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                if (isAdded && _binding != null) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun logoutUser() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.putString("userEmail", null)
        editor.apply()

        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
