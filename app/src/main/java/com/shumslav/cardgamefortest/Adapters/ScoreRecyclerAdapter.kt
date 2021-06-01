package com.shumslav.cardgamefortest.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shumslav.cardgamefortest.Data.Firebase.NODE_DIFICULT_EASY
import com.shumslav.cardgamefortest.Data.Firebase.NODE_DIFICULT_HARD
import com.shumslav.cardgamefortest.Data.Firebase.NODE_DIFICULT_MEDIUM
import com.shumslav.cardgamefortest.Data.Models.Score
import com.shumslav.cardgamefortest.R

class ScoreRecyclerAdapter(private val scores: MutableList<Score?>, private val login: String) :
    RecyclerView.Adapter<ScoreRecyclerAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var scoreName: TextView
        lateinit var scoreSteps: TextView
        lateinit var scoreTime: TextView
        lateinit var scoreDificult: TextView

        init {
            scoreName = view.findViewById(R.id.score_name)
            scoreSteps = view.findViewById(R.id.score_steps)
            scoreTime = view.findViewById(R.id.score_time)
            scoreDificult = view.findViewById(R.id.score_dificult)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.score, parent, false)
        return ScoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        if (scores[position] != null) {
            holder.scoreName.text = login
            when (scores[position]!!.getDificult()) {
                "5" -> holder.scoreDificult.text = NODE_DIFICULT_EASY
                "10" -> holder.scoreDificult.text = NODE_DIFICULT_MEDIUM
                "15" -> holder.scoreDificult.text = NODE_DIFICULT_HARD
            }
            holder.scoreTime.text = scores[position]!!.getTime()
            holder.scoreSteps.text = scores[position]!!.getSteps()
        }
    }

    override fun getItemCount(): Int {
        return scores.size
    }

}