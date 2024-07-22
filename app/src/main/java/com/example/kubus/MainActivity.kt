package com.example.kubus

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
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

    companion object {
        private const val PREFS_NAME = "AppPreferences"
        private const val KEY_FIRST_RUN = "firstRun"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
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

        if (sharedPreferences.getBoolean(KEY_FIRST_RUN, true)) {
            if (!databaseHelper.isTestDataInserted()) {
                insertTestSchedules(databaseHelper)
            }
            sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply()
        }

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
            scheduleAdapter = ScheduleAdapter(schedules, time, from, to)
            recyclerView.adapter = scheduleAdapter
        }
    }

    private fun updateTimeText(textView: TextView, hour: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hour, minute)
        textView.text = formattedTime
    }

    fun insertTestSchedules(dbHelper: DatabaseHelper) {
        val testSchedules = schedules

        testSchedules.forEach { schedule ->
            dbHelper.insertSchedule(schedule)
        }
    }
}
