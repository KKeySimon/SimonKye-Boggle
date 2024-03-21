package com.example.simonkye_boggle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> = _word

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _boardState = MutableLiveData<Array<Array<String>>>()
    val boardState: LiveData<Array<Array<String>>> = _boardState

    private val _buttonStates = MutableLiveData<Array<Array<Boolean>>>()
    val buttonStates: LiveData<Array<Array<Boolean>>> = _buttonStates

    private val _currentWord = MutableLiveData<String>()
    val currentWord: LiveData<String> = _currentWord

    private val _generatedWords = MutableLiveData<Set<String>>()
    val generatedWords: LiveData<Set<String>> = _generatedWords

    init {
        generateNewBoard()
    }

    fun setGeneratedWords(words: Set<String>) {
        _generatedWords.value = words
    }

    fun setCurrentWord(word: String) {
        _currentWord.value = word
    }

    fun setButtonStates(states: Array<Array<Boolean>>) {
        _buttonStates.value = states
    }

    fun setBoardState(state: Array<Array<String>>) {
        _boardState.value = state
    }

    fun setWord(word: String) {
        _word.value = word
    }

    fun generateNewBoard() {
        val alphabet = ('A'..'Z').toList()
        val newBoard = Array(4) { Array(4) { "" } }
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val randomLetter = alphabet.random()
                newBoard[i][j] = randomLetter.toString()
            }
        }

        _boardState.value = newBoard
        _buttonStates.value = Array(4) { Array(4) { true } }
        _currentWord.value = ""
        _generatedWords.value = emptySet()
        _score.value = 0
    }

    fun updateScore(word: String, isCorrect: Boolean) {
        var currScore = _score.value ?: 0
        if (isCorrect) {
            val consonants = word.count { it in "BCDFGHJKLMNPQRSTVWXYZ" }
            val vowels = word.count { it in "AEIOU" }
            val specialConsonants = word.count { it in "SZPXQ" }
            var points = consonants + (vowels * 5)
            if (specialConsonants > 0) {
                points *= 2
            }
            currScore += points
        } else {
            currScore -= 10
        }
        if (currScore >= 0) {
            _score.value = currScore
        } else {
            _score.value = 0
        }
    }
}