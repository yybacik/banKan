package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var profileImage: ImageView


    private val profileImages = arrayOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6,
        R.drawable.img7,
        R.drawable.img8,
        R.drawable.img9,
        R.drawable.img10,
        R.drawable.img11,
        R.drawable.img12,
        R.drawable.img13,
        R.drawable.img14
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val button2 = findViewById<Button>(R.id.p_button2)
        val button4 = findViewById<Button>(R.id.p_button4)
        val button5 = findViewById<Button>(R.id.p_button5)
        val backButton = findViewById<Button>(R.id.backbutton)
        val button6 = findViewById<Button>(R.id.p_button6)


        profileImage = findViewById<ImageView>(R.id.p_image1)


        val nameTextView = findViewById<TextView>(R.id.p_nametext)
        val emailTextView = findViewById<TextView>(R.id.p_mailtext)


        profileImage.setOnClickListener {
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
            profileImage.startAnimation(rotateAnimation)
            showImageSelectionDialog()
        }


        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserInfo(currentUser.uid, nameTextView, emailTextView, profileImage)
        } else {
            Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
            finish()
        }


        button6.setOnClickListener {
            navigateToActivity(UserAnnouncementActivity::class.java)
        }

        button2.setOnClickListener {
            navigateToActivity(p_MyInfoActivity::class.java)
        }

        button4.setOnClickListener {
            navigateToActivity(p_ImportantNotesActivity::class.java)
        }

        button5.setOnClickListener {
            navigateToActivity(p_FingerprintSettingsActivity::class.java)
        }

        backButton.setOnClickListener {
            navigateToActivity(MainActivity::class.java)
        }
    }


    private fun fetchUserInfo(uid: String, nameTextView: TextView, emailTextView: TextView, profileImageView: ImageView) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username") ?: "Kullanıcı Adı Bulunamadı"
                    val email = document.getString("email") ?: "Email Bulunamadı"
                    val profileImageResId = document.getLong("profileImage")?.toInt() ?: R.drawable.img1

                    nameTextView.text = username
                    emailTextView.text = email
                    profileImageView.setImageResource(profileImageResId)
                } else {
                    Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfileActivity", "Kullanıcı bilgileri alınamadı: ${e.message}")
                Toast.makeText(this, "Kullanıcı bilgileri alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun <T> navigateToActivity(targetActivity: Class<T>) {
        try {
            val intent = Intent(this, targetActivity)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Activity navigasyonu sırasında hata oluştu: ${e.message}")
            Toast.makeText(this, "Geçiş sırasında hata oluştu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Profil Resmi Seçin")


        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_image_selection, null)
        builder.setView(dialogView)


        builder.setNegativeButton("İptal") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }


        val dialog = builder.create()

        val gridView = dialogView.findViewById<GridView>(R.id.gridViewImages)


        val adapter = ImageAdapter(this, profileImages) { selectedImageResId ->

            profileImage.setImageResource(selectedImageResId)


            saveProfileImageSelection(selectedImageResId)


            dialog.dismiss()
        }

        gridView.adapter = adapter


        dialog.show()
    }


    private fun saveProfileImageSelection(imageResId: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = db.collection("users").document(currentUser.uid)
            userRef.update("profileImage", imageResId)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profil resmi güncellendi.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileActivity", "Profil resmi güncellenemedi: ${e.message}")
                    Toast.makeText(this, "Profil resmi güncellenemedi.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
