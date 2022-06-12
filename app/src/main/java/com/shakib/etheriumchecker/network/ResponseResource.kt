package com.shakib.etheriumchecker.network


class ResponseResource<T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS, ERROR, LOADING, NETWORK_ERROR
    }

    companion object {
        fun <T> success(data: T?, message: String?): ResponseResource<T> {
            return ResponseResource(Status.SUCCESS, data, message)
        }

        fun <T> error(msg: String?): ResponseResource<T> {
            return ResponseResource(Status.ERROR, null, msg)
        }

        fun <T> networkError(msg: String): ResponseResource<T> {
            return ResponseResource(Status.NETWORK_ERROR, null, msg)
        }


        fun <T> loading(): ResponseResource<T> {
            return ResponseResource(Status.LOADING, null, null)
        }

    }
}


