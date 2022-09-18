package com.example.sosapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactTable")
data class Contact(
    var name: String="",
    var phoneNumber: String="",
    var text: String ="default"
){
    @PrimaryKey(autoGenerate = true) var id = 0

}
