package com.example.sosapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel (application: Application): AndroidViewModel(application){
    val allContacts:LiveData<List<Contact>>
    val repository:ContactRepository

    init {
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun deleteContact (contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contact)
    }


    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }
}