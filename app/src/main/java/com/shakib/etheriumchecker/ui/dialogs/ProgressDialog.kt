package com.shakib.etheriumchecker.ui.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.shakib.etheriumchecker.R


class ProgressDialog(private val activity: Activity) {

    private var dialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(true)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        if (dialog!=null && dialog?.isShowing!!)
            dialog?.dismiss()
    }

}

