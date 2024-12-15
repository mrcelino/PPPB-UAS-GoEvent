package com.example.uaspppb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uaspppb.databinding.ItemAdminBinding
import com.example.uaspppb.databinding.ItemUserBinding

class UserEventAdapter(
    private val events: List<com.example.uaspppb.model.Event>,
    private val onloveClick: (com.example.uaspppb.model.Event) -> Unit,
) : RecyclerView.Adapter<UserEventAdapter.EventViewHolder>() {

    private var onItemClickListener: ((com.example.uaspppb.model.Event) -> Unit)? = null

    fun setOnItemClickListener(listener: (com.example.uaspppb.model.Event) -> Unit) {
        onItemClickListener = listener
    }

    inner class EventViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Memastikan item bisa diklik
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(events[adapterPosition])
            }
        }

        fun bind(event: com.example.uaspppb.model.Event) {
            binding.txtTitle.text = event.judul
            binding.txtLoc.text = event.lokasi
            binding.txtDate.text = event.tanggal
            binding.txtPrice.text = event.harga
            Glide.with(binding.imgId.context)
                .load(event.foto)  // event.imageResource adalah URL gambar
                .into(binding.imgId)  // Memasukkan gambar ke ImageView
            // Handle edit button click
            binding.btnWishlist.setOnClickListener {
                onloveClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}