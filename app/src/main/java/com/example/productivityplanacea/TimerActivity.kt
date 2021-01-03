package com.example.productivityplanacea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class TimerActivity : AppCompatActivity() {

    var timerVal = 5 * 60
    var timerUsedFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val sbTimer = findViewById<SeekBar>(R.id.SBTimerSet)
        var tvTimeLeft = findViewById<TextView>(R.id.TVTimerDisplay)
        tvTimeLeft.text = DateUtils.formatElapsedTime(timerVal.toLong())
        val bSetTime = findViewById<Button>(R.id.BSetTimer)

        sbTimer.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                timerVal = (progress + 1) * 5 * 60
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                if (timerUsedFlag == false) {
                    tvTimeLeft.text = DateUtils.formatElapsedTime(timerVal.toLong())
                }
            }
        })

        bSetTime.setOnClickListener {
            if (timerUsedFlag == false) {
                timerUsedFlag = true
                var countdown = object : CountDownTimer(
                    (timerVal * 1000).toLong(),
                    1 * 1000
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        tvTimeLeft.text = DateUtils.formatElapsedTime(
                            millisUntilFinished / 1000
                        )
                    }

                    override fun onFinish() {
                        timerUsedFlag = false
                        val da = DashboardActivity()
                        da.addPoints(timerVal/(60 * 5))
                    }
                }
                countdown.start()
            }
        }
    }
}