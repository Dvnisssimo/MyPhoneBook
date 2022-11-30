package com.example.myphonebook

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class EditContacts : AppCompatActivity() {

    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        setTitle("Редактор контакта")
        val id = intent.getLongExtra(ContactActivity.EXTRA_KEY, -1)
        val textEditName = findViewById<EditText>(R.id.editTextName)
        val textEditLastName = findViewById<EditText>(R.id.editTextLastName)
        val textEditBirthday = findViewById<EditText>(R.id.editTextBirthday)
        textEditBirthday.setInputType(InputType.TYPE_NULL)
        val textEditPNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val buttonDone = findViewById<Button>(R.id.buttonDone)
        textEditBirthday.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            var month = 0
            var day = 0
            var year = 0
            val cal = Calendar.getInstance()
            if (id >= 0) {
                val conEntity = contactDatabase.getById(id)//0/1/
                day = conEntity.birthdayDate.subSequence(0, 2).toString().replace("/", "").toInt()
                if (day < 10) {
                    month = conEntity.birthdayDate.subSequence(2, 4).toString().replace("/", "")
                        .toInt() - 1 
                    year = (conEntity.birthdayDate.subSequence(
                        4,
                        conEntity.birthdayDate.length
                    )).toString().replace("/", "").toInt()
                } else {
                    month = conEntity.birthdayDate.subSequence(2, 5).toString().replace("/", "")
                        .toInt() - 1 
                    year = (conEntity.birthdayDate.subSequence(
                        5,
                        conEntity.birthdayDate.length
                    )).toString().replace("/", "").toInt()
                }

            } else {
                year = cal.get(Calendar.YEAR)
                month = cal.get(Calendar.MONTH)
                day = cal.get(Calendar.DAY_OF_MONTH)
            }
            val dateDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                    val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    textEditBirthday.setText(date)
                },
                year, month, day
            )
            val maxDate = Calendar.getInstance()
            val maxDay = cal.get(Calendar.DAY_OF_MONTH)
            val maxMonth = cal.get(Calendar.MONTH)
            val maxYear = cal.get(Calendar.YEAR)
            maxDate.set(maxYear, maxMonth - 1, maxDay)
            dateDialog.datePicker.maxDate = maxDate.timeInMillis
            dateDialog.show()
        }

        if (id >= 0) {
            val conEntity = contactDatabase.getById(id)
            textEditName.setText(conEntity.firstName)
            textEditLastName.setText(conEntity.lastName)
            textEditBirthday.setText(conEntity.birthdayDate)
            textEditPNumber.setText(conEntity.phoneNumber)
        }
        var contactEntity: ContactEntity = ContactEntity()
        var buttonBack = findViewById<Button>(R.id.buttonClose)

        buttonBack.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }

        buttonDone.setOnClickListener {
            if ((!textEditName.text.isEmpty()) && (!textEditLastName.text.isEmpty())
                && (!textEditBirthday.text.isEmpty()) && (!textEditPNumber.text.isEmpty())
            ) {
                if (id >= 0)    //update
                {
                    contactEntity.id = id
                    contactEntity.firstName = textEditName.text.toString()
                    contactEntity.lastName = textEditLastName.text.toString()
                    contactEntity.birthdayDate = textEditBirthday.text.toString()
                    contactEntity.phoneNumber = textEditPNumber.text.toString()
                    contactDatabase.update(contactEntity)
                    val startContactActivity = Intent(this, ContactActivity::class.java)
                    startContactActivity.putExtra(ContactActivity.EXTRA_KEY, id)
                    startActivity(startContactActivity)
                    finish()
                } else {         //insert
                    contactEntity.firstName = textEditName.text.toString()
                    contactEntity.lastName = textEditLastName.text.toString()
                    contactEntity.birthdayDate = textEditBirthday.text.toString()
                    contactEntity.phoneNumber = textEditPNumber.text.toString()
                    contactDatabase.insert(contactEntity)
                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    finish()
                }
            }else{
                val toast = Toast.makeText(
                    applicationContext, // контекст
                    "Пожалуйста, заполните все поля",     // текст тоста
                    Toast.LENGTH_SHORT  // длительность показа
                )
                toast.show()
            }
        }
    }
}
