package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val tvAppShareInfo: TextView = findViewById(R.id.tv_app_share_info)
        val tvAppShareMessage: TextView = findViewById(R.id.tv_app_share_message)
        val btnShare: Button = findViewById(R.id.btn_share)

        tvAppShareInfo.text = "BanKan Uygulaması"
        tvAppShareMessage.text = "Hayat kurtarmak için bir adım atın! BanKan uygulamasını paylaşarak topluluğumuza destek olun."
        btnShare.text = "Paylaş"

        btnShare.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        val shareMessage = """
            Hayat kurtarmak için bir adım at! BanKan uygulamasını indirerek kan bağışçıları ve ihtiyaç sahiplerini bir araya getiren topluluğumuza katılın. 💉❤️
            https://play.google.com/store/apps/details?id=com.atlantis.bankan
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        startActivity(Intent.createChooser(shareIntent, "Uygulamayı Paylaş"))
    }
}
