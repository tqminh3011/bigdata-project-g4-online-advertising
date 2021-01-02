package com.bigdata.group4demoapp.model

import com.google.gson.annotations.SerializedName

enum class StoryAction {
    @SerializedName("POB")
    POB,
    @SerializedName("BUY")
    BUY,
    @SerializedName("SEND_MONEY")
    SEND_MONEY,
    @SerializedName("PAY_BILLS")
    PAY_BILLS,
    @SerializedName("BUY_SPOTIFY")
    BUY_SPOTIFY,
    @SerializedName("BUY_UBER")
    BUY_UBER,
    @SerializedName("NONE")
    NONE
}