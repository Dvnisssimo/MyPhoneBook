package com.example.myphonebook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var firstName: String = ""
    var lastName: String = ""
    var birthdayDate: String = ""
    var phoneNumber: String = ""
}
