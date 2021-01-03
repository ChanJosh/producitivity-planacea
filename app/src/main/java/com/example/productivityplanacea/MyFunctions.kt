package com.example.productivityplanacea

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MyFunctions : AppCompatActivity() {
    fun sendAlertStay(message: String, context: Context) { //alert that does not change the activity
        val builder = AlertDialog.Builder(context) //needs context to know where to build the alert
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                })
        val alert = builder.create()
        alert.show()
    }

    fun sendAlertFinish(message: String, context: Context) { //alert that changes the activity
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    if (context is Activity){ //casts context to Activity type
                        context.finish()    //finishes the specific activity
                    }
                })
        val alert = builder.create()
        alert.show()
    }

}