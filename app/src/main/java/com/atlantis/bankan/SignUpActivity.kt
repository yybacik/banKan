package com.atlantis.bankan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Firebase Initialization
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI Elements
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val signInButton: Button = findViewById(R.id.signInButton)

        // Sign Up Button click listener
        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Şifreler eşleşmiyor!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkUsernameAndEmail(username, email) { isUnique ->
                if (isUnique) {
                    registerUser(username, email, password)
                } else {
                    Toast.makeText(this, "Kullanıcı adı veya e-posta zaten kayıtlı!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signInButton.setOnClickListener {

            val intent1 =Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent1)
            finish()

        }

    }

    private fun checkUsernameAndEmail(username: String, email: String, onComplete: (Boolean) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { usernameDocs ->
                if (usernameDocs.isEmpty) {
                    firestore.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { emailDocs ->
                            onComplete(emailDocs.isEmpty)
                        }
                        .addOnFailureListener {
                            onComplete(false)
                        }
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "uid" to user?.uid
                    )

                    firestore.collection("users")
                        .document(user!!.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Kayıt başarılı! Giriş ekranına yönlendiriliyorsunuz.", Toast.LENGTH_SHORT).show()
                            navigateToSignIn()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Kullanıcı verileri kaydedilirken hata oluştu!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Kayıt hatası: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

}
