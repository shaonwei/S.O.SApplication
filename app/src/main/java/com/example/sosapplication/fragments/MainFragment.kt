package com.example.sosapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.sosapplication.SharedViewModel
import com.example.sosapplication.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    companion object {
        lateinit var mctx: Context
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

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
            val appViewModel: SharedViewModel by activityViewModels()

            appViewModel.sendSMS();
            val toast = Toast.makeText(context, appViewModel.contact.value?.text, Toast.LENGTH_SHORT)
            toast.show()        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}