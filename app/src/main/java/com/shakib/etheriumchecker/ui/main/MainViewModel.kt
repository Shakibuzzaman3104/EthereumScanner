package com.shakib.etheriumchecker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shakib.etheriumchecker.model.ModelResult
import com.shakib.etheriumchecker.network.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: RepositoryMain) : ViewModel (){

    fun checkBalance(address: String?) {
        repository.checkBalance(address)
    }

    fun observeBalanceResponse(): LiveData<ResponseResource<ModelResult>> {
        return repository.observeBalanceResponse()
    }

}