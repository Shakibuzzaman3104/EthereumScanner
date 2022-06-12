package com.shakib.etheriumchecker.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class ModelResult : BaseModel() {

    @SerializedName("result")
    var result: String? = null

}

