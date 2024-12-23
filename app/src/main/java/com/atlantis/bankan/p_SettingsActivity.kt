package com.atlantis.bankan

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class p_SettingsActivity : AppCompatActivity() {

    private val colors = listOf("#FFC107", "#03DAC5", "#BB86FC") // Sarı, Yeşil, Mor
    private var colorIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psettings)


        val themeSwitch = findViewById<Switch>(R.id.theme_switch)
        val notificationsSwitch = findViewById<Switch>(R.id.notifications_switch)
        val languageSpinner = findViewById<Spinner>(R.id.language_spinner)
        val sizeSeekBar = findViewById<SeekBar>(R.id.size_seekbar)
        val colorChangeButton = findViewById<Button>(R.id.color_change_button)
        val rootLayout = findViewById<LinearLayout>(R.id.root_layout) // Doğrudan LinearLayout kullanıldı
        val titleTextView = findViewById<TextView>(R.id.title_text_view) // TextView için ayrı değişken


        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Karanlık Tema Aktif Edildi" else "Açık Tema Aktif Edildi"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }


        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Bildirimler Açıldı" else "Bildirimler Kapatıldı"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }


        val languages = listOf("Türkçe", "İngilizce", "Fransızca", "Almanca")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                try {
                    view?.let {
                        Toast.makeText(applicationContext, "Dil: ${languages[position]}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("SpinnerError", "Hata: ${e.message}")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        sizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    val textSize = 12 + progress
                    titleTextView.textSize = textSize.toFloat()
                } catch (e: Exception) {
                    Log.e("SeekBarError", "Hata: ${e.message}")
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        colorChangeButton.setOnClickListener {
            try {
                colorIndex = (colorIndex + 1) % colors.size
                rootLayout.setBackgroundColor(Color.parseColor(colors[colorIndex]))
            } catch (e: Exception) {
                Log.e("ColorChangeError", "Hata: ${e.message}")
            }
        }
    }
}
