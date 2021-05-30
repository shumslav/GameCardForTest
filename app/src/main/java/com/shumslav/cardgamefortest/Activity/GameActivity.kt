package com.shumslav.cardgamefortest.Activity

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
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
        dificult = Pair(5,5)
    }


    override fun onResume() {
        super.onResume()
        val cardBoard = findViewById<TableLayout>(R.id.cardBoard)
        if(!isCreated) {
            for (i in 0..dificult.first - 1) {
                val row = TableRow(this)
                row.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                for (j in 0..dificult.second - 1) {
                    val card = ResourcesCompat.getDrawable(resources, R.drawable.card, theme)
                    val image = ResourcesCompat.getDrawable(resources, R.drawable.airplane, theme)
                    val cardWithImage = TransitionDrawable(arrayOf(card, image))
                    val cardWithImageView = ImageView(this)
                    cardWithImageView.setImageDrawable(cardWithImage)
//                    val params = LinearLayout.LayoutParams(10,10)
//                    cardWithImageView.layoutParams = params
                    val trans = cardWithImageView.drawable as TransitionDrawable
                    cardWithImageView.setOnClickListener { trans.resetTransition() }
                    row.addView(cardWithImageView)
                }
                cardBoard.addView(row, i)
            }
            isCreated = true
        }
    }
}