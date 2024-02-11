package com.example.sosapplication.fragments

import android.app.ActivityManager
import com.example.sosapplication.PowerButtonService
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sosapplication.ContactViewModel
import com.example.sosapplication.MainActivity
import com.example.sosapplication.PowerButtonCallback
import com.example.sosapplication.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment(), PowerButtonCallback {
    val appViewModel: ContactViewModel by activityViewModels()

    companion object {
        lateinit var mctx: Context
//        lateinit var mactivity: MainActivity
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialize mctx when the fragment is attached to the activity
        mctx = context
//        mactivity= (context as? MainActivity)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            sendSMS()
            /* val toast = Toast.makeText(context, appViewModel.contact.value?.text, Toast.LENGTH_SHORT)
             toast.show()*/
        }

        binding.serviceSwitch.isChecked = isServiceRunning(mctx,PowerButtonService::class.java)
        val serviceIntent = Intent(mctx, PowerButtonService::class.java)
//        serviceIntent.putExtra("context", mctx)

        binding.serviceSwitch.setOnCheckedChangeListener { buttonView, isChecked->
            if (isChecked){
                mctx.startService(serviceIntent)
            }
            else{
                mctx.stopService(serviceIntent)
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
    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)

        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


}