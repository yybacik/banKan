package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atlantis.bankan.databinding.ActivityAnnouncements2Binding
import com.atlantis.bankan.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.filterL)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val city=AdressIC()
        val district=AdressIC()
        val hospital=AdressIC()

        val spinnerCity : Spinner =binding.spinnerCity
        val spinnerDistrict: Spinner =binding.spinnerDistrict
        val spinnerHospital: Spinner =binding.spinnerHospital
        val spinnerBloodType: Spinner =binding.spinnerBloodGroup
        val buttonApplyFilter: ImageButton = binding.buttonClose

        spinnerDistrict.visibility = View.GONE
        spinnerHospital.visibility= View.GONE

        val adapterCity = ArrayAdapter(this@FilterActivity, R.layout.spinner_item,city.cities)
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


                    val districtAdapter = ArrayAdapter(this@FilterActivity, R.layout.spinner_item, districts)
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

                    val hospitals = hospital.districtHospital[selectedCity]?.get(selectedDistrict) ?: emptyList()
                    if (hospitals.isEmpty()) {

                        spinnerHospital.visibility = View.GONE
                        Toast.makeText(this@FilterActivity, "Bu ilçede hastane bulunmamaktadır.", Toast.LENGTH_SHORT).show()
                    } else {

                        spinnerHospital.visibility = View.VISIBLE
                        val hospitalAdapter = ArrayAdapter(this@FilterActivity,R.layout.spinner_item2, hospitals)
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
        val kanBelirleme=BloodIC()



        val adapterKan = ArrayAdapter(this@FilterActivity, R.layout.spinner_item2, kanBelirleme.kanHarf)
        adapterKan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodType.adapter = adapterKan

        buttonApplyFilter.setOnClickListener {
            val city = spinnerCity.selectedItem.toString()
            val district = spinnerDistrict.selectedItem?.toString()?:"İlçe Seçilmemiş"
            val hospital = spinnerHospital.selectedItem?.toString()?:"Hastane Seçilmemiş"
            val bloodType = spinnerBloodType.selectedItem?.toString()?:"Kan Grubu Seçilmemiş"

            val resultIntent = Intent()
            resultIntent.putExtra("selected_city", city)
            resultIntent.putExtra("selected_district", district)
            resultIntent.putExtra("selected_hospital", hospital)
            resultIntent.putExtra("selected_bloodType",bloodType)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}