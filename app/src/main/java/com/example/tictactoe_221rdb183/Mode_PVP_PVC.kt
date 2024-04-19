package com.example.tictactoe_221rdb183

import kotlin.random.Random

// Režīms mainīšana un spēle ar datoru
class Mode_PVP_PVC(private val mainActivity: MainActivity) {

    // Parāda, kads tagad režīms
    private fun setModeLabel() {
        var turnModeText = ""
        if(mainActivity.gameMode == MainActivity.GameMode.PvP)
            turnModeText = "Mode PvP"
        else if(mainActivity.gameMode == MainActivity.GameMode.PvC)
            turnModeText = "Mode PvC"

        mainActivity.binding.Mode.text = turnModeText
    }

    // Izmaiņa uz PvC mode
    fun switchToPvCMode(){
        if (mainActivity.gameMode != MainActivity.GameMode.PvC){
            mainActivity.gameMode = MainActivity.GameMode.PvC
            mainActivity.resetBoard()
            setModeLabel()
            // Dators izmanto nullīšus
            if (mainActivity.currentTurn == MainActivity.Turn.NOUGHT){
                computerMove()
            }
        }
    }

    // Izmaiņa uz PvP mode
    fun switchToPvPMode(){
        if (mainActivity.gameMode != MainActivity.GameMode.PvP){
            mainActivity.gameMode = MainActivity.GameMode.PvP
            mainActivity.resetBoard()
            setModeLabel()
        }
    }

    // Datora gājiens, kas staigā random
    fun computerMove() {
        val emptyButtons = mainActivity.boardList.filter { it.text.isEmpty() }
        if (emptyButtons.isNotEmpty()){
            val randomIndex = Random.nextInt(emptyButtons.size)
            emptyButtons[randomIndex].performClick()
        }
    }
}