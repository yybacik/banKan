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

        tvAppShareInfo.text = getString(R.string.share_info)
        tvAppShareMessage.text = getString(R.string.share_message)
        btnShare.text = getString(R.string.share_button)

        btnShare.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        val shareMessage = getString(R.string.share_text)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_message)))
    }
}
