package com.example.cknovelthief.ViewPagerAdapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.example.cknovelthief.Fragment_Bookmark
import com.example.cknovelthief.Fragment_Novel
import com.example.cknovelthief.Fragment_NovelList
import com.example.cknovelthief.Fragment_Setting

class TabLayout_ViewPagerAdapter(

    var mPageTitleList:ArrayList<String>,
    var fm: FragmentManager

) : FragmentPagerAdapter(fm) {
    var mFragmentList=ArrayList<Fragment>()
    init{
        mFragmentList.add(Fragment_NovelList())
        mFragmentList.add(Fragment_Novel())
        mFragmentList.add(Fragment_Bookmark())
        mFragmentList.add(Fragment_Setting())
        init_setArguments()
    }
    override fun getItem(postion: Int): Fragment {
        return mFragmentList.get(postion)
        //return fm.fragments[postion]
    }

    override fun getCount(): Int {
        return mFragmentList.size
        //return fm.fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mPageTitleList.get(position)
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
    }
    fun init_setArguments(){
        for(i in 0 until mFragmentList.size){
            var m_bundle= Bundle()
            m_bundle.putString("hello","hi")
            mFragmentList[i].arguments=m_bundle
        }
    }
}