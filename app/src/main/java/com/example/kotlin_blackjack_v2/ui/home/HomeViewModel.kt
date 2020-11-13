package com.example.kotlin_blackjack_v2.ui.home

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text
    /*

    val B_Hit: Button
    val B_Double: Button
    val B_Stay: Button

    val TV_Main: TextView
    val TV_CPlayer: TextView
    val TV_CDealer: TextView
    val TV_Cash: TextView // At table
    val TV_Name: TextView


    val ET_Name: EditText
    val ET_CurBet: EditText

     */
}