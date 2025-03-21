package com.example.sosapplication

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Logger

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.example.sosapplication.MainActivity.Companion.REQUEST_CODE_LOCATION_PERMISSION
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    val allContacts: LiveData<List<Contact>>
    val repository: ContactRepository
    private val application = application
    private val sharedPreferences = application.baseContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    fun readDataFromSharedPreferences(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun writeToSharedPreferences(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply() // or editor.commit() for synchronous write
    }

    init {
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

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
        val value = readDataFromSharedPreferences("location", "default_value")
        if (value == "true") {
//            val location = getLocation()
            getLocation { locationMessage ->
                if (locationMessage != null) {

                    // Send the SMS with the map link here, e.g., by using locationMessage
                    for (contact in allContacts.value!!) {
                        val txt = "${contact.text} \n$locationMessage"
//                        val locationMessage = "${contact.text}  $locationMessage"

                        try {
                            smsManager.sendTextMessage(contact.phoneNumber.toString(), null, txt+"1", null, null)
                            Log.i("SMS", "SMS 1 sent $txt")
                        } catch (e: Exception) {
                            Log.e("SMS", "Error sending SMS: ${e.message}")
                        }
                        Logger.getLogger(MainActivity::class.java.name).warning("send sms to" + contact.id + ", " + locationMessage)
                    }
                } else {
                    Log.i("location", "location is null")

                    for (contact in repository.allContacts.value!!) {
                        smsManager.sendTextMessage(contact.phoneNumber.toString(), null, contact.text+"2", null, null)
                        Log.i("SMS", "SMS 2 sent $contact.txt")

                        Logger.getLogger(MainActivity::class.java.name).warning("send sms to" + contact.id + ", " + contact.text)
                    }
                }
            }
        } else {
            for (contact in repository.allContacts.value!!) {
                smsManager.sendTextMessage(contact.phoneNumber.toString(), null, contact.text+"3", null, null)
                Log.i("SMS", "SMS 3 sent $contact.txt")
                Logger.getLogger(MainActivity::class.java.name).warning("send sms to" + contact.id + ", " + contact.text)
            }
        }

        //  smsManager.sendTextMessage(number, null, _message.value, null, null)
    }


    /*private fun getLocation(): String? {{
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                // Create a Google Maps URL
                val mapUrl = "https://www.google.com/maps?q=$latitude,$longitude"

                // Include the map URL in the SMS message
                val locationMessage = "My current location: $mapUrl"
                Logger.getLogger(MainActivity::class.java.name).warning(locationMessage)
                return locationMessage
                // Send the SMS with the map link
//                smsManager.sendTextMessage(contact.phoneNumber.toString(), null, locationMessage, null, null)
            }

        }
        return null

    }
*/
    private fun getLocation(callback: (String?) -> Unit) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        application,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Request location permissions here if not granted
                    //  requestPermissions()
                    callback(null)
                    return
                }

                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val mapUrl = "https://www.google.com/maps?q=$latitude,$longitude"
                        callback(mapUrl)
                    } else {
                        // Last known location is null, request location updates
                        val locationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000) // 10 seconds
                            .setFastestInterval(5000) // 5 seconds

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                super.onLocationResult(locationResult)
                                locationResult?.let {
                                    for (loc in it.locations) {
                                        val latitude = loc.latitude
                                        val longitude = loc.longitude
                                        val mapUrl = "https://www.google.com/maps?q=$latitude,$longitude"
                                        callback(mapUrl)
                                        return
                                    }
                                }
                                // If locationResult is null or empty, handle the case here
                                callback(null)
                            }
                        }

                        // Request location updates
                        mFusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            } else {
                Toast.makeText(application, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                application.startActivity(intent)
                callback(null)
            }
        } else {
            // Request location permissions here if not granted
            // requestPermissions()
            callback(null)
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /*private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            MainActivity::class.java.name, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE_LOCATION_PERMISSION
        )
    }*/

}

