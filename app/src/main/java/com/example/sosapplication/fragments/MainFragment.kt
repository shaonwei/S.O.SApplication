package com.example.sosapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sosapplication.ContactViewModel
import com.example.sosapplication.PowerButtonCallback
import com.example.sosapplication.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment(), PowerButtonCallback {
    val appViewModel: ContactViewModel by activityViewModels()

    companion object {
        lateinit var mctx: Context
    }

//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    lateinit var viewModel: ContactViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            sendSMS()
            /* val toast = Toast.makeText(context, appViewModel.contact.value?.text, Toast.LENGTH_SHORT)
             toast.show()*/
        }

        binding.serviceSwitch.isChecked = false
        binding.serviceSwitch.setOnCheckedChangeListener { buttonView, isChecked->
            if (isChecked){
               /* val serviceIntent = Intent(this, MyIntentService::class.java)
                startService(serviceIntent)
*/
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun sendSMS() {
        appViewModel.sendSMS();
    }

}