package com.example.sosapplication

import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.logging.Logger

class SharedViewModel : ViewModel() {

    /*private var _contacts = MutableLiveData<ArrayList<Contact>>()//list of contacts
    val contacts: LiveData<ArrayList<Contact>> = _contacts*/

    private var _message = MutableLiveData<String>("default")
    val message: LiveData<String> = _message//readonly

    private var _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> = _contact//readonly

    fun changeMessage(newMessage: String) {
        _message.value = newMessage
    }

    fun changeContact(newContact: Contact) {
        _contact.value = newContact
    }

    fun sendSMS() {
        val smsManager = SmsManager.getDefault() as SmsManager
        Logger.getLogger(MainActivity::class.java.name).warning("send sms:" + _contact.value)

        smsManager.sendTextMessage(_contact.value?.phoneNumber.toString(), null, _message.value, null, null)

        //  smsManager.sendTextMessage(number, null, _message.value, null, null)
        Logger.getLogger(MainActivity::class.java.name).warning("send sms:" + _message.value)
    }
}