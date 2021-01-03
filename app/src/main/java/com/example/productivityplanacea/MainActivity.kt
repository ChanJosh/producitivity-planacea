package com.example.productivityplanacea

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.io.File
import java.security.MessageDigest

const val EXTRA_SAVEDUSERNAME = "com.example.productivityplanacea.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    //After pressing Create New Account
    fun startCreateAccountActivity(view: View) {
        val intent = Intent(this,CreateAccountActivity::class.java)
        startActivity(intent);
    }

    fun login(view: View){
        val md = MessageDigest.getInstance("SHA-256")
        val cf = MyFunctions()
        var alertFlag : Boolean = true
        var inputUsername = findViewById<EditText>(R.id.TPUsername).text.toString()
        var inputUsernameHex = "";
        var inputPasswordHex = "";
        var temp = (md.digest(inputUsername.toByteArray()))
        temp.forEach { inputUsernameHex = inputUsernameHex + String.format("%02X",it) }

        temp = (md.digest(findViewById<EditText>(R.id.PPassword).text.toString().toByteArray()))
        temp.forEach { inputPasswordHex = inputPasswordHex + String.format("%02X",it) } //Hashed inputted password

        val openedFile = File(filesDir.absolutePath,"accounts.txt")
        if(openedFile.createNewFile() == false) {
            val accounts = openedFile.readLines()
            for (account in accounts) {
                val (username, password) = account.split(":")
                if (inputUsernameHex == username && inputPasswordHex == password) {
                    val intent = Intent(this, DashboardActivity::class.java).apply {
                        putExtra(EXTRA_SAVEDUSERNAME, inputUsername)
                    }
                    alertFlag = false
                    startActivity(intent)
                    finish()
                }
            }
        }
        if (alertFlag == true) cf.sendAlertStay("Incorrect Username/Password.", this)

    }
}