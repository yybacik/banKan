package com.atlantis.bankan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atlantis.bankan.databinding.ActivityUserAnnouncementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAnnouncementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserAnnouncementBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAnnouncementAdapter: UserAnnouncementAdapter
    private val userAnnouncementList = mutableListOf<AnnouncementIC>()
    private val firebaseFirestoreHelper = FireBaseFireStoreHelper()
    private lateinit var announcementAdapter: AnnouncementAdapter
    private val announcementList = mutableListOf<AnnouncementIC>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAnnouncementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()


        recyclerView = binding.recyclerViewMyAnnouncements
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAnnouncementAdapter = UserAnnouncementAdapter(userAnnouncementList) { announcementId ->
            val position = userAnnouncementList.indexOfFirst { it.id == announcementId }
            if (position != -1) {
                deleteAnnouncement(announcementId)
            }
        }
        recyclerView.adapter = userAnnouncementAdapter

        announcementAdapter = AnnouncementAdapter(announcementList)


        loadUserAnnouncements()
        fetchAnnouncements()
        loadGeneralAnnouncements()
    }

    private fun loadUserAnnouncements() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("usersAnnpuncements")
            .document(userId)
            .collection("user_announcements")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(
                        this,
                        "Duyurular alınamadı: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    userAnnouncementList.clear()
                    for (document in snapshots) {
                        val announcement = document.toObject(AnnouncementIC::class.java).apply {
                            id = document.id
                        }
                        userAnnouncementList.add(announcement)
                    }
                    userAnnouncementAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun loadGeneralAnnouncements() {
        firebaseFirestoreHelper.getGeneralAnnouncements(
            onSuccess = { announcements ->
                announcementList.clear()
                announcementList.addAll(announcements)
                announcementAdapter.notifyDataSetChanged() // Sadece liste güncellemesi
            },
            onFailure = { error ->
                Toast.makeText(this, "Genel duyurular alınamadı: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }


    private fun deleteAnnouncement(announcementId: String) {
        firebaseFirestoreHelper.deleteAnnouncement(
            announcementId,
            onSuccess = {
                Toast.makeText(this, "Duyuru başarıyla silindi.", Toast.LENGTH_SHORT).show()


                announcementList.removeAll { it.id == announcementId }
                userAnnouncementList.removeAll { it.id == announcementId }


                announcementAdapter.notifyDataSetChanged()
                userAnnouncementAdapter.notifyDataSetChanged()
            },
            onFailure = { error ->
                Toast.makeText(this, "Hata: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }


    private fun fetchAnnouncements() {
        firebaseFirestoreHelper.getUserAnnouncements(
            onSuccess = { userAnnouncements ->
                userAnnouncementList.clear()
                userAnnouncementList.addAll(userAnnouncements)
                userAnnouncementAdapter.notifyDataSetChanged()
            },
            onFailure = { error ->
                Toast.makeText(this, "Kullanıcı duyuruları alınamadı: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        firebaseFirestoreHelper.getGeneralAnnouncements(
            onSuccess = { generalAnnouncements ->
                announcementList.clear()
                announcementList.addAll(generalAnnouncements)
                announcementAdapter.notifyDataSetChanged()
            },
            onFailure = { error ->
                Toast.makeText(this, "Genel duyurular alınamadı: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
}