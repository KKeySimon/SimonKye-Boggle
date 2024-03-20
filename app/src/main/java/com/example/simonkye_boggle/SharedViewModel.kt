package com.example.simonkye_boggle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> = _word

    fun setWord(word: String) {
        _word.value = word
    }
}