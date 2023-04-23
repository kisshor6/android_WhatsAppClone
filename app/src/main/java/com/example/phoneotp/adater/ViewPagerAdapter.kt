package com.example.phoneotp.adater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.phoneotp.calls
import com.example.phoneotp.chats
import com.example.phoneotp.status

class ViewPagerAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    private val tabTitles = listOf("Chats", "Status", "Calls")

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> chats()
            1-> status()
            else -> calls()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}