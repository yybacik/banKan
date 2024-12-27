package com.atlantis.bankan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class UserAnnouncementAdapter(
    private var announcementList: List<AnnouncementIC>,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<UserAnnouncementAdapter.AnnouncementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcementList[position]


        holder.cityTextView.text = announcement.city ?: "-"
        holder.districtTextView.text = announcement.district ?: "-"
        holder.hospitalTextView.text = announcement.hospital ?: "-"
        holder.bloodTypeTextView.text = announcement.bloodType ?: "-"
        holder.phoneNumberTextView.text = announcement.phoneNumber ?: "-"
        holder.generalInfoTextView.text = announcement.generalInfo ?: "-"


        holder.deleteButton.setOnClickListener {
            announcement.id?.let { id ->
                onDeleteClicked(id)
            } ?: Log.e("UserAnnouncementAdapter", "Duyuru ID'si null")
        }
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }


    fun updateList(newList: List<AnnouncementIC>) {
        (announcementList as MutableList).clear()
        (announcementList as MutableList).addAll(newList)
        notifyDataSetChanged()
    }

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityTextView: TextView = itemView.findViewById(R.id.textCity)
        val districtTextView: TextView = itemView.findViewById(R.id.textDistrict)
        val hospitalTextView: TextView = itemView.findViewById(R.id.textHospital)
        val bloodTypeTextView: TextView = itemView.findViewById(R.id.textBloodType)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.textPhoneNumber)
        val generalInfoTextView: TextView = itemView.findViewById(R.id.textGeneralInfo)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }



}