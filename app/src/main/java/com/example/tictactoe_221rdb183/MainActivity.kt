// Koda autors: Artūrs Melmanis, 6.grupa, 221RDB183, 2.kurss
// Šis kods bija izveidots ar video tutoriāla palīdzību (https://youtu.be/POFvcoRo3Vw?si=ARZbnPQZugcWDvk5)
// Pēc tam kods tika papildināts ar jaunājam funkcijam.

package com.example.tictactoe_221rdb183

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tictactoe_221rdb183.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Definē divas vērtības: NOUGHT un CROSS (Nullīši un krustiņi)
    enum class Turn{
        NOUGHT,
        CROSS
    }

    //Definē divas vērtības: PvP un PvC (Režīmi)
    enum class GameMode{
        PvP,
        PvC
    }

    //Definē divas vērtības: Old un New (Krustiņu un nullīšu stils)
    enum class SwitchStyle{
        Old,
        New
    }

    // Es sākotnēji noteicu, ka pirmais gājiens ir krustiņu izvēlē
    var firstTurn = Turn.CROSS

    // Es sākotnēji noteicu, kura spēlētāja gājiens ir pašlaik
    var currentTurn = Turn.CROSS

    // Es sākotnēji noteicu, kads ir režīms spēles sākumā
    var gameMode = GameMode.PvP

    // Es sākotnēji noteicu, kads ir krustiņu un nullīšu stils spēles sākumā
    var switchstyle = SwitchStyle.Old

    // Spēlētāju uzvaru skaits
    private var crossesScore = 0
    private var noughtsScore = 0

    // Uzvaras es sākotnēji vērtēju kā negatīvu
    //Tas ir nepieciešams, lai uzvarētājam netiktu piešķirti papildu punkti
    private var noughtsWon = false
    private var crossesWon = false

    // Saraksts, lai glabātu visas pogas (Button) spēles laukumā
    var boardList = mutableListOf<Button>()

    // Mainīgais, lai saistītu darbības skatu ar kodu, kas atbilst XML izkārtojumam
    lateinit var binding: ActivityMainBinding

    // Mainīgais,lai glabātu spēlētāja vārdu
    private lateinit var playerName: String

    // Mainīgais, lai saistītu ar režīma vadību
    private lateinit var modePVP_PVC: Mode_PVP_PVC

    // Sākuma funkcija, lai inicializētu aktivitāti, saistītu izkārtojumu ar kodu, inicializē spēles laukumu un spēli,
    // izveidotu spēles režīma pārvaldītāju un pievienotu klausītājus pogām, pārslēgtu spēles režīmus un stila izvēles iespējas
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

    // Izsauc dialoga logu
    private fun initGame() {
        showNameDialog()
    }

    // Pieprasa no lietotāja ievadīt savu vārdu, kas tiks izmantots, lai pielāgotu personificētu sveicienu ziņojumu
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

    // Pielāgota sveiciena ziņa
    private fun setGreetingMessage(playerName: String){
        val greetingMessage = "Welcome $playerName to Tic Tac Toe Game!"
        binding.textView.text = greetingMessage
    }

    // Funkcija, lai inicializētu spēles laukumu
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

    // Funkcija, kas izsaukta, kad lietotājs noklikšķina uz kādas no pogām spēles laukumā
    fun boardTapped(view: View){
        if(view !is Button)
            return

        // Aizpilda tukšos šūnus
        if (view.text.isEmpty()){
            if (switchstyle == SwitchStyle.Old){
                addToBoard(view)
            } else{
                setNewSymbol(view)
            }
        }

        // Pārbauda uzvaru "nulles" simbolu gadījumā un, ja runa ir par PvC režīmu, pārbauda uzvaru datora gadījumā
        if(checkForVictory(NOUGHT)){
            noughtsScore++
            noughtsWon = true
            if (gameMode == GameMode.PvC){
                result("Computer win!")
            } else
                result("Noughts win!")
        }

        // Pārbauda uzvaru "krustiņi" simbolu gadījumā un, ja runa ir par PvC režīmu, pārbauda uzvaru lietotāja gadījumā
        if(checkForVictory(CROSS)){
            crossesScore++
            crossesWon = true
            if (gameMode == GameMode.PvC){
                result("You win!")
            } else
                result("Crosses Win!")
        }

        // Pārbauda spēles izlozi
        if(fullBoard()){
            result("Draw")
            return
        }

        // Datora gājiens un lieku gājienu novēršana
        if (gameMode == GameMode.PvC && currentTurn == Turn.NOUGHT && !noughtsWon && !crossesWon) {
            modePVP_PVC.computerMove()
        }
    }

    // Uzvaras pārbaudes funkcija
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

    // Šūnas simbola klātbūtnes noteikšanai
    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    // Ziņojums par uzvaru un pašreizējo punktu skaitu
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

    // Funkcija laukuma attīrīšanai spēlē un sākotnējo datu piešķiršana
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

    // Pārbauda spēles izlozi
    private fun fullBoard(): Boolean {
        for(button in boardList){
            if(button.text == "")
                return false
        }
        return true
    }

    //Pārbauda, vai pašreizējā šūna ir tukša, un, atkarībā no pašreizējā gājiena,
    //tiek pievienots atbilstošs simbols un mainīts pašreizējais gājiens
    private fun addToBoard(button: Button) {
        if(button.text != "")
            return
        if(currentTurn == Turn.NOUGHT){
            button.text = NOUGHT
            currentTurn = Turn.CROSS
            button.setTextColor(Color.BLACK)
        }
        else if(currentTurn == Turn.CROSS){
            button.text = CROSS
            currentTurn = Turn.NOUGHT
            button.setTextColor(Color.BLACK)
        }
        setTurnLabel()
    }

    // Parāda, kam tagad gājiens
    fun setTurnLabel() {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Turn $CROSS"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Turn $NOUGHT"

        binding.turn.text = turnText
    }

    // Izmaiņa uz PvP mode
    private fun switchToPvPMode(){
        modePVP_PVC.switchToPvPMode()
    }

    // Izmaiņa uz PvC mode
    private fun switchToPvCMode(){
        modePVP_PVC.switchToPvCMode()
    }

    // Izmaiņa uz vecu stilu (krustiņi un nullīši)
    private fun switchToOldStyle(){
        SwitchStyleUtils.switchToOldStyle(this)
    }

    // Izmaiņa uz jaunu stilu (krustiņi un nullīši)
    private fun switchToNewStyle(){
        SwitchStyleUtils.switchToNewStyle(this)
    }

    // Simbola piešķiršana jaunam stilam
    private fun setNewSymbol(button: Button) {
        SwitchStyleUtils.setNewSymbol(this, button)
    }

    // Simboli
    companion object{
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

}