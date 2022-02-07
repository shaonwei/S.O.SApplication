package com.example.sosapplication

import androidx.lifecycle.ViewModel

class AppViewModel:ViewModel() {

    var text="text"
    fun changeText(newText:String){
        text=newText
    }

}