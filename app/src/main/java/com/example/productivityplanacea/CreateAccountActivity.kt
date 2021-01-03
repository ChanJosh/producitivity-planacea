package com.example.productivityplanacea

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.security.MessageDigest

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }

    fun createAccount(view: View) {
        val md = MessageDigest.getInstance("SHA-256")
        val mf = MyFunctions()

        var newUsername = ""
        var temp = (md.digest(findViewById<EditText>(R.id.TPNewUsername).text.toString().toByteArray()))
        temp.forEach { newUsername = newUsername + String.format("%02X",it) } //Hashed inputted username

        val openedFile = File(filesDir.absolutePath,"accounts.txt")

        if(!openedFile.createNewFile()) { //if file is not new, must read all the new files
            val accounts = openedFile.readLines()

            for(account in accounts) { //check if username already used
                val username = account.split(":")[0] //extract hex string of username
                if(newUsername == username){//create alert saying that username already exists
                    mf.sendAlertStay("Please use a different username.",this@CreateAccountActivity)
                    return
                }
            }
        }
        var newPassword = "";
        temp = (md.digest(findViewById<EditText>(R.id.PNewPassword).text.toString().toByteArray()))
        temp.forEach { newPassword = newPassword + String.format("%02X",it) } //Hashed inputted password

        openedFile.appendText("$newUsername:$newPassword\n")
        mf.sendAlertFinish("Successfully created a new account",this@CreateAccountActivity)
    }

}