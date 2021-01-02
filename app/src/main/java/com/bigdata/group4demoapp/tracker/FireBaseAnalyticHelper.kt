package com.bigdata.group4demoapp.tracker

import android.content.Context
import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics

class FireBaseAnalyticHelper(context: Context) {
    private val mFireBaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEventDisplay(tracker: ScreenTracker?) {
        tracker?.let {
            mFireBaseAnalytics.logEvent(tracker.eventName?: EVENT_DISPLAYED, buildBundleEventDisplay(it))
        }
    }

    fun logEventDisplay(tracker: ScreenTracker?, aClass: Class<*>) {
        tracker?.apply {
            screenClass = aClass.simpleName
            mFireBaseAnalytics.logEvent(tracker.eventName?: EVENT_DISPLAYED, buildBundleEventDisplay(this))
        }
    }

    fun logEventClick(tracker: ClickTracker?) {
        tracker?.let {
            mFireBaseAnalytics.logEvent(tracker.eventName?: EVENT_CLICKED, buildBundleEventClick(it))
        }
    }

    fun logEventClick(tracker: ClickTracker?, aClass: Class<*>) {
        tracker?.apply {
            screenClass = aClass.simpleName
            mFireBaseAnalytics.logEvent(tracker.eventName?: EVENT_CLICKED, buildBundleEventClick(this))
        }
    }

    private fun buildBundleEventDisplay(tracker: ScreenTracker): Bundle {
        val bundle = Bundle()
        bundle.putString(PARAM_SCREEN, tracker.screen)
        bundle.putString(PARAM_SCREEN_CLASS, tracker.screenClass)
        bundle.putString(PARAM_DESC, tracker.desc)
        bundle.putString(PARAM_STEP, tracker.step)
        bundle.putLong(PARAM_TIME_ON_SCREEN, tracker.timeOnScreen)
        return bundle
    }

    private fun buildBundleEventClick(tracker: ClickTracker): Bundle {
        val bundle = Bundle()
        bundle.putString(PARAM_SCREEN, tracker.screen)
        bundle.putString(PARAM_SCREEN_CLASS, tracker.screenClass)
        bundle.putString(PARAM_STEP, tracker.step)
        bundle.putString(PARAM_BUTTON, tracker.button)
        return bundle
    }

    companion object {
        private const val EVENT_DISPLAYED = "Event_displayed"
        private const val EVENT_CLICKED = "Event_clicked"
        private const val PARAM_SCREEN = "Screen"
        private const val PARAM_SCREEN_CLASS = "ScreenClass"
        private const val PARAM_STEP = "Step"
        private const val PARAM_DESC = "Desc"
        private const val PARAM_BUTTON = "Button"
        private const val PARAM_TIME_ON_SCREEN = "Time_On_Screen"
    }
}
