package com.atlantis.bankan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupDrawer()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem.itemId)
            true
        }
    }

    private fun handleDrawerNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_profile -> navigateTo(ProfileActivity::class.java, "Profile clicked")
            R.id.nav_settings -> navigateTo(SettingsActivity::class.java, "Settings clicked")
            R.id.nav_share -> navigateTo(ShareActivity::class.java, "Share clicked")
            R.id.nav_about -> navigateTo(InfoActivity::class.java, "About Us clicked")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            handleBottomNavigation(menuItem.itemId)
            true
        }
    }

    private fun handleBottomNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_kizilay -> navigateTo(MapActivity::class.java, "Kızılay Bağış Noktaları")
            R.id.nav_home -> navigateTo(MainActivity::class.java, "Anasayfa")
            R.id.nav_duyurular -> navigateTo(AnnouncementsActivity::class.java, "Duyurular")
        }
    }

    private fun navigateTo(activityClass: Class<*>, message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, activityClass))
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    fun openWebsite(view: View) {
        val url = when (view.id) {
            R.id.exploreMore -> "https://www.memorial.com.tr/saglik-rehberi/kan-vermenin-faydalari"  // Neden Kan Vermeliyiz? için URL
            R.id.requestNow -> "https://www.kanver.org/sayfa/kan-hizmetleri/kan-bagisi-nasil-gerceklesir/52" // Kan Bağış Süreci için URL
            R.id.checkEvents -> "https://gonulluol.org/tr" // Kızılay Etkinlikleri ve Kızılay Gönüllüsü olmak için URL
            else -> ""  // Geçersiz bir durum için boş bir URL
        }

        // Eğer url geçerli değilse, işlemi yapma
        if (url.isNotEmpty()) {
            val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent2)
        }
    }


}
