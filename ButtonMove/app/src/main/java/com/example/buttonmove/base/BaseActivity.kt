package com.example.buttonmove.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.buttonmove.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity: AppCompatActivity() {
    abstract fun getLayoutId(): Int

    private lateinit var fb_primary: FloatingActionButton

    private lateinit var fb_1: FloatingActionButton
    private lateinit var fb_2: FloatingActionButton
    private lateinit var fb_3: FloatingActionButton
    private lateinit var fb_4: FloatingActionButton
    private lateinit var fb_5: FloatingActionButton

    private lateinit var fb_temp_1: FloatingActionButton
    private lateinit var fb_temp_2: FloatingActionButton
    private lateinit var fb_temp_3: FloatingActionButton
    private lateinit var fb_temp_4: FloatingActionButton
    private lateinit var fb_temp_5: FloatingActionButton

    private lateinit var view_type: View

    private var x: Float = 0F
    private var y: Float = 0F
    private var width: Int = 0
    private var height: Int = 0
    private var statusBarHeight: Int = 0

    private var open: Boolean = false
    private var done: Boolean = true

    private var shouldClick: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        height = size.y

        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        fb_primary = findViewById(R.id.floating_button)
        fb_1 = findViewById(R.id.floating_1)
        fb_2 = findViewById(R.id.floating_1)
        fb_3 = findViewById(R.id.floating_1)
        fb_4 = findViewById(R.id.floating_1)
        fb_5 = findViewById(R.id.floating_1)

        fb_temp_1 = findViewById(R.id.floating_temp_1)
        fb_temp_2 = findViewById(R.id.floating_temp_2)
        fb_temp_3 = findViewById(R.id.floating_temp_3)
        fb_temp_4 = findViewById(R.id.floating_temp_4)
        fb_temp_5 = findViewById(R.id.floating_temp_5)

        view_type = findViewById(R.id.view_temp)

        fb_primary.setOnTouchListener { view, motionEvent ->
            if (done) {
                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        actionMove(view, motionEvent)
                        shouldClick = false
                    }
                    MotionEvent.ACTION_DOWN -> {
                        x = motionEvent.x
                        y = motionEvent.y
                        shouldClick = true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (view.x > width / 2) {
                            view.x = width.toFloat() - view.width
                        } else if (view.x <= width / 2) {
                            view.x = 0F
                        }
                        if (shouldClick) {
                            fb_primary.performClick()
                        }
                    }
                }
            }
            true
        }
        fb_primary.setOnClickListener {
            if (done) {
                done = false
                if (!open) {
                    open = true
                    setXYFloatingButton(floating_1, fb_primary)
                    setXYFloatingButton(floating_2, fb_primary)
                    setXYFloatingButton(floating_3, fb_primary)
                    setXYFloatingButton(floating_4, fb_primary)
                    setXYFloatingButton(floating_5, fb_primary)

                    openView()
                } else {
                    open = false
                    closeView()
                }
            }
        }

        view_temp.setOnClickListener {
            if (done) {
                open = false
                closeView()
            }
        }
    }

    private fun openView() {
        openAnimateFloating(floating_1, floating_temp_1)
        openAnimateFloating(floating_2, floating_temp_2)
        openAnimateFloating(floating_3, floating_temp_3)
        openAnimateFloating(floating_4, floating_temp_4)
        openAnimateFloating(floating_5, floating_temp_5)

        view_temp.visibility = View.VISIBLE
        val from = ContextCompat.getColor(this, R.color.blackT)
        val to = ContextCompat.getColor(this, R.color.blackD)
        changeColorBackgroundFrom(from,to)
    }

    private fun closeView(){
        closeAnimateFloating(floating_1)
        closeAnimateFloating(floating_2)
        closeAnimateFloating(floating_3)
        closeAnimateFloating(floating_4)
        closeAnimateFloating(floating_5)

        val from = ContextCompat.getColor(this, R.color.blackD)
        val to = ContextCompat.getColor(this, R.color.blackT)
        changeColorBackgroundFrom(from,to)
    }

    private fun openAnimateFloating(floating: FloatingActionButton, floatingTemp: FloatingActionButton) {
        floating.visibility = View.VISIBLE
        floating.animate()
            .x(floatingTemp.x)
            .y(floatingTemp.y)
            .setDuration(1000)
            .withEndAction {
                floating.x = floatingTemp.x
                floating.y = floatingTemp.y
                if (floating.id == floating_5.id){
                    done = true
                }
            }
            .start()
    }

    private fun closeAnimateFloating(floating: FloatingActionButton) {
        floating.animate()
            .x(fb_primary.x)
            .y(fb_primary.y)
            .setDuration(1000)
            .withEndAction {
                floating.x = fb_primary.x
                floating.y = fb_primary.y
                floating.visibility = View.GONE
                if (floating.id == floating_5.id){
                    done = true
                    view_temp.visibility = View.GONE
                }
            }
            .start()
    }

    private fun actionMove(view: View, motionEvent: MotionEvent) {
        view.x = view.x + motionEvent.x - x
        view.y = view.y + motionEvent.y - y
        if(view.x <0){
            view.x = 0F
        }
        else if ((view.x + view.width)>=width){
            view.x = width.toFloat() - view.width
        }

        if(view.y <0){
            view.y = 0F
        }
        else if ((view.y + view.height)>=(height - statusBarHeight) ){
            view.y = height.toFloat() - view.height - statusBarHeight
        }
    }

    private fun changeColorBackgroundFrom(from: Int, to: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(from, to)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator ->
            view_temp.setBackgroundColor(valueAnimator.animatedValue as Int)
        }

        anim.duration = 1000
        anim.start()
    }

    private fun setXYFloatingButton(floating: FloatingActionButton, fb: FloatingActionButton) {
        floating.x = fb.x
        floating.y = fb.y
    }
}