package com.teamisland.zzazz.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FloatRange
import com.google.android.exoplayer2.SimpleExoPlayer
import com.teamisland.zzazz.utils.UnitConverter.float2DP
import com.teamisland.zzazz.video_trimmer_library.view.RangeSeekBarView
import kotlin.math.abs


/**
 * Ranged seekbar with current position bar.
 */
@Suppress("LeakingThis")
open class CurrentPositionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // current position
    internal val markerPaint = Paint()

    // current position during trimming
    private var trimPaint = Paint()

    @FloatRange(from = 0.0, to = 100.0)
    internal var markerPos = 0.0
    private val layout = LinearLayout(context)
    private val textView = TextView(context)
    internal lateinit var listener: IPositionChangeListener
    private var videoDuration: Int = 0
    private lateinit var range: RangeSeekBarView
    private lateinit var player: SimpleExoPlayer

    private fun getText(): String {
        val pos = videoDuration * markerPos / 100
        val totalSecond = pos / 1000
        val millisecond = (pos / 10) % 100
        val second = totalSecond % 60
        val minute = totalSecond / 60 % 60
        return java.util.Formatter().format("%02d:%02d:%02d", minute.toInt(), second.toInt(), millisecond.toInt()).toString()
    }

    /**
     * Set [player]
     */
    fun setPlayer(simpleExoPlayer: SimpleExoPlayer) {
        player = simpleExoPlayer
    }

    /**
     * Sets range.
     */
    fun setRange(range: RangeSeekBarView) {
        this.range = range
    }

    /**
     * Adds listener to the view.
     */
    fun setListener(listener: IPositionChangeListener) {
        this.listener = listener
    }

    /**
     * Sets duration of the video.
     */
    fun setDuration(duration: Int) {
        videoDuration = duration
    }

    private fun getPointInViewWidth(): Float = (markerPos * (width - 2 * float2DP(12f, resources)) / 100).toFloat() + float2DP(12f, resources)

    init {
        markerPaint.color = 0xffffffff.toInt()
        markerPaint.style = STROKE
        markerPaint.strokeWidth = float2DP(2f, resources)
        markerPaint.strokeCap = Paint.Cap.SQUARE
        markerPaint.setShadowLayer(float2DP(2f, resources), float2DP(2f, resources), 0f, 0x60000000)

        trimPaint.color = 0xffffffff.toInt()
        trimPaint.style = STROKE
        trimPaint.strokeWidth = float2DP(1f, resources)
        trimPaint.strokeCap = Paint.Cap.SQUARE
        trimPaint.alpha = 0

        textView.fontFeatureSettings = "@font/archivo_regular"
        textView.textSize = 10F
        textView.visibility = GONE
        textView.setTextColor(0xccffffff.toInt())

        layout.addView(textView)
    }

    /**
     * Sets the position of the marker.
     * @param pos Position of the marker in percentage (0 ~ 100).
     */
    fun setMarkerPos(pos: Double) {
        markerPos = pos
        invalidate()
    }

    /**
     * Draws the view.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
                getPointInViewWidth(),
                float2DP(28f, resources),
                getPointInViewWidth(),
                height.toFloat(),
                markerPaint
        )

        canvas.drawLine(
                getPointInViewWidth(),
                float2DP(16f, resources),
                getPointInViewWidth(),
                float2DP(32f, resources),
                trimPaint
        )

        layout.measure(width, height)
        layout.layout(0, 0, width, height)
        textView.text = getText()
        // To place the text view somewhere specific:
        canvas.translate(getPointInViewWidth() - textView.width / 2, 0f)
        layout.draw(canvas)
    }

    private fun isClicked(pos: Float): Boolean {
        return abs(getPointInViewWidth() - pos) < float2DP(5f, resources)
    }

    /**
     * Touch event handler
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val coordinate = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isClicked(coordinate)) {
                    return false
                }
                markerPos = ((event.x - float2DP(12f, resources)) * 100 / (width - 2 * float2DP(12f, resources))).toDouble()
                markerPaint.color = 0xffff3898.toInt()
                textView.visibility = VISIBLE
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                markerPaint.color = 0xccffffff.toInt()
                textView.visibility = GONE
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // Calculate the distance moved
                markerPos = ((event.x - float2DP(12f, resources)) * 100 / (width - 2 * float2DP(12f, resources))).toDouble()
                if (markerPos < range.getStart() * 100.0 / videoDuration)
                    markerPos = range.getStart() * 100.0 / videoDuration
                if (markerPos > range.getEnd() * 100.0 / videoDuration)
                    markerPos = range.getEnd() * 100.0 / videoDuration
                if (markerPos < 0.0) markerPos = 0.0
                if (markerPos > 100.0) markerPos = 100.0
                listener.onChange(markerPos)
                player.seekTo((videoDuration * markerPos / 100).toLong())
                invalidate()
                return true
            }
        }
        return false
    }

    /**
     * Make trim current visible.
     */
    fun visibleTrimCurrent() {
        trimPaint.alpha = 255
        textView.visibility = VISIBLE
        markerPaint.alpha = 0
    }

    /**
     * Make marker current visible.
     */
    fun visibleMarkerCurrent() {
        trimPaint.alpha = 0
        textView.visibility = GONE
        markerPaint.alpha = 255
    }
}
