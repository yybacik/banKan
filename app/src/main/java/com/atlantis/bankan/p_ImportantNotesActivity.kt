package com.atlantis.bankan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.atlantis.bankan.adapters.NotesAdapter
import com.atlantis.bankan.databinding.ActivityPimportantNotesBinding
import com.atlantis.bankan.models.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class p_ImportantNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPimportantNotesBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var notesAdapter: NotesAdapter
    private var notesListener: ListenerRegistration? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPimportantNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        setupAddNoteButton()
        fetchNotes(currentUser.uid)
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter { note ->
            deleteNote(note)
        }
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@p_ImportantNotesActivity)
            adapter = notesAdapter
        }
    }

    private fun setupAddNoteButton() {
        binding.addNoteButton.setOnClickListener {
            val noteText = binding.noteInput.text.toString().trim()
            if (noteText.isNotEmpty()) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    addNoteToFirestore(noteText, currentUser.uid)
                } else {
                    Toast.makeText(this, "Kullanıcı oturumu kapalı.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Not boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNoteToFirestore(text: String, userId: String) {
        val note = Note(text = text, userId = userId)
        firestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Not eklendi", Toast.LENGTH_SHORT).show()
                binding.noteInput.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Not eklenirken hata oluştu", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchNotes(userId: String) {
        notesListener = firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .orderBy("text")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Veriler alınırken hata oluştu", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val notes = snapshots.documents.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.copy(id = doc.id)
                    }
                    notesAdapter.submitList(notes)
                }
            }
    }

    private fun deleteNote(note: Note) {
        firestore.collection("notes").document(note.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Not silindi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Not silinirken hata oluştu", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        notesListener?.remove()
    }
}
