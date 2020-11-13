package com.example.kotlin_blackjack_v2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    val _Cash: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>(100)
    }
    val _MinBet: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>(100)
    }
    val _UseCash: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>(true)
    }

    val _PName: MutableLiveData<String> by lazy{
        MutableLiveData<String>("Player1")
    }
    val _CashScaler: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>(4)
    }

    var Cash: Int = 500
    var MinBet: Int = 100
    var UseCash: Boolean = true
    var PName: String = "Player1"
    var CashScaler: Int = 4
    //var GameState: Int = 0 // I want to make it forget the state each time I leave the table.
}