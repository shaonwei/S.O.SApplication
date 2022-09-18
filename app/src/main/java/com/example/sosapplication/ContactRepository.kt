package com.example.sosapplication

import androidx.lifecycle.LiveData

class ContactRepository (private val contactsDao: ContactsDao){

    val allContacts: LiveData<List<Contact>> = contactsDao.getAllContact()

    suspend fun insert(contact: Contact) {
        contactsDao.insert(contact)
    }

    suspend fun delete(contact: Contact){
        contactsDao.delete(contact)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(contact: Contact){
        contactsDao.update(contact)
    }
}