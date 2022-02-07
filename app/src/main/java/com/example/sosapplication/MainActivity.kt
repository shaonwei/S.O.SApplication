package com.example.sosapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sosapplication.ActivityEdit.CONTACT_PICKER_REQUEST
import com.example.sosapplication.databinding.ActivityMainBinding
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.MultiContactPicker
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    public var myNumber = 972528494174

    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val tel = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        val PhoneNumber = tel.line1Number



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
       navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        /*val intent = intent
        val text = intent.getStringExtra("text")
        val contactName = intent.getStringExtra("contactName")
        val contact = intent.getStringExtra("contact")


        val message = SOSMessage(
            "inputEditText.toString()",
            null,
            false
        )
*/

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            /*val intent = Intent(this, ActivityEdit::class.java).apply {
//                putExtra("text",message.text)
//                putExtra("location",message.location)
//                putExtra("contact",message.contacts.)
            }
            startActivity(intent)*/
        }
    }

    private fun checkContactPermission(): Boolean {
        //check if permission was granted/allowed or not, returns true if granted/allowed, false if not
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
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
            R.id.action_settings -> navController.navigate(R.id.action_MainFragment_to_SettingsFragment)
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

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

    fun getNum(): Long {
        return myNumber
    }
}