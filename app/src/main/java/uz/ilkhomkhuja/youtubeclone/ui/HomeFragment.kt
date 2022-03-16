package uz.ilkhomkhuja.youtubeclone.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import uz.ilkhomkhuja.youtubeclone.R
import uz.ilkhomkhuja.youtubeclone.adapters.ViewPagerAdapter
import uz.ilkhomkhuja.youtubeclone.databinding.FragmentHomeBinding
import uz.ilkhomkhuja.youtubeclone.databinding.ItemTabBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        loadCategory()
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, categoryList)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setTabs()
        binding.search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadCategory() {
        categoryList = ArrayList()
        categoryList.add("All")
        categoryList.add("VideoGame")
        categoryList.add("Live")
        categoryList.add("Game")
        categoryList.add("Graphic")
        categoryList.add("Future")
        categoryList.add("Travel")
    }

    private fun setTabs() {
        for (i in 0 until binding.tabLayout.tabCount) {
            val itemTabBinding: ItemTabBinding =
                ItemTabBinding.inflate(LayoutInflater.from(binding.root.context))
            itemTabBinding.itemTv.text = categoryList[i]
            binding.tabLayout.getTabAt(i)?.customView = itemTabBinding.root
            if (i == 0) {
                itemTabBinding.itemTv.setTextColor(Color.WHITE)
                itemTabBinding.back.setBackgroundResource(R.drawable.design2)
            } else {
                itemTabBinding.itemTv.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black_dark
                    )
                )
                itemTabBinding.back.setBackgroundResource(R.drawable.design1)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                try {
                    val itemTabBinding = ItemTabBinding.bind(customView!!)
                    itemTabBinding.itemTv.setTextColor(Color.WHITE)
                    itemTabBinding.back.setBackgroundResource(R.drawable.design2)
                } catch (e: Exception) {
                     e.printStackTrace()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                val itemTabBinding = ItemTabBinding.bind(customView!!)
                itemTabBinding.itemTv.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black_dark
                    )
                )
                itemTabBinding.back.setBackgroundResource(R.drawable.design1)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}