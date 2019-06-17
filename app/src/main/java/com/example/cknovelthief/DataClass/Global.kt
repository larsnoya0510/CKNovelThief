package com.example.cknovelthief.DataClass

class Global {
    companion object {
        val PAGE_REQUEST_PREV = 0
        val PAGE_REQUEST_NEXT = 1
        val PAGE_REQUEST_TARGET = 2
        val PAGE_RESULT_PREV = 3
        val PAGE_RESULT_NEXT = 4
        val PAGE_RESULT_TARGET = 5
        val PAGE_BOOKMARK = 6
        val SETTING = 7
        val LIST_REQUEST_NEXT =8
        val LIST_RESULT_NEXT =9
        val LIST_REQUEST_PREV =10
        val LIST_RESULT_PREV=11
        val LIST_REQUEST_TARGET = 12
        val LIST_RESULT_TARGET = 13
    }
}

class BookMarkHtml {
    var title: String = ""
    var html: String = ""
}