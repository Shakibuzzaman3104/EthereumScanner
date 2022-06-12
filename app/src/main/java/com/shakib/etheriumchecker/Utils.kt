package com.shakib.etheriumchecker

import android.util.Log
import org.web3j.utils.Convert
import java.text.DecimalFormat

object Utils {

    fun generateCommaSeparatedNumber(input: String?): String {
        if (input.isNullOrEmpty()) return ""
        val generatedValue = Convert.fromWei(input, Convert.Unit.ETHER).toString()
        val formatter = DecimalFormat("#,###.0000##")
        val value = generatedValue.toDouble()



        Log.d("GeneratedValue", "val: $generatedValue")
        return formatter.format(value)
    }

}