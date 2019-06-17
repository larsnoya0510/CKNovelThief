package com.example.cknovelthief.DataClass

class NovelsDataSet() {
    var iconArray: MutableList<String> = mutableListOf<String>()
    var nameArray: MutableList<String> = mutableListOf<String>()
    var linkArray: MutableList<String> = mutableListOf<String>()


    fun AllClear() {
        iconArray.clear()
        nameArray.clear()
        linkArray.clear()
    }
}
class NovelDataLink() {
    var icon: String=""
    var name: String=""
    var link: String=""
    fun getList(mNovelsDataSet : NovelsDataSet): MutableList<NovelDataLink> {
        val novelList = mutableListOf<NovelDataLink>()
        for (i in mNovelsDataSet.iconArray.indices) {
            var mobject=NovelDataLink()
            mobject.icon=mNovelsDataSet.iconArray[i]
            mobject.name= mNovelsDataSet.nameArray[i]
            mobject.link= mNovelsDataSet.linkArray[i]
            novelList.add(mobject)
        }
        return novelList
    }
}