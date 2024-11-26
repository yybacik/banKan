package com.atlantis.bankan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar'ı ActionBar olarak ayarla
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // DrawerLayout ve NavigationView'i bul
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // Hamburger menü için ActionBarDrawerToggle kullanımı
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,  // Navigation açıldığında gösterilecek açıklama
            R.string.close_nav  // Navigation kapandığında gösterilecek açıklama
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()  // Hamburger ikonunu senkronize et, görünmesini sağlar

        // NavigationView'e tıklama olaylarını ekle
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()


                    // Yeni Activity'yi başlatmak
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_share -> {
                    Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, ShareActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    Toast.makeText(this, "About Us clicked", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, InfoActivity::class.java)
                    startActivity(intent)
                }







            }
            drawerLayout.closeDrawer(GravityCompat.START) // Menü tıklanınca otomatik kapanır
            true
        }
    }

    // Geri tuşuna basıldığında, menü açıksa önce menüyü kapat
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}