package com.example.myphonebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_KEY = "EXTRA"
    }

    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    private lateinit var adapter: RecyclerAdapter
    var ListOfContacts = mutableListOf<ContactEntity>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactDao = contactDatabase
        setContentView(R.layout.activity_main)
        setTitle("Контакты")
        adapter = RecyclerAdapter(ListOfContacts) {
            val intent = Intent(this, ContactActivity::class.java)
            intent.putExtra(EXTRA_KEY, ListOfContacts[it].id)
            startActivity(intent)
            finish()
        }
        val RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        ListOfContacts.clear()
        ListOfContacts.addAll(contactDao.all)
        val ContactFind = findViewById<EditText>(R.id.EditText)
        val buttonCreateContact = findViewById<Button>(R.id.button)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        RecyclerView.adapter = adapter
        buttonCreateContact.setOnClickListener() {
            val editContactsActivity = Intent(this, EditContacts::class.java)
            startActivity(editContactsActivity)
            finish()
        }

        ContactFind.doAfterTextChanged {
            adapter.updateAdapter(ListOfContacts.filter {
                ContactFind.text.toString().lowercase() in it.firstName.lowercase() ||
                        ContactFind.text.toString().lowercase() in it.lastName.lowercase()
            })
            adapter.run { notifyDataSetChanged() }
        }
    }
}