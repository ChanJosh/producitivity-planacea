package com.example.productivityplanacea

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType.*
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.net.PasswordAuthentication

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnEmail = findViewById<FloatingActionButton>(R.id.BYourEmail) as Button
        val btnPartnerEmail = findViewById<FloatingActionButton>(R.id.BPartnerEmail) as Button
        btnEmail.setOnClickListener {
            val linLayout = LinearLayout(this@SettingsActivity)
            linLayout.orientation = LinearLayout.VERTICAL
            val email = EditText(this@SettingsActivity)
            email.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            val password = EditText(this@SettingsActivity)
            password.transformationMethod = PasswordTransformationMethod()
            linLayout.addView(email)
            linLayout.addView(password)
            val builder = AlertDialog.Builder(this@SettingsActivity)
            builder.setTitle("Enter Your Email and Password to save on this device")
                .setView(linLayout)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    saveASharedPreferece(getString(R.string.var_your_email),email.text.toString())
                    saveASharedPreferece(getString(R.string.var_partner_email),password.text.toString())
                })
            val alert = builder.create()
            alert.show()
        }

        btnPartnerEmail.setOnClickListener {
            val email = EditText(this@SettingsActivity)
            email.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            val builder = AlertDialog.Builder(this@SettingsActivity)

            builder.setTitle("Enter partner email")
                .setView(email)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    saveASharedPreferece(getString(R.string.var_your_email),email.text.toString())
                })
            val alert = builder.create()
            alert.show()
        }
    }

    fun saveASharedPreferece(setting : String, settingVal : String){
        val savedUsername = intent.getStringExtra(EXTRA_SAVEDUSERNAME)
        val sharedPrefEmailCreds = this@SettingsActivity.getSharedPreferences(
            getString(R.string.file_shared), Context.MODE_PRIVATE
        ) ?: return
        val key = savedUsername + setting
        with(sharedPrefEmailCreds.edit()){
            putString(key,settingVal) //NEED TO FIX BY ENCRYPTING STRING FIRST THEN PUTSTRING
        }
    }


}