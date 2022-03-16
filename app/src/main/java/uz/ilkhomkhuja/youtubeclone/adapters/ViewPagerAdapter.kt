package uz.ilkhomkhuja.youtubeclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.ilkhomkhuja.youtubeclone.ui.MainFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, var list:ArrayList<String>) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return MainFragment(list[position])
    }
}