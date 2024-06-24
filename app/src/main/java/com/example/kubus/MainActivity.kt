package com.example.kubus

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        val timeSelector: TextView = findViewById(R.id.tvTimeSelector)
        val spinFrom: Spinner = findViewById(R.id.spinFrom)
        val spinTo: Spinner = findViewById(R.id.spinTo)
        val btnSearch: MaterialButton = findViewById(R.id.btnSearch)
        val locations = arrayOf("Shuwaikh", "Shadadiyyah", "Kaifan")

        val adapterFrom = ArrayAdapter(this, R.layout.spinner_item, locations)
        val adapterTo = ArrayAdapter(this, R.layout.spinner_item, locations)
        adapterFrom.setDropDownViewResource(R.layout.spinner_item)
        adapterTo.setDropDownViewResource(R.layout.spinner_item)
        spinFrom.adapter = adapterFrom
        spinTo.adapter = adapterTo

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        updateTimeText(timeSelector, currentHour, currentMinute)

        insertTestSchedules(databaseHelper)

        timeSelector.setOnClickListener {
            TimePickerDialog(this, { _, hourOfDay, minute -> updateTimeText(timeSelector, hourOfDay, minute) }, currentHour, currentMinute, true).show()
        }

        btnSearch.setOnClickListener {
            val to = spinTo.selectedItem.toString()
            val from = spinFrom.selectedItem.toString()
            val time = timeSelector.text.toString()

            if (TextUtils.isEmpty(to) || TextUtils.isEmpty(from) || TextUtils.isEmpty(time)) {
                Toast.makeText(this@MainActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("MainActivity", "To: $to, From: $from, Time: $time")


            recyclerView.layoutManager = LinearLayoutManager(this)

            val schedules = databaseHelper.getAllSchedules()
            scheduleAdapter = ScheduleAdapter(schedules, time)
            recyclerView.adapter = scheduleAdapter
        }
    }

    private fun updateTimeText(textView: TextView, hour: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hour, minute)
        textView.text = formattedTime
    }

    fun insertTestSchedules(dbHelper: DatabaseHelper) {
        val testSchedules = listOf(
            Schedule(0, "88", "05:00", "Shuwaikh", "Shadadiyyah", 30, true),
            Schedule(0, "88", "06:00", "Shadadiyyah", "Kaifan", 20, true),
            Schedule(0, "89", "07:00", "Kaifan", "Shuwaikh", 25, true),
            Schedule(0, "90", "08:00", "Shuwaikh", "Kaifan", 15, true),
            Schedule(0, "91", "09:00", "Shadadiyyah", "Shuwaikh", 40, true),
            Schedule(0, "92", "10:00", "Kaifan", "Shadadiyyah", 35, true),
            Schedule(0, "93", "11:00", "Shuwaikh", "Kaifan", 50, true),
            Schedule(0, "94", "12:00", "Shadadiyyah", "Kaifan", 20, true),
            Schedule(0, "95", "13:00", "Kaifan", "Shuwaikh", 25, true),
            Schedule(0, "96", "14:00", "Shuwaikh", "Shadadiyyah", 30, true),
            Schedule(0, "97", "15:00", "Shadadiyyah", "Kaifan", 20, true),
            Schedule(0, "98", "16:00", "Kaifan", "Shuwaikh", 25, true),
            Schedule(0, "99", "17:00", "Shuwaikh", "Kaifan", 15, true),
            Schedule(0, "100", "18:00", "Shadadiyyah", "Shuwaikh", 40, true),
            Schedule(0, "101", "19:00", "Kaifan", "Shadadiyyah", 35, true),
            Schedule(0, "102", "20:00", "Shuwaikh", "Kaifan", 50, true),
            Schedule(0, "103", "21:00", "Shadadiyyah", "Kaifan", 20, true),
            Schedule(0, "104", "22:00", "Kaifan", "Shuwaikh", 25, true),
            Schedule(0, "105", "23:00", "Shuwaikh", "Shadadiyyah", 30, true),
            Schedule(0, "106", "00:00", "Shadadiyyah", "Kaifan", 20, true)
        )
        testSchedules.forEach { schedule ->
            dbHelper.insertSchedule(schedule)
        }
    }
}
