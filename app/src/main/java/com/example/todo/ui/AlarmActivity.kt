package com.example.todo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.todo.databinding.ActivityAlarmBinding
import com.example.todo.utils.AlarmReceiver

class AlarmActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent!!.getStringExtra(AlarmReceiver.EXTRA_MESSAGE)
        val description = intent.getStringExtra(AlarmReceiver.EXTRA_DESCRIPTION)
        val time = intent.getStringExtra(AlarmReceiver.EXTRA_TIME)

        binding.textTitleAlarm.text = message
        binding.textDescriptionAlarm.text = description
        binding.textTimeAlarm.text = time
        binding.buttonCloseAlarm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == binding.buttonCloseAlarm) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.finish()
        }
    }
}