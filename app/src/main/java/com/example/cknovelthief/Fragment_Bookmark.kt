package com.example.cknovelthief


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cknovelthief.DB.BookMarkData
import com.example.cknovelthief.DB.BookmarkDBHelper
import com.example.cknovelthief.ViewPagerAdapter.BookmarkRecycleViewAdaper

class Fragment_Bookmark : Fragment() {
    var mBookMarkData = BookMarkData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment__bookmark, container, false)
        queryBookmarkList(view)

        return view
    }
    fun queryBookmarkList(){
        queryBookmarkList(this.view!!)
    }
    fun queryBookmarkList(view :View){
        mBookMarkData.clear()
        var SqlConnect= BookmarkDBHelper.getInstance(this.context!!.applicationContext)
        var msqlBookmarkList= SqlConnect.query("bookmark","")
        var mBookmarkList= mBookMarkData.setBookMarkList(msqlBookmarkList)
        var rv_bookmark = view.findViewById<RecyclerView>(R.id.rv_bookmark)
        rv_bookmark.layoutManager = LinearLayoutManager(this.context)
        val adapter = BookmarkRecycleViewAdaper(this.context!!,mBookmarkList)
        //adapter.setOnItemClickListener(adapter)
        rv_bookmark.adapter = adapter
        rv_bookmark.itemAnimator = DefaultItemAnimator()
        var mDivider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        rv_bookmark.addItemDecoration(mDivider)
    }


}
