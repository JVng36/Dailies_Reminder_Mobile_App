package com.example.dailiesandroidapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//helper class for managing SQLite DB. handles CRUD operations
class DbManager(context: Context?) :
    SQLiteOpenHelper(context, dbname, null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val query =
            "create table tbl_reminder(id integer primary key autoincrement,title text,date text,time text)"
        sqLiteDatabase.execSQL(query)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        val query =
            "DROP TABLE IF EXISTS tbl_reminder"
        sqLiteDatabase.execSQL(query)
        onCreate(sqLiteDatabase)
    }

    fun addreminder(title: String?, date: String?, time: String?): String {
        val database = this.readableDatabase
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("date", date)
        contentValues.put("time", time)
        val result = database.insert("tbl_reminder", null, contentValues)
            .toFloat()
        return if (result == -1f) {
            "Failed"
        } else {
            "Insert Successful!"
        }
    }

    fun deleteremindertitle(title: String): Int {
        val database = this.writableDatabase
        val whereClause = "title = ?"
        val whereArgs = arrayOf(title)
        return database.delete("tbl_reminder", whereClause, whereArgs)
    }

    fun readallreminders(): Cursor {
        val database = this.writableDatabase
        val query =
            "select * from tbl_reminder order by id desc"
        return database.rawQuery(query, null)
    }

    companion object {
        private const val dbname = "reminder"
    }
}

