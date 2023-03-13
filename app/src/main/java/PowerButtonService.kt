import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.view.Display
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.sosapplication.*
import java.util.logging.Logger

class PowerButtonService : Service() {

    private var powerClickCount = 0
    private var volumeUpClickCount = 0
    private lateinit var windowManager: WindowManager
    private lateinit var displayListener: DisplayManager.DisplayListener

    val smsManager = SmsManager.getDefault() as SmsManager
    private lateinit var myViewModel: ContactViewModel

    val allContacts: LiveData<List<Contact>>
    val repository: ContactRepository

    init {
        val dao = ContactDatabase.getDatabase(application).getContactsDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    override fun onCreate() {
        super.onCreate()
//        myViewModel = ViewModelProvider(MainActivity()).get(ContactViewModel::class.java)

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

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
