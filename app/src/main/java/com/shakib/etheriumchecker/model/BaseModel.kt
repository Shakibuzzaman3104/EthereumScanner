package com.shakib.etheriumchecker.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class BaseModel {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null
}