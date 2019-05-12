package com.example.myfpm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import  androidx.fragment.app.FragmentManager
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_time_table.*

class TimeTableFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_time_table, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = MyViewPagerAdapter(activity?.supportFragmentManager!!)
//        adapter.addFragment(MondayFragment(), "Monday")
//        adapter.addFragment(TuesdayFragment(), "Tuesday")
//        adapter.addFragment(WednesdayFragment(), "Wednesday")
//        adapter.addFragment(ThursdayFragment(), "Thursday")
//        adapter.addFragment(FridayFragment(), "Friday")
        viewPagerTimeTable.adapter = adapter
        tabs.setupWithViewPager(viewPagerTimeTable)
    }
}

class MyViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(manager){
    private val fragmentList : MutableList<Fragment> = ArrayList()
    private val titleList : MutableList<String> = ArrayList()

    override fun getCount(): Int {
       return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment (fragment: Fragment, title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}
