package com.hafiztaruligani.gamesday.presentation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.hafiztaruligani.gamesday.R

class LoadingBar(context: Context, private val back:()->Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_bar)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun state(value : Boolean){
        if (value) this@LoadingBar.show()
        else this@LoadingBar.dismiss()
    }

}