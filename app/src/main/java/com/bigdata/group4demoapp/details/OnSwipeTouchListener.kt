package com.bigdata.group4demoapp.details

import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs


open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

    private val gestureDetector: GestureDetectorCompat

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            onHoldScreen()
        } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            onReleaseScreen()
        }
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }
    }

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}

    open fun onHoldScreen() {}

    open fun onReleaseScreen() {}

    companion object {
        private val SWIPE_THRESHOLD = 300
        private val SWIPE_VELOCITY_THRESHOLD = 300
    }
}