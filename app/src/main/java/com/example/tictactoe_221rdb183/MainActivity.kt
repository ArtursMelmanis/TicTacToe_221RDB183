package com.example.tictactoe_221rdb183

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tictactoe_221rdb183.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class Turn{
        NOUGHT,
        CROSS
    }

    enum class GameMode{
        PvP,
        PvC
    }

    enum class SwitchStyle{
        Old,
        New
    }

    private var firstTurn = Turn.CROSS
    var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var noughtsWon = false
    private var crossesWon = false

    var boardList = mutableListOf<Button>()

    lateinit var binding: ActivityMainBinding
    private lateinit var playerName: String
    private lateinit var modePVP_PVC: Mode_PVP_PVC
    var gameMode = GameMode.PvP
    var switchstyle = SwitchStyle.Old

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
        initGame()

        modePVP_PVC = Mode_PVP_PVC(this)

        binding.pvp.setOnClickListener { switchToPvPMode() }
        binding.pvc.setOnClickListener { switchToPvCMode() }
        binding.oldButton.setOnClickListener { switchToOldStyle() }
        binding.newButton.setOnClickListener { switchToNewStyle() }
    }

    private fun initGame() {
        showNameDialog()
    }

    private fun showNameDialog() {
        val nameEditText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Enter Your Name")
            .setView(nameEditText)
            .setPositiveButton("Start Game") { _,_ ->
                playerName = nameEditText.text.toString().trim()
                setGreetingMessage(playerName)
            }
            .setCancelable(false)
            .show()
    }

    private fun setGreetingMessage(playerName: String){
        val greetingMessage = "Welcome $playerName to Tic Tac Toe Game!"
        binding.textView.text = greetingMessage
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    fun boardTapped(view: View){
        if(view !is Button)
            return

        if (view.text.isEmpty()){
            if (switchstyle == SwitchStyle.Old){
                addToBoard(view)
            } else{
                setNewSymbol(view)
            }
        }


        if(checkForVictory(NOUGHT)){
            noughtsScore++
            noughtsWon = true
            if (gameMode == GameMode.PvC){
                result("Computer win!")
            } else
                result("Noughts win!")
        }

        if(checkForVictory(CROSS)){
            crossesScore++
            crossesWon = true
            if (gameMode == GameMode.PvC){
                result("You win!")
            } else
                result("Crosses Win!")
        }

        if(fullBoard()){
            result("Draw")
        }

        if (gameMode == GameMode.PvC && currentTurn == Turn.NOUGHT && !noughtsWon && !crossesWon) {
            modePVP_PVC.computerMove()
        }
    }

    private fun checkForVictory(s: String): Boolean {
        if(match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if(match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if(match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        if(match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if(match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if(match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        if(match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if(match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true

        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String) {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset"){
                _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()

    }

    fun resetBoard() {
        for(button in boardList){
            button.text = ""
            button.background = null
        }
        if (firstTurn == Turn.NOUGHT)
            firstTurn = Turn.CROSS
        else if(firstTurn == Turn.CROSS)
            firstTurn = Turn.NOUGHT

        currentTurn = firstTurn
        setTurnLabel()

        crossesWon = false
        noughtsWon = false
    }

    private fun fullBoard(): Boolean {
        for(button in boardList){
            if(button.text == "")
                return false
        }
        return true
    }

    private fun addToBoard(button: Button) {
        if(button.text != "")
            return
        if(currentTurn == Turn.NOUGHT){
            button.text = NOUGHT
            currentTurn = Turn.CROSS
        }
        else if(currentTurn == Turn.CROSS){
            button.text = CROSS
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    fun setTurnLabel() {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"

        binding.turn.text = turnText
    }

    private fun switchToPvPMode(){
        modePVP_PVC.switchToPvPMode()
    }

    private fun switchToPvCMode(){
        modePVP_PVC.switchToPvCMode()
    }

    private fun switchToOldStyle(){
        SwitchStyleUtils.switchToOldStyle(this)
    }

    private fun switchToNewStyle(){
        SwitchStyleUtils.switchToNewStyle(this)
    }

    private fun setNewSymbol(button: Button) {
        SwitchStyleUtils.setNewSymbol(this, button)
    }

    companion object{
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

}