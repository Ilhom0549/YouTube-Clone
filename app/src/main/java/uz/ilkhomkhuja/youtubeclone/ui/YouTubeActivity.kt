package uz.ilkhomkhuja.youtubeclone.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import uz.ilkhomkhuja.youtubeclone.R
import uz.ilkhomkhuja.youtubeclone.adapters.HomeRvAdapter
import uz.ilkhomkhuja.youtubeclone.databinding.ActivityYouTubeBinding
import uz.ilkhomkhuja.youtubeclone.models.Item
import uz.ilkhomkhuja.youtubeclone.utils.Status
import uz.ilkhomkhuja.youtubeclone.viewmodel.MainViewModel

class YouTubeActivity : AppCompatActivity() {

    lateinit var videoId: String
    private lateinit var youTubeMainViewModel: MainViewModel
    private var _binding: ActivityYouTubeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeRvAdapter: HomeRvAdapter
    private var check = false
    private var check2 = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityYouTubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = intent.getStringExtra("video_id")!!
        val channelTitle = intent.getStringExtra("channel_title")!!
        val caption = intent.getStringExtra("caption")!!
        val time = intent.getStringExtra("views")!!
        initViewModel()
        getVideos(channelTitle)

        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

        binding.apply {
            titleName.text = caption
            channelName.text = channelTitle
            views1.text = "10.0k views • $time"

            click1.setOnClickListener {
                check = if (!check) {
                    likeIcon.setBackgroundResource(R.drawable.ic_liked_icon)
                    disLikeIcon.setBackgroundResource(R.drawable.ic_dis_like_icon)
                    true
                } else {
                    likeIcon.setBackgroundResource(R.drawable.ic_like_icon)
                    false
                }
            }
            click2.setOnClickListener {
                check2 = if (!check2) {
                    likeIcon.setBackgroundResource(R.drawable.ic_like_icon)
                    disLikeIcon.setBackgroundResource(R.drawable.ic_dis_liked_icon)
                    true
                } else {
                    disLikeIcon.setBackgroundResource(R.drawable.ic_dis_like_icon)
                    false
                }
            }
            click3.setOnClickListener { shareData("https://www.youtube.com/watch?v=$videoId") }
            click4.setOnClickListener { Snackbar.make(it, "Downloading...", 3000).show() }
            click5.setOnClickListener { Snackbar.make(it, "Saved", 3000).show() }
        }
    }

    private fun getVideos(query: String) {
        youTubeMainViewModel.getVideoByTag(binding.root.context, query).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    homeRvAdapter =
                        HomeRvAdapter(it.data!!.items, object : HomeRvAdapter.OnItemClickListener {
                            override fun onItemClick(item: Item) {
                                val intent =
                                    Intent(binding.root.context, YouTubeActivity::class.java)
                                        .putExtra("video_id", item.id.videoId)
                                        .putExtra("caption", item.snippet.title)
                                        .putExtra("channel_title", item.snippet.channelTitle)
                                        .putExtra("views", item.snippet.publishTime)

                                finish()
                                startActivity(intent)
                            }
                        })
                    binding.rv.adapter = homeRvAdapter
                }
                Status.LOADING -> {}

                Status.ERROR -> {}
            }
        }
    }

    private fun initViewModel() {
        youTubeMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun shareData(string: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, string)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}