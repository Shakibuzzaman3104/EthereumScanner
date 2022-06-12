package com.shakib.etheriumchecker.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.zxing.integration.android.IntentIntegrator
import com.shakib.etheriumchecker.Utils
import com.shakib.etheriumchecker.databinding.FragmentMainBinding
import com.shakib.etheriumchecker.network.ResponseResource
import com.shakib.etheriumchecker.ui.dialogs.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert

@AndroidEntryPoint
class MainFragment : Fragment() {


    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: FragmentMainBinding
    private var progressDialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        init()
        initListeners()
        initObserver()

        return binding.root
    }

    private fun init() {
        progressDialog = ProgressDialog(requireActivity())
    }

    private fun initListeners() {
        binding.btnCheckBalance.setOnClickListener {
            val publicKey = binding.etPublicKey.text.toString()
            if (binding.etPublicKey.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a public key first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.checkBalance(publicKey)
            }
        }
        binding.btnScanner.setOnClickListener {
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setPrompt("Scan a barcode or QR Code")
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.initiateScan()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                var ether = intentResult.contents.toString()
                if (ether.contains("ethereum:"))
                    ether = ether.replace("ethereum:", "")
                binding.etPublicKey.setText(ether)
                viewModel.checkBalance(ether)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun initObserver() {
        viewModel.observeBalanceResponse().observe(viewLifecycleOwner) {
            when (it.status) {
                ResponseResource.Status.SUCCESS -> {
                    if (it.data != null) {
                        progressDialog?.dismissDialog()
                        binding.tvBalance.text =
                            if (!it.data.result.isNullOrEmpty()) Utils.generateCommaSeparatedNumber(it.data.result) else it.data.message


                    }
                }
                ResponseResource.Status.LOADING -> {
                    progressDialog?.startLoadingDialog()
                }
                ResponseResource.Status.NETWORK_ERROR, ResponseResource.Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    progressDialog?.dismissDialog()
                }
            }
        }
    }


}