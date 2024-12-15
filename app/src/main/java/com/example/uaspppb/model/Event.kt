package com.example.uaspppb.model

import com.google.gson.annotations.SerializedName

data class Event(


    @SerializedName("_id")
    val id: String,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("foto")
    val foto: String // URL gambar
)
