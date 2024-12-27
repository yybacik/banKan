package com.atlantis.bankan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atlantis.bankan.databinding.ActivityAnnouncementAddBinding


class AnnouncementAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnnouncementAddBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityAnnouncementAddBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val city = AdressIC()
        val district = AdressIC()
        val hospital = AdressIC()


        val spinnerCity: Spinner= binding.spinnerCity
        val spinnerDistrict: Spinner = binding.spinnerDistrict
        val spinnerHospital: Spinner = binding.spinnerHospital
        val spinnerBloodType: Spinner = binding.spinnerBloodGroup
        val editTextPhone: EditText = binding.editTextPhoneNumber
        val editTextGeneralInfo: EditText = binding.editTextGeneralInformation
        val buttonAddAnnouncement: Button = binding.buttonAdd
        val buttonClose: ImageButton = binding.buttonClose

        editTextPhone.inputType = InputType.TYPE_CLASS_NUMBER


        spinnerDistrict.visibility = View.GONE
        spinnerHospital.visibility = View.GONE


        val adapterCity = ArrayAdapter(this@AnnouncementAddActivity, R.layout.spinner_item, city.cities)
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapterCity


        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selectedCity = city.cities[position]

                if (selectedCity == "Şehir Seçilmemiş") {

                    spinnerDistrict.visibility = View.GONE
                    spinnerHospital.visibility = View.GONE
                } else {
                    spinnerDistrict.visibility = View.VISIBLE

                    val districts = district.cityDistrict[selectedCity] ?: emptyList()

                    val districtAdapter =
                        ArrayAdapter(this@AnnouncementAddActivity, R.layout.spinner_item, districts)
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerDistrict.adapter = districtAdapter
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {

                spinnerDistrict.visibility = View.GONE
            }
        }

        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selectedCity = spinnerCity.selectedItem.toString()
                val selectedDistrict = spinnerDistrict.selectedItem.toString()

                if (selectedDistrict == "İlçe Seçilmemiş") {
                    spinnerHospital.visibility = View.GONE

                } else {
                    val hospitals = hospital.districtHospital[selectedCity]?.get(selectedDistrict)
                        ?: emptyList()
                    if (hospitals.isEmpty()) {

                        spinnerHospital.visibility = View.GONE
                        Toast.makeText(this@AnnouncementAddActivity, "Bu ilçede hastane bulunmamaktadır.", Toast.LENGTH_SHORT).show()
                    } else {

                        spinnerHospital.visibility = View.VISIBLE
                        val hospitalAdapter = ArrayAdapter(this@AnnouncementAddActivity, R.layout.spinner_item2, hospitals)
                        hospitalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerHospital.adapter = hospitalAdapter
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinnerHospital.visibility = View.GONE
            }
        }

        // KAN GRUBU FİLTRELEME
        val kanBelirleme = BloodIC()

        val adapterKan = ArrayAdapter(this@AnnouncementAddActivity, R.layout.spinner_item, kanBelirleme.kanHarf)
        adapterKan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodType.adapter = adapterKan

        buttonAddAnnouncement.setOnClickListener {
            val city = spinnerCity.selectedItem.toString()
            val district = spinnerDistrict.selectedItem.toString()
            val hospital = spinnerHospital.selectedItem?.toString() ?: "Hastane Seçilmemiş"
            val bloodType = spinnerBloodType.selectedItem.toString()
            val phone = editTextPhone.text.toString()
            val generalInfo = editTextGeneralInfo.text.toString()


            if (city.isEmpty() || city == "Şehir Seçilmemiş") {
                Toast.makeText(this, "Lütfen bir şehir seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (district.isEmpty() || district == "İlçe Seçilmemiş") {
                Toast.makeText(this, "Lütfen bir ilçe seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (hospital.isEmpty() || hospital == "Hastane Seçilmemiş") {
                Toast.makeText(this, "Lütfen bir hastane seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (bloodType.isEmpty() || bloodType == "Kan Grubu Seçilmemiş") {
                Toast.makeText(this, "Lütfen bir kan grubu seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length != 10 || phone.isEmpty() || !phone.matches("\\d+".toRegex())) {
                Toast.makeText(this, "Lütfen telefon numarası girin. Numara sadece sayılardan oluşmalıdır ve 10 haneli olmalıdır!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (generalInfo.isEmpty()) {
                Toast.makeText(this, "Lütfen genel bilgi girin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val announcement = AnnouncementIC(city, district, hospital, bloodType, phone, generalInfo)
            val firestoreHelper = FireBaseFireStoreHelper()
            firestoreHelper.addAnnouncement(announcement, onSuccess = {
                Toast.makeText(this, "Duyuru başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("new_announcement", announcement)
                setResult(RESULT_OK, resultIntent)
                finish() //
            }, onFailure = { error ->
                Toast.makeText(this, "Hata: $error", Toast.LENGTH_SHORT).show()
            })

        }

        buttonClose.setOnClickListener{
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}