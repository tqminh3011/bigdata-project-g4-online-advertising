package com.bigdata.group4demoapp.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.bigdata.group4demoapp.R
import com.bigdata.group4demoapp.model.Story
import com.bigdata.group4demoapp.model.StoryAction
import com.bigdata.group4demoapp.model.StoryDefaultModel
import com.bigdata.group4demoapp.tracker.AnalyticConstants
import com.bigdata.group4demoapp.tracker.ClickTracker
import com.bigdata.group4demoapp.tracker.FireBaseAnalyticHelper
import com.bigdata.group4demoapp.tracker.ScreenTracker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.genius.multiprogressbar.MultiProgressBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_story_default_template.*
import java.util.*

class StoryDefaultTemplateFragment : Fragment() {

    private var isScreenVisible = false
    private var loggingStatus: Int = LOG_STATUS_INIT
    private var story = Story()
    private var startTimeInMillis = 0L

    // at this moment only 1 story
    private var noStories = 1

    private lateinit var analytic: FireBaseAnalyticHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            analytic = FireBaseAnalyticHelper(it)
        }
    }

    override fun onStart() {
        super.onStart()
        if (isScreenVisible) {
            onScreenVisible()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isScreenVisible) {
            onScreenNotVisible()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isScreenVisible = isVisibleToUser
        if (isResumed) {
            if (isScreenVisible) {
                loggingStatus = LOG_STATUS_INIT
                onScreenVisible()
            } else {
                onScreenNotVisible()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_default_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            story = bundle.getParcelable(STORY_KEY)!!
            initStory(story)
        }

        btnStoryNegative.setOnClickListener {
            onCloseStories()
        }

        btnAction.setOnClickListener {
            logEventClick(resources.getResourceEntryName(btnAction.id), getStoryAction().name)
            onCallToAction()
        }

        val visibility =
            if (story.action?.equals(StoryAction.NONE) != false) View.GONE else View.VISIBLE
        btnAction.visibility = visibility

        btnAction.text = story.actionText

        context?.let {
            storyContainer.setOnTouchListener(object : OnSwipeTouchListener(it) {
                override fun onSwipeBottom() {
                    onCloseStories()
                }

                override fun onSwipeTop() {
                    onCloseStories()
                }

                override fun onHoldScreen() {
                    pauseStoryProgress()
                }

                override fun onReleaseScreen() {
                    resumeStoryProgress()
                }
            })

            svContent.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    svContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val childPadding =
                        clScrollableContent.paddingTop + clScrollableContent.paddingBottom
                    val isScrollable =
                        svContent.height - clScrollableContent.height + childPadding < 0

                    if (isScrollable) {
                        val padding = resources.getDimensionPixelOffset(R.dimen.margin_8)
                        tvStoryTitle.setPadding(padding, 0, padding, 0)
                        tvStoryDescription.setPadding(padding, 0, padding, 0)
                    }

                    svContent.setOnTouchListener(object : OnSwipeTouchListener(it) {
                        override fun onSwipeBottom() {
                            if (!isScrollable) {
                                onCloseStories()
                            }
                        }

                        override fun onSwipeTop() {
                            if (!isScrollable) {
                                onCloseStories()
                            }
                        }

                        override fun onHoldScreen() {
                            pauseStoryProgress()
                        }

                        override fun onReleaseScreen() {
                            resumeStoryProgress()
                        }
                    })
                }

            })
        }

        ivExitStory.setOnClickListener {
            logEventClick(resources.getResourceEntryName(ivExitStory.id))
            onCloseStories()
        }
    }

    override fun onDestroyView() {
        onScreenNotVisible()
        super.onDestroyView()
    }

    private fun logEventClick(btnName: String, step: String? = null) {
        val screenNameFormatted = "Story: ${story.storyShortcut.name}"
        val tracker = ClickTracker(
            screen = screenNameFormatted,
            screenClass = this::class.java.canonicalName,
            button = btnName,
            step = step
        )
        analytic.logEventClick(tracker)
    }

    private fun logEventDisplay(desc: String, timeOnScreen: Long = 0L) {
        val screenNameFormatted = "Story: ${story.storyShortcut.name}"
        val screenTracker = ScreenTracker(
            screen = screenNameFormatted,
            screenClass = this::class.java.canonicalName,
            desc = desc,
            timeOnScreen = timeOnScreen
        )
        analytic.logEventDisplay(screenTracker)
        Log.d("TQM", "Log event $screenNameFormatted with $desc with time $timeOnScreen")
    }

    private fun initStory(story: Story?) {
        val gson = Gson()
        val storyDetail = gson.fromJson<StoryDefaultModel>(
            story!!.storyDetail.content,
            StoryDefaultModel::class.java
        )

        context?.let {
            Glide.with(it)
                .load(storyDetail.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().centerCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivStoryHeader)
        }

        tvStoryTitle.text = storyDetail.title
        tvStoryDescription.text = storyDetail.description
    }

    private fun startStoriesProgress() {
        storiesProgress.setListener(object : MultiProgressBar.ProgressStepChangeListener {
            override fun onProgressStepChange(newStep: Int) {
                // at this moment, only 1 progress per story
                // so newStep = 1 -> progress completed
                if (newStep == noStories) {
                    onCompleteStory()
                }
            }
        })
        storiesProgress.start()
    }

    private fun onCompleteStory() {
        if (parentFragment is StoryDetailsAdapter.StoryDetailInteraction) {
            (parentFragment as StoryDetailsAdapter.StoryDetailInteraction).onCompleteStory()
        }
    }

    private fun onCloseStories() {
        if (parentFragment is StoryDetailsAdapter.StoryDetailInteraction) {
            (parentFragment as StoryDetailsAdapter.StoryDetailInteraction).onCloseStories()
        }
    }

    private fun onCallToAction() {
        if (parentFragment is StoryDetailsAdapter.StoryDetailInteraction) {
            (parentFragment as StoryDetailsAdapter.StoryDetailInteraction).onCallToAction(story)
        }
    }

    private fun stopStoriesProgress() {
        if (storiesProgress != null) {
            storiesProgress.setListener(null)
            storiesProgress.pause()
            storiesProgress.clear()
        }
    }

    private fun pauseStoryProgress() {
        if (storiesProgress != null) {
            storiesProgress.pause()
        }
    }

    private fun resumeStoryProgress() {
        if (storiesProgress != null) {
            storiesProgress.start()
        }
    }

    private fun onScreenVisible() {
        logEventOpen()
        startStoriesProgress()
    }

    private fun onScreenNotVisible() {
        logEventClose()
        stopStoriesProgress()
    }

    private fun logEventOpen() {
        if (loggingStatus == LOG_STATUS_INIT) {
            startTimeInMillis = Calendar.getInstance().timeInMillis
            logEventDisplay(AnalyticConstants.Marketing.DESC_OPEN_STORY)
            loggingStatus = LOG_STATUS_OPENED
        }
    }

    private fun logEventClose() {
        if (loggingStatus == LOG_STATUS_OPENED) {
            val currentTimeInMillis = Calendar.getInstance().timeInMillis
            val elapsedTime = (currentTimeInMillis - startTimeInMillis) / 1000
            logEventDisplay(
                timeOnScreen = elapsedTime,
                desc = AnalyticConstants.Marketing.DESC_TIME_ON_STORY
            )

            logEventDisplay(AnalyticConstants.Marketing.DESC_CLOSE_STORY)
            loggingStatus = LOG_STATUS_CLOSED
        }
    }

    private fun getStoryAction(): StoryAction {
        return story.action ?: StoryAction.NONE
    }

    companion object {
        fun newInstance(
            story: Story
        ): StoryDefaultTemplateFragment {
            val fragment = StoryDefaultTemplateFragment()
            val bundle = Bundle()
            bundle.putParcelable(STORY_KEY, story)
            fragment.arguments = bundle
            return fragment
        }

        const val STORY_KEY = "STORY_KEY"

        private const val LOG_STATUS_INIT = 0
        private const val LOG_STATUS_OPENED = 1
        private const val LOG_STATUS_CLOSED = 2
    }
}
