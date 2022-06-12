package com.shakib.etheriumchecker.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import com.shakib.etheriumchecker.model.ModelResult
import com.shakib.etheriumchecker.network.ApiCheckBalance
import com.shakib.etheriumchecker.network.NetworkRequestHelper
import com.shakib.etheriumchecker.network.ResponseResource
import javax.inject.Inject

class RepositoryMain @Inject constructor(
    private val api: ApiCheckBalance,
    private val apiCheckBalance: NetworkRequestHelper<ModelResult>,
) {

    fun checkBalance(address: String?) {
        apiCheckBalance.networkCall(api.getEthereumBalance("account", "balance", address, "latest"))
    }

    fun observeBalanceResponse(): LiveData<ResponseResource<ModelResult>> {
        return apiCheckBalance.getResponse()
    }

}