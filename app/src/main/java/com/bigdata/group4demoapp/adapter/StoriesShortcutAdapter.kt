package com.bigdata.group4demoapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bigdata.group4demoapp.R
import com.bigdata.group4demoapp.model.Story
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.stories_adapter_item.view.*

class StoriesShortcutAdapter(
    private val context: Context,
    private val itemClickListener: ItemClickedListener?
) :
    BaseAdapter<Story, StoriesShortcutAdapter.StoryHolder>(context) {

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        return StoryHolder(
            mLayoutInflater.inflate(R.layout.stories_adapter_item, parent, false)
        )
    }

    inner class StoryHolder(val view: View) : BaseHolder(view) {

        fun bind(item: Story) {
            view.tvStoryShortCutName.text = item.storyShortcut.name

            val bgRes = if (mData[layoutPosition].seen) R.drawable.bg_story_default_seen else R.drawable.bg_story_default
            view.ivStoryShortcut.setBackgroundResource(bgRes)

            Glide.with(context)
                .load(item.storyShortcut.thumbnailUrl)
                .thumbnail(Glide.with(context).load(R.drawable.skeleton_round_dark))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().circleCrop())
                .into(view.ivStoryShortcut)

            itemClickListener?.let {
                view.clStoryShortcutContainer.setOnClickListener {
                    itemClickListener.onItemClickedListener(mData[layoutPosition])
                }
            }
        }
    }

    interface ItemClickedListener {
        fun onItemClickedListener(info: Story)
    }
}