package com.example.productivityplanacea

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class DashboardActivity : AppCompatActivity() {
    companion object {
        var allTasks = mutableListOf<String>()
        lateinit var adapter: ArrayAdapter<String> //enable use in outside functions
                                                    // even though not initialized yet
        var defaultStreakString = ""
        lateinit var savedUsername : String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //draw startactivity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        savedUsername = intent.getStringExtra(EXTRA_SAVEDUSERNAME).toString()
        //init var/vals
        var defaultStreakString = ""

        val date = Calendar.getInstance()   //Calendar object
        val df = SimpleDateFormat("dd-MM-yyyy") //init format and creates Date object
        //defaultStreakString
        for (i in 0 until 29) {
            //var dateSelect = dateToday.add(i).getDateTimeFormat(DateTimeFormatter.ISO_LOCAL_DATE)
            val formattedDate = df.format(date.time)
            defaultStreakString += "$formattedDate,0,"
            date.add(Calendar.DAY_OF_YEAR, -1)
        }
        val formattedDate = df.format(date.time)
        defaultStreakString += "$formattedDate,0"
        //get saved streaks
        val sharedPrefStreaks = this@DashboardActivity.getSharedPreferences(
            getString(R.string.file_shared), Context.MODE_PRIVATE
        ) ?: return
        var savedStreakString = sharedPrefStreaks.getString(
            savedUsername + getString(R.string.var_saved_thirty_streaks),
            ""
        )
        val savedStreakArray : List<String>
        var todayStreakString = ""
        if (savedStreakString.isNullOrEmpty()) {
            todayStreakString = defaultStreakString
        } else {
            //update saved streaks
            val date = Calendar.getInstance()
            savedStreakArray = savedStreakString.split(",")
            //Log.d("SAVEDSTREAK",savedStreakString.toString())
            val diffMillis = df.parse(savedStreakArray.get(0)).time - date.timeInMillis
            val diff = (TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS)).toInt()
            //need to check if the new array was created today already, then skip creating at all
            //below creates a new array consisting of today and 0's of the difference days
            //need a way to add 1 to today's streak without altering the other days in the string
            if (diff in 1..30) { //create a new array based on last streak string created
                for (i in 0 until diff) {
                    val formattedDate = df.format(date.time)
                    todayStreakString += "$formattedDate,0,"
                    date.add(Calendar.DAY_OF_YEAR, -1)
                }
                for (i in 0 until 30 - diff - 1) {
                    todayStreakString += savedStreakArray.get(i * 2) + "," +
                            savedStreakArray.get(i * 2 + 1) + ","
                }
                todayStreakString += savedStreakArray.get((diff-1) * 2) + "," +
                        savedStreakArray.get((diff-1) * 2 + 1)
            }
            else if (diff > 30){
                todayStreakString = defaultStreakString
            }
            else{
                todayStreakString = savedStreakString
            }
        }
        val key = savedUsername + getString(R.string.var_saved_thirty_streaks)
        Log.d("TODAYSAVED", todayStreakString)
        with(sharedPrefStreaks.edit()) {
            putString(key, todayStreakString)
            apply()
        }

        adapter = ArrayAdapter(this@DashboardActivity, R.layout.item, allTasks)
        val listView = findViewById<ListView>(R.id.LVtodo)
        listView.setAdapter(adapter)

        val ButtonAddItem = findViewById<FloatingActionButton>(R.id.FABAddItem)
        ButtonAddItem.setOnClickListener {
            val input = EditText(this@DashboardActivity)
            val builder = AlertDialog.Builder(this@DashboardActivity)
            builder.setTitle("Add a new item")
                .setView(input)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    allTasks.add(input.getText().toString().trim())
                })
            val alert = builder.create()
            alert.show()
        }
/*
        val aM : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val bgIntent = Intent(this@DashboardActivity,
            SendEmail(this@DashboardActivity,savedUsername)
            ::class.java)
        val pendingIntent = PendingIntent.getService(this@DashboardActivity,
            0, bgIntent, 0)
        calendar.clear()
        calendar.set(Calendar.HOUR_OF_DAY,20)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        aM.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        
 */
    }


    fun onCheckboxClicked(view: View) {
        allTasks.remove((view as CheckBox).text)
        adapter.notifyDataSetChanged()
        addPoints(2)
    }

    fun addPoints(checkBoxPoints :Int){
        val savedUsername = intent.getStringExtra(EXTRA_SAVEDUSERNAME)
        val sharedPrefStreaks = this@DashboardActivity.getSharedPreferences(
            getString(R.string.file_shared), Context.MODE_PRIVATE
        ) ?: return
        val key = savedUsername + getString(R.string.var_saved_thirty_streaks)
        var savedStreakArray = sharedPrefStreaks.getString(
            key,
            Companion.defaultStreakString
        )?.split(",")?.toMutableList()
        //update saved streaks
        if (savedStreakArray != null) {
            with(sharedPrefStreaks.edit()) {
                savedStreakArray[1] = (savedStreakArray[1].trim().toInt() +
                        checkBoxPoints).toString()
                var todayStreakString = savedStreakArray.joinToString(separator = ",")
                Log.d("SAVEDSTRING", todayStreakString)
                putString(key, todayStreakString)//remove "[]"
                apply()
            }
        }
    }

    fun onTimerClicked(view: View) {
        val intent = Intent(this, TimerActivity::class.java).apply {
            putExtra(EXTRA_SAVEDUSERNAME, savedUsername)
        }
        startActivity(intent)
    }

    fun onStreaksClicked(view: View) {
        val intent = Intent(this, StreakActivity::class.java).apply {
            putExtra(EXTRA_SAVEDUSERNAME, savedUsername)
        }
        startActivity(intent)
    }

    fun onSettingsClicked(view: View) {
        val intent = Intent(this, SettingsActivity::class.java).apply {
            putExtra(EXTRA_SAVEDUSERNAME, savedUsername)
        }
        startActivity(intent)
    }


}

class SendEmail {
    constructor(context: Context, savedUsername : String) : super()
    {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.file_shared), Context.MODE_PRIVATE
        )
        val keyemail = savedUsername + context.getString(R.string.var_your_email)
        val keypass = savedUsername + context.getString(R.string.var_your_password)
        val keypartner = savedUsername + context.getString(R.string.var_partner_email)
        val email = sharedPref.getString(keyemail, "")
        val pass = sharedPref.getString(keypass, "")
        val partner = sharedPref.getString(keypartner, "")

        val prop = Properties()
        prop["mail.smtp.host"] = "smtp.gmail.com"
        prop["mail.smtp.port"] = "587"
        prop["mail.smtp.auth"] = "true"
        prop["mail.smtp.starttls.enable"] = "true" //TLS

        val session = Session.getInstance(prop,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(email, pass)
                }
            })

        try {
            val message: javax.mail.Message = MimeMessage(session)
            message.setFrom(InternetAddress("from@gmail.com"))
            message.setRecipients(
                javax.mail.Message.RecipientType.TO,
                InternetAddress.parse("$partner")
            )
            message.subject = "Testing Gmail TLS"
            message.setText(
                ""
            )
            Transport.send(message)
            println("Done")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}

