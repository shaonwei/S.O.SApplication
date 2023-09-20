package com.example.sosapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PowerButtonReceiver(private val callback: PowerButtonCallback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON || intent?.action == Intent.ACTION_SCREEN_OFF) {
            val activity = context as? MainActivity
            activity?.contactViewModel?.sendSMS() // Call the function in the shared ViewModel
        }
    }
}