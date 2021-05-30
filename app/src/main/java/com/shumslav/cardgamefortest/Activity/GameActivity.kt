package com.shumslav.cardgamefortest.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
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
import androidx.core.graphics.drawable.toBitmap
import com.shumslav.cardgamefortest.Data.Models.Card
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.listOfImages
import com.shumslav.cardgamefortest.makeToast
import java.util.*

class GameActivity : AppCompatActivity() {
    private var dificult = 10
    private var volumeLevel = 100
    private var isCreated = false
    private var firstCheckedCard:Card? = null
    private var secondCheckedCard:Card? = null
    private var countOfCard = 0
    private var steps = 0
    private var countFound = 0
    private val sqlHelper = SQLiteHelper(this)
    private var allCardOnBoard = mutableListOf<Card>()

    private lateinit var cardBoard: TableLayout
    private lateinit var textSteps: TextView
    private lateinit var imagesId: MutableList<Int>
    private lateinit var settings:SettingsApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        textSteps= findViewById(R.id.countSteps)
        cardBoard = findViewById(R.id.cardBoard)
        settings= sqlHelper.getSettings()
        dificult = settings.dificult.toInt()
        volumeLevel = settings.volumeLevel.toInt()
        imagesId = mutableListOf()
        makeToast(this, listOfImages.size.toString())
        val unusedImagesList = mutableListOf<Int>()
        listOfImages.forEach { unusedImagesList.add(it) }
        for (i in 1..dificult){
            val randomId = unusedImagesList.random()
            unusedImagesList.remove(randomId)
            imagesId.add(randomId)
            imagesId.add(randomId)
        }
        makeToast(this, imagesId.size.toString())
    }




    override fun onResume() {
        super.onResume()
        if(!isCreated) {
            for (i in 0..(dificult*2/5)-1) {
                val row = TableRow(this)
                row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                for (j in 0..4) {
                    countOfCard+=1
                    val card = makeCard(i,j)
                    allCardOnBoard.add(card)
                    row.addView(card.image)
                }
                cardBoard.addView(row, i)
            }
            isCreated = true
        }
    }

    private fun checkImages():Boolean{
        return firstCheckedCard!!.image.tag == secondCheckedCard!!.image.tag
    }

    private fun makeCard(x:Int, y:Int):Card{
        val imageId = imagesId.random()
        imagesId.remove(imageId)
        val cardImage = ResourcesCompat.getDrawable(resources, R.drawable.card, theme)
        val image = ResourcesCompat.getDrawable(resources, imageId, theme)
        val cardWithImage = TransitionDrawable(arrayOf(cardImage, image))
        val cardWithImageView = ImageView(this)
        cardWithImageView.tag = imageId
        cardWithImageView.setImageDrawable(cardWithImage)
        val card = Card(cardWithImageView, Point(x,y))
        card.image.setOnClickListener {
            if (!card.isFind) {
                val transition = card.image.drawable as TransitionDrawable
                if (firstCheckedCard == null) {
                    steps+=1
                    allCardOnBoard.forEach { val trans = it.image.drawable as TransitionDrawable
                    trans.resetTransition()}
                    firstCheckedCard = card
                    transition.startTransition(0)
                } else {
                    if (!card.equelsPoint(firstCheckedCard!!)) {
                        steps+=1
                        if (secondCheckedCard == null) {
                            secondCheckedCard = card
                            transition.startTransition(0)
                            if (checkImages()) {
                                firstCheckedCard!!.image.visibility = View.INVISIBLE
                                secondCheckedCard!!.image.visibility = View.INVISIBLE
                                firstCheckedCard!!.isFind = true
                                secondCheckedCard!!.isFind = true
                                firstCheckedCard = null
                                secondCheckedCard = null
                                countFound+=1
                                if (countFound==dificult)
                                    startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                val transitionOtherCard = firstCheckedCard!!.image.drawable as TransitionDrawable
                                transition.reverseTransition(1000)
                                transitionOtherCard.reverseTransition(1000)
                                firstCheckedCard = null
                                secondCheckedCard = null
                            }
                        }
                    }
                }
                textSteps.text = steps.toString()
            }
        }
        return card
    }
}