package com.kluivert.knote.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.kluivert.knote.R
import com.kluivert.knote.adapter.SlideAdapter
import com.kluivert.knote.data.entities.SlideModel
import com.kluivert.knote.databinding.ActivitySlideBinding
import com.kluivert.knote.ui.activities.MainActivity
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class SlideActivity : AppCompatActivity() {

 private lateinit var slideBinding : ActivitySlideBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
        val view = slideBinding.root
        setContentView(view)


        var slideList = mutableListOf(
            SlideModel(R.drawable.slideone,"Organise your thoughts","Built specifically for you at your comfort"),
            SlideModel(R.drawable.slidetwo,"Beautiful user interface and experience","Knote cares about design and simplicity"),
            SlideModel(R.drawable.slidethree,"Take notes with ease","Note taking has never been better, Knote makes it easy")
        )

        val adapter = SlideAdapter(slideList)
        slideBinding.slidePager.adapter = adapter

        slideBinding.indicatorView
            .setSliderColor(R.color.colorSlideInAct,R.color.colorSlideAct)
            .setSliderWidth(resources.getDimension(R.dimen._17sdp))
            .setSliderHeight(resources.getDimension(R.dimen._5sdp))
            .setSlideMode(IndicatorSlideMode.WORM)
            .setIndicatorStyle(IndicatorStyle.CIRCLE)
            .setupWithViewPager(slideBinding.slidePager)

        slideBinding.slidePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        slideBinding.slidePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (position==2){
                    slideBinding.btnNext.text = getString(R.string.finish)
                    slideBinding.btnNext.visibility = View.VISIBLE
                    Intent(this@SlideActivity,MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(it)
                        finish()
                    }
                  }

                }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        val prefs = "slide"
        val sharedPreferences = getSharedPreferences("slideprefs",Context.MODE_PRIVATE)
        if(!sharedPreferences.getBoolean(prefs,false)){
            val editor = sharedPreferences.edit()
            editor.putBoolean(prefs,true)
            editor.apply()
        }else {
            Intent(this@SlideActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}