package uz.ilkhomkhuja.youtubeclone.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.ilkhomkhuja.youtubeclone.R
import uz.ilkhomkhuja.youtubeclone.adapters.HomeRvAdapter
import uz.ilkhomkhuja.youtubeclone.databinding.FragmentResultBinding
import uz.ilkhomkhuja.youtubeclone.models.Item
import uz.ilkhomkhuja.youtubeclone.utils.Status
import uz.ilkhomkhuja.youtubeclone.viewmodel.MainViewModel

class ResultFragment : Fragment() {

    private var str: String? = null
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var homeRvAdapter: HomeRvAdapter? = null

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        arguments?.let {
            str = it.getString("str")
        }
        initViewModel()
        getVideos(str!!)
        doneButtonClick()
        binding.mic.setOnClickListener { askSpeechInput() }
        binding.search.setText(str)

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, true)
        }
        return binding.root
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun getVideos(q: String) {
        mainViewModel.getVideoByTag(binding.root.context, q).observe(viewLifecycleOwner) {
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
        findNavController().popBackStack(R.id.homeFragment, true)
        _binding = null
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                val s = results?.get(0) ?: "Hi"
                val bundle = Bundle()
                bundle.putString("str", s)
                findNavController().navigate(R.id.resultFragment, bundle)
                getVideos(s)
                s
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun askSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
        startActivityForResult(intent, 1)
    }

    private fun doneButtonClick() {
        binding.search.setOnEditorActionListener(
            TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_NEXT
                ) {
                    getVideos(binding.search.text.toString())
                    closeKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })
    }

    private fun closeKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}