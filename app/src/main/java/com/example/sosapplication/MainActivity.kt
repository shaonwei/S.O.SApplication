package com.example.sosapplication

import PowerButtonService
import android.content.BroadcastReceiver
import android.content.Context
import android.view.KeyEvent

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.sosapplication.databinding.ActivityMainBinding

import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.example.sosapplication.fragments.MainFragment
import java.util.logging.Logger


const val MY_PERMISSIONS_REQUEST_SEND_SMS = 1

//const val RESULT_PICK_CONTACT = 111*/
const val REQUEST_CODE = 11
/*const val RESULT_CODE = 12
const val EXTRA_KEY_TEST = "testKey"

const val RESULT_PICK_CONTACT = 13*/

const val CONTACT_PICK_REQUEST = 1000

private val CONTACT_PERMISSION_CODE = 1;
val CONTACT_PICK_CODE = 2

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

     val contactViewModel: ContactViewModel by viewModels()


    /*private var screenOffReceiver: BroadcastReceiver? = null
    private var screenOnReceiver: BroadcastReceiver? = null
    var globalCount: Int = 0*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.getLogger(MainActivity::class.java.name).warning("Hello..")

//        val tel = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        val PhoneNumber = tel.line1Number

        if (checkContactPermission() && checkSMSPermission()) {
            //allowed
        } else {
            //not allowed, request
            requestSMSPermission()
            requestContactPermission()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val mainFragment = MainFragment() // Create an instance of your fragment
        val powerButtonReceiver = PowerButtonReceiver(mainFragment)
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(powerButtonReceiver, filter)

//        val intent = Intent(this.applicationContext, PowerButtonService::class.java)
//        this.applicationContext.startService(intent)


        // Register broadcast receivers
        /*registerScreenOffReceiver()
        registerScreenOnReceiver()*/

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
    }

/*    fun openActivityForResult() {
        startForResult.launch(Intent(this, AnotherActivity::class.java))
    }


    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            //do stuff here
        }
    }
    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_PICK_CONTACT -> {
                    var cursor: Cursor? = null
                    try {
                        var phoneNo: String? = null
                        var name: String? = null
                        val uri: Uri? = data.data
                        cursor = contentResolver.query(uri!!, null, null, null, null)
                        cursor?.moveToFirst()
                        val phoneIndex: Int = cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex: Int = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        phoneNo = cursor.getString(phoneIndex)
                        name = cursor.getString(nameIndex)
                       // Log.e("Name and Contact number is", "$name,$phoneNo")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
           // Log.e("Failed", "Not able to pick contact")
        }
    }*/

    private fun requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS
                )

                // REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } /*else {
            // Permission has already been granted
        }*/
    }

    private fun requestContactPermission() {
        //request the READ_CONTACTS permission
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //handle permission request results || calls when user from Permission request dialog presses Allow or Deny
        //todo: fix first install
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted, can pick contact
            } else {
                //permission denied, cann't pick contact, just show message
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkContactPermission(): Boolean {
        //check if permission was granted/allowed or not, returns true if granted/allowed, false if not
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkSMSPermission(): Boolean {
        //check if permission was granted/allowed or not, returns true if granted/allowed, false if not
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            //go to settings
            R.id.action_settings -> navController.navigate(R.id.action_MainFragment_to_BlankFragment)
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Handle the power button press event here
            // You can perform any action you want when the power button is pressed
            // For example, you can show a message or trigger some functionality.
            return true // Consume the event
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "mainactivity", Toast.LENGTH_LONG).show()
        if (requestCode === CONTACT_PICKER_REQUEST) {
            if (resultCode === RESULT_OK) {
                val results: List<ContactResult> = MultiContactPicker.obtainResult(data)
//                Log.d("MyTag", results[0].displayName)
                println("con" + results.get(0).displayName)

                Toast.makeText(this, "contact", Toast.LENGTH_SHORT).show()
            } else if (resultCode === RESULT_CANCELED) {
                println("User closed the picker without selecting items.")
            }
        }
    }
*/

    /*  @SuppressLint("Range")
      override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(reqCode, resultCode, data)
          if (0 == reqCode) {
              if (resultCode == RESULT_OK) {
                  println("in on ActivityResult")
                  val contactData = data?.data
                  val c = managedQuery(contactData, null, null, null, null)
                  if (c.moveToFirst()) {
                      val id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                      val hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                      if (hasPhone.equals("1", ignoreCase = true)) {
                          val phones = contentResolver.query(
                              ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                              null,
                              ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                      + " = " + id, null, null
                          )
                          phones!!.moveToFirst()
                          val cNumber = phones.getString(phones.getColumnIndex("data1"))
                          val name = phones.getString(phones.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                          //here you can find out all the thing.
                          System.out.println("NAME:$name")
                          *//*Snackbar.make(binding, name, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()*//*
                        Toast.makeText(applicationContext,name,LENGTH_LONG).show()
                        //etFirst.setText(cNumber)
                    }
                }
            }
        }
    }*/

/*    override fun onDestroy() {
        super.onDestroy()

        // Unregister the broadcast receivers when the activity is destroyed
        unregisterReceiver(screenOffReceiver)
        unregisterReceiver(screenOnReceiver)
    }

    private fun registerScreenOffReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)

        screenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Handle the screen off event here
                if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                    // Perform your action when the screen turns off\
                    binding.toolbar.title = "OFF"
                    globalCount++
                }
            }
        }
        registerReceiver(screenOffReceiver, filter)
    }

    private fun registerScreenOnReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        screenOnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Handle the screen on event here
                if (intent?.action == Intent.ACTION_SCREEN_ON) {
                    // Perform your action when the screen turns on
                    binding.toolbar.title = "ON:$globalCount"
                    globalCount++
                    if (globalCount % 4 == 0) {
                        globalCount = 0

                    }
                }
            }
        }
        registerReceiver(screenOnReceiver, filter)
    }*/


}