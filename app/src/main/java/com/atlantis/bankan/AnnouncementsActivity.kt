package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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



    }
}