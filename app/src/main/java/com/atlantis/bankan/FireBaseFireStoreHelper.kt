package com.atlantis.bankan

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FireBaseFireStoreHelper {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun addAnnouncement(
        announcement: AnnouncementIC,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val documentId = firestore.collection("announcements").document().id
        val timestamp = System.currentTimeMillis()

        val announcementData = hashMapOf(
            "city" to announcement.city,
            "district" to announcement.district,
            "hospital" to announcement.hospital,
            "bloodType" to announcement.bloodType,
            "phoneNumber" to announcement.phoneNumber,
            "generalInfo" to announcement.generalInfo,
            "timestamp" to timestamp  // Timestamp eklendi
        )

        firestore.collection("announcements")
            .document(documentId)
            .set(announcementData)
            .addOnSuccessListener {
                firestore.collection("usersAnnouncements")
                    .document(userId)
                    .collection("user_announcements")
                    .document(documentId)
                    .set(announcementData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Kullanıcı duyurusu eklenemedi: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Genel duyuru eklenemedi: ${e.message}")
            }
    }

    fun getUserAnnouncements(onSuccess: (List<AnnouncementIC>) -> Unit, onFailure: (String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("usersAnnouncements")
            .document(userId)
            .collection("user_announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val announcements = mutableListOf<AnnouncementIC>()
                for (document in result) {
                    val announcement = document.toObject(AnnouncementIC::class.java)
                    announcement.id = document.id
                    announcements.add(announcement)
                }
                onSuccess(announcements)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message)
            }
    }

    fun getGeneralAnnouncements(onSuccess: (List<AnnouncementIC>) -> Unit, onFailure: (String?) -> Unit) {
        Log.d("FirestoreHelper", "Fetching general announcements...")
        firestore.collection("announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val announcements = mutableListOf<AnnouncementIC>()
                Log.d("FirestoreHelper", "Documents retrieved: ${result.size()}")
                for (document in result) {
                    try {
                        val announcement = document.toObject(AnnouncementIC::class.java)
                        announcement.id = document.id
                        announcements.add(announcement)
                        Log.d("FirestoreHelper", "Parsed announcement: ${announcement.city} - ${announcement.district}")
                    } catch (e: Exception) {
                        Log.e("FirestoreHelper", "Error parsing document: ${e.message}")
                    }
                }
                Log.d("FirestoreHelper", "Total announcements parsed: ${announcements.size}")
                onSuccess(announcements)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreHelper", "Error fetching announcements: ${exception.message}")
                onFailure(exception.message)
            }
    }

    fun deleteAnnouncement(
        announcementId: String,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("announcements")
            .document(announcementId)
            .delete()
            .addOnSuccessListener {
                firestore.collection("usersAnnouncements")
                    .document(userId)
                    .collection("user_announcements")
                    .document(announcementId)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Kullanıcı duyurusu silinemedi: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Genel duyuru silinemedi: ${e.message}")
            }
    }
}
