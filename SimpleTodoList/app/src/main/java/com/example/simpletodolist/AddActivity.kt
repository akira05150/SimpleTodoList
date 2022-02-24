package com.example.simpletodolist

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class AddActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //val edt_location = findViewById<EditText>(R.id.edt_location)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // 抓到現有位置並顯示
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val edt_location = findViewById<EditText>(R.id.edt_location)
        checkLocationPermission(edt_location)

        // 顯示現在位置按鈕
        val btn_location = findViewById<Button>(R.id.btn_location)
        btn_location.setOnClickListener {
            checkLocationPermission(edt_location)
        }

        // 顯示日期
        val created = findViewById<TextView>(R.id.txv_created)
        val due = findViewById<TextView>(R.id.txv_due)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val format = setDateFormat(year, month, day)
        created.text = format
        due.text = format

        created.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                run {
                    val format = setDateFormat(year, month, day)
                    created.text = format
                }
            }, year, month, day).show()
        }

        due.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                run {
                    val format = setDateFormat(year, month, day)
                    due.text = format
                }
            }, year, month, day).show()
        }


        // 新增按紐
        val edt_title = findViewById<EditText>(R.id.edt_titile)
        val edt_description = findViewById<EditText>(R.id.edt_description)
        val btn_complete = findViewById<Button>(R.id.btn_complete)

        btn_complete.setOnClickListener{
            if(edt_title.text.toString() == ""){
                Toast.makeText(this, "請輸入標題", Toast.LENGTH_SHORT).show()
            }
            else if(edt_description.text.toString() == ""){
                Toast.makeText(this, "請輸入描述內容", Toast.LENGTH_SHORT).show()
            }
            else if(edt_location.text.toString() == ""){
                Toast.makeText(this, "請輸入位置座標", Toast.LENGTH_SHORT).show()
            }
            else{
                val model = Model(
                    "ID",
                    edt_title.text.toString(),
                    edt_description.text.toString(),
                    created.text.toString(),
                    due.text.toString(),
                    edt_location.text.toString())
                insertData(model)
                finish()
            }
        }
    }


    // 格式化日期
    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        var str_month = (month+1).toString()
        var str_day = day.toString()

        if((month+1) < 10){
            str_month = "0" + str_month
        }
        if(day < 10){
            str_day = "0" + str_day
        }
        return "$year-${str_month}-$str_day"
    }

    // 抓取目前位置與驗證
    private fun checkLocationPermission(location:EditText) {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        task.addOnSuccessListener{
            if(it != null){
                location.setText(it.latitude.toString() + ", " +it.latitude.toString())
            }
        }
    }

    // 加入資料
    fun insertData(model: Model){
        val dbHelper = SQLiteDataBaseHelper(this)
        val db = dbHelper.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, model.title)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, model.description)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_CREATED, model.created)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_DUE, model.due)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION, model.location)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        db.close()
    }
}