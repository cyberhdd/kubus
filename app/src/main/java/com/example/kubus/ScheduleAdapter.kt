package com.example.kubus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(private val scheduleList: List<Schedule>, private val initialTime: String) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private val filteredScheduleList: List<Schedule> = scheduleList.filter { it.sStatus && isBusComingAfterSelectedTime(it.sTime) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_sched_layout, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val currentItem = filteredScheduleList[position]

        holder.busNoTextView.text = currentItem.bNo

        val etd = currentItem.sTime
        val duration = currentItem.duration

        val etdTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(etd)
        val calendar = Calendar.getInstance()
        calendar.time = etdTime!!
        calendar.add(Calendar.MINUTE, duration)
        val etaTime = calendar.time

        val initialTimeDate = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(initialTime)!!
        val timeRemaining = ((etdTime.time - initialTimeDate.time) / 60000).toInt()

        val eta = SimpleDateFormat("HH:mm", Locale.getDefault()).format(etaTime)

        holder.departureTextView.text = etd
        holder.arrivalTextView.text = eta
        holder.arrivingTextView.text = timeRemaining.toString()
    }

    override fun getItemCount() = filteredScheduleList.size

    private fun isBusComingAfterSelectedTime(busTime: String): Boolean {
        val busTimeDate = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(busTime)!!
        val selectedTimeDate = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(initialTime)!!
        return busTimeDate.after(selectedTimeDate)
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val busNoTextView: TextView = itemView.findViewById(R.id.tvBusNo)
        val departureTextView: TextView = itemView.findViewById(R.id.tvDepart)
        val arrivalTextView: TextView = itemView.findViewById(R.id.tvArrive)
        val arrivingTextView: TextView = itemView.findViewById(R.id.tvArriving)
    }
}
