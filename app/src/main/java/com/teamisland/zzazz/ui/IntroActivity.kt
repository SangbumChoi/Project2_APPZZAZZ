package com.teamisland.zzazz.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.teamisland.zzazz.R
import com.teamisland.zzazz.utils.PermissionManager
import com.teamisland.zzazz.utils.UnitConverter.float2DP
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.*

/**
 * Main activity of Intro Activity
 */
class IntroActivity : AppCompatActivity() {

    companion object {
        /**
         * Uri of the video retrieved.
         */
        const val VIDEO_URI: String = "origin_video"

        private const val LOAD_VIDEO = 1

        private const val TAKE_VIDEO = 2
    }

    private lateinit var circle: ImageView
    private lateinit var underline: ImageView

    private val permissionManager = PermissionManager(this, this)

    @Suppress("SameParameterValue")
    private fun getVideo(requestCode: Int) {
        if (requestCode == LOAD_VIDEO) {
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
                type = "video/*"
                startActivityForResult(this, requestCode)
            }
        } else if (requestCode == TAKE_VIDEO) {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
                startActivityForResult(it, requestCode)
            }
        }
    }

    /**
     * [AppCompatActivity.onCreate]
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        window.navigationBarColor = getColor(R.color.Background)

        val random = Random()
        val textArray = arrayOf(
            getString(R.string.intro1),
            getString(R.string.intro2),
            getString(R.string.intro3),
            getString(R.string.intro4),
            getString(R.string.intro5)
        )
        val index = random.nextInt(textArray.size - 1)
        random_text.text = textArray[index]
        random_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f)

        checkPermission()

        circle = ImageView(this).apply {
            setImageResource(R.drawable.circle_point)
            id = View.generateViewId()
        }
        underline = ImageView(this).apply {
            setImageResource(R.drawable.underline)
            id = View.generateViewId()
        }
        constraintLayout.apply {
            addView(circle)
            addView(underline)
        }

        when (index) {
            0 -> {
                setPosition(circle.id, 132, 132)
                setPosition(underline.id, 16, 44)
            }
            1 -> {
                setPosition(circle.id, 40, 88)
                setPosition(underline.id, 16, 44)
            }
            2 -> {
                setPosition(circle.id, 152, 132)
                setPosition(underline.id, 16, 44)
            }
            3 -> {
                setPosition(circle.id, 16, 132)
                setPosition(underline.id, 16, 44)
            }
            4 -> {
                setPosition(circle.id, 178, 132)
                setPosition(underline.id, 64, 44)
            }
        }

        val shrink = AnimationUtils.loadAnimation(this, R.anim.shrink)
        zzazz.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    take.visibility = View.INVISIBLE
                    v.startAnimation(shrink)
                }
                MotionEvent.ACTION_UP -> {
                    getVideo(LOAD_VIDEO)
                    v.clearAnimation()
                    take.visibility = View.VISIBLE
                }
            }
            true
        }

        take.setOnClickListener { getVideo(TAKE_VIDEO) }

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        linearLayout.startAnimation(bounce)
        load_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
    }

    private fun checkPermission() {
        if (permissionManager.checkPermission())
            permissionManager.requestPermission()
    }

    /**
     * [AppCompatActivity.onRequestPermissionsResult]
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!permissionManager.permissionResult(requestCode, grantResults))
            permissionManager.requestPermission()
    }

    private fun setPosition(id: Int, startMargin: Int, bottomMargin: Int) {
        val bias =
            if (id == circle.id) 1 - (bottomMargin + 24) / 220f
            else 1 - (bottomMargin + 4) / 220f
        ConstraintSet().apply {
            clone(constraintLayout)
            connect(
                id,
                ConstraintSet.START,
                constraintLayout.id,
                ConstraintSet.START,
                float2DP(startMargin.toFloat(), resources).toInt()
            )
            connect(
                id,
                ConstraintSet.TOP,
                constraintLayout.id,
                ConstraintSet.TOP
            )
            connect(
                id,
                ConstraintSet.BOTTOM,
                linearLayout.id,
                ConstraintSet.TOP
            )
            setVerticalBias(id, bias)
        }.applyTo(constraintLayout)
    }

    /**
     * Retrieve uri from request. Checks whether the uri is valid under the restrictions.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        val videoUri = when (requestCode) {
            LOAD_VIDEO, TAKE_VIDEO -> (data ?: return).data ?: return
            else -> null
        }

        if (videoUri != null) {
            Intent(this, TrimmingActivity::class.java).also {
                it.putExtra(VIDEO_URI, videoUri)
                startActivity(it)
            }
        }
    }
}
