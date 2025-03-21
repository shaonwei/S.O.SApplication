package com.example.sosapplication.nouse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Communicator : ViewModel() {

    val message = MutableLiveData<Any>()

    fun setMsgCommunicator(msg: String) {
        message.setValue(msg)
    }
}