package com.example.uaspppb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uaspppb.database.AppDatabase
import com.example.uaspppb.database.EventEntity
import com.example.uaspppb.databinding.FragmentUserFavoritesBinding
import com.example.uaspppb.model.Event
import kotlinx.coroutines.launch

class UserFavoritesFragment : Fragment() {

    private var _binding: FragmentUserFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserEventAdapter // Mendeklarasikan adapter sebagai properti fragment
    private var favoriteEvents: MutableList<Event> = mutableListOf() // List mutable untuk event favorit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserFavoritesBinding.inflate(inflater, container, false)

        // Menampilkan data favorit dari Room
        showFavoriteEvents()

        return binding.root
    }

    private fun showFavoriteEvents() {
        val db = AppDatabase.getDatabase(requireContext()) // Inisialisasi database
        val eventDao = db.eventDao()

        lifecycleScope.launch {
            // Ambil data favorit dari Room dan konversi ke List<Event>
            val favoriteEventsFromDb = eventDao.getAllEvents().map { convertToEvent(it) }
            Log.d("UserFavoritesFragment", "Favorite events size: ${favoriteEventsFromDb.size}")
            favoriteEventsFromDb.forEach {
                Log.d("UserFavoritesFragment", "Event: ${it.judul}")
                Log.d("UserFavoritesFragment", "Image URL: ${it.foto}")
            }

            // Cek jika favoriteEvents memiliki data
            if (favoriteEventsFromDb.isEmpty()) {
                Log.d("UserFavoritesFragment", "No favorite events found")
            }

            // Update favoriteEvents list
            favoriteEvents.clear()
            favoriteEvents.addAll(favoriteEventsFromDb)

            // Inisialisasi adapter dengan favoriteEvents dan onloveClick
            adapter = UserEventAdapter(favoriteEvents) { event ->
                lifecycleScope.launch {
                    // Menghapus event dari Room
                    eventDao.deleteEventById(event.id) // Menghapus berdasarkan ID
                    // Update list favoriteEvents setelah penghapusan
                    favoriteEvents.remove(event)
                    // Notifikasi adapter bahwa data telah berubah
                    adapter.notifyDataSetChanged() // Memperbarui tampilan
                }
            }

            // Set listener untuk klik item card
            adapter.setOnItemClickListener { event ->
                val intent = Intent(requireContext(), UserDetailAcara::class.java)
                // Mengirim data ke Activity UserDetailAcara
                intent.putExtra("judul", event.judul)
                intent.putExtra("lokasi", event.lokasi)
                intent.putExtra("tanggal", event.tanggal)
                intent.putExtra("harga", event.harga)
                intent.putExtra("deskripsi", event.deskripsi)
                intent.putExtra("foto", event.foto)
                startActivity(intent)
            }

            // Mengatur RecyclerView
            binding.cardUser.apply {
                layoutManager = GridLayoutManager(requireContext(), 2) // 2 kolom
                this.adapter = this@UserFavoritesFragment.adapter
            }

            Log.d("UserFavoritesFragment", "RecyclerView adapter set with ${favoriteEvents.size} items.")
        }
    }


    // Fungsi untuk konversi EventEntity ke Event
    fun convertToEvent(eventEntity: EventEntity): Event {
        return Event(
            eventEntity.id,
            eventEntity.judul,
            eventEntity.lokasi,
            eventEntity.tanggal,
            eventEntity.deskripsi,
            eventEntity.harga,
            eventEntity.foto
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding ketika view dihancurkan
    }
}



