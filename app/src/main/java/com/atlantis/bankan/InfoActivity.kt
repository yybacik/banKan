package com.atlantis.bankan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InfoActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        database = FirebaseDatabase.getInstance().getReference("Feedbacks")


        val tvAppInfo: TextView = findViewById(R.id.tv_app_info)
        val tvAppInfoContent: TextView = findViewById(R.id.tv_app_info_content)
        val tvFeedbackTitle: TextView = findViewById(R.id.tv_feedback_title)
        val etFeedback: EditText = findViewById(R.id.et_feedback)
        val btnSendFeedback: Button = findViewById(R.id.btn_send_feedback)


        tvAppInfo.text = "BanKan Uygulaması"
        tvAppInfoContent.text = "BanKan, kan bağışçıları ve ihtiyaç sahiplerini hızlı ve kolay bir şekilde bir araya getiren bir kan bağışı platformudur. Hayat kurtarmak için bir adım at! Geri bildirimleriniz bizim için çok önemli."
        tvFeedbackTitle.text = "Geri Bildirim Gönder"
        btnSendFeedback.text = "Gönder"
        etFeedback.hint = "Geri bildiriminizi buraya yazın..."


        btnSendFeedback.setOnClickListener {
            val feedback = etFeedback.text.toString().trim()

            if (feedback.isNotEmpty()) {
                sendFeedback(feedback)
                etFeedback.text.clear()
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
