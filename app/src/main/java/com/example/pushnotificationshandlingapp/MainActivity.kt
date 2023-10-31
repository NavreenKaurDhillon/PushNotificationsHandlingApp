package com.example.pushnotificationshandlingapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleNotification()
    }

    private fun handleNotification() {
        val type = intent.getStringExtra("type")
        if (type != null) {
            //push is received success - skip splash screen
            when (type) {
                "1" -> Log.d(TAG, "handleNotification: type 1")//case for 1
                "2" -> Log.d(TAG, "handleNotification: type 2")//case for 2
                "3" -> //1 -> navigate to booking details screen
                {
                    Log.d(TAG, "checkForNavigation: // booking id = ")
                    val detailsBundle = Bundle()
                    detailsBundle.putInt("id", 8)
                    //navController.navigate(R.id.action_to_detailsFragment, detailsBundle)
                }
            } //tjtykyf
        } else {
            //failure or null case - normal execution through splash

        }
    }
}