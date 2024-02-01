package net.kteam.pixelarthunter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context

class LoadinDialog constructor(context:Context){

    val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog
    init {
        builder.setView(R.layout.loading_dialog)
        dialog = builder.create()
        dialog.setCancelable(false)
    }


    fun showLoadingDialog() {
        dialog.show()
    }

    fun hideLoadingDialog(){
        if(dialog.isShowing) {
            dialog.hide()
        }
    }
}