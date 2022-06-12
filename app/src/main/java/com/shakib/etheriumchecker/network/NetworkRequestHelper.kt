package com.shakib.etheriumchecker.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.shakib.etheriumchecker.R
import com.shakib.etheriumchecker.model.BaseModel
import com.shakib.etheriumchecker.network.RxExtensions.applySchedulers
import com.shakib.etheriumchecker.network.RxExtensions.retryWithDelay
import io.reactivex.rxjava3.core.Observable
import retrofit2.HttpException

import com.shakib.etheriumchecker.network.RxExtensions.checkErrorCodes

import org.json.JSONObject


open class NetworkRequestHelper<T : BaseModel>(private val application: Application) {

    private var responseLiveData: MutableLiveData<ResponseResource<T>> = MutableLiveData()

    private val TAG = "Q#_"

    private fun setError(message: String?) {
        responseLiveData.value = ResponseResource.error(message)
    }

    fun setSuccess(data: T?, message: String) {
        responseLiveData.value = ResponseResource.success(data, message)
    }

    private fun setNetWorkError() {
        responseLiveData.value =
            ResponseResource.networkError(application.getString(R.string.label_something_went_wrong))
    }

    private fun setLoading() {
        responseLiveData.value = ResponseResource.loading()
    }

    private fun replaceLiveData(input: ResponseResource<T>) {
        responseLiveData.value = input
    }


    fun networkCall(observable: Observable<T>) {
        setLoading()
        if (NetworkUtils.getConnectivityStatus(application) == NetworkUtils.TYPE_NOT_CONNECTED) {
            setNetWorkError()
        } else
            observable
                .applySchedulers()
                .retryWithDelay()
                .onErrorReturn {
                    Log.d(TAG, "OnError: networkCall: ${it.localizedMessage}")
                    val base = BaseModel()
                    base.status = "0"
                    if (it is HttpException) {
                        try {
                            val jsonObject = JSONObject(it.response()!!.errorBody()!!.string())
                            base.message = jsonObject.get("message").toString()
                        } catch (e: Exception) {

                        }
                    }
                    base as T
                }
                .map {
                    Log.d(TAG, "map: networkCall: ${it.status}")
                    RxExtensions.mapToResource(it, it.status, it.message)
                }
                .subscribe({
                    Log.d(TAG, "networkCall: ${it.status}")
                    Log.d(TAG, "networkCall: ${Gson().toJson(it.data)}")
                    if (it.status == ResponseResource.Status.SUCCESS) {
                        replaceLiveData(it)
                    } else setError(
                        it.message ?: application.getString(R.string.label_something_went_wrong)
                    )

                }, {
                    Log.d(TAG, "networkCall: ${it.localizedMessage}")
                    for (i in it.stackTrace) {
                        Log.d(TAG, "networkCall: $i")
                    }
                    setError(
                        "${application.getString(R.string.label_something_went_wrong)}. ${
                            checkErrorCodes(
                                it.localizedMessage
                            )
                        }"
                    )
                })

    }


    open fun getResponse(): LiveData<ResponseResource<T>> {
        Log.d(TAG, "getResponse: ")
        return responseLiveData
    }






}