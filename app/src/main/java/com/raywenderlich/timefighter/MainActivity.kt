package com.raywenderlich.timefighter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    //score counter
    private var score = 0

    //assigning class TAG
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView

    private var gameStarted = false
    //countdown timer object and math
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60

    // tracks the variables to save when rotating
    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    private lateinit var tapMeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //message logged when activity is created
        Log.d(TAG, "onCreate called. Score is: $score")

        // links views with these names to the variable
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        //click listener on incrementScore
        tapMeButton.setOnClickListener { incrementScore() }

        //checking if savedInstanceState contains a value and saving from bundle if it does
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }


    }

    // passes in the current score when rotated instead of refreshing
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeft")
    }
    //calling onDestroy for memory cleanup
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

        //a method to start the game when the tapMeButton is tapped and increment the score
    private fun incrementScore() {
        if (!gameStarted){
            startGame()
        }

        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        // set score to 0 and reset score and time left from strings.xml
        score = 0
        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore
        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft
        // create new countdown and pass in the timer math
        countDownTimer = object : CountDownTimer(
            initialCountDown,
            countDownInterval
        ) {
            // Calls onTick every second for 60 seconds and updates how many their are left
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(
                    R.string.time_left,
                    timeLeft
                )
                timeLeftTextView.text = timeLeftString
            }
            //ends game
            override fun onFinish() {
                endGame()
            }
        }
        //game has not started
        gameStarted = false
    }
    //starts the game and countdown
    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }
    //ends the game, displays the score, and called reset
    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message,
            score), Toast.LENGTH_LONG).show()
        resetGame()
    }
    //sets up the TextViews and countDownTimer using the Bundle
    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore
        val restoredTime = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTime
        countDownTimer = object : CountDownTimer((timeLeft *
                1000).toLong(), countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left,
                    timeLeft)
                timeLeftTextView.text = timeLeftString
            }
            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }
}
