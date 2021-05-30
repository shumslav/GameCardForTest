package com.shumslav.cardgamefortest.Data.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shumslav.cardgamefortest.Data.Models.SettingsApp


val DB_NAME = "CardGame"
val TABLE_NAME_SETTINGS = "Settings"
val SETTINGS_COL_DIFICULT = "Dificult"
val SETTINGS_COL_VOLUME = "Volume"

class SQLiteHelper(val context: Context): SQLiteOpenHelper(context, DB_NAME,null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsers = "CREATE TABLE " + TABLE_NAME_SETTINGS + " (" +
                SETTINGS_COL_DIFICULT + " VARCHAR(256), " +
                SETTINGS_COL_VOLUME + " VARCHAR(256))"
        db?.execSQL(createTableUsers)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertSettings(settings:SettingsApp) {
        var db = this.writableDatabase
        db.delete(TABLE_NAME_SETTINGS, null, null)
        db.close()
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put(SETTINGS_COL_DIFICULT, settings.dificult)
        cv.put(SETTINGS_COL_VOLUME, settings.volumeLevel)
        db.insert(TABLE_NAME_SETTINGS, null, cv)
    }

    fun getSettings():SettingsApp{
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME_SETTINGS"
        val settings = SettingsApp()
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                settings.dificult = result.getString(result.getColumnIndex(SETTINGS_COL_DIFICULT)).toString()
                settings.volumeLevel = result.getString(result.getColumnIndex(SETTINGS_COL_VOLUME)).toString()
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return settings
    }
}