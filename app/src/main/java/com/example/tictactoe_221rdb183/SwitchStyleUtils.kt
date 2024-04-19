package com.example.tictactoe_221rdb183

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.core.content.ContextCompat

class SwitchStyleUtils {
    companion object {
        fun switchToOldStyle(mainActivity: MainActivity){
            if (mainActivity.switchstyle != MainActivity.SwitchStyle.Old){
                mainActivity.switchstyle = MainActivity.SwitchStyle.Old
                mainActivity.resetBoard()
                switchStylefunc(mainActivity, MainActivity.SwitchStyle.Old)
            }
        }

        fun switchToNewStyle(mainActivity: MainActivity){
            if (mainActivity.switchstyle != MainActivity.SwitchStyle.New){
                mainActivity.switchstyle = MainActivity.SwitchStyle.New
                mainActivity.resetBoard()
                switchStylefunc(mainActivity, MainActivity.SwitchStyle.New)
            }
        }

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

        private fun getStyleDrawable(mainActivity: MainActivity, symbol: String): Drawable? {
            return when (symbol){
                MainActivity.NOUGHT -> ContextCompat.getDrawable(mainActivity, R.drawable.new_nought)
                MainActivity.CROSS -> ContextCompat.getDrawable(mainActivity, R.drawable.new_cross)
                else -> null
            }
        }

        fun setNewSymbol(mainActivity: MainActivity, button: Button) {
            if(button.background != null)
                return
            if(mainActivity.currentTurn == MainActivity.Turn.NOUGHT){
                button.background = ContextCompat.getDrawable(mainActivity, R.drawable.new_nought)
                button.text = MainActivity.NOUGHT
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