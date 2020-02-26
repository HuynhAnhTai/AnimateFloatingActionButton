package com.example.buttonmove

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

@Suppress("DEPRECATION")
class LogInActivity : AppCompatActivity() {
    private lateinit var bt_login: Button
    private lateinit var et_password: EditText

    private var showPassword: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        bt_login = findViewById(R.id.bt_login_activity_log_in)
        et_password = findViewById(R.id.et_password_activity_log_in)

        bt_login.setOnClickListener {
            //press login and intent to MenuButtonActivity
            startActivity(Intent(applicationContext,MenuButtonActivity::class.java))
        }

        et_password.setOnTouchListener { _, motionEvent ->
            var DRAWABLE_RIGHT = 2
            //check if press icon right et_password
            if(motionEvent.action == MotionEvent.ACTION_UP) {
                if(motionEvent.rawX >= (et_password.right - et_password.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    //check password is show or not
                    if (!showPassword) {
                        showPassword()
                    }else{
                        hidePassword()
                    }
                }
            }
            false
        }
    }

    //function to hide password
    private fun hidePassword() {
        showPassword = false
        var draw = resources.getDrawable(R.drawable.ic_eye_off)
        et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null)
        et_password.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    //function to show password
    private fun showPassword() {
        showPassword = true
        var draw = resources.getDrawable(R.drawable.ic_eye_on)
        et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null)
        et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }
}
