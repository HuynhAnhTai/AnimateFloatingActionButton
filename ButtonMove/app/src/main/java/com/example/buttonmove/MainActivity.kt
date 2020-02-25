package com.example.buttonmove

import android.os.Bundle
import com.example.buttonmove.base.BaseActivity

@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}
