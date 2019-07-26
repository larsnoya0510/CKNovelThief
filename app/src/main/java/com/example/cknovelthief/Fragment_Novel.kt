package com.example.cknovelthief

import android.annotation.SuppressLint
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
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import com.example.cknovelthief.DB.BookmarkDBHelper
import com.example.cknovelthief.DataClass.Global
import com.example.cknovelthief.Model.StatedFragment
import kotlinx.android.synthetic.main.fragment__novel.*
import kotlinx.android.synthetic.main.fragment__novel_list.editText_nowHtml
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.w3c.dom.Element
import java.net.URL

class Fragment_Novel : StatedFragment() {
    private lateinit var loadingView: View
    private lateinit var ComeBackView :View
    companion object {
        var nowPageValue: Int = 0
        var prevPageValue: Int = 0
        var nextPageValue: Int = 0
        var TotalPageValue: Int = 0
        var nowHtml: String = ""
        var nowTitle: String = ""
        var nowWebNovel : String =""
        var nowNovelSets = mutableListOf<String>()
        var nowScrollWebView =0
        var loadingFlag=true
    }
    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
//        Log.d("CheckState", "onSaveState Novel")
        outState.putString("nowWebNovel", nowWebNovel)
        nowScrollWebView=mScrollView.scrollY
        outState.putInt("nowScrollWebView", nowScrollWebView)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        super.onRestoreState(savedInstanceState)
        Log.d("CheckState", "onRestoreState Novel")

        nowWebNovel= savedInstanceState!!.getString("nowWebNovel")
        if(nowWebNovel!="") loadNovel(nowWebNovel)

        nowScrollWebView= savedInstanceState!!.getInt("nowScrollWebView")
        //wv_Novel.scrollY=nowScrollWebView
        //mScrollView.scrollTo(0,nowScrollWebView)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("CheckState", "onAttach Novel")
    }
    override fun onDetach() {
        super.onDetach()
        Log.d("CheckState", "onDetach Novel")
    }
    override fun onStart() {
        super.onStart()
        Log.d("CheckState", "onStart Novel")

    }
    override fun onResume() {
        super.onResume()
        Log.d("CheckState", "onResume Novel")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("CheckState", "onActivityCreated Novel")
    }
    override fun onFirstTimeLaunched() {
        Log.d("CheckState", "onFirstTimeLaunched Novel")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CheckState", "onCreate Novel")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CheckState", "onDestroyView Novel")
    }
    override fun onStop() {
        super.onStop()
        Log.d("CheckState", "onStop Novel")
    }
    override fun onPause() {
        super.onPause()
        Log.d("CheckState", "onPause Novel")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("CheckState", "onDestroy Novel")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        Log.d("CheckState", "onCreateView Novel")
        var view: View = inflater.inflate(R.layout.fragment__novel, container, false)
        ComeBackView = view
        loadingView = LayoutInflater.from(this.context).inflate(R.layout.loading_layout, view as ViewGroup, false)
        var tv_Novel :TextView = view.findViewById(R.id.tv_Novel)
        tv_Novel.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(loadingFlag==false && tv_Novel.text!="") removeLoadingView()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        tv_Novel.setOnLongClickListener {
            var intentGetDialog = Intent(context, Activity_PageDialog::class.java)
            var bundle = Bundle()
            bundle.putInt("nextPageValue", nextPageValue)
            bundle.putInt("prevPageValue", prevPageValue)
            bundle.putInt("nowPageValue", nowPageValue)
            bundle.putInt("totalPageValue", TotalPageValue)
            bundle.putInt("PageDialogType", Global.TYPE_NOVEL)

            intentGetDialog.putExtras(bundle)
            intentGetDialog.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            (context as MainActivity).startActivityForResult(intentGetDialog, Global.CALL_PAGE_DIALOG)
            true
        }
        reloadSetting(view)
        return view
    }

    fun watchNovelStart() {
        addLoadingView()
//        Log.d("CheckState", "watchNovelStart Novel")
        val getbundle = getArguments()
        nowHtml = getbundle?.getString("novelLink")!!
        nowTitle = getbundle?.getString("noveltitle")
        editText_nowHtml.setText(nowHtml)
        handleHtmlWeb(nowHtml)
    }

    fun reloadSetting(view :View) {
        //runOnUiThread {
        Log.d("CheckState", "reloadSetting Novel")
        var sharePreferenceProfile_Local =
            this.getActivity()!!.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
        var mfontSize = sharePreferenceProfile_Local.getInt("NovelFontSize", 0)
        var mfontColor_R = sharePreferenceProfile_Local.getInt("FontColor_Red", 0)
        var mfontColor_G = sharePreferenceProfile_Local.getInt("FontColor_Green", 0)
        var mfontColor_B = sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
        var mBackColor_R = sharePreferenceProfile_Local.getInt("BackColor_Red", 0)
        var mBackColor_G = sharePreferenceProfile_Local.getInt("BackColor_Green", 0)
        var mBackColor_B = sharePreferenceProfile_Local.getInt("BackColor_Blue", 0)
        //套用設定
        //==
        var tv_Novel=view.findViewById<TextView>(R.id.tv_Novel)
        tv_Novel.textSize=mfontSize.toFloat()
        tv_Novel.setTextColor(Color.rgb(mfontColor_R, mfontColor_G, mfontColor_B))
        tv_Novel.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
        //==
        var editText_nowHtml=view.findViewById<EditText>(R.id.editText_nowHtml)
        editText_nowHtml.setTextColor(Color.rgb(mfontColor_R, mfontColor_G, mfontColor_B))
        editText_nowHtml.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
    }

    fun handleHtmlWeb(m_string: String) {
//        Log.d("CheckState", "handleHtmlWeb Novel")
        Thread {
            Runnable {
                nowNovelSets.clear()
//                Log.d("CheckState", "handleHtmlWeb")
                var url: URL = URL(m_string)
                var response = Jsoup.connect(url.toString()).followRedirects(true).execute()
                if(response.statusCode()== 200) {
                    val doc = Jsoup.connect(url.toString()).timeout(60000).maxBodySize(0).get()
                    val select_content = doc.select("body").select("td[class=t_f]")
                    val selectPage_content = doc.select("body").select("div[class=pg]").first().select("a[href],strong")
                    val selectPage_content_content_NowPage =
                        doc.select("body").select("div[class=pg]").first().select("strong")
                    val abpath = javaClass.getResourceAsStream("/assets/web/local.html")
                    var doclocal = Jsoup.parse(abpath, "UTF-8", "http://example.com/")
                    //Runnable {
                    Log.d("CheckState", "check Skip A")
                    if (select_content.size > 0) {
                        val head_content = doclocal.select("head")
                        var body_content = doclocal.select("body")
                        body_content.first().text("")
                        for (i in 0 until select_content.size) {
                            body_content.append(select_content[i].html().replace(" ", "") + "<br><br>")
                        }
                        body_content.add(0, head_content.first())
                        body_content.wrap("<html xmlns=\"http://www.w3.org/1999/xhtml\"></html>")
                        activity!!.runOnUiThread {
                            nowWebNovel = body_content.toString()
                            loadNovel(nowWebNovel)
                        }
                    }
                    if (selectPage_content.size > 0) {
                        var psize = selectPage_content.size
                        if (selectPage_content[psize - 1].text() != "下一頁") {
                            TotalPageValue = selectPage_content[psize - 1].text().toInt()
                        } else if (selectPage_content[psize - 1].text() == "下一頁" && psize - 2 >= 0) {
                            TotalPageValue = selectPage_content[psize - 2].text().replace(".", "").trim().toInt()
                        } else {
                        }
                    }
                    if (selectPage_content_content_NowPage.size > 0) {
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
                    }
                }
                else{
                    activity!!.runOnUiThread {
                        Toast.makeText(this.context,"網路異常 請確認網路環境",Toast.LENGTH_SHORT).show()
                    }
                }
            }.run()
        }.start()
    }

    fun loadNovel(mstring: String) {
//        Log.d("CheckState", "loadNovel Novel")
        loadingFlag=false
        tv_Novel.text=""
        addLoadingView()
        mScrollView.smoothScrollTo(0,0)
        tv_Novel.setText(Html.fromHtml(mstring))

    }

    fun pageJump(m_nowPageHtml: String, nowPage: Int) {
//        Log.d("CheckState", "pageJump Novel")
        nowPageValue = nowPage
        prevPageValue = nowPage - 1
        nextPageValue = nowPage + 1
        var jumpingPage: String = ""

        if (m_nowPageHtml.contains("thread")) {
            var cutStart: Int = m_nowPageHtml.lastIndexOf("-")
            var cutAgain = m_nowPageHtml.substring(0, cutStart)
            cutStart = cutAgain.lastIndexOf("-")
            jumpingPage = cutAgain.substring(0, cutStart + 1) + nowPageValue.toString() + "-1.html"
            nowHtml = jumpingPage
            activity!!.runOnUiThread {
                addLoadingView()
//                Log.d("CheckState", "check Skip H")
                editText_nowHtml.setText(nowHtml)
            }
        } else if (m_nowPageHtml.contains("mod=viewthread")) {
            var cutStart: Int = m_nowPageHtml.lastIndexOf("=")
            jumpingPage = m_nowPageHtml.substring(0, cutStart + 1) + nowPageValue.toString()
            nowHtml = jumpingPage
            activity!!.runOnUiThread {
                addLoadingView()
                editText_nowHtml.setText(nowHtml)
            }
        }
        handleHtmlWeb(jumpingPage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("CheckState", "onActivityResult Novel")
//        Log.d("CheckState", "B requestCode: " + requestCode + " resultCode: " + resultCode)
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
                if (data != null) {
                    nowPageValue = data.getIntExtra("JumpPage", 0)
                    Thread {
                        Runnable {
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
    private fun addLoadingView() {
        var iv_Loading = view?.findViewById<ImageView>(R.id.iv_Loading)
        iv_Loading?.visibility = View.VISIBLE

    }
    private fun removeLoadingView(){
        var iv_Loading = view?.findViewById<ImageView>(R.id.iv_Loading)
        iv_Loading?.visibility = View.GONE
        loadingFlag=false
    }
}
