package com.example.uaspppb

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.uaspppb.databinding.ActivityAdminAddAcaraBinding
import com.example.uaspppb.network.ApiClient
import retrofit2.Response
import com.example.uaspppb.model.Event
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.Calendar
import java.util.UUID

class AdminAddAcara : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddAcaraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding
        binding = ActivityAdminAddAcaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnAdd.setOnClickListener {
            // Ambil nilai dari form input
            val judul = binding.txtJudul.text.toString()
            val lokasi = binding.txtLokasi.text.toString()
            val tanggal = binding.txtTanggal.text.toString()
            val deskripsi = binding.txtLokasi.text.toString()
            val harga = binding.txtHarga.text.toString()
            val foto = binding.txtFoto.text.toString()

            // Panggil fungsi addEvent dengan parameter yang benar
            addEvent(judul, lokasi, tanggal, deskripsi, harga, foto)
        }

        // Menambahkan listener untuk tombol btnBack
        binding.btnBack.setOnClickListener {
            // Pastikan NavHostFragment ditemukan
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            if (navHostFragment != null) {
                // Menggunakan NavController untuk berpindah ke AdminKelolaAcaraFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.adminKelolaAcaraFragment) // Menavigasi ke AdminKelolaAcaraFragment
            } else {
                // Jika navHostFragment tidak ditemukan, beri log atau toast untuk debugging
                println("NavHostFragment not found!")
            }
            finish() // Menyelesaikan activity ini
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.txtTanggal.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun addEvent(
        judul: String,
        lokasi: String,
        tanggal: String,
        deskripsi: String,
        harga: String,
        foto: String
    ) {
        // Log all input values before sending
        Log.d("EventUpload", "Judul: $judul, Tipe Data: ${judul.javaClass.simpleName}")
        Log.d("EventUpload", "Lokasi: $lokasi, Tipe Data: ${lokasi.javaClass.simpleName}")
        Log.d("EventUpload", "Tanggal: $tanggal, Tipe Data: ${tanggal.javaClass.simpleName}")
        Log.d("EventUpload", "Deskripsi: $deskripsi, Tipe Data: ${deskripsi.javaClass.simpleName}")
        Log.d("EventUpload", "Harga: $harga, Tipe Data: ${harga.javaClass.simpleName}")
        Log.d("EventUpload", "Foto: $foto, Tipe Data: ${foto.javaClass.simpleName}")

        val jsonData = Gson().toJsonTree(
            mapOf(
                "judul" to judul,
                "lokasi" to lokasi,
                "tanggal" to tanggal,
                "deskripsi" to deskripsi,
                "harga" to harga,
                "foto" to foto
            )
        ).toString()

        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        ApiClient.getInstance().addEvent(requestBody).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    // Tangani jika data berhasil dikirimkan
                    Toast.makeText(applicationContext, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    finish()
                    println("Data berhasil dikirim: ${response.body()}")
                } else {
                    // Tangani jika ada error
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                // Tangani jika terjadi kesalahan jaringan atau lainnya
                println("Koneksi gagal: ${t.message}")
            }
        })
    }

}
