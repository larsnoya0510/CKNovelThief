package com.example.cknovelthief

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.example.cknovelthief.ViewPagerAdapter.TabLayout_ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.data



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var m_FragmemtManager =supportFragmentManager
        var  mPageTitleList=ArrayList<String>()
        mPageTitleList.add(getResources().getString(R.string.novellist))
        mPageTitleList.add(getResources().getString(R.string.novel))
        mPageTitleList.add(getResources().getString(R.string.bookmark))
        mPageTitleList.add(getResources().getString(R.string.setting))

        var mViewPagerAdapter= TabLayout_ViewPagerAdapter(mPageTitleList,m_FragmemtManager)

        vp_This.adapter=mViewPagerAdapter
        vp_This.offscreenPageLimit=3
        tl_This.setupWithViewPager(vp_This)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}
