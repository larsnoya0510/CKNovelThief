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
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.cknovelthief.DB.BookmarkDBHelper
import com.example.cknovelthief.DataClass.Global
import com.example.cknovelthief.Model.StatedFragment
import kotlinx.android.synthetic.main.fragment__novel.*
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
        var nowScrollWebView =0
    }
    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
        Log.d("watch", "onSaveState Novel")
        outState.putString("nowWebNovel", nowWebNovel)
        nowScrollWebView=wv_Novel.scrollY
        outState.putInt("nowScrollWebView", nowScrollWebView)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        super.onRestoreState(savedInstanceState)
        Log.d("watch", "onRestoreState Novel")

        nowWebNovel= savedInstanceState!!.getString("nowWebNovel")
        loadNovel(nowWebNovel)

        nowScrollWebView= savedInstanceState!!.getInt("nowScrollWebView")
        wv_Novel.scrollY=nowScrollWebView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("watch", "onCreate Novel")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("watch", "onDestroyView Novel")
    }

    override fun onStop() {
        super.onStop()
        Log.d("watch", "onStop Novel")
    }

    override fun onPause() {
        super.onPause()
        Log.d("watch", "onPause Novel")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("watch", "onDestroy Novel")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("watch", "onCreateView Novel")
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment__novel, container, false)
        var mWebView : WebView = view.findViewById(R.id.wv_Novel)
        mWebView.setOnLongClickListener {
            var intentGetDialog = Intent(context, Activity_PageDialog::class.java)
            var bundle = Bundle()
            bundle.putInt("nextPageValue", nextPageValue)
            bundle.putInt("prevPageValue", prevPageValue)
            bundle.putInt("nowPageValue", nowPageValue)
            bundle.putInt("totalPageValue", TotalPageValue)
            bundle.putInt("PageDialogType", Global.TYPE_NOVEL)

            intentGetDialog.putExtras(bundle)
            intentGetDialog.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            //(mContext as Activity).startActivityForResult(intentGetDialog, 1)
            (context as MainActivity).startActivityForResult(intentGetDialog, Global.CALL_PAGE_DIALOG)
            true
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("watch", "onAttach Novel")

    }
    override fun onDetach() {
        super.onDetach()
        Log.d("watch", "onDetach Novel")
    }

    override fun onStart() {
        super.onStart()
        Log.d("watch", "onStart Novel")
        reloadSetting()
    }
    override fun onResume() {
        super.onResume()
        Log.d("watch", "onResume Novel")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("watch", "onActivityCreated Novel")
    }
    override fun onFirstTimeLaunched() {
        Log.d("watch", "onFirstTimeLaunched Novel")
    }

    fun watchNovelStart() {
        Log.d("watch", "watchNovelStart Novel")
        val b = getArguments()
        nowHtml = b?.getString("novelLink")!!
        nowTitle = b?.getString("noveltitle")
        editText_nowHtml.setText(nowHtml)
        handleHtmlWeb(nowHtml)
        //recycleViewBinding(nowNovelSets)
    }

    fun reloadSetting() {
        //runOnUiThread {
        Log.d("watch", "reloadSetting Novel")
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
    }

    fun handleHtmlWeb(m_string: String) {
        Log.d("watch", "handleHtmlWeb Novel")
        Thread {
            Runnable {
                nowNovelSets.clear()
                Log.d("watch", "handleHtmlWeb")
               var  response  =Jsoup.connect(m_string).timeout(60000).maxBodySize(0).followRedirects(false).execute()
                if(response.statusCode()== 200) {
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
//                    Log.d("watch", "check Skip B")
                        val head_content = doclocal.select("head")
                        var body_content = doclocal.select("body")
                        body_content.first().text("")
                        for (i in 0 until select_content.size) {
                            body_content.append(select_content[i].html().replace(" ", "") + "<br><br>")
                            //Log.d("watch", "i :" + i)
                        }
//                    Log.d("watch", "check Skip C")
//                    Log.d("watch", "get nowNovelSets : " + nowNovelSets.size)
                        body_content.add(0, head_content.first())
                        body_content.wrap("<html xmlns=\"http://www.w3.org/1999/xhtml\"></html>")
                        activity!!.runOnUiThread {
                            nowWebNovel = body_content.html()
                            loadNovel(nowWebNovel)
                        }
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
        Log.d("watch", "loadNovel Novel")
        wv_Novel.loadDataWithBaseURL("", mstring, "text/html", "UTF-8", "");
    }

    fun pageJump(m_nowPageHtml: String, nowPage: Int) {
        Log.d("watch", "pageJump Novel")
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
        Log.d("watch", "onActivityResult Novel")
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
}
