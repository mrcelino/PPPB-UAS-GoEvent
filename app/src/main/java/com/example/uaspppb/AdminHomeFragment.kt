package com.example.uaspppb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uaspppb.databinding.FragmentAdminHomeBinding
import com.example.uaspppb.model.Event
import com.example.uaspppb.network.ApiClient
import retrofit2.Call
import retrofit2.Response

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
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
        // Panggil API untuk mendapatkan semua event
        val apiService = ApiClient.getInstance()

        apiService.getAllEvents().enqueue(object : retrofit2.Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                // Periksa apakah binding masih valid
                if (isAdded && _binding != null) {
                    if (response.isSuccessful && response.body() != null) {
                        val events = response.body()!!.toMutableList()  // Mengubah List menjadi MutableList

                        // Atur adapter RecyclerView
                        val adapter = EventAdapter(
                            events,
                            onEditClick = { event ->
                                val intent = Intent(requireContext(), AdminEditAcara::class.java)
                                intent.putExtra("event_id", event.id)
                                intent.putExtra("judul", event.judul)
                                intent.putExtra("lokasi", event.lokasi)
                                intent.putExtra("tanggal", event.tanggal)
                                intent.putExtra("deskripsi", event.deskripsi)
                                intent.putExtra("harga", event.harga)
                                intent.putExtra("foto", event.foto)
                                startActivity(intent)
                            },
                            onDeleteClick = { event ->
                                deleteEvent(event, events)  // Panggil fungsi deleteEvent
                            }
                        )

                        // Menyambungkan adapter ke RecyclerView
                        _binding?.cardAdmin?.apply {
                            layoutManager = GridLayoutManager(requireContext(), 2) // 2 kolom
                            this.adapter = adapter
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                // Periksa apakah binding masih valid
                if (isAdded && _binding != null) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun deleteEvent(event: Event, events: MutableList<Event>) {
        // Cek apakah fragment sudah terpasang dan binding tidak null
        if (isAdded && _binding != null) {
            val apiService = ApiClient.getInstance()

            // Tampilkan Toast sebelum menghapus
            Toast.makeText(requireContext(), "Delete ${event.judul}", Toast.LENGTH_SHORT).show()

            // Panggil API untuk menghapus event berdasarkan ID
            apiService.deleteEvent(event.id!!).enqueue(object : retrofit2.Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        // Menghapus event dari list dan memberi tahu adapter untuk memperbarui UI
                        val position = events.indexOf(event)
                        if (position != -1) {
                            events.removeAt(position)  // Menghapus item
                            binding.cardAdmin.adapter?.notifyItemRemoved(position)  // Memberitahu adapter untuk menghapus item di posisi tertentu
                        }
                        Log.d("AdminHomeFragment", "Event ${event.judul} berhasil dihapus.")
                    } else {
                        Toast.makeText(requireContext(), "Gagal menghapus event", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun logoutUser() {
        // Menghapus status login dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.putString("userEmail", null)
        editor.apply()

        // Mengarahkan pengguna ke halaman login
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView() // Panggil ulang API untuk mendapatkan data terbaru
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
