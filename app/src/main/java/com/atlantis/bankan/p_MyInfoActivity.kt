package com.atlantis.bankan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class p_MyInfoActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pmy_info)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // UI Elementlerini tanımlama
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText) // "usernameEditText" ID'si
        val surnameEditText = findViewById<EditText>(R.id.surname_edit_text)
        val bloodTypeEditText = findViewById<EditText>(R.id.blood_type_edit_text)
        val cityEditText = findViewById<EditText>(R.id.city_edit_text)
        val tcNoEditText = findViewById<EditText>(R.id.tcno_edit_text)
        val genderEditText = findViewById<EditText>(R.id.gender_edit_text)
        val ageEditText = findViewById<EditText>(R.id.age_edit_text)
        val heightEditText = findViewById<EditText>(R.id.height_edit_text)
        val weightEditText = findViewById<EditText>(R.id.weight_edit_text)
        val saveButton = findViewById<Button>(R.id.save_button)

        val currentUser = auth.currentUser
        currentUser?.let {
            loadUserInfo(
                it.uid,
                usernameEditText,
                surnameEditText,
                bloodTypeEditText,
                cityEditText,
                tcNoEditText,
                genderEditText,
                ageEditText,
                heightEditText,
                weightEditText
            )
        } ?: run {
            Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
            finish()
        }

        saveButton.setOnClickListener {
            val updatedUsername = usernameEditText.text.toString()
            val updatedSurname = surnameEditText.text.toString()
            val updatedBloodType = bloodTypeEditText.text.toString()
            val updatedCity = cityEditText.text.toString()
            val updatedTcNo = tcNoEditText.text.toString()
            val updatedGender = genderEditText.text.toString()
            val updatedAge = ageEditText.text.toString()
            val updatedHeight = heightEditText.text.toString()
            val updatedWeight = weightEditText.text.toString()

            currentUser?.let {
                saveUserInfo(
                    it.uid,
                    updatedUsername,
                    updatedSurname,
                    updatedBloodType,
                    updatedCity,
                    updatedTcNo,
                    updatedGender,
                    updatedAge,
                    updatedHeight,
                    updatedWeight
                )
            }
        }
    }

    private fun saveUserInfo(
        uid: String,
        username: String,
        surname: String,
        bloodType: String,
        city: String,
        tcNo: String,
        gender: String,
        age: String,
        height: String,
        weight: String
    ) {
        val userInfo = hashMapOf(
            "username" to username, // "username" olarak güncellendi
            "surname" to surname,
            "bloodType" to bloodType,
            "city" to city,
            "tcNo" to tcNo,
            "gender" to gender,
            "age" to age,
            "height" to height,
            "weight" to weight
        )

        db.collection("users").document(uid)
            .set(userInfo, SetOptions.merge()) // Var olan veriyi korumak için merge kullanılıyor
            .addOnSuccessListener {
                Log.d("p_MyInfoActivity", "Bilgiler başarıyla kaydedildi.")
                Toast.makeText(this, "Bilgiler başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("p_MyInfoActivity", "Bilgiler kaydedilemedi", e)
                Toast.makeText(this, "Bilgiler kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo(
        uid: String,
        usernameEditText: EditText,
        surnameEditText: EditText,
        bloodTypeEditText: EditText,
        cityEditText: EditText,
        tcNoEditText: EditText,
        genderEditText: EditText,
        ageEditText: EditText,
        heightEditText: EditText,
        weightEditText: EditText
    ) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("p_MyInfoActivity", "Kullanıcı verileri yüklendi.")
                    usernameEditText.setText(document.getString("username") ?: "")
                    surnameEditText.setText(document.getString("surname") ?: "")
                    bloodTypeEditText.setText(document.getString("bloodType") ?: "")
                    cityEditText.setText(document.getString("city") ?: "")
                    tcNoEditText.setText(document.getString("tcNo") ?: "")
                    genderEditText.setText(document.getString("gender") ?: "")
                    ageEditText.setText(document.getString("age") ?: "")
                    heightEditText.setText(document.getString("height") ?: "")
                    weightEditText.setText(document.getString("weight") ?: "")
                } else {
                    Log.d("p_MyInfoActivity", "Belge bulunamadı.")
                    Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("p_MyInfoActivity", "Bilgiler yüklenemedi", e)
                Toast.makeText(this, "Bilgiler yüklenemedi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
