package com.shumslav.cardgamefortest.Data.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.Models.User


val DB_NAME = "CardGame"
val TABLE_NAME_SETTINGS = "Settings"
val TABLE_NAME_USER = "User"
val SETTINGS_COL_DIFICULT = "Dificult"
val SETTINGS_COL_VOLUME = "Volume"
val USER_COL_LOGIN = "Login"
val USER_COL_PASSWORD = "Password"

class SQLiteHelper(val context: Context): SQLiteOpenHelper(context, DB_NAME,null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSettings = "CREATE TABLE " + TABLE_NAME_SETTINGS + " (" +
                SETTINGS_COL_DIFICULT + " VARCHAR(256), " +
                SETTINGS_COL_VOLUME + " VARCHAR(256))"
        db?.execSQL(createTableSettings)
        val createTableUser = "CREATE TABLE " + TABLE_NAME_USER + " (" +
                USER_COL_LOGIN + " VARCHAR(256), " +
                USER_COL_PASSWORD + " VARCHAR(256))"
        db?.execSQL(createTableUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertSettings(settings:SettingsApp) {
        var db = this.writableDatabase
        db.delete(TABLE_NAME_SETTINGS, null, null)
        db.close()

        db = this.writableDatabase
        val cv = ContentValues()
        cv.put(SETTINGS_COL_DIFICULT, settings.getDificult().toString())
        cv.put(SETTINGS_COL_VOLUME, settings.getVolumeLevel().toString())
        db.insert(TABLE_NAME_SETTINGS, null, cv)
        db.close()
    }

    fun getSettings():SettingsApp{
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME_SETTINGS"
        val settings = SettingsApp()
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                settings.setDificult(result.getString(result.getColumnIndex(SETTINGS_COL_DIFICULT)).toString())
                settings.setVolumeLevel(result.getString(result.getColumnIndex(SETTINGS_COL_VOLUME)).toString())
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return settings
    }

    fun insertUser(user: User){
        var db = this.writableDatabase
        db.delete(TABLE_NAME_USER, null, null)
        db.close()

        db = this.writableDatabase
        var cv = ContentValues()
        cv.put(USER_COL_LOGIN, user.getLogin())
        cv.put(USER_COL_PASSWORD, user.getPassword())
        db.insert(TABLE_NAME_USER, null, cv)
        db.close()
    }

    fun getUser():User{
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME_USER"
        val user = User()
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                user.setLogin(result.getString(result.getColumnIndex(USER_COL_LOGIN)).toString())
                user.setPassword(result.getString(result.getColumnIndex(USER_COL_PASSWORD)).toString())
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return user
    }

    fun deleteUserAndSettings(){
        var db = this.writableDatabase
        db.delete(TABLE_NAME_USER, null, null)
        db.close()

        db = this.writableDatabase
        db.delete(TABLE_NAME_SETTINGS, null, null)
        db.close()
    }


}