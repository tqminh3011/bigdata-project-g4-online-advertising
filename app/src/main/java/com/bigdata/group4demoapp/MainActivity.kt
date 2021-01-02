package com.bigdata.group4demoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigdata.group4demoapp.adapter.StoriesShortcutAdapter
import com.bigdata.group4demoapp.details.StoryDetailsDialogFragment
import com.bigdata.group4demoapp.model.Stories
import com.bigdata.group4demoapp.model.Story
import com.bigdata.group4demoapp.model.StoryAction
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var stories = ArrayList<Story>()

    private val adapter by lazy {
        StoriesShortcutAdapter(this, object : StoriesShortcutAdapter.ItemClickedListener {
            override fun onItemClickedListener(story: Story) {
                showStory(story)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFirebaseId()
        initView()
    }

    private fun checkFirebaseId() {

        // get Firebase ID first then generate header
        val firebaseInstance: FirebaseAnalytics? = getFirebaseInstance(this)
        firebaseInstance?.appInstanceId?.addOnCompleteListener { task: Task<String?> ->
            val firebaseId = if (task.isSuccessful) "Firebase Id: ${task.result}" else "Firebase Id not found"
            tvFirebaseId.text = firebaseId
        }
    }

    private fun getFirebaseInstance(context: Context): FirebaseAnalytics? {
        return try {
            FirebaseAnalytics.getInstance(context)
        } catch (ignore: Exception) {
            Log.e("TQM", "Error: cannot get firebase instance", ignore)
            null
        }
    }

    private fun showStory(story: Story) {
        stories.forEach {
            it.selected = false
        }
        stories.find {
            it.id == story.id
        }?.apply {
            selected = true
            seen = true
        }

        adapter.notifyDataSetChanged()

        val dialog = StoryDetailsDialogFragment.newInstance(ArrayList(stories))
        dialog.onActionCalled = {
            onStoryActionCalled(it)
        }

        dialog.onStoryClosed = {
            onStoryViewed(it)
        }

        dialog.show(
            supportFragmentManager,
            StoryDetailsDialogFragment::class.java.canonicalName
        )
    }

    private fun onStoryViewed(story: Story) {

    }

    private fun onStoryActionCalled(story: Story) {
        story.action?.let {
            when (story.action) {
                StoryAction.POB -> goToProofOfBank()
                StoryAction.BUY -> goToBuyDashboard()
                StoryAction.SEND_MONEY -> goToSendMoney()
                StoryAction.PAY_BILLS -> goToBillPayment()
                StoryAction.BUY_SPOTIFY -> goToBuySpotify()
                StoryAction.BUY_UBER -> goToBuyUber()
                else -> {
                }
            }
        }
    }

    private fun initView() {
        val gson = Gson()
        val stories = gson.fromJson<Stories>(getAssetJsonData(this), Stories::class.java)
        this.stories = stories.storiesList

        adapter.data = stories.storiesList
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvStoriesShortcut.layoutManager = layoutManager
        rvStoriesShortcut.adapter = adapter
    }

    private fun getAssetJsonData(context: Context): String? {
        val json: String?
        json = try {
            val `is`: InputStream = context.assets.open("stories.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun goToProofOfBank() {
        showToast(message = "Go to Proof Of Bank!")
    }

    private fun goToBuyDashboard() {
        showToast(message = "Go to Buy Data!")
    }

    private fun goToSendMoney() {
        showToast(message = "Go to Send Money!")
    }

    private fun goToBillPayment() {
        showToast(message = "Go to Bill Payment!")
    }

    private fun goToBuySpotify() {
        showToast(message = "Go to Buy Spotify!")
    }

    private fun goToBuyUber() {
        showToast(message = "Go to Buy Uber!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}