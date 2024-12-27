package com.atlantis.bankan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class SettingsActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    private lateinit var editTextUsername: EditText
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        editTextUsername = findViewById(R.id.edittext_username)
        buttonUpdate = findViewById(R.id.button_update)


        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid


            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: ""
                        editTextUsername.setText(username)
                    } else {
                        Toast.makeText(this, "Kullanıcı bilgisi bulunamadı.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()

        }


        buttonUpdate.setOnClickListener {
            val newUsername = editTextUsername.text.toString().trim()

            if (newUsername.isEmpty()) {
                editTextUsername.error = "Kullanıcı adı boş olamaz"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (currentUser != null) {
                val userId = currentUser.uid


                firestore.collection("users").document(userId)
                    .update("username", newUsername)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kullanıcı adı güncellendi.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Güncelleme başarısız: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
