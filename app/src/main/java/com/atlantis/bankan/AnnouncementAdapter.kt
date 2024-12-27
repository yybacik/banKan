package com.atlantis.bankan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnnouncementAdapter(private var announcements: MutableList<AnnouncementIC>) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewSubtitle: TextView = view.findViewById(R.id.textViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcements[position]
        holder.textViewTitle.text = "${announcement.city} - ${announcement.district}"
        holder.textViewSubtitle.text =
            "Hastane: ${announcement.hospital}\nKan Grubu: ${announcement.bloodType}\nTel: ${announcement.phoneNumber}\nGenel Bilgi: ${announcement.generalInfo}"
    }

    override fun getItemCount() = announcements.size

    fun updateList(newList: List<AnnouncementIC>) {
        this.announcements = newList.toMutableList()
        notifyDataSetChanged()
    }
}