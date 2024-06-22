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
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define vars
        //textview
        val timeSelector: TextView = findViewById(R.id.tvTimeSelector)
        //spinner
        val spinFrom: Spinner = findViewById(R.id.spinFrom)
        val spinTo: Spinner = findViewById(R.id.spinTo)
        //button
        val btnSearch: MaterialButton = findViewById(R.id.btnSearch)
        //sample loc data
        val locations = arrayOf("Shuwaikh", "Shadadiyyah", "Kaifan")


        //bind spinner with adapter
        //array adapter with default spinner layout
        val adapterFrom = ArrayAdapter(this,R.layout.spinner_item, locations)
        val adapterTo = ArrayAdapter(this,R.layout.spinner_item, locations)
        //layout when choices appear
        adapterFrom.setDropDownViewResource(R.layout.spinner_item)
        adapterTo.setDropDownViewResource(R.layout.spinner_item)
        //apply adapter to spinner
        spinFrom.adapter = adapterFrom
        spinTo.adapter = adapterTo

        //set textview to current time
        // Set default time to current time
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        updateTimeText(timeSelector, currentHour, currentMinute)

        //time picker when time selector clicked
        timeSelector.setOnClickListener(){
            TimePickerDialog(this,{_, hourOfDay, minute -> updateTimeText(timeSelector, hourOfDay, minute)}, currentHour, currentMinute, true).show()
        }

        //search button
        btnSearch.setOnClickListener(){
            // Retrieve the selected values from the Spinners
            val to = spinTo.selectedItem.toString()
            val from = spinFrom.selectedItem.toString()
            val time = timeSelector.text.toString()

            // Validate input data
            if (TextUtils.isEmpty(to) || TextUtils.isEmpty(from) || TextUtils.isEmpty(time)) {
                Toast.makeText(this@MainActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Print values in the logs
            Log.d("MainActivity", "To: $to, From: $from, Time: $time")
        }

    }

    //function to set time to text view
    private fun updateTimeText(textView: TextView, hour: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hour, minute)
        textView.text = formattedTime
    }
}