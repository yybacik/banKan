package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atlantis.bankan.databinding.ActivityAnnouncements2Binding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AnnouncementsActivity : AppCompatActivity() {

    private var announcementList = mutableListOf<AnnouncementIC>()
    private lateinit var announcementAdapter: AnnouncementAdapter
    private var filteredAnnouncementList = mutableListOf<AnnouncementIC>()

    private lateinit var binding: ActivityAnnouncements2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnnouncements2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = binding.recyclerViewAnnouncements
        val buttonAddAnnouncement: FloatingActionButton = binding.fabAddAnnouncement
        val buttonFilterAnnouncements: ImageButton = binding.buttonFilter

        announcementAdapter = AnnouncementAdapter(announcementList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = announcementAdapter

        buttonAddAnnouncement.setOnClickListener {
            val intent = Intent(this, AnnouncementAddActivity::class.java)
            startActivityForResult(intent, 1)
        }
        buttonFilterAnnouncements.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivityForResult(intent, 2)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newAnnouncement = data?.getSerializableExtra("new_announcement") as? AnnouncementIC
            newAnnouncement?.let {
                announcementList.add(0, it)
                announcementAdapter.notifyItemInserted(0)
                binding.recyclerViewAnnouncements.scrollToPosition(0)
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            val selectedCity = data?.getStringExtra("selected_city") ?: ""
            val selectedDistrict = data?.getStringExtra("selected_district") ?: ""
            val selectedHospital = data?.getStringExtra("selected_hospital") ?: ""
            val selectedBloodType = data?.getStringExtra("selected_bloodType") ?: ""

            applyFilters(selectedCity, selectedDistrict, selectedHospital, selectedBloodType)
        }
    }

    private fun applyFilters(city: String, district: String, hospital: String, bloodType: String) {

        val filteredList = announcementList.filter { announcement ->
            (city=="Şehir Seçilmemiş" || announcement.city == city) &&
                    (district=="İlçe Seçilmemiş" || announcement.district == district) &&
                    (hospital=="Hastane Seçilmemiş"|| announcement.hospital == hospital) &&
                    (bloodType=="Kan Grubu Seçilmemiş" || announcement.bloodType == bloodType)

        }
        if (filteredList.isNullOrEmpty()) {
            Toast.makeText(this, "Filtreye uygun duyuru bulunamamıştır", Toast.LENGTH_SHORT).show()
            announcementAdapter.updateList(emptyList())
        } else {
            filteredAnnouncementList.clear()
            filteredAnnouncementList.addAll(filteredList)
            announcementAdapter.updateList(filteredAnnouncementList)
        }

    }
}