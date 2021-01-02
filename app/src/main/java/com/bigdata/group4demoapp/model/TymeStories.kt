package com.bigdata.group4demoapp.model

import com.google.gson.annotations.SerializedName

data class Stories(@SerializedName("stories") var storiesList: ArrayList<Story>)