package com.example.kubus


data class Schedule(
    val sID: Int,
    val bNo: String,
    val sTime: String,
    val sOrigin: String,
    val sDest: String,
    val duration: Int,
    val sStatus: Boolean
)