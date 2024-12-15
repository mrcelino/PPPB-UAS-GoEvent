package com.example.uaspppb

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uaspppb.databinding.ActivityUserTiketBinding
import kotlin.random.Random

class UserTiket : AppCompatActivity() {

    private lateinit var binding: ActivityUserTiketBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        binding = ActivityUserTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val username = sharedPreferences.getString("USERNAME", "Unknown User") ?: "Unknown User"
        val judul = intent.getStringExtra("judul")
        val lokasi = intent.getStringExtra("lokasi")
        val tanggal = intent.getStringExtra("tanggal")
        val harga = intent.getStringExtra("harga")
        val foto = intent.getStringExtra("foto")

        // Generate order_id dan gate secara acak
        val orderId = "ORD-${Random.nextInt(10000, 99999)}" // ID acak dengan format "ORD-XXXXX"
        val gate = "Gate-${('A'..'Z').random()}" // Gate acak berupa huruf dari A-Z

        // Tampilkan data ke tampilan menggunakan ViewBinding
        binding.username.text = username
        binding.txtTitle.text = judul
        binding.txtLoc.text = lokasi
        binding.txtDate.text = tanggal
        binding.txtDate2.text = tanggal
        binding.txtPrice.text = harga
        binding.orderId.text = orderId // Tampilkan order_id
        binding.gate.text = gate // Tampilkan gate

        // Gunakan Glide atau library lain untuk menampilkan gambar
        Glide.with(this).load(foto).into(binding.img)

        // Menangani tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressed() // Kembali ke activity sebelumnya
        }
    }
}
