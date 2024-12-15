package com.example.uaspppb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uaspppb.databinding.ItemAdminBinding
import com.example.uaspppb.model.Event

class EventAdapter(
    private val events: List<com.example.uaspppb.model.Event>,
    private val onEditClick: (com.example.uaspppb.model.Event) -> Unit,
    private val onDeleteClick: (com.example.uaspppb.model.Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.txtTitle.text = event.judul
            binding.txtLoc.text = event.lokasi
            binding.txtDate.text = event.tanggal
            binding.txtPrice.text = event.harga
            Glide.with(binding.imgId.context)
                .load(event.foto)  // event.imageResource adalah URL gambar
                .into(binding.imgId)  // Memasukkan gambar ke ImageView

            // Handle edit button click
            binding.btnEdit.setOnClickListener {
                onEditClick(event)
            }

            // Handle delete button click
            binding.btnDelete.setOnClickListener {
                onDeleteClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}