package com.example.cknovelthief

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.example.cknovelthief.ViewPagerAdapter.TabLayout_ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.data
import android.util.Log
import android.R.string
import android.widget.Toast
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AAAA","onCreate")
        setContentView(R.layout.activity_main)

        netWorkTest()
//        var mViewPagerAdapter= TabLayout_ViewPagerAdapter(mPageTitleList,m_FragmemtManager)
//
//        vp_This.adapter=mViewPagerAdapter
//        //vp_This.offscreenPageLimit=3
//        tl_This.setupWithViewPager(vp_This)
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
        val call = client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // 連線成功，自response取得連線結果
                runOnUiThread {
                    var m_FragmemtManager =supportFragmentManager
                    var  mPageTitleList=ArrayList<String>()
                    mPageTitleList.add(getResources().getString(R.string.novellist))
                    mPageTitleList.add(getResources().getString(R.string.novel))
                    mPageTitleList.add(getResources().getString(R.string.bookmark))
                    mPageTitleList.add(getResources().getString(R.string.setting))
                    var mViewPagerAdapter= TabLayout_ViewPagerAdapter(mPageTitleList,m_FragmemtManager)

                    vp_This.adapter=mViewPagerAdapter
                    //vp_This.offscreenPageLimit=3
                    tl_This.setupWithViewPager(vp_This)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 連線失敗
                runOnUiThread {
                    Toast.makeText(this@MainActivity,"網路異常 請確認網路環境", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
