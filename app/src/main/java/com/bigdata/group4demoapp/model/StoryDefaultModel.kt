package com.bigdata.group4demoapp.model

import com.google.gson.annotations.SerializedName

data class StoryDefaultModel(
    @SerializedName("title") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("image") var image: String = ""
)