package com.example.sosapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import com.example.sosapplication.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appViewModel:AppViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var viewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        var text=binding.editText.text.toString()
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        //back to main fragment
        binding.btnSave.setOnClickListener {
            appViewModel.changeText(text)
            findNavController().navigate(R.id.action_SettingsFragment_to_MainFragment)
        }
/*
        binding.btnSelectContacts.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_ContactsFragment)
            val intent = Intent(context, ActivityEdit::class.java).apply {
//                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
            Toast.makeText(context, "second fragment", Toast.LENGTH_LONG).show()
            */
/* MultiContactPicker.Builder(this)//Activity/fragment context
                 .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                 .hideScrollbar(false) //Optional - default: false
                 .showTrack(true) //Optional - default: true
                 .searchIconColor(Color.WHITE) //Option - default: White
                 .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                 .handleColor(resources.getColor(R.color.coffee_pot)) //Optional - default: Azure Blue
                 .bubbleColor(resources.getColor(R.color.coffee_pot)) //Optional - default: Azure Blue
                 .trackColor(resources.getColor(R.color.dusty_rose))
                 .bubbleTextColor(Color.WHITE) //Optional - default: White
                 .setTitleText("Select Contacts") //Optional - default: Select Contacts
                 .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                 .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                 .setActivityAnimations(
                     android.R.anim.fade_in, android.R.anim.fade_out,
                     android.R.anim.fade_in,
                     android.R.anim.fade_out
                 ) //Optional - default: No animation overrides
                 .showPickerForResult(ActivityContact.CONTACT_PICKER_REQUEST)*//*

        }
*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}