package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Düğme referansları
        val button1 = findViewById<Button>(R.id.p_button1)
        val button2 = findViewById<Button>(R.id.p_button2)
        val button4 = findViewById<Button>(R.id.p_button4)
        val button5 = findViewById<Button>(R.id.p_button5)

        // Profil resmi referansı
        val profileImage = findViewById<ImageView>(R.id.p_image1)

        // Profil resmine tıklanınca animasyon başlat
        profileImage.setOnClickListener {
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            profileImage.startAnimation(rotateAnimation)
        }

        // Butonların tıklama işlemleri
        button1.setOnClickListener {
            navigateToActivity(p_SettingsActivity::class.java)
        }

        button2.setOnClickListener {
            navigateToActivity(p_MyInfoActivity::class.java)
        }

        button4.setOnClickListener {
            navigateToActivity(p_ImportantNotesActivity::class.java)
        }

        button5.setOnClickListener {
      //      navigateToActivity(p_FingerprintSettingsActivity::class.java)
        }
    }

    // Genel bir navigasyon metodu
    private fun <T> navigateToActivity(targetActivity: Class<T>) {
        try {
            val intent = Intent(this, targetActivity)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Activity navigasyonu sırasında hata oluştu: ${e.message}")
        }
    }
}
