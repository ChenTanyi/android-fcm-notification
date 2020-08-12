package com.tangenty.notification

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.tangenty.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

        checkGooglePlayServices()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                val method = "getInstanceId"
                val msg: String
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_failed, method, "", task.exception.toString())
                    Log.e(TAG, msg, task.exception)
                } else {
                    val token = task.result?.token
                    msg = getString(R.string.msg_succeeded, method, token)
                    Log.d(TAG, msg)
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }

        binding.subscribeButton.setOnClickListener {
            val topic = binding.topic.text.toString()
            val method = getString(R.string.subscribe)
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    val msg: String
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_failed, method, topic, task.exception.toString())
                        Log.e(TAG, msg, task.exception)
                    } else {
                        msg = getString(R.string.msg_succeeded, method, topic)
                        Log.d(TAG, msg)
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
        }

        binding.unsubscribeButton.setOnClickListener {
            val topic = binding.topic.text.toString()
            val method = getString(R.string.unsubscribe)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    val msg: String
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_failed, method, topic, task.exception.toString())
                        Log.e(TAG, msg, task.exception)
                    } else {
                        msg = getString(R.string.msg_succeeded, method, topic)
                        Log.d(TAG, msg)
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkGooglePlayServices() {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        val msg: String
        if (status == ConnectionResult.SUCCESS) {
            msg = getString(R.string.msg_google_play_services_check, "Success")
            Log.d(TAG, msg)
        } else {
            msg = getString(R.string.msg_google_play_services_check, "Fail")
            Log.e(TAG, msg)
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}