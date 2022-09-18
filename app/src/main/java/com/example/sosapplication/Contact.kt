package com.example.sosapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactTable")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String="",
    var phoneNumber: String="",
    var text: String ="default"
)