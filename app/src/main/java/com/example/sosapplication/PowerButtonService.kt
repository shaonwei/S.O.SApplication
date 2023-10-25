package com.example.sosapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.view.Display
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import java.util.logging.Logger

class PowerButtonService : Service() {

    private var powerClickCount = 0
    private var volumeUpClickCount = 0
    private lateinit var windowManager: WindowManager
    private lateinit var displayListener: DisplayManager.DisplayListener
    private val NOTIFICATION_ID = 123 // Replace with your chosen ID

    val smsManager = SmsManager.getDefault() as SmsManager
    private lateinit var myViewModel: ContactViewModel

    lateinit var allContacts: LiveData<List<Contact>>
    lateinit var repository: ContactRepository

    /*init {
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }*/

    override fun onCreate() {
        super.onCreate()
//        myViewModel = ViewModelProvider(MainActivity()).get(ContactViewModel::class.java)
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        displayListener = object : DisplayManager.DisplayListener {
            override fun onDisplayAdded(displayId: Int) {}

            override fun onDisplayRemoved(displayId: Int) {}

            override fun onDisplayChanged(displayId: Int) {
                val display = windowManager.defaultDisplay
                if (display.state == Display.STATE_OFF) {
                    // Power button pressed
                    powerClickCount++
                    if (powerClickCount == 3 && volumeUpClickCount == 1) {
                        sendSMS()
                    }
                } else if (display.state == Display.STATE_ON) {
                    // Power button released
                    powerClickCount = 0
                }
            }
        }

        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)
        Logger.getLogger(MainActivity::class.java.name).warning("oncreate-service")

    }
    private fun createNotification(): Notification {
        // Notification channel settings
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("My Service")
            .setContentText("Service is running in the background")
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)

        return notificationBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val extraData = intent.getStringExtra("your_key_here")

            if (extraData != null) {
                // You have successfully retrieved the extra data.
                // Use it as needed.
                // ...
            } else {
                // The extra data with the specified key was not found in the Intent.
                // Handle this case as appropriate.
                // ...
            }
        }
        val notification = createNotification()

        // Start the service in the foreground with the notification.
        startForeground(NOTIFICATION_ID, notification)
//        myViewModel.sendSMS()
        return START_STICKY
    }

    fun sendSMS(){
        for (contact in allContacts.value!!) {
            smsManager.sendTextMessage(contact.phoneNumber.toString(), null, contact.text, null, null)
            Logger.getLogger(MainActivity::class.java.name).warning("send sms to" + contact.id + "," + contact.text)
        }
    }
}
