package com.example.buttonmove

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import com.example.buttonmove.constant.Constant


@Suppress("DEPRECATION")
class StartUpActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var iv_background: ImageView

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        iv_background = findViewById(R.id.iv_loading_activity_loading)
        loadImageLoading()
    }

    //change image background start up activity
    @SuppressLint("Recycle")
    private fun loadImageLoading() {
        var arrayImage = resources.obtainTypedArray(R.array.imagesLoading)

        //load image by count slow down and change background
        object : CountDownTimer(Constant.totalTimeLoading, Constant.timeChangeScreenLoading) {

            override fun onTick(millisUntilFinished: Long) {
                iv_background.setImageResource(arrayImage.getResourceId(count,0))
                count++
            }

            //count down finish intent to LogInActivity
            override fun onFinish() {
                startActivity(Intent(applicationContext,LogInActivity::class.java))
                finish()
            }
        }.start()
    }
}
