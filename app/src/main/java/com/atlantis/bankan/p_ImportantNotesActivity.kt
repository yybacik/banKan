package com.atlantis.bankan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class p_ImportantNotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pimportant_notes)

        val noteInput = findViewById<EditText>(R.id.note_input)
        val addNoteButton = findViewById<Button>(R.id.add_note_button)
        val notesContainer = findViewById<LinearLayout>(R.id.notes_container)

        addNoteButton.setOnClickListener {
            val noteText = noteInput.text.toString()
            if (noteText.isNotEmpty()) {
                val noteView = createNoteView(noteText)
                notesContainer.addView(noteView)
                noteInput.text.clear()
            }
        }
    }

    private fun createNoteView(noteText: String): LinearLayout {
        val noteLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8, 8, 8, 8)
        }

        val noteTextView = EditText(this).apply {
            setText(noteText)
            textSize = 16f
            setPadding(8, 8, 8, 8)
            setBackgroundResource(R.drawable.note_background)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val deleteButton = Button(this).apply {
            text = "X"
            textSize = 12f
            setBackgroundResource(R.drawable.red_button_background)
            layoutParams = LinearLayout.LayoutParams(64, 64).apply {
                setMargins(8, 0, 0, 0) // Butonun sol boşluğu
            }
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0) // Buton iç boşlukları sıfırlandı
            setOnClickListener {
                (noteLayout.parent as LinearLayout).removeView(noteLayout)
            }
        }

        noteLayout.addView(noteTextView)
        noteLayout.addView(deleteButton)

        return noteLayout
    }
}
