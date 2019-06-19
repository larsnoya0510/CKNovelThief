package com.example.cknovelthief

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cknovelthief.DB.BookmarkDBHelper
import com.example.cknovelthief.DataClass.Global
import com.example.cknovelthief.DataClass.NovelDataLink
import com.example.cknovelthief.Fragment_NovelList.Companion.nextPageValue
import com.example.cknovelthief.Fragment_NovelList.Companion.nowPageValue
import com.example.cknovelthief.Fragment_NovelList.Companion.prevPageValue
import com.example.cknovelthief.Model.StatedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment__novel.*
import kotlinx.android.synthetic.main.fragment__novel_list.*
import kotlinx.android.synthetic.main.fragment__novel_list.editText_nowHtml
import org.jsoup.Jsoup


class Fragment_Novel : StatedFragment() {
    companion object {
        var nowPageValue: Int = 0
        var prevPageValue: Int = 0
        var nextPageValue: Int = 0
        var TotalPageValue: Int = 0
        var nowHtml: String = ""
        var nowTitle: String = ""
        var nowWebNovel : String =""
        var nowNovelSets = mutableListOf<String>()
        var recylerViewState: Parcelable? = null
    }
    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
        Log.d("watch", "onSaveState")

        outState.putStringArray("NovelListData", nowNovelSets.toTypedArray())
//        recylerViewState = rv_NovelRecycleView.getLayoutManager()!!.onSaveInstanceState();
        outState.putParcelable("recylerViewState", recylerViewState)
        Log.d("watch", "set recylerViewState " + recylerViewState)
        outState.putString("nowWebNovel", nowWebNovel)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        super.onRestoreState(savedInstanceState)
        Log.d("watch", "onRestoreState")
        nowNovelSets = savedInstanceState?.getStringArray("NovelListData")!!.toMutableList()
        recycleViewBinding(nowNovelSets)

        recylerViewState= savedInstanceState?.getParcelable("recylerViewState")
        Log.d("watch", "get recylerViewState " +recylerViewState)
        Log.d("watch", "get layoutManager " +rv_NovelRecycleView.layoutManager)

        nowWebNovel= savedInstanceState?.getString("nowWebNovel")
        loadNovel(nowWebNovel)
//        rv_NovelRecycleView.layoutManager = LinearLayoutManager(this.context)
//        rv_NovelRecycleView.layoutManager!!.onRestoreInstanceState(recylerViewState);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recylerViewState = rv_NovelRecycleView.getLayoutManager()!!.onSaveInstanceState();
    }

    override fun onStop() {
        super.onStop()
        recylerViewState = rv_NovelRecycleView.getLayoutManager()!!.onSaveInstanceState();
    }

    override fun onPause() {
        super.onPause()
        recylerViewState = rv_NovelRecycleView.getLayoutManager()!!.onSaveInstanceState();
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment__novel, container, false)
        var rv_NovelRecycleView = view.findViewById<RecyclerView>(R.id.rv_NovelRecycleView)
        rv_NovelRecycleView.layoutManager = LinearLayoutManager(this.context)
        rv_NovelRecycleView.layoutManager!!.onRestoreInstanceState(recylerViewState);
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onDetach() {
        super.onDetach()
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        rv_NovelRecycleView.layoutManager = LinearLayoutManager(this.context)
        reloadSetting()

        rv_NovelRecycleView.layoutManager = LinearLayoutManager(this.context)
        rv_NovelRecycleView.layoutManager!!.onRestoreInstanceState(recylerViewState);
    }
    override fun onResume() {
        super.onResume()
        rv_NovelRecycleView.layoutManager = LinearLayoutManager(this.context)
        rv_NovelRecycleView.layoutManager!!.onRestoreInstanceState(recylerViewState);
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //rv_NovelRecycleView.setOnScrollChangeListener(Fragment_Novel.this)
    }
    override fun onFirstTimeLaunched() {

    }

    fun watchNovelStart() {
        val b = getArguments()
        //Log.d("watch", "look " + b?.getBundle("novelLink").toString() + " " + b?.getBundle("noveltitle").toString())
        nowHtml = b?.getString("novelLink")!!
        nowTitle = b?.getString("noveltitle")
        editText_nowHtml.setText(nowHtml)
//        Thread {
//            Runnable {
        handleHtmlWeb(nowHtml)
        recycleViewBinding(nowNovelSets)
//            }.run()
//        }.start()
    }

    fun reloadSetting() {
        //runOnUiThread {
        var sharePreferenceProfile_Local =
            this.getActivity()!!.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
        var mfontSize = sharePreferenceProfile_Local.getInt("FontSize", 0)
        var mfontColor_R = sharePreferenceProfile_Local.getInt("FontColor_Red", 0)
        var mfontColor_G = sharePreferenceProfile_Local.getInt("FontColor_Green", 0)
        var mfontColor_B = sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
        var mBackColor_R = sharePreferenceProfile_Local.getInt("BackColor_Red", 0)
        var mBackColor_G = sharePreferenceProfile_Local.getInt("BackColor_Green", 0)
        var mBackColor_B = sharePreferenceProfile_Local.getInt("BackColor_Blue", 0)
        //套用設定
        editText_nowHtml.setTextColor(Color.rgb(mfontColor_R, mfontColor_G, mfontColor_B))
        editText_nowHtml.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
        var mWebSettings=wv_Novel.settings
        mWebSettings.textZoom=150
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setBlockNetworkImage(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setGeolocationEnabled(false);
        mWebSettings.setNeedInitialFocus(false);
//        tv_webContent.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
//        tv_webContent.setTextColor(Color.rgb(mfontColor_R, mfontColor_G, mfontColor_B))
//        tv_webContent.textSize = mfontSize.toFloat()
        //}
    }

    fun handleHtmlWeb(m_string: String) {
        Thread {
            Runnable {
                nowNovelSets.clear()
                Log.d("watch", "handleHtmlWeb")
                val doc = Jsoup.connect(m_string).timeout(60000).maxBodySize(0).get()
                val select_content = doc.select("body").select("td[class=t_f]")
                val selectPage_content = doc.select("body").select("div[class=pg]").first().select("a[href],strong")
                val selectPage_content_content_NowPage =
                    doc.select("body").select("div[class=pg]").first().select("strong")
                val abpath = javaClass.getResourceAsStream("/assets/web/local.html")
                var doclocal = Jsoup.parse(abpath, "UTF-8", "http://example.com/")

                //Runnable {
                Log.d("watch", "check Skip A")
                if (select_content.size > 0) {
                    Log.d("watch", "check Skip B")
                    val head_content = doclocal.select("head")
                    var body_content = doclocal.select("body")
                    body_content.first().text("")
                    for (i in 0 until select_content.size) {
                        body_content.append(select_content[i].html().replace(" ", "") + "<br><br>")
                        //nowNovelSets.add(i, select_content[i].html().replace(" ", "") + "<br><br>")
                        Log.d("watch", "i :" + i)
                    }
                    Log.d("watch", "check Skip C")
                    Log.d("watch", "get nowNovelSets : " + nowNovelSets.size)
                    body_content.add(0, head_content.first())
                    body_content.wrap("<html xmlns=\"http://www.w3.org/1999/xhtml\"></html>")
                    activity!!.runOnUiThread {
                        //                        tv_webContent.text = ""
//                        mScrollView.smoothScrollTo(0, 0)
//                        nowPageNovelContent = body_content.html()

                        nowWebNovel=body_content.html()
                        loadNovel(nowWebNovel)
                        //recycleViewBinding(nowNovelSets)
                    }
//                tv_webContent.text=""
//                mScrollView.smoothScrollTo(0,0)
//                tv_webContent.setText(Html.fromHtml(body_content.html()))
                }
                Log.d("watch", "check Skip D")
                if (selectPage_content.size > 0) {
                    Log.d("watch", "check Skip E")
                    var psize = selectPage_content.size
                    if (selectPage_content[psize - 1].text() != "下一頁") {
                        TotalPageValue = selectPage_content[psize - 1].text().toInt()
                    } else if (selectPage_content[psize - 1].text() == "下一頁" && psize - 2 >= 0) {
                        TotalPageValue = selectPage_content[psize - 2].text().replace(".", "").trim().toInt()
                    } else {
                    }
                }
                if (selectPage_content_content_NowPage.size > 0) {
                    Log.d("watch", "check Skip F")
                    nowPageValue = selectPage_content_content_NowPage[0].text().toInt()

                    if (nowPageValue + 1 <= TotalPageValue) {
                        nextPageValue = nowPageValue + 1
                    } else {
                        nextPageValue = TotalPageValue
                    }
                    if (prevPageValue - 1 >= 0) {
                        prevPageValue = nowPageValue - 1
                    } else {
                        prevPageValue = nowPageValue
                    }
                } else {
                    Log.d("watch", "check Skip G")
                }
            }.run()
        }.start()
    }

    fun loadNovel(mstring: String) {
//        //getActivity()!!.runOnUiThread {
//        Log.d("watch", "check Skip Z")
//            tv_webContent.setText(Html.fromHtml(mstring))
////        tv_webContent.setText(mstring)
//        //}
        wv_Novel.loadDataWithBaseURL("", mstring, "text/html", "UTF-8", "");
    }

    fun pageJump(m_nowPageHtml: String, nowPage: Int) {
        nowPageValue = nowPage
        prevPageValue = nowPage - 1
        nextPageValue = nowPage + 1

        var jumpingPage: String = ""

        //Runnable {
        if (m_nowPageHtml.contains("thread")) {
            var cutStart: Int = m_nowPageHtml.lastIndexOf("-")
            var cutAgain = m_nowPageHtml.substring(0, cutStart)
            cutStart = cutAgain.lastIndexOf("-")
            jumpingPage = cutAgain.substring(0, cutStart + 1) + nowPageValue.toString() + "-1.html"
            nowHtml = jumpingPage
            activity!!.runOnUiThread {
                Log.d("watch", "check Skip H")
                editText_nowHtml.setText(nowHtml)
            }
        } else if (m_nowPageHtml.contains("mod=viewthread")) {
            var cutStart: Int = m_nowPageHtml.lastIndexOf("=")
            jumpingPage = m_nowPageHtml.substring(0, cutStart + 1) + nowPageValue.toString()
            nowHtml = jumpingPage
            activity!!.runOnUiThread {
                Log.d("watch", "check Skip I")
                editText_nowHtml.setText(nowHtml)
            }
        }
//        Thread {
        Log.d("watch", "check Skip J")
        handleHtmlWeb(jumpingPage)
//        }.start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("watch", "B requestCode: " + requestCode + " resultCode: " + resultCode)
        if (requestCode == Global.CALL_PAGE_DIALOG) {
            if (resultCode == Global.PAGE_RESULT_NEXT) {
                Thread {
                    Runnable {
                        pageJump(nowHtml, nextPageValue)
                    }.run()
                }.start()

            } else if (resultCode == Global.PAGE_RESULT_PREV) {
                Thread {
                    Runnable {
                        pageJump(nowHtml, prevPageValue)
                    }.run()
                }.start()
            } else if (resultCode == Global.PAGE_RESULT_TARGET) {
                //Log.d("Info", "now Targetjump")
                if (data != null) {
                    nowPageValue = data.getIntExtra("JumpPage", 0)
                    Thread {
                        Runnable {
                            //Log.d("Info", "start Targetjump" + nowHtml + "value " + nowPageValue)
                            pageJump(nowHtml, nowPageValue)
                        }.run()
                    }.start()
                } else {
                    Toast.makeText(this.context, "JumpError", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Global.PAGE_BOOKMARK) {
                var SqlConnect = BookmarkDBHelper.getInstance(this.context!!.applicationContext)
                var mdatasets = arrayListOf<BookmarkDBHelper.bookmarkInfo>()
                var mdata = BookmarkDBHelper.bookmarkInfo()
                mdata.title = nowTitle
                mdata.html = nowHtml
                mdatasets.add(mdata)
                SqlConnect.insert("bookmark", mdatasets)
                Toast.makeText(this.context, "已加入書籤", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class novelRecycleViewAdaper(
        private val context: Context,
        private val novelList: MutableList<String>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var mContext: Context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View = inflater.inflate(R.layout.novel_recycle_linear, parent, false)
            mContext = parent.getContext()
            return novelViewHolder(view)
        }

        override fun getItemCount(): Int {
            return novelList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var vh: novelViewHolder = holder as novelViewHolder
//            vh.tv_item.text = novelList[position]
            val abpath = javaClass.getResourceAsStream("/assets/web/local.html")
            var doclocal = Jsoup.parse(abpath, "UTF-8", "http://example.com/")

            val head_content = doclocal.select("head")
            var body_content = doclocal.select("body")
            //Log.d("watch","vh.tv_item: "+body_content.html())

            body_content.first().text("")
            //body_content.append((123+position).toString().replace(" ", "") + "<br><br>")
            body_content.append(novelList[position].replace(" ", "") + "<br><br>")
            body_content.add(0, head_content.first())
            //body_content.append(novelList[position].replace(" ", "") + "<br><br>")

            body_content.wrap("<html xmlns=\"http://www.w3.org/1999/xhtml\"></html>")
//            Log.d("watch","novelList size: "+novelList.size)
//            Log.d("watch","novelList[position]"+novelList[position])
            vh.tv_item.setText(Html.fromHtml(body_content.html()))
            //vh.tv_item.setText((123+position).toString())
            vh.tv_item.setOnLongClickListener {
                var intentGetDialog = Intent(this.context, Activity_PageDialog::class.java)
                var bundle = Bundle()
                bundle.putInt("nextPageValue", nextPageValue)
                bundle.putInt("prevPageValue", prevPageValue)
                bundle.putInt("nowPageValue", nowPageValue)
                bundle.putInt("totalPageValue", TotalPageValue)
                bundle.putString("nowPageHtml", nowHtml)
                bundle.putInt("PageDialogType", Global.TYPE_NOVEL)
                intentGetDialog.putExtras(bundle)
                intentGetDialog.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                Log.d("watch", "look requestCode " + Global.CALL_PAGE_DIALOG)
                (context as MainActivity).startActivityForResult(intentGetDialog, Global.CALL_PAGE_DIALOG)
                true
            }
        }

        inner class novelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tv_item = view.findViewById<TextView>(R.id.tv_item)
            var ll_item = view.findViewById<LinearLayout>(R.id.ll_item)

            //呼叫建構子 預先讀取sharePreferenceProfile設定檔
            init {
                loadingSettingValue()
            }

            //讀取sharePreferenceProfile設定檔
            fun loadingSettingValue() {
                var sharePreferenceProfile_Local =
                    context.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
                tv_item.setTextColor(
                    Color.rgb(
                        sharePreferenceProfile_Local.getInt("FontColor_Red", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Green", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
                    )
                )
                tv_item.textSize = sharePreferenceProfile_Local.getInt("FontSize", 0).toFloat()
                ll_item.setBackgroundColor(
                    Color.rgb(
                        sharePreferenceProfile_Local.getInt("BackColor_Red", 0),
                        sharePreferenceProfile_Local.getInt("BackColor_Green", 0),
                        sharePreferenceProfile_Local.getInt("BackColor_Blue", 0)
                    )
                )
            }
        }
    }

    fun recycleViewBinding(nowNovelSets: MutableList<String>) {
        Log.d("watch", "recycleViewBinding")
        getActivity()!!.runOnUiThread {
            Log.d("watch", "nowNovelSets: " + nowNovelSets.size)
            val adapter = Fragment_Novel.novelRecycleViewAdaper(this.context!!, nowNovelSets)
            rv_NovelRecycleView.adapter = adapter
            rv_NovelRecycleView.itemAnimator = DefaultItemAnimator()
            var mDivider = DividerItemDecoration(this.context!!, DividerItemDecoration.VERTICAL)
            rv_NovelRecycleView.addItemDecoration(mDivider)
            editText_nowHtml.setText(Fragment_Novel.nowHtml)
        }
    }

    /** * RecyclerView 移动到当前位置，
     *  * * @param manager 设置RecyclerView对应的manager
     *  * @param mRecyclerView 当前的RecyclerView
     *  * @param n 要跳转的位置
     *  */
    fun MoveToPosition(manager: LinearLayoutManager, mRecyclerView: RecyclerView, n: Int) {
        val firstItem = manager.findFirstVisibleItemPosition()
        val lastItem = manager.findLastVisibleItemPosition()
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n)
        } else if (n <= lastItem) {
            val top = mRecyclerView.getChildAt(n - firstItem).top
            mRecyclerView.scrollBy(0, top)
        } else {
            mRecyclerView.scrollToPosition(n)
        }
    }
}
