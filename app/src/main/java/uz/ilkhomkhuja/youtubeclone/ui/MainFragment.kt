package uz.ilkhomkhuja.youtubeclone.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uz.ilkhomkhuja.youtubeclone.adapters.HomeRvAdapter
import uz.ilkhomkhuja.youtubeclone.databinding.FragmentMainBinding
import uz.ilkhomkhuja.youtubeclone.models.Item
import uz.ilkhomkhuja.youtubeclone.utils.Status
import uz.ilkhomkhuja.youtubeclone.viewmodel.MainViewModel


class MainFragment(private var q: String) : Fragment() {

    private lateinit var youTubeMainViewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var homeRvAdapter: HomeRvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initViewModel()
        getVideos()
        return binding.root
    }

    private fun getVideos() {
        youTubeMainViewModel.getVideoByTag(binding.root.context, q).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    homeRvAdapter =
                        HomeRvAdapter(it.data!!.items, object : HomeRvAdapter.OnItemClickListener {
                            override fun onItemClick(item: Item) {
                                val intent =
                                    Intent(binding.root.context, YouTubeActivity::class.java)
                                        .putExtra("video_id", item.id.videoId)
                                        .putExtra("caption", item.snippet.title)
                                        .putExtra("channel_title", item.snippet.channelTitle)
                                        .putExtra("views", item.snippet.publishTime)

                                requireActivity().startActivity(intent)
                            }
                        })
                    binding.rv.adapter = homeRvAdapter

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViewModel() {
        youTubeMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}