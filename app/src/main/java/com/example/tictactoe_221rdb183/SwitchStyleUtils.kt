// Koda autors: Artūrs Melmanis, 6.grupa, 221RDB183, 2.kurss
package com.example.tictactoe_221rdb183

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.core.content.ContextCompat

// Stila izmaiņas
class SwitchStyleUtils {
    companion object {
        // Izmaiņa uz vecu stilu (krustiņi un nullīši)
        fun switchToOldStyle(mainActivity: MainActivity){
            if (mainActivity.switchstyle != MainActivity.SwitchStyle.Old){
                mainActivity.switchstyle = MainActivity.SwitchStyle.Old
                mainActivity.resetBoard()
                switchStylefunc(mainActivity, MainActivity.SwitchStyle.Old)
            }
        }

        // Izmaiņa uz jaunu stilu (krustiņi un nullīši)
        fun switchToNewStyle(mainActivity: MainActivity){
            if (mainActivity.switchstyle != MainActivity.SwitchStyle.New){
                mainActivity.switchstyle = MainActivity.SwitchStyle.New
                mainActivity.resetBoard()
                switchStylefunc(mainActivity, MainActivity.SwitchStyle.New)
            }
        }

        // Funkcija tiek izmantota, lai mainītu spēles laukuma pogu izskatu, atkarībā no izvēlētā stila
        private fun switchStylefunc(mainActivity: MainActivity, style: MainActivity.SwitchStyle) {
            for(button in mainActivity.boardList){
                button.text = ""
                if(style == MainActivity.SwitchStyle.Old){
                    button.background = null
                } else {
                    button.background = null
                    val symbol = button.text.toString()
                    val drawable = getStyleDrawable(mainActivity, symbol)
                    if (drawable != null) {
                        button.background = drawable
                    }
                }
            }
        }

        // Funkcija tiek izmantota, lai iegūtu attēlus (Drawable) attiecīgi no spēles laukumā izmantotā simbola
        // Funkcijā getStyleDrawable izveidota ar ChatGPT
        private fun getStyleDrawable(mainActivity: MainActivity, symbol: String): Drawable? {
            return when (symbol){
                //Šeit man pastāvīgi norādīta kāda kļūda ar failu new_nought.png, taču tā nekādā veidā neietekmē kodā darbībā
                MainActivity.NOUGHT -> ContextCompat.getDrawable(mainActivity, R.drawable.new_nought)
                MainActivity.CROSS -> ContextCompat.getDrawable(mainActivity, R.drawable.new_cross)
                else -> null
            }
        }

        //Pārbauda, vai pašreizējā šūna ir tukša, un, atkarībā no pašreizējā gājiena,
        //tiek pievienots atbilstošs simbols jaunājā stilā un mainīts pašreizējais gājiens
        fun setNewSymbol(mainActivity: MainActivity, button: Button) {
            if(button.background != null)
                return
            if(mainActivity.currentTurn == MainActivity.Turn.NOUGHT){
                //Šeit man pastāvīgi norādīta kāda kļūda ar failu new_nought.png, taču tā nekādā veidā neietekmē kodā darbībā
                button.background = ContextCompat.getDrawable(mainActivity, R.drawable.new_nought)
                button.text = MainActivity.NOUGHT
                // Diemžēl es nevarēju atbrīvoties no vecā simbola, tad funkcija checkForVictory pārstāja darboties
                // Tāpēc es izmantoju vecos simbolus kopā ar jaunajiem, bet vecos padaru neredzamus
                button.setTextColor(Color.TRANSPARENT)
                mainActivity.currentTurn = MainActivity.Turn.CROSS
            } else if(mainActivity.currentTurn == MainActivity.Turn.CROSS){
                button.background = ContextCompat.getDrawable(mainActivity, R.drawable.new_cross)
                button.text = MainActivity.CROSS
                button.setTextColor(Color.TRANSPARENT)
                mainActivity.currentTurn = MainActivity.Turn.NOUGHT
            }

            mainActivity.setTurnLabel()
        }
    }
}