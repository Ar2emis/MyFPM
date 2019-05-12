package com.example.myfpm


import android.app.FragmentManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_monday.*

class MondayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        monday_fragment_name.text = "Monday"
    }

}
