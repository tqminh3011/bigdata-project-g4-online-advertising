package com.bigdata.group4demoapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    @SerializedName("id") var id: String = "",
    @SerializedName("storyShortcut") var storyShortcut: StoryShortcut = StoryShortcut(),
    @SerializedName("storyDetail") var storyDetail: StoryDetail = StoryDetail(),
    @SerializedName("seen") var seen: Boolean = false,
    @SerializedName("rank") var rank: Int = 0,
    @SerializedName("action") var action: StoryAction? = StoryAction.NONE,
    @SerializedName("actionText") var actionText: String = "",
    var selected: Boolean = false
) : Parcelable {

    @Parcelize
    data class StoryShortcut(
        @SerializedName("name") var name: String = "",
        @SerializedName("thumbnailUrl") var thumbnailUrl: String = ""
    ) : Parcelable

    @Parcelize
    data class StoryDetail(
        @SerializedName("template") var template: Template = Template.DEFAULT,
        @SerializedName("content") var content: String = "",
        @SerializedName("providerCode") var providerCode: Int? = -1
    ) : Parcelable

    enum class Template {
        DEFAULT
    }

}