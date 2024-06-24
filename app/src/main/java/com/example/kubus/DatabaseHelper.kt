package com.example.kubus

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "schedule.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_SCHEDULE = "schedule"
        private const val COLUMN_ID = "id"
        private const val COLUMN_BUS_NO = "bus_no"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_ORIGIN = "origin"
        private const val COLUMN_DEST = "dest"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_STATUS = "status"

        private const val CREATE_TABLE_SCHEDULE = "CREATE TABLE $TABLE_SCHEDULE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_BUS_NO TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_ORIGIN TEXT, " +
                "$COLUMN_DEST TEXT, " +
                "$COLUMN_DURATION INTEGER, " +
                "$COLUMN_STATUS INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SCHEDULE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULE")
        onCreate(db)
    }

    fun insertSchedule(schedule: Schedule): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_BUS_NO, schedule.bNo)
            put(COLUMN_TIME, schedule.sTime)
            put(COLUMN_ORIGIN, schedule.sOrigin)
            put(COLUMN_DEST, schedule.sDest)
            put(COLUMN_DURATION, schedule.duration)
            put(COLUMN_STATUS, if (schedule.sStatus) 1 else 0)
        }
        return db.insert(TABLE_SCHEDULE, null, contentValues)
    }

    fun getAllSchedules(): List<Schedule> {
        val scheduleList = mutableListOf<Schedule>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SCHEDULE WHERE $COLUMN_STATUS = 1", null)
        if (cursor.moveToFirst()) {
            do {
                val schedule = Schedule(
                    sID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    bNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUS_NO)),
                    sTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                    sOrigin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN)),
                    sDest = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    sStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) == 1
                )
                scheduleList.add(schedule)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return scheduleList
    }

    fun getSchedulesAfterTime(selectedTime: String): List<Schedule> {
        val scheduleList = mutableListOf<Schedule>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_SCHEDULE WHERE $COLUMN_STATUS = 1 AND $COLUMN_TIME > ?",
            arrayOf(selectedTime)
        )
        if (cursor.moveToFirst()) {
            do {
                val schedule = Schedule(
                    sID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    bNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUS_NO)),
                    sTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                    sOrigin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN)),
                    sDest = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    sStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) == 1
                )
                scheduleList.add(schedule)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return scheduleList
    }
}
