package com.example.sosapplication.nouse

import com.wafflecopter.multicontactpicker.ContactResult

data class SOSMessage (
    val text: String,
    val contacts: List<ContactResult>?,
    val location: Boolean
){
    companion object
}
