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
    private val allAnnouncements = mutableListOf<AnnouncementIC>()
    private lateinit var announcementAdapter: AnnouncementAdapter
    private lateinit var firestoreHelper: FireBaseFireStoreHelper
    private lateinit var binding: ActivityAnnouncements2Binding


    private var currentFilters: FilterState? = null

    data class FilterState(
        val city: String,
        val district: String,
        val hospital: String,
        val bloodType: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnnouncements2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestoreHelper = FireBaseFireStoreHelper()



        setupRecyclerView()
        setupButtons()
        fetchAnnouncements()
    }

    private fun setupRecyclerView() {
        announcementAdapter = AnnouncementAdapter(mutableListOf())
        binding.recyclerViewAnnouncements.apply {
            layoutManager = LinearLayoutManager(this@AnnouncementsActivity)
            adapter = announcementAdapter
        }
    }

    private fun setupButtons() {
        binding.fabAddAnnouncement.setOnClickListener {
            startActivityForResult(
                Intent(this, AnnouncementAddActivity::class.java),
                1
            )
        }
        binding.buttonFilter.setOnClickListener {
            startActivityForResult(
                Intent(this,FilterActivity::class.java),2
            )
        }



    }


    private fun fetchAnnouncements() {
        firestoreHelper.getGeneralAnnouncements(
            onSuccess = { announcements ->
                allAnnouncements.clear()
                allAnnouncements.addAll(announcements)

                if (currentFilters != null) {
                    applyCurrentFilters()
                } else {

                    announcementAdapter.updateList(announcements)
                }
            },
            onFailure = { error ->
                Toast.makeText(this, "Duyurular alınamadı: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        fetchAnnouncements()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val selectedCity = data.getStringExtra("selected_city") ?: "Şehir Seçilmemiş"
            val selectedDistrict = data.getStringExtra("selected_district") ?: "İlçe Seçilmemiş"
            val selectedHospital = data.getStringExtra("selected_hospital") ?: "Hastane Seçilmemiş"
            val selectedBloodType = data.getStringExtra("selected_bloodType") ?: "Kan Grubu Seçilmemiş"


            currentFilters = FilterState(
                selectedCity,
                selectedDistrict,
                selectedHospital,
                selectedBloodType
            )

            applyCurrentFilters()

        }
    }

    private fun applyCurrentFilters() {
        currentFilters?.let { filters ->
            val filteredList = allAnnouncements.filter { announcement ->
                val cityMatch = filters.city == "Şehir Seçilmemiş" || announcement.city == filters.city
                val districtMatch = filters.district == "İlçe Seçilmemiş" || announcement.district == filters.district
                val hospitalMatch = filters.hospital == "Hastane Seçilmemiş" || announcement.hospital == filters.hospital
                val bloodTypeMatch = filters.bloodType == "Kan Grubu Seçilmemiş" || announcement.bloodType == filters.bloodType

                cityMatch && districtMatch && hospitalMatch && bloodTypeMatch
            }.toMutableList()

            if (filteredList.isEmpty()) {
            }

            announcementAdapter.updateList(filteredList)
        }
    }
}