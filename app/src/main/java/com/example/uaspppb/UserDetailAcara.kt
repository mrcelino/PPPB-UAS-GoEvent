package com.example.uaspppb

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.uaspppb.databinding.ActivityUserDetailAcaraBinding

class UserDetailAcara : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailAcaraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailAcaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val judul = intent.getStringExtra("judul")
        val lokasi = intent.getStringExtra("lokasi")
        val tanggal = intent.getStringExtra("tanggal")
        val deskripsi = intent.getStringExtra("deskripsi")
        val harga = intent.getStringExtra("harga")
        val foto = intent.getStringExtra("foto")

        // Tampilkan data ke tampilan menggunakan ViewBinding
        binding.txtTitle.text = judul
        binding.txtLoc.text = lokasi
        binding.txtDate.text = tanggal
        binding.txtDesc.text = deskripsi
        binding.txtPrice.text = harga

        // Gunakan Glide atau library lain untuk menampilkan gambar
        Glide.with(this).load(foto).into(binding.imgId)

        // Menangani tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressed() // Kembali ke activity sebelumnya
        }

        // Menangani tombol beli
        binding.btnBuy.setOnClickListener {
            // Membuat Intent untuk menuju UserTicket
            val intent = Intent(this, UserTiket::class.java)
            intent.putExtra("judul", judul)
            intent.putExtra("lokasi", lokasi)
            intent.putExtra("tanggal", tanggal)
            intent.putExtra("harga", harga)
            intent.putExtra("foto", foto)
            startActivity(intent)
        }
    }
}