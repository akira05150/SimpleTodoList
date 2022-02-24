package com.example.simpletodolist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_CREATED = "created"
        const val COLUMN_NAME_DUE = "due"
        const val COLUMN_NAME_LOCATION = "location"
    }
}

object QuoteContract {
    // Table contents are grouped together in an anonymous object.
    object QuoteEntry : BaseColumns {
        const val TABLE_NAME = "quote"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_CONTENT = "content"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_CREATED} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_DUE} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION} TEXT)"

private const val SQL_CREATE_QUOTE =
    "CREATE TABLE ${QuoteContract.QuoteEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${QuoteContract.QuoteEntry.COLUMN_NAME_DATE} TEXT," +
            "${QuoteContract.QuoteEntry.COLUMN_NAME_CONTENT} TEXT)"


private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"
private const val SQL_DELETE_QUOTES = "DROP TABLE IF EXISTS ${QuoteContract.QuoteEntry.TABLE_NAME}"

class SQLiteDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_QUOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_DELETE_QUOTES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "FeedReader.db"
    }
}