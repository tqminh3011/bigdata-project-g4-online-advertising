package com.bigdata.group4demoapp.tracker

data class ScreenTracker(
        var eventName: String? = null,
        var screen: String? = null,
        var screenClass: String? = null,
        var step: String? = null,
        var desc: String? = null,
        var id: String? = null,
        var timeOnScreen: Long = 0L
) {
    constructor(screen: String?, screenClass: String?) : this() {
        this.screen = screen
        this.screenClass = screenClass
    }
}
