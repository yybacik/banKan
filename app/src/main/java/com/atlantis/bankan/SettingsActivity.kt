package com.atlantis.bankan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var usernameLabel: TextView
    private lateinit var editTextUsername: EditText

    private lateinit var emailLabel: TextView
    private lateinit var editTextEmail: EditText

    private lateinit var passwordLabel: TextView
    private lateinit var editTextPassword: EditText

    private lateinit var buttonUpdateInfo: Button

    private lateinit var deleteAccountLabel: TextView
    private lateinit var editTextDeleteReason: EditText
    private lateinit var buttonDeleteAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Firebase instance
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // XML'deki view'leri Kotlin tarafında tanımlıyoruz
        usernameLabel = findViewById(R.id.username_label)
        editTextUsername = findViewById(R.id.edittext_username)

        emailLabel = findViewById(R.id.email_label)
        editTextEmail = findViewById(R.id.edittext_email)

        passwordLabel = findViewById(R.id.password_label)
        editTextPassword = findViewById(R.id.edittext_password)

        buttonUpdateInfo = findViewById(R.id.button_update_info)

        deleteAccountLabel = findViewById(R.id.delete_account_label)
        editTextDeleteReason = findViewById(R.id.edittext_delete_reason)
        buttonDeleteAccount = findViewById(R.id.button_delete_account)

        // TextView, EditText ve Button'ların text/hint değerlerini Activity içinde veriyoruz
        usernameLabel.text = "Kullanıcı Adı"
        editTextUsername.hint = "Kullanıcı adınızı girin"

        emailLabel.text = "E-posta Adresi"
        editTextEmail.hint = "Yeni e-posta adresinizi girin"

        passwordLabel.text = "Yeni Şifre"
        editTextPassword.hint = "Yeni şifrenizi girin"

        buttonUpdateInfo.text = "Bilgileri Güncelle"

        deleteAccountLabel.text = "Hesabınızı Silmek İster misiniz?"
        editTextDeleteReason.hint = "Hesabı neden siliyorsunuz?"
        buttonDeleteAccount.text = "Hesabı Sil"

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
                        Toast.makeText(
                            this,
                            "Kullanıcı bilgisi bulunamadı.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Hata: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            val currentEmail = currentUser.email
            editTextEmail.setText(currentEmail ?: "")
        } else {
            Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
        }

        buttonUpdateInfo.setOnClickListener {
            if (currentUser == null) {
                Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUsername = editTextUsername.text.toString().trim()
            val newEmail = editTextEmail.text.toString().trim()
            val newPassword = editTextPassword.text.toString().trim()

            if (newUsername.isEmpty()) {
                editTextUsername.error = "Kullanıcı adı boş olamaz"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (newEmail.isEmpty()) {
                editTextEmail.error = "E-posta boş olamaz"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (newPassword.isNotEmpty() && newPassword.length < 6) {
                editTextPassword.error = "Şifre en az 6 karakter olmalı"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            val userId = currentUser.uid
            firestore.collection("users").document(userId)
                .update("username", newUsername)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Kullanıcı adı güncellendi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Kullanıcı adı güncellenemedi: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            currentUser.updateEmail(newEmail)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "E-posta güncellendi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "E-posta güncellenemedi: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            if (newPassword.isNotEmpty()) {
                currentUser.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Şifre güncellendi.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Şifre güncellenemedi: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        buttonDeleteAccount.setOnClickListener {
            val deleteReason = editTextDeleteReason.text.toString().trim()
            if (deleteReason.isEmpty()) {
                editTextDeleteReason.error = "Lütfen bir silme nedeni giriniz."
                editTextDeleteReason.requestFocus()
                return@setOnClickListener
            }

            if (currentUser == null) {
                Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = currentUser.uid

            val reasonData = mapOf(
                "deleteReason" to deleteReason,
                "deletedAt" to System.currentTimeMillis()
            )

            firestore.collection("users").document(userId)
                .update(reasonData)
                .addOnSuccessListener {
                    currentUser.delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Hesabınız silindi.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Hesap silme hatası: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Silme nedeni kaydedilemedi: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
