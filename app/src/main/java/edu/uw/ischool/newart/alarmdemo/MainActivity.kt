package edu.uw.ischool.newart.alarmdemo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


const val ALARM_ACTION = "edu.uw.ischool.newart.ALARM"

class MainActivity : AppCompatActivity() {
    lateinit var button : Button
    var receiver : BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button.setOnClickListener { snoozeAlarm() }
    }
    override fun onPause() {
        super.onPause()

        unregisterReceiver(receiver)
        receiver = null
    }

    fun snoozeAlarm() {
        val activityThis = this

        if (receiver == null) {
            // Make sure of our BroadcastReceiver; remember how we can
            // create an object on the fly that inherits from a class?
            // Let's use that to create an anonymous subclass of the
            // BroadcastReceiver type, and then register it dynamically
            // since we don't really need much beyond just catching the
            // intent fired at us from the AlarmManager
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Toast.makeText(activityThis, "WAKE UP! ${intent?.action}", Toast.LENGTH_SHORT).show()
                }
            }
            val filter = IntentFilter(ALARM_ACTION)
            registerReceiver(receiver, filter)
        }

        // Create the PendingIntent
        val intent = Intent(ALARM_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Get the Alarm Manager
        val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 5000,
            pendingIntent)
    }
}