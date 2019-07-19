package com.example.cknovelthief.ViewPagerAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cknovelthief.DB.BookMarkData
import com.example.cknovelthief.DB.BookmarkDBHelper
import com.example.cknovelthief.Fragment_Novel
import com.example.cknovelthief.MainActivity
import com.example.cknovelthief.R
import kotlinx.android.synthetic.main.activity_main.*

class BookmarkRecycleViewAdaper(
    private val context: Context,
    private val bookmarkList: MutableList<BookMarkData.BookMarkDataInfo>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = inflater.inflate(R.layout.item_bookmark_recycleview, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = bookmarkList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var vh: ItemHolder = holder as ItemHolder
        //vh.iv_icon.setImageResource(movieList[position].image)
        //Glide.with(this.context).load(bookmarkList[position].image).into(holder.iv_icon)
        vh.tv_title.text = bookmarkList[position].title
        vh.tv_html.text = bookmarkList[position].html
        vh.tv_id.text = bookmarkList[position].id.toString()
//            vh.ll_item.setOnClickListener { v -> itemClickListener?.onItemClick(v, position) }
//            vh.ll_item.setOnLongClickListener { v ->
//                itemLongClickListener?.onItemLongClick(v, position)
//
//                true
//            }
        vh.ll_item.setOnClickListener {
            Log.d("search", "bookmark press")
//            var intentNovelLink = Intent(this.context, Activity_NovelShow::class.java)
//            intentNovelLink.putExtra("novelLink", bookmarkList[position].html)
//            intentNovelLink.putExtra("noveltitle", bookmarkList[position].title)
//            this.context.startActivity(intentNovelLink)
//            (this.context as Activity).finish()
            var fm = (this.context as MainActivity).supportFragmentManager
            var m_bundle = Bundle()
            m_bundle.putString("novelLink", bookmarkList[position].html)
            m_bundle.putString("noveltitle", bookmarkList[position].title)
            for(i in 0 until fm.fragments.size){
                if(fm.fragments[i] is Fragment_Novel){
                    fm.fragments[i].arguments = m_bundle
                    (fm.fragments[i] as Fragment_Novel).watchNovelStart()
                    (this.context as MainActivity).vp_This.setCurrentItem(1)
                }
            }
//            fm.fragments[1].arguments = m_bundle
//            Log.d("watch", "start watchNovelStart")
//            (fm.fragments[1] as Fragment_Novel).watchNovelStart()
//            (this.context as MainActivity).vp_This.setCurrentItem(1)
        }
        vh.btn_del.setOnClickListener {
            //刪除sql
            var SqlConnect= BookmarkDBHelper.getInstance(context.applicationContext)
            SqlConnect.delete("bookmark","id="+bookmarkList[position].id)
            //Log.d("search","sql delete cmd : "+"id="+bookmarkList[position].id)
            //刪除介面
            bookmarkList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }
    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ll_item = view.findViewById<LinearLayout>(R.id.ll_item)
        //var iv_icon = view.findViewById<ImageView>(R.id.iv_icon)
        var tv_title = view.findViewById<TextView>(R.id.tv_title)
        var tv_html = view.findViewById<TextView>(R.id.tv_html)
        var tv_id= view.findViewById<TextView>(R.id.tv_id)
        var btn_del = view.findViewById<Button>(R.id.Btn_Remove)
    }
}