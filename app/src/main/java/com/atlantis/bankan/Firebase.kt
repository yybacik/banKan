package com.atlantis.bankan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Firebase {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Kullanıcı kaydı
    fun signUp(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception?.message)
                }
            }
    }

    // Kullanıcı girişi
    fun signIn(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception?.message)
                }
            }
    }

    // Mevcut kullanıcıyı getir
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Çıkış yap
    fun signOut() {
        auth.signOut()
    }







}