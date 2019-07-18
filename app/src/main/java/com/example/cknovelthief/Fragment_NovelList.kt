package com.example.cknovelthief


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.cknovelthief.DataClass.Global
import com.example.cknovelthief.DataClass.NovelDataLink
import com.example.cknovelthief.DataClass.NovelsDataSet
import com.example.cknovelthief.Model.StatedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment__novel_list.*
import org.jsoup.Jsoup
import java.net.URL

class Fragment_NovelList : StatedFragment() {
    var mNovelsData = NovelsDataSet()
    lateinit var editText_nowHtml: EditText
    lateinit var llayout_main: LinearLayout
    lateinit var rv_novelListRecycleView: RecyclerView
    var textSize=0F

    companion object {
        val homePageHtml = "https://ck101.com/forum-237-1.html"
        var nowPageValue: Int = 0
        var prevPageValue: Int = 0
        var nextPageValue: Int = 0
        var totalPageValue: Int = 0
        var nowPageHtml: String = ""
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
        Log.d("watch", "onSaveState Novelist")
        // For example:
        //outState.putString(text, tvSample.getText().toString());
        Log.d("watch", " onSaveState NovelListData start")
        outState.putSerializable("NovelListData", mNovelsData)
        Log.d("watch", " onSaveState NovelListData end")
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        super.onRestoreState(savedInstanceState)
        Log.d("watch", "onRestoreState Novelist")
        var mNovelDataLink = NovelDataLink()
        mNovelsData = savedInstanceState?.getSerializable("NovelListData") as NovelsDataSet
        recycleViewBinding(mNovelDataLink.getList(mNovelsData))
    }

    override fun onFirstTimeLaunched() {
        Log.d("watch", "onFirstTimeLaunched Novelist")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nowPageHtml = homePageHtml
        Log.d("watch", "onCreate Novelist")
    }

    override fun onStart() {
        super.onStart()
        Log.d("watch", "onStart Novelist")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("watch", "onCreateView Novelist")
        //return inflater.inflate(R.layout.fragment__novel_list, container, false)
        var view: View = inflater.inflate(R.layout.fragment__novel_list, container, false)
        initView(view)
        reloadSetting(view)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("watch", "onAttach Novelist")
    }

    fun reloadSetting(view: View) {
        Log.d("watch", "reloadSetting Novelist")
        getActivity()!!.runOnUiThread {
            var sharePreferenceProfile_Local =
                this.getActivity()!!.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
            var mfontSize = sharePreferenceProfile_Local.getInt("ListFontSize", 0)
            var mfontColor_R = sharePreferenceProfile_Local.getInt("FontColor_Red", 0)
            var mfontColor_G = sharePreferenceProfile_Local.getInt("FontColor_Green", 0)
            var mfontColor_B = sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
            var mBackColor_R = sharePreferenceProfile_Local.getInt("BackColor_Red", 0)
            var mBackColor_G = sharePreferenceProfile_Local.getInt("BackColor_Green", 0)
            var mBackColor_B = sharePreferenceProfile_Local.getInt("BackColor_Blue", 0)
            //套用設定
            editText_nowHtml.setTextColor(Color.rgb(mfontColor_R, mfontColor_G, mfontColor_B))
            editText_nowHtml.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
            llayout_main.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
            rv_novelListRecycleView.setBackgroundColor(Color.rgb(mBackColor_R, mBackColor_G, mBackColor_B))
            textSize=mfontSize.toFloat()
        }
    }

    fun initView(view: View) {
        editText_nowHtml = view.findViewById<EditText>(R.id.editText_nowHtml)
        llayout_main = view.findViewById<LinearLayout>(R.id.llayout_main)
        rv_novelListRecycleView = view.findViewById<RecyclerView>(R.id.rv_novelListRecycleView)
        rv_novelListRecycleView.layoutManager = LinearLayoutManager(this.context)
    }

    fun GetHtmlData(urlString: String, callback: () -> Unit) {
        Log.d("watch", "GetHtmlData Novelist")
        nowPageHtml = urlString
        mNovelsData.AllClear()
        var url: URL = URL(urlString)
        //jsoup連線方式改為不限制buffer容量以及加長timeout時間避免資料被截斷
        var response = Jsoup.connect(url.toString()).followRedirects(false).execute()
        Log.d("watch", "GetHtmlData Novelist response " + response)
        if (response.statusCode() == 200) {
            val doc = Jsoup.connect(url.toString()).timeout(60000).maxBodySize(0).get()
            val novelList = doc.select("tbody[id^=normalthread]")
            val selectPage_content = doc.select("body").select("div[class=pg]").first().select("a[href],strong")
            val selectPage_content_content_NowPage = doc.select("body").select("div[class=pg]").first().select("strong")
            if (novelList.size > 0) {
                for (i in 0..novelList.size - 1) {
                    var novelPic = novelList[i].select("img[src]").attr("src")
                    var novelhtml = novelList[i].select("a").attr("href")
                    var novelname = novelList[i].select("a").attr("title")
                    mNovelsData.iconArray.add(novelPic)
                    mNovelsData.linkArray.add(novelhtml)
                    mNovelsData.nameArray.add(novelname)
                }
            }
            //取得全部頁數
            if (selectPage_content.size > 0) {
                var size = selectPage_content.size
                if (selectPage_content[size - 1].text() != "下一頁") {
                    totalPageValue = selectPage_content[size - 1].text().toInt()
                } else if (selectPage_content[size - 1].text() == "下一頁" && size - 2 >= 0) {
                    totalPageValue = selectPage_content[size - 2].text().replace(".", "").trim().toInt()
                } else {
                }
            }
            //取得目前頁數以及頁數參數設定
            if (selectPage_content_content_NowPage.size > 0) {
                nowPageValue = selectPage_content_content_NowPage[0].text().toInt()
                if (nowPageValue + 1 <= totalPageValue) {
                    nextPageValue = nowPageValue + 1
                } else {
                    nextPageValue = totalPageValue
                }
                if (prevPageValue - 1 >= 0) {
                    prevPageValue = nowPageValue - 1
                } else {
                    prevPageValue = nowPageValue
                }
            } else {
            }
        } else {
            activity!!.runOnUiThread {
                Toast.makeText(this.context, "網路異常 請確認網路環境", Toast.LENGTH_SHORT).show()
            }
        }

        callback.invoke()
    }

    fun recycleViewBinding(mNovelDataLinkList: MutableList<NovelDataLink>) {
        Log.d("watch", "recycleViewBinding Novelist")
        getActivity()!!.runOnUiThread {
            val adapter = novelRecycleViewAdaper(this.context!!, mNovelDataLinkList, textSize)
            rv_novelListRecycleView.adapter = adapter
            rv_novelListRecycleView.itemAnimator = DefaultItemAnimator()
            var mDivider = DividerItemDecoration(this.context!!, DividerItemDecoration.VERTICAL)
            rv_novelListRecycleView.addItemDecoration(mDivider)
            editText_nowHtml.setText(nowPageHtml)
        }
    }

    class novelRecycleViewAdaper(
        private val context: Context,
        private val novelList: MutableList<NovelDataLink>,
        private val textSize: Float
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var mContext: Context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View = inflater.inflate(R.layout.novellist_recycle_linear, parent, false)
            mContext = parent.getContext()
            return novelViewHolder(view)
        }

        override fun getItemCount(): Int {
            return novelList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var vh: novelViewHolder = holder as novelViewHolder
            //判斷沒有圖片時使用預設圖片代替
            if (novelList[position].icon == "static/image/common/nothumb.jpg") {
                Glide.with(this.context).load(R.drawable.noimage).into(holder.iv_icon)
            } else {
                Glide.with(this.context).load(novelList[position].icon).into(holder.iv_icon)
            }
            vh.tv_name.text = novelList[position].name
            vh.tv_name.textSize=textSize
            vh.tv_desc.text = novelList[position].link
            vh.ll_item.setOnClickListener {
                //開啟小說頁面
//                var intentNovelLink = Intent(this.context, Activity_NovelShow::class.java)
//                intentNovelLink.putExtra("novelLink", novelList[position].link)
//                intentNovelLink.putExtra("noveltitle", novelList[position].name)
//                this.context.startActivity(intentNovelLink)
                Log.d("watch", "start Jump to FragmentNovel")
                var fm = (this.context as MainActivity).supportFragmentManager
                var m_bundle = Bundle()
                m_bundle.putString("novelLink", novelList[position].link)
                m_bundle.putString("noveltitle", novelList[position].name)
                fm.fragments[1].arguments = m_bundle
                Log.d("watch", "start watchNovelStart")
                (fm.fragments[1] as Fragment_Novel).watchNovelStart()
                (this.context as MainActivity).vp_This.setCurrentItem(1)
            }
            vh.ll_item.setOnLongClickListener {
                //呼叫換頁視窗
                var intentGetDialog = Intent(context, Activity_PageDialog::class.java)
                var bundle = Bundle()
                bundle.putInt("nextPageValue", nextPageValue)
                bundle.putInt("prevPageValue", prevPageValue)
                bundle.putInt("nowPageValue", nowPageValue)
                bundle.putInt("totalPageValue", totalPageValue)
                bundle.putInt("PageDialogType", Global.TYPE_NOVELLIST)

                intentGetDialog.putExtras(bundle)
                intentGetDialog.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                //(mContext as Activity).startActivityForResult(intentGetDialog, 1)
                (mContext as MainActivity).startActivityForResult(intentGetDialog, Global.CALL_PAGE_DIALOG)
                true
            }
        }

        inner class novelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var ll_item = view.findViewById<LinearLayout>(R.id.ll_item)
            var iv_icon = view.findViewById<ImageView>(R.id.iv_icon)
            var tv_name = view.findViewById<TextView>(R.id.tv_name)
            var tv_desc = view.findViewById<TextView>(R.id.tv_desc)

            //呼叫建構子 預先讀取sharePreferenceProfile設定檔
            init {
                loadingSettingValue()
            }

            //讀取sharePreferenceProfile設定檔
            fun loadingSettingValue() {
                var sharePreferenceProfile_Local =
                    context.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
                tv_name.setTextColor(
                    Color.rgb(
                        sharePreferenceProfile_Local.getInt("FontColor_Red", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Green", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
                    )
                )
                tv_desc.textSize = sharePreferenceProfile_Local.getInt("FontSize", 0).toFloat()
                tv_desc.setTextColor(
                    Color.rgb(
                        sharePreferenceProfile_Local.getInt("FontColor_Red", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Green", 0),
                        sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
                    )
                )
                iv_icon.setBackgroundColor(
                    Color.rgb(
                        sharePreferenceProfile_Local.getInt("BackColor_Red", 0),
                        sharePreferenceProfile_Local.getInt("BackColor_Green", 0),
                        sharePreferenceProfile_Local.getInt("BackColor_Blue", 0)
                    )
                )
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

    fun pageJump(m_nowPageHtml: String, nowPage: Int) {
        Log.d("watch", "pageJump Novelist")
        nowPageValue = nowPage
        prevPageValue = nowPage - 1
        nextPageValue = nowPage + 1

        var jumpingPage: String = ""
        if (m_nowPageHtml.contains("forum")) {
            var cutStart: Int = m_nowPageHtml.lastIndexOf("-")
            jumpingPage = m_nowPageHtml.substring(0, cutStart + 1) + nowPageValue.toString() + ".html"
            nowPageHtml = jumpingPage
        }
        Thread {
            Runnable {
                GetHtmlData(jumpingPage) {
                    var mNovelDataLink = NovelDataLink()
                    recycleViewBinding(mNovelDataLink.getList(mNovelsData))
                }
            }.run()
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("watch", "onActivityResult Novelist")
        Log.d("watch", "A requestCode: " + requestCode + " resultCode: " + resultCode)
        if (requestCode == Global.CALL_PAGE_DIALOG) {
            if (resultCode == Global.LIST_RESULT_NEXT) {
                Log.d("watch", "PAGE_RESULT_NEXT")
                Thread {
                    Runnable {
                        pageJump(nowPageHtml, nextPageValue)
                    }.run()
                }.start()
            } else if (resultCode == Global.LIST_RESULT_PREV) {
                Log.d("watch", "PAGE_RESULT_PREV")
                Thread {
                    Runnable {
                        pageJump(nowPageHtml, prevPageValue)
                    }.run()
                }.start()
            } else if (resultCode == Global.LIST_RESULT_TARGET) {
                if (data != null) {
                    nowPageValue = data.getIntExtra("JumpPage", 0)
                    Thread {
                        Runnable {
                            pageJump(nowPageHtml, nowPageValue)
                        }.run()
                    }.start()
                } else {
                    Toast.makeText(this.context, "JumpError", Toast.LENGTH_SHORT).show()
                }
//            } else if (resultCode == Global.PAGE_BOOKMARK) {
//
            } else if (resultCode == Global.SETTING) {
                if (data != null) {
                    reloadSetting(this.view!!)
//                recycleViewBinding()
                    //連結recycleView
                    var mNovelDataLink = NovelDataLink()
                    recycleViewBinding(mNovelDataLink.getList(mNovelsData))
                }
            }
        }
    }

    fun startGetNovelList() {
        Thread {
            Runnable {
                GetHtmlData(nowPageHtml) {
                    Log.d("watch", "go GetHtmlData Novelist")
                    if (mNovelsData.iconArray.size > 0) {
                        //連結recycleView
                        var mNovelDataLink = NovelDataLink()
                        recycleViewBinding(mNovelDataLink.getList(mNovelsData))
                    }
                }
            }.run()
        }.start()
    }
}
