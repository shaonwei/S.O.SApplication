package com.example.sosapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapplication.databinding.FragmentBlankBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sosapplication.*
import android.Manifest
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar


class BlankFragment : Fragment(), ContactClickInterface, ContactClickDeleteInterface {

//    lateinit var viewModel: ContactViewModel //by activityViewModels()

    val viewModel: ContactViewModel by activityViewModels()
    lateinit var contactsRV: RecyclerView
    lateinit var addBTN: Button
    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: ContactAdapter
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        contactsRV = binding.contactsRv
        contactsRV.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { ContactAdapter(it, this, this) }!!
        contactsRV.adapter = adapter

        //show list
        viewModel.allContacts.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter?.updateList(it)
            }
        })

        //show text
        viewModel.allContacts.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                if (!it.isEmpty())
                    binding.etMessage.setText(it.get(0)?.text)
            }
        })


        binding.btnAdd.setOnClickListener {
            pickContact()
        }

        binding.btnSave.setOnClickListener {
            updateMessage(binding.etMessage.text.toString())
            findNavController().navigate(R.id.action_BlankFragment_to_MainFragment)
        }

        binding.locationSwitch.isChecked=viewModel.readDataFromSharedPreferences("location","default").toBoolean()

        binding.locationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {

                viewModel.writeToSharedPreferences("location", "true")
                if (isLocationEnabled(requireContext())) {
                    // Location is enabled
                } else {
                    // Location is not enabled, you can show a message or prompt the user to enable it
                    val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(locationSettingsIntent)
                }
                requestLocationPermission()
            } else {
                viewModel.writeToSharedPreferences("location", "false")
            }
        }


        return binding.root
    }

    private fun updateMessage(msg: String) {
        viewModel.allContacts.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for (i in it) {
                    i.text = msg
                    viewModel.updateContact(i)
                }
                adapter?.updateList(it)
            }
        })
    }


    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var contact = Contact()
//        val c: Cursor = requireActivity().managedQuery(contactData, null, null, null, null)
        val contentResolver = requireActivity().contentResolver

        if (resultCode == AppCompatActivity.RESULT_OK) {
            //calls when user click a contact from contacts (intent) list
            if (requestCode == CONTACT_PICK_CODE) {
//                binding.contactLayout..text = ""

                val cursor1: Cursor
                val cursor2: Cursor?

                //get data from intent
                val uri = data!!.data
                cursor1 = contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor1.moveToFirst()) {
                    //get contact details
                    val contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                    val contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val contactThumbnail = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val idResultHold = idResults.toInt()
                    //set details: contact id, contact name, image

                    //set image, first check if uri/thumbnail is not null

                    contact.name = contactName
                    /* if (contactThumbnail != null) {
                         binding.contactLayout.imgProfile.setImageURI(Uri.parse(contactThumbnail))
                     }*/
                    //check if contact has a phone number or not
                    if (idResultHold == 1) {
                        cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
                        //a contact may have multiple phone numbers
                        while (cursor2!!.moveToNext()) {
                            //get phone number
                            val contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            //set phone number
//                            binding.secondCheckBox.append("\nPhone: $contactNumber")
                            contact.phoneNumber = contactNumber
                        }
                        cursor2.close()
                    }
                    cursor1.close()
//                    binding.contactLayout.tvContactName.text = contact.name
//                    binding.contactLayout.tvContactNumber.text = contact.phoneNumber
                    contact.text = binding.etMessage.text.toString()

                    viewModel.addContact(contact)
                }
            }

        } else {
            //cancelled picking contact
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDeleteIconClick(contact: Contact) {
        viewModel.deleteContact(contact)
    }

    override fun onEditClick(contact: Contact) {
        TODO("Not yet implemented")
    }


    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already
//            showUserLocation()
//            Snackbar.make(this,"you have permission",Snackbar.LENGTH_SHORT)
        }
    }

    private fun showUserLocation() {
        // Enable the My Location layer and move the camera to the user's location
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            mMap.isMyLocationEnabled = true
            // Additional code to customize map view or add markers, etc.
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation()
            }
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


}