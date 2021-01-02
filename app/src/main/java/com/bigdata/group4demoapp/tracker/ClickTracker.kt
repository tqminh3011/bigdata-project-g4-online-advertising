package com.bigdata.group4demoapp.tracker

data class ClickTracker(
        var eventName: String? = null,
        var screen: String? = null,
        var screenClass: String? = null,
        var step: String? = null,
        var button: String? = null
)
