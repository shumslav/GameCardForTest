package com.shumslav.cardgamefortest.Activity

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.makeToast

class GameActivity : AppCompatActivity() {
    private lateinit var dificult: Pair<Int, Int>
    private lateinit var size: Size
    private lateinit var linearLayout: LinearLayout
    private var isCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        dificult = Pair(5, 5)
        linearLayout = findViewById(R.id.game_layout)
    }


    override fun onResume() {
        super.onResume()
        val cardBoard = findViewById<TableLayout>(R.id.cardBoard)
        size = Size(linearLayout.layoutParams.width, linearLayout.layoutParams.height)
        val widthCard = size.width/dificult.first
        val heightCard = size.height/dificult.second
        if(!isCreated) {
            for (i in 0..dificult.first - 1) {
                val row = TableRow(this)
                row.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                for (j in 0..dificult.second - 1) {
                    val a = ResourcesCompat.getDrawable(resources, R.drawable.card1, theme)
                    val b = ResourcesCompat.getDrawable(resources, R.drawable.airplane, theme)
                    val card1 = TransitionDrawable(arrayOf(a, b))
                    val card = ImageView(this)
                    card.setImageDrawable(card1)
                    val trans = card.drawable as TransitionDrawable
                    card.setOnClickListener { trans.startTransition(1000) }
                    row.addView(card)
                }
                cardBoard.addView(row, i)
            }
            isCreated = true
        }
    }
}