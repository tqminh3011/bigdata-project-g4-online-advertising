package com.bigdata.group4demoapp.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.bigdata.group4demoapp.R
import com.bigdata.group4demoapp.model.Story
import com.bigdata.group4demoapp.tracker.AnalyticConstants
import com.bigdata.group4demoapp.tracker.FireBaseAnalyticHelper
import com.bigdata.group4demoapp.utils.CubeOutAnimation
import kotlinx.android.synthetic.main.fragment_stories.*
import com.bigdata.group4demoapp.tracker.ScreenTracker

class StoryDetailsDialogFragment : DialogFragment(), StoryDetailsAdapter.StoryDetailInteraction {

    var onActionCalled: ((Story) -> Unit)? = null
    var onStoryClosed: ((Story) -> Unit)? = null

    private var stories: MutableList<Story> = mutableListOf()

    private lateinit var analytic: FireBaseAnalyticHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
        val bundle = arguments
        if (bundle != null) {
            stories = bundle.getParcelableArrayList(KEY_STORIES)!!
        }
        context?.let {
            analytic = FireBaseAnalyticHelper(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stories, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(R.style.Dialog_InUp_OutDown_Animation)
        initStories(stories)
    }

    override fun onDestroy() {
        logEventDisplay(AnalyticConstants.Marketing.DESC_CLOSE_ALL_STORIES)
        super.onDestroy()
    }

    private fun logEventDisplay(desc: String) {
        val screenNameFormatted = "Story Container Screen"
        val screenTracker = ScreenTracker(
            screen = screenNameFormatted,
            screenClass = this::class.java.canonicalName,
            desc = desc
        )
        analytic.logEventDisplay(screenTracker)
        Log.d("TQM", "Log event $screenNameFormatted with $desc")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initStories(stories: MutableList<Story>) {

        val adapter = StoryDetailsAdapter(childFragmentManager, stories)

        vpStories.setPageTransformer(true, CubeOutAnimation())
        vpStories.adapter = adapter

        vpStories.currentItem = stories.indexOfFirst {
            it.selected
        }

        vpStories.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val story = stories[position]
                if (parentFragment is StoryManagementListener) {
                    (parentFragment as StoryManagementListener).onStoryViewed(story)
                }
            }

        })
    }

    private fun onStoryCompleted() {
        val currentStoryIndex = vpStories.currentItem
        val nextStoryIndex = currentStoryIndex + 1
        if (nextStoryIndex == vpStories.adapter!!.count) {
            // reach to last story -> close all story
            dismiss()
        } else {
            vpStories.currentItem = nextStoryIndex
        }
    }

    override fun onCloseStories() {
        onStoryClosed?.invoke(stories[vpStories.currentItem])
        dismiss()
    }

    override fun onCompleteStory() {
        onStoryCompleted()
    }

    override fun onCallToAction(story: Story) {
        onStoryClosed?.invoke(stories[vpStories.currentItem])
        onActionCalled?.invoke(story)
        dismiss()
    }

    interface StoryManagementListener {
        fun onStoryViewed(story: Story)
    }

    companion object {
        fun newInstance(
            stories: ArrayList<Story>
        ): StoryDetailsDialogFragment {
            val fragment =
                StoryDetailsDialogFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(KEY_STORIES, stories)
            fragment.arguments = bundle
            return fragment
        }

        const val KEY_STORIES = "KEY_STORIES"
    }
}
