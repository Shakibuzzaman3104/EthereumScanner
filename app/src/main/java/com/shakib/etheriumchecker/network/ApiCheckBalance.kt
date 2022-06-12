package com.shakib.etheriumchecker.network

import com.shakib.etheriumchecker.AppConstants
import com.shakib.etheriumchecker.model.ModelResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCheckBalance {

    @GET(AppConstants.API_ENDPOINT)
    fun getEthereumBalance(
        @Query("module") module: String,
        @Query("action") action: String,
        @Query("address") address: String?,
        @Query("tag") tag: String?
    ): Observable<ModelResult>

}