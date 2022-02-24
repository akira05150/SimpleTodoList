package com.example.simpletodolist

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


interface OnItemClick {
    fun onDelete(ID: String?)
}

class MainActivity : AppCompatActivity(), OnItemClick {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 設定每日一句
        dailyQuote()

        // 抓取資料
        val dataList = fetchData()

        // 設置RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // pass the values to RvAdapter
        val rvAdapter = RvAdapter(dataList, this)
        recyclerView.adapter = rvAdapter

        // 進入新增頁面
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

    }

    // 抓取資料
    fun fetchData() : ArrayList<Model> {
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
            FeedReaderContract.FeedEntry.COLUMN_NAME_CREATED,
            FeedReaderContract.FeedEntry.COLUMN_NAME_DUE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,
        )

        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_DUE} ASC"
        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        val dataList = ArrayList<Model>()
        with(cursor) {
            while (moveToNext()) {
                var model = Model(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                )
                dataList.add(model)
            }
        }
        cursor.close()
        db.close()
        return dataList
    }

    // 刪除資料
    override fun onDelete(ID: String?) {
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.readableDatabase

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(ID)

        val deletedRows = db.delete(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            selection,
            selectionArgs)
        db.close()
    }

    // 更新頁面
    override fun onResume() {
        super.onResume()
        val dataList = fetchData()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // pass the values to RvAdapter
        val rvAdapter = RvAdapter(dataList,this)
        recyclerView.adapter = rvAdapter
    }

    // 每日更新
    fun dailyQuote(){
        // 查詢並顯示過去句子
        val result = queryQuote()
        if(result.get(0) == ""){  //若沒有資料 代表第一次打開app
            insertQuote()
        }
        else{
            val date = result.get(0).toInt()
            val time = Calendar.getInstance()
            if (time.get(Calendar.DATE) > date){
                // 換日更新
                updateQuote()
            }
            else{
                // 當日從資料庫取出
                queryQuote()
            }
        }
    }

    fun insertQuote(){
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.writableDatabase

        val str_quote = getQuote()
        var dailyQuote = findViewById<TextView>(R.id.txv_dailyQuote)
        dailyQuote.text = str_quote

        val time = Calendar.getInstance()
        val str_date = time.get(Calendar.DATE)

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(QuoteContract.QuoteEntry.COLUMN_NAME_CONTENT, str_quote)
            put(QuoteContract.QuoteEntry.COLUMN_NAME_DATE, str_date)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(QuoteContract.QuoteEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun queryQuote() : ArrayList<String> {
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            QuoteContract.QuoteEntry.COLUMN_NAME_DATE,
            QuoteContract.QuoteEntry.COLUMN_NAME_CONTENT
        )

        val cursor = db.query(
            QuoteContract.QuoteEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null              // The sort order
        )

        var query = arrayListOf<String>("","")
        with(cursor) {
            while (moveToNext()) {
                query.set(0, cursor.getString(0))  //DATE
                query.set(1, cursor.getString(1))  //CONTENT
                println("query = ${query}")
                println(cursor.count)
            }
        }

        // 顯示查詢的句子
        var dailyQuote = findViewById<TextView>(R.id.txv_dailyQuote)
        dailyQuote.text = query.get(1)

        cursor.close()
        db.close()
        return query
    }

    fun updateQuote(){
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.writableDatabase

        // 查詢昨天的句子
        val query_quote = queryQuote().get(1)

        // 顯示更新的quote
        val quote = getQuote()
        var dailyQuote = findViewById<TextView>(R.id.txv_dailyQuote)
        dailyQuote.text = quote

        // 取得更新的日期
        val time = Calendar.getInstance()
        val str_date = time.get(Calendar.DATE)

        val values = ContentValues().apply {
            put(QuoteContract.QuoteEntry.COLUMN_NAME_CONTENT, quote)
            put(QuoteContract.QuoteEntry.COLUMN_NAME_DATE, str_date)
        }

        // Which row to update, based on the title
        val selection = "${QuoteContract.QuoteEntry.COLUMN_NAME_CONTENT} LIKE ?"
        val selectionArgs = arrayOf(query_quote)    //CONTENT
        val count = db.update(
            QuoteContract.QuoteEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs)

        db.close()
    }

    // 引入cpp函數
    companion object {
        init {
            System.loadLibrary("daily_quote")
        }
    }
    external fun getQuote() : String
}
