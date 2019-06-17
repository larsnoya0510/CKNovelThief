package com.example.cknovelthief

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.cknovelthief.DataClass.Global
import kotlinx.android.synthetic.main.activity__page_dialog.*

class Activity_PageDialog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__page_dialog)
        var bundle = intent.extras
        var bundle_nowPageValue= bundle.getInt("nowPageValue",0)
        var bundle_TotalPageValue= bundle.getInt("totalPageValue",0)
        //限定novel列表下不顯示加入書籤
        var bundle_PageDialogType= bundle.getInt("PageDialogType",-1)
        if(bundle_PageDialogType==0){
            Btn_BookMark.visibility = View.GONE
        }
        else if(bundle_PageDialogType==1){
            Btn_BookMark.visibility = View.VISIBLE
        }
        tv_NowPage.setText(bundle_nowPageValue.toString())
        tv_TotalPage.text = bundle_TotalPageValue.toString()

        Btn_PrevPage.setOnClickListener {
//            setResult(Global.PAGE_RESULT_PREV)
            if(bundle_PageDialogType==0){
                setResult(Global.LIST_RESULT_PREV)
            }
            else if(bundle_PageDialogType==1){
                setResult(Global.PAGE_RESULT_PREV)
            }
            finish()
        }
        Btn_NextPage.setOnClickListener {
            Log.d("watch","Btn_NextPage")
//            setResult(Global.PAGE_RESULT_NEXT)
            if(bundle_PageDialogType==0){
                setResult(Global.LIST_RESULT_NEXT)
            }
            else if(bundle_PageDialogType==1){
                setResult(Global.PAGE_RESULT_NEXT)
            }
            finish()
        }
        Btn_TargetPage.setOnClickListener {
            Log.d("Info", "changeTarget" + tv_NowPage.text.toString().toInt())
            intent.putExtra("JumpPage", tv_NowPage.text.toString().toInt())
            //setResult(Global.PAGE_RESULT_TARGET,intent)
            if(bundle_PageDialogType==0){
                setResult(Global.LIST_RESULT_TARGET)
            }
            else if(bundle_PageDialogType==1){
                setResult(Global.PAGE_RESULT_TARGET)
            }
            finish()
        }
        Btn_BookMark.setOnClickListener {
            setResult(Global.PAGE_BOOKMARK)
            finish()
        }
    }
}
