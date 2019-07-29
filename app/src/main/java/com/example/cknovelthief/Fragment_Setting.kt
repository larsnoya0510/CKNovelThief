package com.example.cknovelthief

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.cknovelthief.DataClass.NovelDataLink

class Fragment_Setting : Fragment() {
    lateinit var btn_save: Button
    lateinit var tv_ExampleText: TextView
    lateinit var tv_fontColorRed: TextView
    lateinit var tv_fontColorGreen: TextView
    lateinit var tv_fontColorBlue: TextView
    lateinit var tv_backColorRed: TextView
    lateinit var tv_backColorGreen: TextView
    lateinit var tv_backColorBlue: TextView
    lateinit var seekBar_NovelListtextsize: SeekBar
    lateinit var seekBar_Noveltextsize: SeekBar
    lateinit var seekBar_fontColorRed: SeekBar
    lateinit var seekBar_fontColorGreen: SeekBar
    lateinit var seekBar_fontColorBlue: SeekBar
    lateinit var seekBar_backColorRed: SeekBar
    lateinit var seekBar_backColorGreen: SeekBar
    lateinit var seekBar_backColorBlue: SeekBar
    lateinit var rb_list: RadioButton
    lateinit var rb_novel: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment__setting, container, false)
        initView(view)
        loadsharePreferenceProfile()
        seekBar_NovelListtextsize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_ExampleText.textSize = (8 + progress).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_Noveltextsize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_ExampleText.textSize = (8 + progress).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_fontColorRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_fontColorRed.text = progress.toString()
                tv_ExampleText.setTextColor(
                    Color.rgb(
                        seekBar_fontColorRed.progress,
                        seekBar_fontColorGreen.progress,
                        seekBar_fontColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_fontColorGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_fontColorGreen.text = progress.toString()
                tv_ExampleText.setTextColor(
                    Color.rgb(
                        seekBar_fontColorRed.progress,
                        seekBar_fontColorGreen.progress,
                        seekBar_fontColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_fontColorBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_fontColorBlue.text = progress.toString()
                tv_ExampleText.setTextColor(
                    Color.rgb(
                        seekBar_fontColorRed.progress,
                        seekBar_fontColorGreen.progress,
                        seekBar_fontColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_backColorRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_backColorRed.text = progress.toString()
                tv_ExampleText.setBackgroundColor(
                    Color.rgb(
                        seekBar_backColorRed.progress,
                        seekBar_backColorGreen.progress,
                        seekBar_backColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_backColorGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_backColorGreen.text = progress.toString()
                tv_ExampleText.setBackgroundColor(
                    Color.rgb(
                        seekBar_backColorRed.progress,
                        seekBar_backColorGreen.progress,
                        seekBar_backColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBar_backColorBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_backColorBlue.text = progress.toString()
                tv_ExampleText.setBackgroundColor(
                    Color.rgb(
                        seekBar_backColorRed.progress,
                        seekBar_backColorGreen.progress,
                        seekBar_backColorBlue.progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        rb_list.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked==true){
                seekBar_Noveltextsize.visibility= View.GONE
                seekBar_NovelListtextsize.visibility= View.VISIBLE
                tv_ExampleText.textSize = (8 + seekBar_NovelListtextsize.progress).toFloat()
            }
        }
        rb_novel.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked==true){
                seekBar_NovelListtextsize.visibility= View.GONE
                seekBar_Noveltextsize.visibility= View.VISIBLE
                tv_ExampleText.textSize = (8 + seekBar_Noveltextsize.progress).toFloat()
            }
        }
        btn_save.setOnClickListener {
            savesharePreferenceProfile()
            Toast.makeText(this.context, "已儲存", Toast.LENGTH_SHORT).show()
            var m_FragmemtManager = getActivity()!!.supportFragmentManager
            for( i in 0 until m_FragmemtManager.fragments.size) {
                if(m_FragmemtManager.fragments[i] is Fragment_NovelList){
                    (m_FragmemtManager.fragments[i] as Fragment_NovelList).reloadSetting(m_FragmemtManager.fragments[i].view!!)
                    var mNovelDataLink = NovelDataLink()
                    (m_FragmemtManager.fragments[i] as Fragment_NovelList).recycleViewBinding(mNovelDataLink.getList((m_FragmemtManager.fragments[i] as Fragment_NovelList).mNovelsData))
                }
               else if(m_FragmemtManager.fragments[i] is Fragment_Novel){
                    (m_FragmemtManager.fragments[i] as Fragment_Novel).reloadSetting(m_FragmemtManager.fragments[i].view!!)
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun initView(view: View) {
        btn_save = view.findViewById<Button>(R.id.btn_save)
        tv_ExampleText = view.findViewById<TextView>(R.id.tv_ExampleText)
        tv_fontColorRed = view.findViewById<TextView>(R.id.tv_fontColorRed)
        tv_fontColorGreen = view.findViewById<TextView>(R.id.tv_fontColorGreen)
        tv_fontColorBlue = view.findViewById<TextView>(R.id.tv_fontColorBlue)
        tv_backColorRed = view.findViewById<TextView>(R.id.tv_backColorRed)
        tv_backColorGreen = view.findViewById<TextView>(R.id.tv_backColorGreen)
        tv_backColorBlue = view.findViewById<TextView>(R.id.tv_backColorBlue)
        seekBar_NovelListtextsize = view.findViewById<SeekBar>(R.id.seekBar_NovelListtextsize)
        seekBar_Noveltextsize = view.findViewById<SeekBar>(R.id.seekBar_Noveltextsize)
        seekBar_fontColorRed = view.findViewById<SeekBar>(R.id.seekBar_fontColorRed)
        seekBar_fontColorGreen = view.findViewById<SeekBar>(R.id.seekBar_fontColorGreen)
        seekBar_fontColorBlue = view.findViewById<SeekBar>(R.id.seekBar_fontColorBlue)
        seekBar_backColorRed = view.findViewById<SeekBar>(R.id.seekBar_backColorRed)
        seekBar_backColorGreen = view.findViewById<SeekBar>(R.id.seekBar_backColorGreen)
        seekBar_backColorBlue = view.findViewById<SeekBar>(R.id.seekBar_backColorBlue)
        rb_list= view.findViewById<RadioButton>(R.id.rb_list)
        rb_novel= view.findViewById<RadioButton>(R.id.rb_novel)
    }

    fun loadsharePreferenceProfile() {
        var sharePreferenceProfile_Local =
            this.getActivity()!!.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
        seekBar_NovelListtextsize.progress = sharePreferenceProfile_Local.getInt("ListFontSize", 0) - 8
        seekBar_Noveltextsize.progress = sharePreferenceProfile_Local.getInt("NovelFontSize", 0) - 8
        seekBar_fontColorRed.progress = sharePreferenceProfile_Local.getInt("FontColor_Red", 0)
        tv_fontColorRed.text = seekBar_fontColorRed.progress.toString()
        seekBar_fontColorGreen.progress = sharePreferenceProfile_Local.getInt("FontColor_Green", 0)
        tv_fontColorGreen.text = seekBar_fontColorGreen.progress.toString()
        seekBar_fontColorBlue.progress = sharePreferenceProfile_Local.getInt("FontColor_Blue", 0)
        tv_fontColorBlue.text = seekBar_fontColorBlue.progress.toString()
        seekBar_backColorRed.progress = sharePreferenceProfile_Local.getInt("BackColor_Red", 255)
        tv_backColorRed.text = seekBar_backColorRed.progress.toString()
        seekBar_backColorGreen.progress = sharePreferenceProfile_Local.getInt("BackColor_Green", 255)
        tv_backColorGreen.text = seekBar_backColorGreen.progress.toString()
        seekBar_backColorBlue.progress = sharePreferenceProfile_Local.getInt("BackColor_Blue", 255)
        tv_backColorBlue.text = seekBar_backColorBlue.progress.toString()
        tv_ExampleText.textSize = (seekBar_NovelListtextsize.progress + 8).toFloat()
        tv_ExampleText.setTextColor(
            Color.rgb(
                seekBar_fontColorRed.progress,
                seekBar_fontColorGreen.progress,
                seekBar_fontColorBlue.progress
            )
        )
        tv_ExampleText.setBackgroundColor(
            Color.rgb(
                seekBar_backColorRed.progress,
                seekBar_backColorGreen.progress,
                seekBar_backColorBlue.progress
            )
        )
    }

    fun savesharePreferenceProfile() {
        var sharePreferenceProfile_Local =
            this.getActivity()!!.getSharedPreferences("LocalProfileSetting", Context.MODE_PRIVATE)
        var localProfileSet = sharePreferenceProfile_Local.edit()
        localProfileSet.putInt("FontColor_Red", seekBar_fontColorRed.progress)
        localProfileSet.putInt("FontColor_Green", seekBar_fontColorGreen.progress)
        localProfileSet.putInt("FontColor_Blue", seekBar_fontColorBlue.progress)
        localProfileSet.putInt("BackColor_Red", seekBar_backColorRed.progress)
        localProfileSet.putInt("BackColor_Green", seekBar_backColorGreen.progress)
        localProfileSet.putInt("BackColor_Blue", seekBar_backColorBlue.progress)
        localProfileSet.putInt("ListFontSize", seekBar_NovelListtextsize.progress + 8)
        localProfileSet.putInt("NovelFontSize", seekBar_Noveltextsize.progress + 8)
        localProfileSet.apply() //加入或刪除檔案時 讓其生效 -> 非同步
    }
}
