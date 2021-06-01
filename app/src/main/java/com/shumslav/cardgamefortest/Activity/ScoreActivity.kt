package com.shumslav.cardgamefortest.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shumslav.cardgamefortest.Adapters.ScoreRecyclerAdapter
import com.shumslav.cardgamefortest.Data.Firebase.*
import com.shumslav.cardgamefortest.Data.Models.Score
import com.shumslav.cardgamefortest.Data.Models.User
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R

class ScoreActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var user: User

    private val sqlHelper = SQLiteHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        initFirebase()
        user = sqlHelper.getUser()
        val ref = REF_DATABASE_ROOT.child(NODE_SCORES).child(user.getLogin())
        recyclerView = findViewById(R.id.scores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ref.addListenerForSingleValueEvent(
            AppValueEventListener {
                val scores = mutableListOf<Score?>()
                if (it.hasChild(NODE_DIFICULT_EASY)){
                    val easy = it.child(NODE_DIFICULT_EASY).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                if (it.hasChild(NODE_DIFICULT_MEDIUM)){
                    val medium = it.child(NODE_DIFICULT_MEDIUM).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                if (it.hasChild(NODE_DIFICULT_HARD)){
                    val hard = it.child(NODE_DIFICULT_HARD).children.map { scores.add(it.getValue(Score::class.java)) }
                }
                recyclerView.adapter = ScoreRecyclerAdapter(scores, user.getLogin())
            }
        )
    }
}
