package com.bigdata.group4demoapp.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bigdata.group4demoapp.model.Story

class StoryDetailsAdapter(
    fragmentManager: FragmentManager,
    private val stories: MutableList<Story>
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val item = stories[position]
        if (item.storyDetail.template == Story.Template.DEFAULT) {
            return createStoryDefaultTemplateFragmentV2Instance(item)
        }
        return createStoryDefaultTemplateFragmentV2Instance(item)
    }

    private fun createStoryDefaultTemplateFragmentV2Instance(story: Story): StoryDefaultTemplateFragment {
        return StoryDefaultTemplateFragment.newInstance(story)
    }

    override fun getCount(): Int {
        return stories.count()
    }

    interface StoryDetailInteraction {
        fun onCloseStories()
        fun onCompleteStory()
        fun onCallToAction(story: Story)
    }

}