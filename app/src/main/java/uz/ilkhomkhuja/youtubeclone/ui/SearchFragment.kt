package uz.ilkhomkhuja.youtubeclone.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import uz.ilkhomkhuja.youtubeclone.R
import uz.ilkhomkhuja.youtubeclone.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        doneButtonClick()
        binding.mic.setOnClickListener { askSpeechInput() }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                val s = results?.get(0) ?: "Hi"
                val bundle = Bundle()
                bundle.putString("str", s)
                findNavController().navigate(R.id.resultFragment, bundle)
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
            TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_NEXT
                ) {
                    val bundle = Bundle()
                    bundle.putString("str", binding.search.text.toString())
                    findNavController().navigate(R.id.resultFragment, bundle)
                    return@OnEditorActionListener true
                }
                false
            })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}