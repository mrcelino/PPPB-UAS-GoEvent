package com.example.uaspppb.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "favorite_events")
data class EventEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(), // Ubah tipe data ke String
    val judul: String,
    val lokasi: String,
    val tanggal: String,
    val deskripsi: String,
    val harga: String,
    val foto: String,
)

