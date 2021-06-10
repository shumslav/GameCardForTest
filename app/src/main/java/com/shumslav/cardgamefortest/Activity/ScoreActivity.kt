package com.sigufyndufi.finfangam.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sigufyndufi.finfangam.Adapters.ScoreRecyclerAdapter
import com.sigufyndufi.finfangam.Data.Firebase.*
import com.sigufyndufi.finfangam.Data.Models.Score
import com.sigufyndufi.finfangam.Data.Models.User
import com.sigufyndufi.finfangam.Data.SQLite.SQLiteHelper
import com.sigufyndufi.finfangam.R

class ScoreActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var user: User
    private lateinit var backButton: Button

    private val sqlHelper = SQLiteHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        initFirebase()

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        user = sqlHelper.getUser()
        val ref = REF_DATABASE_ROOT.child(NODE_SCORES).child(user.getLogin())
        recyclerView = findViewById(R.id.scores)
        recyclerView.layoutManager = GridLayoutManager(this,1)
        ref.addListenerForSingleValueEvent(
            AppValueEventListener {
                val scores = mutableListOf<Score?>()
                if (it.hasChild(NODE_DIFICULT_EASY)){
                    it.child(NODE_DIFICULT_EASY).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                if (it.hasChild(NODE_DIFICULT_MEDIUM)){
                    it.child(NODE_DIFICULT_MEDIUM).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                if (it.hasChild(NODE_DIFICULT_HARD)){
                    it.child(NODE_DIFICULT_HARD).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                recyclerView.adapter = ScoreRecyclerAdapter(scores, user.getLogin())
            }
        )
    }
}
