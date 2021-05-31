package com.shumslav.cardgamefortest.Data.Firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.shumslav.cardgamefortest.Data.Firebase.Callbacks.CallbackForSettings
import com.shumslav.cardgamefortest.Data.Firebase.Callbacks.CallbackForUser
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.Models.User
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import java.util.ArrayList

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val NODE_USERS = "Users"
const val NODE_DIFICULT = "Dificult"
const val NODE_DIFICULT_EASY = "Easy"
const val NODE_DIFICULT_MEDIUM = "Medium"
const val NODE_DIFICULT_HARD = "Hard"
const val NODE_SCORES = "Scores"
const val NODE_SETTINGS = "settings"
const val NODE_LOGIN = "login"
const val NODE_PASSWORD = "password"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

fun addNewScoreFirebase(login: String, score: String, dificult: Int) {
    initFirebase()
    var ref = REF_DATABASE_ROOT.child(NODE_SCORES).child(login).child(NODE_DIFICULT)
    when (dificult) {
        5 -> ref = ref.child(NODE_DIFICULT_EASY)
        10 -> ref = ref.child(NODE_DIFICULT_MEDIUM)
        15 -> ref = ref.child(NODE_DIFICULT_HARD)
    }
}

fun addNewUserFirebase(user: User) {
    initFirebase()
    REF_DATABASE_ROOT.child(NODE_USERS).child(user.getLogin()).setValue(user)
}

fun resetSettingsFirebase(login: String, settingsApp: SettingsApp) {
    var ref = REF_DATABASE_ROOT.child(NODE_SETTINGS).child(login).setValue(settingsApp)
}

fun insertUserFromFirebaseToSQLite(context: Context, login: String) {
    initFirebase()

    fun helperForInsertUserFromFirebase(login: String, firebaseCallback: CallbackForUser) {
        val ref = REF_DATABASE_ROOT.child(NODE_USERS).child(login)
        val listData = ArrayList<User?>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val new_user = snapshot.getValue(User::class.java)
                listData.add(new_user)
                firebaseCallback.onCallback(listData)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    val sqlHelper = SQLiteHelper(context)
    var user = User()
    helperForInsertUserFromFirebase(login, object : CallbackForUser {
        override fun onCallback(list: MutableList<User?>) {
            super.onCallback(list)
            user = list[0]!!
            sqlHelper.insertUser(user)
        }
    })
}

fun insertSettingsFromFirebaseToSQLite(context: Context, login: String) {
    initFirebase()

    fun helperForInsertSettingsFromFirebase(login: String, firebaseCallback: CallbackForSettings) {
        val ref = REF_DATABASE_ROOT.child(NODE_SETTINGS).child(login)
        val listData = ArrayList<SettingsApp?>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val settingsApp = snapshot.getValue(SettingsApp::class.java)
                listData.add(settingsApp)
                firebaseCallback.onCallback(listData)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    val sqlHelper = SQLiteHelper(context)
    var settingsApp = SettingsApp()
    helperForInsertSettingsFromFirebase(login, object : CallbackForSettings {
        override fun onCallback(list: MutableList<SettingsApp?>) {
            super.onCallback(list)
            settingsApp = list[0]!!
            sqlHelper.insertSettings(settingsApp)
        }
    })
}


