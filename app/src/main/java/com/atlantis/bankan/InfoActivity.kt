package com.atlantis.bankan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atlantis.bankan.databinding.ActivityInfoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DataBinding ile bağlantıyı oluştur
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Realtime Database referansı
        database = FirebaseDatabase.getInstance().getReference("Feedbacks")

        binding.btnSendFeedback.setOnClickListener {
            val feedback = binding.etFeedback.text.toString().trim()

            if (feedback.isNotEmpty()) {
                sendFeedback(feedback)
                binding.etFeedback.text.clear()
            } else {
                Toast.makeText(this, "Lütfen bir geri bildirim girin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendFeedback(feedback: String) {
        val feedbackId = database.push().key
        if (feedbackId != null) {
            database.child(feedbackId).setValue(feedback).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Geri bildirim gönderildi.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Hata oluştu. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
