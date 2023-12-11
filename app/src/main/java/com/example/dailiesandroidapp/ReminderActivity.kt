package com.example.dailiesandroidapp
import com.example.dailiesandroidapp.AlarmBroadcast
import com.example.dailiesandroidapp.DbManager
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
//import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

//This class takes in input from the User and inserts into DB. Also helps set up alarm
class ReminderActivity : AppCompatActivity() {
    var mSubmitbtn: Button? = null
    //var mDelbtn: ImageButton? = null
    var mDatebtn: Button? = null
    var mTimebtn: Button? = null
    var mTitledit: EditText? = null
    var timeTonotify: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        mTitledit = findViewById<View>(R.id.editTitle) as EditText
        mDatebtn =
            findViewById<View>(R.id.btnDate) as Button
        mTimebtn = findViewById<View>(R.id.btnTime) as Button
        mSubmitbtn = findViewById<View>(R.id.btnSubmit) as Button

        mTimebtn!!.setOnClickListener {
            selectTime() //Opens up Clock
        }
        mDatebtn!!.setOnClickListener { selectDate() } //Opens up calendar

        mSubmitbtn!!.setOnClickListener {
            val title = mTitledit!!.text.toString()
                .trim { it <= ' ' }
            val date = mDatebtn!!.text.toString()
                .trim { it <= ' ' }
            val time = mTimebtn!!.text.toString()
                .trim { it <= ' ' }
            if (title.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter text", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (time == "time" || date == "date") {
                    Toast.makeText(
                        applicationContext,
                        "Please select date and time",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    processinsert(title, date, time)
                }
            }
        }
    }

    private fun processinsert(title: String, date: String, time: String) {
        val result: String = DbManager(this).addreminder(
            title,
            date,
            time
        ) //Insert into SQLITE
        setAlarm(title, date, time)
        mTitledit!!.setText("")
        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
    }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this,
            { timePicker, i, i1 ->
                timeTonotify = "$i:$i1" //temp
                mTimebtn!!.text = FormatTime(i, i1)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun selectDate() {//Opens up Calendar
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this,
            { datePicker, year, month, day ->
                mDatebtn!!.text =
                    day.toString() + "-" + (month + 1) + "-" + year
            }, year, month, day
        )
        datePickerDialog.show()
    }

    fun FormatTime(
        hour: Int,
        minute: Int
    ): String {
        var time: String
        time = ""
        val formattedMinute: String
        formattedMinute = if (minute / 10 == 0) {
            "0$minute"
        } else {
            "" + minute
        }
        time = if (hour == 0) {
            "12:$formattedMinute AM"
        } else if (hour < 12) {
            "$hour:$formattedMinute AM"
        } else if (hour == 12) {
            "12:$formattedMinute PM"
        } else {
            val temp = hour - 12
            "$temp:$formattedMinute PM"
        }
        return time
    }

    private fun setAlarm(text: String, date: String, time: String) {
        val am =
            getSystemService(ALARM_SERVICE) as AlarmManager //
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        intent.putExtra(
            "event",
            text
        ) //Sends to alarm class
        intent.putExtra("time", date)
        intent.putExtra("date", time)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val dateandtime = "$date $timeTonotify"
        val formatter: DateFormat = SimpleDateFormat("d-M-yyyy hh:mm")
        try {
            val date1 = formatter.parse(dateandtime)
            am[AlarmManager.RTC_WAKEUP, date1.time] = pendingIntent
            Toast.makeText(applicationContext, "Alarm set", Toast.LENGTH_SHORT).show()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val intentBack = Intent(
            applicationContext,
            MainActivity::class.java
        )
        intentBack.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentBack) //Go back to main activity
    }
}