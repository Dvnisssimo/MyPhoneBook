package com.example.myphonebook

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_KEY = "EXTRA"
    }

    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        setTitle("Детали")

        fun callPhone(phoneNumber: String) {
            if (phoneNumber[0] == '7')
            {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:+$phoneNumber"))
                startActivity(intent)
            }
            else
            {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                startActivity(intent)
            }

        }

        fun dialogYesOrNo(
            activity: Activity,
            title: String,
            message: String,
            listener: DialogInterface.OnClickListener
        ) {
            val builder = AlertDialog.Builder(activity)
            builder.setPositiveButton("Да", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                listener.onClick(dialog, id)
            })
            builder.setNegativeButton("Нет", null)
            val alert = builder.create()
            alert.setTitle(title)
            alert.setMessage(message)
            alert.show()
        }
        val id = intent.getLongExtra(MainActivity.EXTRA_KEY, -1)
        val firstNameText = findViewById<TextView>(R.id.textFirstName)
        val lastNameText = findViewById<TextView>(R.id.textLastName)
        val birthDayDate = findViewById<TextView>(R.id.textBirthdayDate)
        val phoneNumberText = findViewById<TextView>(R.id.textPhoneNumber)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        val contactDao = contactDatabase
        val contactEntity = contactDatabase.getById(id)
        firstNameText.text = firstNameText.text.toString() + " " + contactEntity.firstName
        lastNameText.text = lastNameText.text.toString() + " " + contactEntity.lastName
        birthDayDate.text = birthDayDate.text.toString() + " " + contactEntity.birthdayDate
        phoneNumberText.text = phoneNumberText.text.toString() + " " + contactEntity.phoneNumber
        buttonBack.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }

        buttonEdit.setOnClickListener {
            val startContactActivity = Intent(this, EditContacts::class.java)
            startContactActivity.putExtra(EXTRA_KEY, id)
            startActivity(startContactActivity)
            finish()
        }
        buttonDelete.setOnClickListener {
            val conEntity = contactDatabase.getById(id)
            dialogYesOrNo(
                this,
                "Подтвердите действие",
                "Удалить контакт ${conEntity.firstName} ${conEntity.lastName}?",
                DialogInterface.OnClickListener {dialog,id ->
                    contactDao.delete(contactEntity)
                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    finish()
                })
        }
    }
}