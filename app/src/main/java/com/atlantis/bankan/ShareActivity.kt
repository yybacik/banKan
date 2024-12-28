package com.atlantis.bankan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.google.firebase.firestore.firestoreSettings

class ShareActivity : AppCompatActivity() {

    private lateinit var tvAppShareInfo: TextView
    private lateinit var tvAppShareMessage: TextView
    private lateinit var btnShare: Button

    private lateinit var btnCreatePdf: Button
    private lateinit var btnDownloadPdf: Button
    private lateinit var btnSharePdf: Button

    private var pdfFile: File? = null

    companion object {
        private const val CREATE_FILE_REQUEST_CODE = 1001
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 2001
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        tvAppShareInfo = findViewById(R.id.tv_app_share_info)
        tvAppShareMessage = findViewById(R.id.tv_app_share_message)
        btnShare = findViewById(R.id.btn_share)
        btnCreatePdf = findViewById(R.id.btn_create_pdf)
        btnDownloadPdf = findViewById(R.id.btn_download_pdf)
        btnSharePdf = findViewById(R.id.btn_share_pdf)

        tvAppShareInfo.text = "BanKan'ı Paylaş"
        tvAppShareMessage.text = "Hayat kurtarmak için bir adım atın! BanKan uygulamasını paylaşarak topluluğumuza destek olabilirsiniz!"
        btnShare.text = "Profil Paylaş"
        btnCreatePdf.text = "PDF Oluştur"
        btnDownloadPdf.text = "PDF İndir"
        btnSharePdf.text = "PDF Paylaş"

        btnShare.setOnClickListener {
            shareTextMessage()
        }

        btnCreatePdf.setOnClickListener {
            fetchUserDataAndCreatePdf()
        }

        btnDownloadPdf.setOnClickListener {
            if (pdfFile == null) {
                Toast.makeText(this, "Lütfen önce PDF oluşturun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val filename = pdfFile!!.name
                createPdfInSAF(filename)
            } else {
                checkAndRequestPermissions()
            }
        }

        btnSharePdf.setOnClickListener {
            pdfFile?.let {
                sharePdf(it)
            } ?: Toast.makeText(this, "Önce PDF oluşturun!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDataAndCreatePdf() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Kullanıcı oturumu kapalı!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username") ?: "Bilinmiyor"
                    val name = document.getString("firstname") ?: "Bilinmiyor"
                    val surname = document.getString("surname") ?: "Bilinmiyor"
                    val email = document.getString("email") ?: "Bilinmiyor"
                    val tcNo = document.getString("tcNo") ?: "Bilinmiyor"
                    val gender = document.getString("gender") ?: "Bilinmiyor"
                    val bloodType = document.getString("bloodType") ?: "Bilinmiyor"
                    val age = document.getString("age") ?: "Bilinmiyor"

                    val phone = document.getString("phone") ?: "Bilinmiyor"
                    val additional = document.getString("additionalData") ?: "Bilinmiyor"

                    pdfFile = createPdfForUser(
                        context = this,
                        username = username,
                        name = name,
                        surname = surname,
                        tcNo = tcNo,
                        gender = gender,
                        bloodType = bloodType,
                        age = age,
                        userEmail = email,
                        userPhone = phone,
                        additionalData = additional
                    )

                    if (pdfFile != null) {
                        Toast.makeText(this, "PDF oluşturuldu: ${pdfFile?.name}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "PDF oluşturulamadı!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Kullanıcı verisi bulunamadı!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                Toast.makeText(this, "Veri alınırken hata oluştu: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun shareTextMessage() {
        val shareMessage = "Bankan uygulamasıyla profilimi paylaşıyorum!"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(shareIntent, "Paylaşım Seçenekleri"))
    }


    private fun sharePdf(file: File) {
        try {
            // BuildConfig.APPLICATION_ID yerine context.packageName kullanıyoruz
            val authority = "${applicationContext.packageName}.provider"
            val uri: Uri = FileProvider.getUriForFile(
                this,
                authority,
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "PDF Paylaş"))
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Toast.makeText(this, "Dosya paylaşılırken bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun createPdfInSAF(filename: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, filename)
        }
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                pdfFile?.let { file ->
                    contentResolver.openOutputStream(uri)?.use { output ->
                        file.inputStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    Toast.makeText(this, "PDF kaydedildi: $uri", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun savePdfToDownloads(pdfFile: File): Boolean {
        return try {
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val targetFile = File(downloadsPath, pdfFile.name)

            pdfFile.inputStream().use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {

                val success = savePdfToDownloads(pdfFile!!)
                if (success) {
                    Toast.makeText(this, "PDF Downloads klasörüne kopyalandı.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "PDF kopyalanamadı!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {

            val success = savePdfToDownloads(pdfFile!!)
            if (success) {
                Toast.makeText(this, "PDF Downloads klasörüne kopyalandı.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "PDF kopyalanamadı!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val success = savePdfToDownloads(pdfFile!!)
                if (success) {
                    Toast.makeText(this, "PDF Downloads klasörüne kopyalandı.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "PDF kopyalanamadı!", Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(this, "İzin verilmedi. PDF'i kopyalayamıyorum.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun createPdfForUser(
        context: Context,
        username: String,
        name: String,
        surname: String,
        tcNo: String,
        gender: String,
        bloodType: String,
        age: String,
        userEmail: String,
        userPhone: String,
        additionalData: String
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 boyutu
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 16f
        }

        var currentY = 100f
        canvas.drawText("Kullanıcı Adı: $username", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Adı: $name", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Soyadı: $surname", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("T.C. No: $tcNo", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Cinsiyet: $gender", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Kan Grubu: $bloodType", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Yaş: $age", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("E-Posta: $userEmail", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Telefon: $userPhone", 50f, currentY, paint)
        currentY += 40f
        canvas.drawText("Ek Bilgi: $additionalData", 50f, currentY, paint)

        pdfDocument.finishPage(page)

        val pdfFile = File(context.filesDir, "user_${username}.pdf")

        return try {
            pdfFile.outputStream().use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            pdfDocument.close()
            pdfFile
        } catch (e: IOException) {
            e.printStackTrace()
            pdfDocument.close()
            null
        }
    }
}
