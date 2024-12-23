package com.atlantis.bankan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class p_MyInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pmy_info)

        val nameSurname = "Yusuf Yiğit Bacık"
        val bloodType = "0+"
        val city = "Trabzon"
        val tcNo = "12345678901"
        val gender = "Erkek"
        val age = "22"
        val height = "170 cm"
        val weight = "56 kg"



        val nameSurnameEditText = findViewById<EditText>(R.id.name_surname_edit_text)
        val bloodTypeEditText = findViewById<EditText>(R.id.blood_type_edit_text)
        val cityEditText = findViewById<EditText>(R.id.city_edit_text)
        val tcNoEditText = findViewById<EditText>(R.id.tcno_edit_text)
        val genderEditText = findViewById<EditText>(R.id.gender_edit_text)
        val ageEditText = findViewById<EditText>(R.id.age_edit_text)
        val heightEditText = findViewById<EditText>(R.id.height_edit_text)
        val weightEditText = findViewById<EditText>(R.id.weight_edit_text)
        val saveButton = findViewById<Button>(R.id.save_button)

        nameSurnameEditText.setText(nameSurname)
        bloodTypeEditText.setText(bloodType)
        cityEditText.setText(city)
        tcNoEditText.setText(tcNo)
        genderEditText.setText(gender)
        ageEditText.setText(age)
        heightEditText.setText(height)
        weightEditText.setText(weight)

        saveButton.setOnClickListener {
            val updatedNameSurname = nameSurnameEditText.text.toString()
            val updatedBloodType = bloodTypeEditText.text.toString()
            val updatedCity = cityEditText.text.toString()
            val updatedTcNo = tcNoEditText.text.toString()
            val updatedGender = genderEditText.text.toString()
            val updatedAge = ageEditText.text.toString()
            val updatedHeight = heightEditText.text.toString()
            val updatedWeight = weightEditText.text.toString()


            saveUserInfo(
                updatedNameSurname,
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

    private fun saveUserInfo(
        nameSurname: String, bloodType: String, city: String, tcNo: String,
        gender: String, age: String, height: String, weight: String
    ) {
        // Bu bilgileri veritabanına veya SharedPreferences'e kaydedin.
        // Örnek: SharedPreferences veya Room kullanabilirsiniz.
        // Burada bir log ile kontrol edilebilir.
        println("Bilgiler kaydedildi: $nameSurname, $bloodType, $city, $tcNo, $gender, $age, $height, $weight")
    }
}
