package com.example.sosapplication.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sosapplication.CONTACT_PICK_CODE
import com.example.sosapplication.Contact
import com.example.sosapplication.R
import com.example.sosapplication.SharedViewModel
import com.example.sosapplication.databinding.FragmentSettingsBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SettingsFragment : Fragment() {


    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    val contact = Contact()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        sharedViewModel.message.observe(viewLifecycleOwner, { message ->
            binding.etMessage.setText(message)
        })
        sharedViewModel.contact.observe(viewLifecycleOwner, { contact ->
            binding.contactLayout.tvContactName.text = contact.name
            binding.contactLayout.tvContactNumber.text = contact.phoneNumber
        })

        binding.btnSave.setOnClickListener {
            sharedViewModel.changeMessage(binding.etMessage.text.toString())
            sharedViewModel.changeContact(contact)

            findNavController().navigate(R.id.action_SettingsFragment_to_MainFragment)

        }

        binding.contactLayout.btnEditContact.setOnClickListener {
            /*val contactPickerIntent = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            startActivityForResult(contactPickerIntent, REQUEST_CODE)*/

            pickContact()

        }
        return binding.root
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
                    if (contactThumbnail != null) {
                        binding.contactLayout.imgProfile.setImageURI(Uri.parse(contactThumbnail))
                    }
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
                    binding.contactLayout.tvContactName.text = contact.name
                    binding.contactLayout.tvContactNumber.text = contact.phoneNumber
                }
            }

        } else {
            //cancelled picking contact
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
