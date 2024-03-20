package com.example.simonkye_boggle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simonkye_boggle.databinding.FragmentBoggleBoardBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class BoardFragment : Fragment() {
    private var _binding: FragmentBoggleBoardBinding? = null
    private lateinit var buttonArray: Array<Array<Button>>
    private var prevButton: Button? = null
    private val currWord = mutableListOf<Char>()
    private val generatedWords = mutableSetOf<String>()

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentBoggleBoardBinding.inflate(layoutInflater, container, false)
        val sharedViewModel : SharedViewModel by activityViewModels()
        binding.apply {
            buttonArray = arrayOf(
                arrayOf(button1, button2, button3, button4),
                arrayOf(button5, button6, button7, button8),
                arrayOf(button9, button10, button11, button12),
                arrayOf(button13, button14, button15, button16)
            )
            clearButton.setOnClickListener {
                clear()
            }
            submitButton.setOnClickListener {
                if (checkAnswer()) {
                    sharedViewModel.setWord(currWord.joinToString(""))
                    Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT).show()
                } else {
                    sharedViewModel.setWord("-1")
                }
                clear()
            }
        }

        val alphabet = ('A'..'Z').toList()
        buttonArray.forEach { row ->
            row.forEach { button ->
                val randomLetter = alphabet.random()
                button.text = randomLetter.toString()
                button.setOnClickListener {
                    if (prevButton == null) {
                        currWord.add(button.text.single())
                        binding.currentText.text = currWord.toString()
                        prevButton = button
                        button.isEnabled = false
                    } else {
                        val r = getButtonRow(button)
                        val c = getButtonCol(button)
                        val prevR = getButtonRow(prevButton)
                        val prevC = getButtonCol(prevButton)
                            // left right
                        if ((r == prevR && (c == prevC - 1 || c == prevC + 1))
                            //up down
                            || (c == prevC && (r == prevR - 1 || r == prevR + 1))
                            //diagonal
                            || (c == prevC + 1 && r == prevR + 1)
                            || (c == prevC + 1 && r == prevR - 1)
                            || (c == prevC - 1 && r == prevR + 1)
                            || (c == prevC - 1 && r == prevR - 1)) {

                            currWord.add(button.text.single())
                            binding.currentText.text = currWord.toString()

                            prevButton = button
                            button.isEnabled = false
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun clear() {
        buttonArray.forEach { row ->
            row.forEach { button ->
                button.isEnabled = true
            }
        }
        prevButton = null
        currWord.clear()
        binding.currentText.text = currWord.toString()
    }
    private fun checkAnswer() : Boolean {
        val inputStream = requireContext().assets.open("words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val words = reader.readLines().map { it.uppercase() }
        val inputWord = currWord.joinToString("")
        if (words.contains(inputWord)) {
            if (!generatedWords.contains(inputWord)) {
                val vowels = setOf('A', 'E', 'I', 'O', 'U')
                if (inputWord.count {it in vowels} >= 2) {
                    if (inputWord.length >= 4) {
                        generatedWords.add(inputWord)
                        return true
                    } else {
                        Toast.makeText(requireContext(), "Incorrect! (Isn't longer than 4 characters)", Toast.LENGTH_SHORT).show()
                        return false
                    }

                } else {
                    Toast.makeText(requireContext(), "Incorrect! (Doesn't contain more than 2 vowels)", Toast.LENGTH_SHORT).show()
                    return false
                }
            } else {
                Toast.makeText(requireContext(), "Incorrect! (You already guessed this word)", Toast.LENGTH_SHORT).show()
                return false
            }
        } else {
            Toast.makeText(requireContext(), "Incorrect! (Not a valid word)", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun getButtonRow(button: Button?) : Int {
        if (button == null) {
            return -1
        }
        buttonArray.forEachIndexed { rowIndex, row ->
            if (row.contains(button)) {
                return rowIndex
            }
        }
        return -1
    }

    private fun getButtonCol(button: Button?): Int {
        if (button == null) {
            return -1
        }
        buttonArray.forEach { row ->
            row.forEachIndexed { colIndex, btn ->
                if (btn == button) {
                    return colIndex
                }
            }
        }
        return -1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}