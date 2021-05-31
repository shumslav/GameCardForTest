package com.shumslav.cardgamefortest.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.shumslav.cardgamefortest.Data.Firebase.AppValueEventListener
import com.shumslav.cardgamefortest.Data.Firebase.NODE_USERS
import com.shumslav.cardgamefortest.Data.Firebase.REF_DATABASE_ROOT
import com.shumslav.cardgamefortest.Data.Models.User
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.makeToast

class LoginActivity : AppCompatActivity() {
    private lateinit var passwordText:EditText
    private lateinit var loginText:EditText
    private lateinit var nextButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        passwordText = findViewById(R.id.password)
        loginText = findViewById(R.id.login)
        nextButton = findViewById(R.id.next)
        val login = loginText.text.toString()
        val password = passwordText.text.toString()

        nextButton.setOnClickListener {
            if (login.isEmpty() || password.isEmpty()){
                makeToast(this, "Fill all fields")
            }
            else{
                REF_DATABASE_ROOT.child(NODE_USERS).addListenerForSingleValueEvent(
                    AppValueEventListener{
                        if (it.hasChild(login)){
                            val user = it.child(login).getValue(User::class.java)
                            if (user!!.getPassword()==password){

                            }
                        }
                    }
                )
            }
        }

    }
}