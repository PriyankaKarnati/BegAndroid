package com.example.timetapper



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog



class MainActivity : AppCompatActivity() {
    internal var scores:Int = 0
    internal lateinit var tapMeButton: Button //properties
    internal lateinit var gameScoreText: TextView
    internal lateinit var timerLeftText: TextView
    //internal lateinit var menu: Menu
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var CDval : Long = 20000
    internal var CDinterval : Long = 1000
    internal var timeLeft : Long = 20000
    companion object{
        private val TAG = MainActivity::class.java.simpleName
        private const val  SCORE_LEFT = "SCORE_LEFT"
        private const val TIME_LEFT = "TIME_LEFT"//we need to have a property that hold's it
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"onCreate called.Score is: $scores")
        /* gameScoreText.text="0" */
        this.tapMeButton = findViewById(R.id.tap_me_button)
        this.gameScoreText = findViewById(R.id.Display_score)
        this.timerLeftText = findViewById(R.id.Display_time_left)

        tapMeButton.setOnClickListener{ view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }
        if(savedInstanceState!=null){
            scores = savedInstanceState.getInt(SCORE_LEFT)
            timeLeft= savedInstanceState.getLong(TIME_LEFT)
            restoreGame()
        }
        else{
            resetGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu,menu)


        //this.registerForContextMenu(findViewById(R.id.actionMenu))
        //Log.d(TAG, "showInfor called")
        //super.onCreateOptionsMenu(menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return when (item.itemId) {
            R.id.actionMenu->{
                Log.d(TAG,"showInfor 3 called")
                showInfo()
                return true
            }
            else->super.onContextItemSelected(item)
        }

//        Log.d(TAG,"showInfor 2 called")
//
//            if(item.itemId==R.id.actionMenu){
//                Log.d(TAG,"showInfor 3 called")
//                showInfo()
//            }
//            else{
//            super.onContextItemSelected(item)
//            }
//        return true
    }

    private fun showInfo(){

        Log.d(TAG,"showInfor 4 called")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.aboutTitle)
        builder.setMessage(R.string.aboutMessage)
        Log.d(TAG,"cant figure out")
        builder.show()
        Log.d(TAG,"help me out")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Log.d(TAG,"onSave called")
        outState.putInt(SCORE_LEFT,scores)
        outState.putLong(TIME_LEFT,timeLeft)
        countDownTimer.cancel()
        //onCreate(savedInstanceState = outState)
        Log.d(TAG,"onSave score:$scores and time : $timeLeft called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy called")
    }
    private fun incrementScore() {
        if(!gameStarted){
            startGame()
        }
        scores+=1
        val newScore = getString(R.string.score,scores)
        gameScoreText.text  = newScore

        val blinkAnim = AnimationUtils.loadAnimation(this,R.anim.blink)
        gameScoreText.startAnimation(blinkAnim)
    }

    private fun resetGame(){
        scores = 0
        gameScoreText.text = getString(R.string.score,scores)

        timerLeftText.text = getString(R.string.time_left,CDval/CDinterval)
        //val blinkAni = AnimationUtils.loadAnimation(this,R.anim.blink)

        countDownTimer = object :CountDownTimer(CDval,CDinterval){
            override fun onTick(millisUntilFinished: Long) {
                //timerLeftText.startAnimation(blinkAni)
                timeLeft = millisUntilFinished
                timerLeftText.text = getString(R.string.time_left,millisUntilFinished/CDinterval)

            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false

    }

    private fun restoreGame(){
        gameScoreText.text = getString(R.string.score,scores)
        timerLeftText.text = getString(R.string.time_left,timeLeft/CDinterval)

        countDownTimer = object :CountDownTimer(timeLeft,CDinterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                timerLeftText.text=getString(R.string.time_left,millisUntilFinished/CDinterval)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = true
        countDownTimer.start()
    }

    private fun startGame(){
        gameStarted = true
        //start the timer
        countDownTimer.start()
    }

    private fun endGame(){
        Toast.makeText(this,getString(R.string.timeUp,scores),Toast.LENGTH_LONG).show()
        resetGame()
    }

}
