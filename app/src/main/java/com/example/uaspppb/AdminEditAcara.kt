package com.example.uaspppb

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uaspppb.databinding.ActivityAdminEditAcaraBinding
import com.example.uaspppb.model.Event
import com.example.uaspppb.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditAcara : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEditAcaraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding
        binding = ActivityAdminEditAcaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val eventId = intent.getStringExtra("event_id")
        val judul = intent.getStringExtra("judul")
        val lokasi = intent.getStringExtra("lokasi")
        val tanggal = intent.getStringExtra("tanggal")
        val deskripsi = intent.getStringExtra("deskripsi")
        val harga = intent.getStringExtra("harga")
        val foto = intent.getStringExtra("foto")

        // Set data ke EditText
        binding.txtJudul.setText(judul)
        binding.txtLokasi.setText(lokasi)
        binding.txtTanggal.setText(tanggal)
        binding.txtDeskripsi.setText(deskripsi)
        binding.txtHarga.setText(harga)
        binding.txtFoto.setText(foto)

        // Menambahkan listener untuk tombol btnBack
        binding.btnBack.setOnClickListener {
            // Menavigasi kembali ke AdminKelolaAcaraFragment
            onBackPressed()
        }

        // Menambahkan listener untuk tombol btnSave
        binding.btnEdit.setOnClickListener {
            // Ambil data dari EditText
            val updatedJudul = binding.txtJudul.text.toString()
            val updatedLokasi = binding.txtLokasi.text.toString()
            val updatedTanggal = binding.txtTanggal.text.toString()
            val updatedDeskripsi = binding.txtDeskripsi.text.toString()
            val updatedHarga = binding.txtHarga.text.toString()
            val updatedFoto = binding.txtFoto.text.toString()

            // Pastikan eventId tidak null
            if (eventId != null) {
                // Membuat objek Event dengan data baru
                val updatedEvent = Event(
                    id = eventId,
                    judul = updatedJudul,
                    lokasi = updatedLokasi,
                    tanggal = updatedTanggal,
                    deskripsi = updatedDeskripsi,
                    harga = updatedHarga,
                    foto = updatedFoto
                )
                Log.d("API Request", "Updating Event with ID: $eventId")
                Log.d("Updated Event", "ID: ${updatedEvent.id}, Judul: ${updatedEvent.judul}, Lokasi: ${updatedEvent.lokasi}, Tanggal: ${updatedEvent.tanggal}")


                // Panggil API untuk update event
                val apiService = ApiClient.getInstance()
                apiService.updateEvent(eventId, updatedEvent).enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        if (response.isSuccessful) {
                            // Tampilkan pesan sukses
                            Toast.makeText(this@AdminEditAcara, "Event berhasil diupdate", Toast.LENGTH_SHORT).show()
                            finish()  // Kembali ke halaman sebelumnya setelah sukses
                        } else {
                            // Tampilkan pesan error
                            Toast.makeText(this@AdminEditAcara, "Gagal mengupdate event", Toast.LENGTH_SHORT).show()
                            Log.e("API Error", "Response Code: ${response.code()}, Message: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        // Tampilkan pesan error
                        Toast.makeText(this@AdminEditAcara, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@AdminEditAcara, "Event ID tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
