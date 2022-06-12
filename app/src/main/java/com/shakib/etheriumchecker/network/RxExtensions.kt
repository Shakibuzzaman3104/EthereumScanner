package com.shakib.etheriumchecker.network

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object RxExtensions {

    fun <T> Observable<T>.applySchedulers(): Observable<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> mapToResource(entry: T, status: String?, message: String?): ResponseResource<T> {
        return if (status=="0") {
            ResponseResource.error(message)
        } else {
            ResponseResource.success(entry, message)
        }
    }


    fun <T> Observable<T>.retryWithDelay(): Observable<T> {
        var retryCount = 0
        val maxRetries = 2
        val retryDelayMillis = 30L
        return retryWhen { thObservable ->
            thObservable.flatMap { throwable ->

                var isExceptionFound = true

                try {
                    for (i in retryList) {
                        if (throwable.localizedMessage.contains(i)) {
                            isExceptionFound = false
                            break;
                        }
                    }
                } catch (e: Exception) {
                }

                when {
                    isExceptionFound -> {
                        Observable.error(throwable)
                    }
                    ++retryCount < maxRetries -> {
                        Observable.timer(retryDelayMillis, TimeUnit.SECONDS)
                    }
                    else -> {
                        Observable.error(throwable)
                    }
                }
            }
        }
    }

    private val retryList = listOf("500")

    fun checkErrorCodes(error: String?): String {
        if (error == null)
            return ""
        return when {
            error.contains(HttpErrorCodes.HTTP_400.value) -> HttpErrorCodes.HTTP_400.value
            error.contains(HttpErrorCodes.HTTP_401.value) -> HttpErrorCodes.HTTP_401.value
            error.contains(HttpErrorCodes.HTTP_401.value) -> HttpErrorCodes.HTTP_401.value
            error.contains(HttpErrorCodes.HTTP_403.value) -> HttpErrorCodes.HTTP_403.value
            error.contains(HttpErrorCodes.HTTP_404.value) -> HttpErrorCodes.HTTP_404.value
            error.contains(HttpErrorCodes.HTTP_408.value) -> HttpErrorCodes.HTTP_408.value
            error.contains(HttpErrorCodes.HTTP_500.value) -> HttpErrorCodes.HTTP_500.value
            error.contains(HttpErrorCodes.HTTP_501.value) -> HttpErrorCodes.HTTP_501.value
            error.contains(HttpErrorCodes.HTTP_502.value) -> HttpErrorCodes.HTTP_502.value
            error.contains(HttpErrorCodes.HTTP_503.value) -> HttpErrorCodes.HTTP_503.value
            error.contains(HttpErrorCodes.HTTP_504.value) -> HttpErrorCodes.HTTP_504.value
            else -> ""
        }
    }

    enum class HttpErrorCodes(val value: String) {
        HTTP_400("HTTP 400"),
        HTTP_401("HTTP 401"),
        HTTP_403("HTTP 403"),
        HTTP_404("HTTP 404"),
        HTTP_408("HTTP 408"),
        HTTP_500("HTTP 500"),
        HTTP_501("HTTP 501"),
        HTTP_502("HTTP 502"),
        HTTP_503("HTTP 503"),
        HTTP_504("HTTP 504"),
    }


}