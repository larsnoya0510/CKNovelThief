package com.example.cknovelthief

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.example.cknovelthief.ViewPagerAdapter.TabLayout_ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var loadingView: View
    private var networkStateTest = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AAAA", "onCreate")
        setContentView(R.layout.activity_main)
        initUniTest()//功能測試
        initView()
        if (!networkStateTest) {
            netWorkTest()
        }
    }
    private fun initUniTest() {
        networkStateTest = false //測試網路
    }

    private fun addLoadingView() {
        (window.decorView as ViewGroup).addView(loadingView)
        var retryBtn = (window.decorView as ViewGroup).findViewById<Button>(R.id.btn_retry)
        retryBtn.setOnClickListener {
            networkStateTest = false
            netWorkTest()
        }
    }
    private fun removeLoadingView(){
        (window.decorView as ViewGroup).removeView(loadingView)
    }
    private fun initView() {
        loadingView = LayoutInflater.from(this).inflate(R.layout.loading_layout, window.decorView as ViewGroup, false)
        addLoadingView()
        var m_FragmemtManager = supportFragmentManager
        var mPageTitleList = ArrayList<String>()
        mPageTitleList.add(getResources().getString(R.string.novellist))
        mPageTitleList.add(getResources().getString(R.string.novel))
        mPageTitleList.add(getResources().getString(R.string.bookmark))
        mPageTitleList.add(getResources().getString(R.string.setting))
        var mViewPagerAdapter = TabLayout_ViewPagerAdapter(mPageTitleList, m_FragmemtManager)
        vp_This.adapter = mViewPagerAdapter
        //vp_This.offscreenPageLimit=3
        tl_This.setupWithViewPager(vp_This)
        vp_This.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                if(p0==2){
                    var fm = supportFragmentManager
                    for(i in 0 until fm.fragments.size){
                        if(fm.fragments[i] is Fragment_Bookmark){
                            (fm.fragments[i] as Fragment_Bookmark).queryBookmarkList()
                        }
                    }
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun netWorkTest() {
        //var Result : Boolean
        val client = OkHttpClient().newBuilder().build()
        val request = Request.Builder()
            .url("https://ck101.com/forum-237-1.html")
            .build()

        // 建立Call
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // 連線成功，自response取得連線結果
                runOnUiThread {
                    removeLoadingView()
                    var m_FragmemtManager = supportFragmentManager
                    for( i in 0 until m_FragmemtManager.fragments.size) {
                        if(m_FragmemtManager.fragments[i] is Fragment_NovelList){
                            (m_FragmemtManager.fragments[i] as Fragment_NovelList).startGetNovelList()
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 連線失敗
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "網路異常 請確認網路環境", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
