package com.example.cknovelthief.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper


class BookmarkDBHelper(var context: Context) :
        ManagedSQLiteOpenHelper(context,"Novel.db", null, 1) {
        var tableList: List<String> = listOf("bookmark")
        companion object {
            private val TAG = "BookmarkDB"
            //var DB_NAME = "Novel.db"
            //var TABLE_NAME = "user_info"
            //var CURRENT_VERSION = 1
            private var instance: BookmarkDBHelper?=null
            @Synchronized
            fun getInstance(ctx: Context, version: Int = 0): BookmarkDBHelper {
                if (instance == null) {
                    instance =BookmarkDBHelper(ctx.applicationContext)
                }
                Log.d("TAG", "Instance RETURN")
                return instance!!
            }
        }
        override fun onCreate(db: SQLiteDatabase) {
            Log.d("TAG", "onCreate")
            DBInital(tableList,db)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            Log.d("TAG", "onUpgrade")
        }
        fun DBInital(listTable : List<String>, db: SQLiteDatabase){
            Log.d("TAG", "Inital start")
            //var db: SQLiteDatabase
            for(i : Int in 0..listTable.count()-1){
                when(listTable[i]){
                    "bookmark"->{
                        //db = this.context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
                        var create_sql = "CREATE TABLE IF NOT EXISTS "+listTable[i] +" ("
                        create_sql += "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                        create_sql += "title VARCHAR NOT NULL,"
                        create_sql += "html VARCHAR NOT NULL"
                        create_sql += ");"
                        Log.d("TAG", "create_sql:" + create_sql)
                        db.execSQL(create_sql)
                        Log.d("TAG", "create bookmark table Done")
                    }
                }
            }
        }
        fun query(tableName :String,condition: String): List<bookmarkInfo> {
            Log.d("TAG", "start query")
            var m_conditionJudge : String =
                if(condition=="") {
                    "1=1"
                }else {
                    condition
                }
            val sql = "select * from $tableName where $m_conditionJudge;"
            Log.d("TAG", "query sql: " + sql)
            var infoArray = mutableListOf<bookmarkInfo>()
            use {
                Log.d("TAG", "start get query")
                val cursor = rawQuery(sql, null)
                Log.d("TAG", "cursor: "+cursor.count)
                if(cursor.count>0) {
                    if (cursor.moveToFirst()) {
                        while (true) {
                            val info = bookmarkInfo()
                            info.id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                            info.title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                            info.html = cursor.getString(cursor.getColumnIndexOrThrow("html"))
                            infoArray.add(info)
                            if (cursor.isLast) {
                                break
                            }
                            cursor.moveToNext()
                        }
                    }
                }
                cursor.close()
            }
            return infoArray
        }
        fun insert(tableName :String, infoArray: MutableList<bookmarkInfo>): Long {
            Log.d("TAG", "start insert")
            var result: Long = -1
            for (i in infoArray.indices) {
                val info = infoArray[i]
                //var tempArray: List<bookmarkInfo>
                val cv = ContentValues()
                cv.put("title", info.title)
                cv.put("html", info.html)
                use {
                    result = insert(tableName, "", cv)
                }
                // 添加成功后返回行号，失败后返回-1
                if (result == -1L) {
                    return result
                }
            }
            return result
        }
        fun delete(tableName :String, condition: String): Int {
            Log.d("TAG", "start delete")
            var count = 0
            use {
                count = delete(tableName, condition, null)
            }
            return count
        }
        fun update(tableName :String,info: bookmarkInfo, condition: String = "id=${info.id}"): Int {
            val cv = ContentValues()
            cv.put("title", info.title)
            cv.put("html", info.html)
            var count = 0
            condition
            use {
                count = update(tableName, cv, condition, null)
            }
            return count
        }
        class bookmarkInfo{
            var id :Long = 0
            var title: String = ""
            var html: String = ""
        }
    }

    class BookMarkData{
        data class BookMarkDataInfo(
            var title: String,
            var html: String,
            var id: Long
        )
        var titleArray = arrayListOf<String>()
        var htmlArray = arrayListOf<String>()
        var idArray = arrayListOf<Long>()
        fun setBookMarkList( mbookmarklist :List<BookmarkDBHelper.bookmarkInfo>):MutableList<BookMarkDataInfo>{

            //putsqldata in  list
            var bookmarkList = mutableListOf<BookMarkDataInfo>()
            for (i in mbookmarklist.indices) {
                titleArray.add(mbookmarklist[i].title)
                htmlArray.add(mbookmarklist[i].html)
                idArray.add(mbookmarklist[i].id)
                bookmarkList.add(BookMarkDataInfo(titleArray[i], htmlArray[i], idArray[i]))
            }
            return bookmarkList
        }
        fun clear(){
            titleArray.clear()
            htmlArray.clear()
            idArray.clear()
        }
    }
