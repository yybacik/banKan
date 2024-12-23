package com.atlantis.bankan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat

class p_FingerprintSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pfingerprint_settings)

        val fingerprintIcon = findViewById<ImageView>(R.id.fingerprint_icon)
        val fingerprintStatusText = findViewById<TextView>(R.id.fingerprint_status_text)
        val btnEnableFingerprint = findViewById<Button>(R.id.btn_fingerprint_authenticate)
        val btnRemoveFingerprint = findViewById<Button>(R.id.btn_remove_fingerprint)

        // Simulate enabling fingerprint
        btnEnableFingerprint.setOnClickListener {
            fingerprintStatusText.text = "Parmak izi doğrulaması etkinleştirildi."
            showSnackbar("Parmak izi doğrulaması etkinleştirildi.", true)
        }

        // Simulate removing fingerprint
        btnRemoveFingerprint.setOnClickListener {
            fingerprintStatusText.text = "Parmak izi doğrulaması kaldırıldı."
            showSnackbar("Parmak izi doğrulaması kaldırıldı.", true)
        }
    }

    private fun showSnackbar(message: String, isSuccess: Boolean) {
        val color = if (isSuccess) android.R.color.holo_green_light else android.R.color.holo_red_light
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, color))
            .show()
    }
}
