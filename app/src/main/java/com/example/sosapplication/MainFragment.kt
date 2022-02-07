package com.example.sosapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.sosapplication.databinding.FragmentMainBinding
import com.example.sosapplication.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import java.net.URI

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var number = 972528494174
        //var viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()));
        }
        val py = Python.getInstance()
        val module = py.getModule("myscript")
        binding.btnSend.setOnClickListener {
           /* val sendIntent: Intent = Intent().apply {
                *//*action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                setPackage("com.whatsapp")
                type = "text/bold"*//*
            }
            Snackbar.make(view, appViewModel.text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            var url =
                "https://api.whatsapp.com/send?phone=" + number + "&text=" + appViewModel.text
            val actionIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                setData(Uri.parse(url))
                setPackage("com.whatsapp")
            }
            val shareIntent = Intent.createChooser(actionIntent, null)
//            shareIntent.setPackage("com.whatsapp")
            startActivity(shareIntent)*/
            try {
            val status=module.callAttr("sendWhatsapp",number,appViewModel.text)

            } catch (e: PyException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}