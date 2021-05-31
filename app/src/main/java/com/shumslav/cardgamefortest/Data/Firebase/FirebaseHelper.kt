package com.shumslav.cardgamefortest.Data.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val NODE_USERS = "Users"
const val NODE_DIFICULT = "Dificult"
const val NODE_DIFICULT_EASY = "Easy"
const val NODE_DIFICULT_MEDIUM = "Medium"
const val NODE_DIFICULT_HARD = "Hard"
const val NODE_SCORE = "Score"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

fun addNewScore(login:String, score:String, dificult:Int){
    initFirebase()
    var ref = REF_DATABASE_ROOT.child(NODE_USERS).child(login).child(NODE_DIFICULT)
    when(dificult){
        5 -> ref = ref.child(NODE_DIFICULT_EASY)
        10 -> ref = ref.child(NODE_DIFICULT_MEDIUM)
        15 -> ref = ref.child(NODE_DIFICULT_HARD)
    }
}