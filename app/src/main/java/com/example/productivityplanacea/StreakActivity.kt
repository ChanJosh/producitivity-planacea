package com.example.productivityplanacea

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class StreakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streak)
        val savedUsername = intent.getStringExtra(EXTRA_SAVEDUSERNAME)

        // in this example, a LineChart is initialized from xml
        var chart = findViewById<View>(R.id.bar_chart_streaks) as BarChart
        var streakDays = mutableListOf<String>()
        var streakPoints = mutableListOf<Int>()
        val entries = mutableListOf<BarEntry>()
        val df = SimpleDateFormat("dd-MM-yyyy")
        val date = Calendar.getInstance()
        val sharedPrefStreaks = this@StreakActivity.getSharedPreferences(
            getString(R.string.file_shared), Context.MODE_PRIVATE) ?: return
        var savedStreakString = sharedPrefStreaks.getString(savedUsername +
                getString(R.string.var_saved_thirty_streaks),
            ""
        )
        Log.d("stringers",savedStreakString.toString())
        //savedStreakString = "01-01-2021,6,31-12-2020,0,30-12-2020,0,29-12-2020,0,28-12-2020,0,27-12-2020,0,26-12-2020,0,25-12-2020,0,24-12-2020,0,23-12-2020,0,22-12-2020,0,21-12-2020,0,20-12-2020,0,19-12-2020,0,18-12-2020,0,17-12-2020,0,16-12-2020,0,15-12-2020,0,14-12-2020,0,13-12-2020,0,12-12-2020,0,11-12-2020,0,10-12-2020,0,09-12-2020,0,08-12-2020,0,07-12-2020,0,06-12-2020,0,05-12-2020,0,04-12-2020,0,03-12-2020,0"
        var savedStreakArray = savedStreakString?.split(",")

        for(i in 30-1 downTo 0){
            savedStreakArray?.get(i*2)?.let { streakDays.add(it) }
            savedStreakArray?.get(i*2+1)?.toInt()?.let { streakPoints.add(it) }
        }

        for (i in 0 until 30) {
            entries.add(BarEntry(i.toFloat(),streakPoints[i].toFloat()))
        }
        var dataSet = BarDataSet(entries,"Points"); // add entries to dataset
        var barData = BarData(dataSet)
        barData.setBarWidth(0.9f)
        barData.setDrawValues(false)
        class xAxisVF : ValueFormatter(){
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return streakDays.getOrNull(value.toInt()) ?: value.toString()
            }
        }
        chart.xAxis.valueFormatter = xAxisVF()
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        chart.setData(barData)
        chart.setFitBars(true)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.setNoDataText("No Points recorded")
        chart.xAxis.isEnabled = true
        chart.xAxis.textSize = 12F
        chart.axisLeft.axisMinimum = 0F
        chart.axisLeft.textSize = 12F
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.labelRotationAngle = 90F
        chart.xAxis.labelCount = 30
        chart.axisLeft.isEnabled = true
        chart.axisRight.isEnabled = false
        chart.xAxis.setDrawGridLines(false)
        chart.invalidate()
        chart.notifyDataSetChanged()
    }


}