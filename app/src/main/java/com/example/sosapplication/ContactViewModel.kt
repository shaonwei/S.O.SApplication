package com.example.sosapplication

import android.app.Application
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Logger

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    val allContacts: LiveData<List<Contact>>
    val repository: ContactRepository

    init {
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contact)
    }


    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }

     fun sendSMS() {
        val smsManager = SmsManager.getDefault() as SmsManager
        Logger.getLogger(MainActivity::class.java.name).warning("sending sms")
        for (contact in allContacts.value!!) {

            smsManager.sendTextMessage(contact.phoneNumber.toString(), null, contact.text, null, null)
            Logger.getLogger(MainActivity::class.java.name).warning("send sms to" + contact.id + "," + contact.text)
        }

        //  smsManager.sendTextMessage(number, null, _message.value, null, null)
    }
}