package com.example.buttonmove.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.buttonmove.R
import com.example.buttonmove.constant.Constant
import kotlinx.android.synthetic.main.activity_main.*
//base activity to animate image button filter, profile, list, sound, attach and background view
abstract class BaseActivity: AppCompatActivity() {
    abstract fun getLayoutId(): Int
    //image button  menu
    private lateinit var image_button_menu: ImageButton

    //image buttons depend on functions
    private lateinit var image_button_filter: ImageButton
    private lateinit var image_button_profile: ImageButton
    private lateinit var image_button_list: ImageButton
    private lateinit var image_button_attach: ImageButton
    private lateinit var image_button_sound: ImageButton

    //image buttons mark location to animate
    private lateinit var image_button_temp_filter: ImageButton
    private lateinit var image_button_temp_profile: ImageButton
    private lateinit var image_button_temp_list: ImageButton
    private lateinit var image_button_temp_attach: ImageButton
    private lateinit var image_button_temp_sound: ImageButton

    //view background change color when the menu button click open or close
    private lateinit var view_background: View

    //postion x of image_button_menu
    private var x: Float = 0F
    //postition y of image_button_menu
    private var y: Float = 0F
    //width of screen telehpone
    private var width: Int = 0
    //height of screen telephone
    private var height: Int = 0
    //height of status bar telephone
    private var statusBarHeight: Int = 0
    //check image_button_menu is open or close
    private var open: Boolean = false
    //check animate is done or not
    private var done: Boolean = true
    //check image_button_menu should click
    private var shouldClick: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        //activity main has image button menu, other image button so if ID layout
        // not R.layout.activity_main, it will anounce by toast
        if (getLayoutId() == R.layout.activity_main) {
            //get width and height of screen telephone to limit area image_button_menu can move, and don't
            // let image_button_menu move out of screen
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            width = size.x
            height = size.y

            val resourceId = resources.getIdentifier("status_bar_height", "dimen"
                , "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }

            //find Id image button fuctions
            image_button_menu = findViewById(R.id.image_button_menu_activity_main)
            image_button_filter = findViewById(R.id.image_button_filter_activity_main)
            image_button_profile = findViewById(R.id.image_button_profile_activity_main)
            image_button_list = findViewById(R.id.image_button_list_activity_main)
            image_button_attach = findViewById(R.id.image_button_attach_activity_main)
            image_button_sound = findViewById(R.id.image_button_sound_activity_main)
            //find Id floating action button location
            image_button_temp_filter = findViewById(R.id.image_temp_filter_activity_main)
            image_button_temp_profile = findViewById(R.id.image_temp_profile_activity_main)
            image_button_temp_list = findViewById(R.id.image_temp_list_activity_main)
            image_button_temp_attach = findViewById(R.id.image_temp_attach_activity_main)
            image_button_temp_sound = findViewById(R.id.image_temp_sound_activity_main)
            //find Id view background
            view_background = findViewById(R.id.view_background_activity_main)

            //control image_button_menu when move, press, or release and when image_button_menu is moving, image_button_menu
            //can't click and it controls by variable shouldClick
            image_button_menu.setOnTouchListener { view, motionEvent ->
                if (done) {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_MOVE -> {
                            actionMove(view, motionEvent)
                            shouldClick = false
                        }
                        MotionEvent.ACTION_DOWN -> {
                            //save position when press image_button_menu
                            x = motionEvent.x
                            y = motionEvent.y
                            shouldClick = true
                        }
                        MotionEvent.ACTION_UP -> {
                            //when release if image_button_menu has x > wide/2 so image_button_menu will go to right
                            // else go to left of screen
                            if (view.x > width / 2) {
                                view.x = width.toFloat() - view.width
                            } else if (view.x <= width / 2) {
                                view.x = 0F
                            }
                            // if image_button_menu don't move, we can press image_button_menu to show or hide
                            // those image button functions
                            if (shouldClick) {
                                image_button_menu.performClick()
                            }
                        }
                    }
                }
                true
            }
            image_button_menu.setOnClickListener {
                //if animate have not done, we can't continue click to show or hide
                // those image button functions
                if (done) {
                    done = false
                    //check image_button_menu is showing or hiding those image button functions
                    if (!open) {
                        open = true
                        //if image_button_menu is pressed to show, first we need set loaction of these image button
                        // functions to image_button_menu, so that when they animate, they can begin
                        // at image_button_menu location
                        setXYImageButton(image_button_filter, image_button_menu)
                        setXYImageButton(image_button_profile, image_button_menu)
                        setXYImageButton(image_button_list, image_button_menu)
                        setXYImageButton(image_button_attach, image_button_menu)
                        setXYImageButton(image_button_sound, image_button_menu)
                        //show these floating action buttons functions
                        showFloatingButtonFuctions()
                    } else {
                        open = false
                        //hide these floating action buttons functions
                        hideImageButtonFuctions()
                    }
                }
            }

            //if view_backgound is visible and is clicked, we will hide these image buttons functions
            view_background_activity_main.setOnClickListener {
                if (done) {
                    open = false
                    hideImageButtonFuctions()
                }
            }
        }
        else{
            Toast.makeText(applicationContext,"ID layout is not activity_main",Toast.LENGTH_SHORT).show()
        }
    }
    //function to call functions animateImageButtonShow() to animate show image buttons functions
    // and call changeColorViewBackground() to animate change color background
    private fun showFloatingButtonFuctions() {
        animateImageButtonShow(image_button_filter, image_button_temp_filter)
        animateImageButtonShow(image_button_profile, image_button_temp_profile)
        animateImageButtonShow(image_button_list, image_button_temp_list)
        animateImageButtonShow(image_button_attach, image_button_temp_attach)
        animateImageButtonShow(image_button_sound, image_button_temp_sound)

        view_background_activity_main.visibility = View.VISIBLE
        val from = ContextCompat.getColor(this, R.color.blackT)
        val to = ContextCompat.getColor(this, R.color.blackD)
        changeColorViewBackground(from,to)
    }

    //function to call functions animateImageButtonHide() to animate show image buttons functions
    // and call changeColorViewBackground() to animate change color background
    private fun hideImageButtonFuctions(){
        animateImageButtonHide(image_button_filter)
        animateImageButtonHide(image_button_profile)
        animateImageButtonHide(image_button_list)
        animateImageButtonHide(image_button_attach)
        animateImageButtonHide(image_button_sound)

        val from = ContextCompat.getColor(this, R.color.blackD)
        val to = ContextCompat.getColor(this, R.color.blackT)
        changeColorViewBackground(from,to)
    }

    //function animate show image button actions and when finish, variable will done to continue
    //different animate and can move image_button_menu
    private fun animateImageButtonShow(image: ImageButton, imageTemp: ImageButton) {
        image.visibility = View.VISIBLE
        image.animate()
            .x(imageTemp.x)
            .y(imageTemp.y)
            .setDuration(Constant.timeToAnimate)
            .withEndAction {
                image.x = imageTemp.x
                image.y = imageTemp.y
                if (image.id == image_button_sound.id){
                    done = true
                }
            }
            .start()
    }

    //function animate hide image_button button actions and when finish, variable will done to continue
    //different animate and can move image_button_menu and view_background will gone
    private fun animateImageButtonHide(image_button: ImageButton) {
        image_button.animate()
            .x(image_button_menu.x)
            .y(image_button_menu.y)
            .setDuration(Constant.timeToAnimate)
            .withEndAction {
                image_button.x = image_button_menu.x
                image_button.y = image_button_menu.y
                image_button.visibility = View.GONE
                if (image_button.id == image_button_sound.id){
                    done = true
                    view_background_activity_main.visibility = View.GONE
                }
            }
            .start()
    }

    //funtions to verify image_button_menu don't move out of area of screen telephone
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

    //function to animate change background from this color to another color
    private fun changeColorViewBackground(from: Int, to: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(from, to)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator ->
            view_background_activity_main.setBackgroundColor(valueAnimator.animatedValue as Int)
        }

        anim.duration = Constant.timeToAnimate
        anim.start()
    }

    //functions set x,y of image button function to image_button_menu location
    private fun setXYImageButton(image: ImageButton, image_button_menu: ImageButton) {
        image.x = image_button_menu.x
        image.y = image_button_menu.y
    }
}