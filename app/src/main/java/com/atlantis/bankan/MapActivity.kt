package com.atlantis.bankan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // MapView'i bağlama
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Kızılay Trabzon Bağış Noktalarının Koordinatları
        val kizilayTrabzon1 = LatLng(41.0000, 39.7228)
        googleMap.addMarker(MarkerOptions().position(kizilayTrabzon1).title("Türk Kızılayı Trabzon Şubesi Kan Bağış Merkezi"))

        val kizilayTrabzon2 = LatLng(41.0010, 39.7265)
        googleMap.addMarker(MarkerOptions().position(kizilayTrabzon2).title("Türk Kızılayı Trabzon Kan Bağış Merkezi (Meydan Şubesi)"))

        val kizilayAkcaabat = LatLng(40.9534, 39.5721)
        googleMap.addMarker(MarkerOptions().position(kizilayAkcaabat).title("Türk Kızılayı Akçaabat Kan Bağış Noktası"))

        val kizilayVakfikebir = LatLng(40.9385, 39.2778)
        googleMap.addMarker(MarkerOptions().position(kizilayVakfikebir).title("Türk Kızılayı Vakfıkebir Kan Bağış Noktası"))

        val kizilayOrtahisar = LatLng(41.0084, 39.7260)
        googleMap.addMarker(MarkerOptions().position(kizilayOrtahisar).title("Türk Kızılayı Ortahisar Kan Bağış Noktası"))

        //Trabzondaki Hastaneler

        val hastaneTrabzon1 = LatLng(41.0027, 39.7107)
        googleMap.addMarker(MarkerOptions().position(hastaneTrabzon1).title("Karadeniz Teknik Üniversitesi Farabi Hastanesi"))

        val hastaneTrabzon2 = LatLng(41.0045, 39.7213)
        googleMap.addMarker(MarkerOptions().position(hastaneTrabzon2).title("Trabzon Ahi Evren Göğüs Kalp ve Damar Cerrahisi Eğitim ve Araştırma Hastanesi"))

        val hastaneTrabzon3 = LatLng(41.0056, 39.7261)
        googleMap.addMarker(MarkerOptions().position(hastaneTrabzon3).title("Trabzon Kanuni Eğitim ve Araştırma Hastanesi"))

        val hastaneAkcaabat = LatLng(40.9500, 39.5700)
        googleMap.addMarker(MarkerOptions().position(hastaneAkcaabat).title("Akçaabat Haçkalı Baba Devlet Hastanesi"))

        val hastaneVakfikebir = LatLng(40.9342, 39.2735)
        googleMap.addMarker(MarkerOptions().position(hastaneVakfikebir).title("Vakfıkebir Devlet Hastanesi"))

        val hastaneOf = LatLng(40.9466, 40.2555)
        googleMap.addMarker(MarkerOptions().position(hastaneOf).title("Of Devlet Hastanesi"))

        val hastaneArakli = LatLng(40.9380, 40.0490)
        googleMap.addMarker(MarkerOptions().position(hastaneArakli).title("Araklı Bayram Halil Devlet Hastanesi"))

        val trabzonMerkez = LatLng(41.0027, 39.7168)
        googleMap.addMarker(MarkerOptions().position(trabzonMerkez).title("Trabzon Şehir Merkezi"))



        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trabzonMerkez, 15f))

    }

    // MapView Yaşam Döngüsü Fonksiyonları
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}