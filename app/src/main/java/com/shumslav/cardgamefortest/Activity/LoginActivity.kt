package com.sigufyndufi.finfangam.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.sigufyndufi.finfangam.Data.Firebase.*
import com.sigufyndufi.finfangam.Data.Models.SettingsApp
import com.sigufyndufi.finfangam.Data.Models.User
import com.sigufyndufi.finfangam.Data.SQLite.SQLiteHelper
import com.sigufyndufi.finfangam.R
import com.sigufyndufi.finfangam.makeToast

class LoginActivity : Activity() {
    private lateinit var passwordText:EditText
    private lateinit var loginText:EditText
    private lateinit var nextButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initFirebase()

        passwordText = findViewById(R.id.password)
        loginText = findViewById(R.id.login)
        nextButton = findViewById(R.id.next)
        val sqlHelper = SQLiteHelper(this)

        nextButton.setOnClickListener {
            val login = loginText.text.toString()
            val password = passwordText.text.toString()
            if (login.isEmpty() || password.isEmpty()){
                makeToast(this, "Fill all fields")
            }
            else{
                REF_DATABASE_ROOT.child(NODE_USERS).addListenerForSingleValueEvent(
                    AppValueEventListener{
                        if (it.hasChild(login)){
                            val user = it.child(login).getValue(User::class.java)
                            if (user!!.getPassword()==password){
                                insertUserFromFirebaseToSQLite(this,login)
                                insertSettingsFromFirebaseToSQLite(this,login)
//                                makeToast(this,"Login")
                                startActivity(Intent(this,MainActivity::class.java))
                            }
                            else{
                                makeToast(this, "Wrong Password")
                            }
                        }
                        else{
                            val user = User()
                            val settingsApp = SettingsApp()
                            user.setLogin(login)
                            user.setPassword(password)
                            addNewUserFirebase(user)
                            resetSettingsFirebase(login,settingsApp)
                            sqlHelper.insertUser(user)
                            sqlHelper.insertSettings(settingsApp)
                            makeToast(this,"NewLogin")
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                    }
                )
            }
        }
    }
}