package com.example.dailiesandroidapp
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
//This is the main screen of the android app. NightMode functionalities are WIP
class MainActivity : AppCompatActivity() {
    var mCreateRem: FloatingActionButton? = null
    var mRecyclerview: RecyclerView? = null
    var dataholder = ArrayList<Model>()
    var adapter: myAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nightModeSwitch: Switch = findViewById(R.id.switchThemeBtn)

        nightModeSwitch.isChecked = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            else -> false
        }

        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Disable night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        //Recycler View and Button Listener
        mRecyclerview = findViewById<View>(R.id.recyclerView) as RecyclerView
        mRecyclerview!!.layoutManager = LinearLayoutManager(applicationContext)
        mCreateRem =
            findViewById<View>(R.id.create_reminder) as FloatingActionButton
        mCreateRem!!.setOnClickListener {
            val intent = Intent(applicationContext, ReminderActivity::class.java)
            startActivity(intent)
        }

        val cursor: Cursor =
            DbManager(applicationContext).readallreminders()
        while (cursor.moveToNext()) {
            val model = Model(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            dataholder.add(model)
        }
        adapter = myAdapter(dataholder)
        mRecyclerview!!.adapter = adapter
    }

    override fun onBackPressed() {
        finish() //Exit from app if back button is pressed
        super.onBackPressed()
    }

}