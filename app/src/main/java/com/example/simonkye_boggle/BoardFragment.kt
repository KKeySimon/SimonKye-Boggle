package com.example.simonkye_boggle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.simonkye_boggle.databinding.FragmentBoggleBoardBinding

class BoardFragment : Fragment() {
    private var _binding: FragmentBoggleBoardBinding? = null
    private lateinit var buttonArray: Array<Array<Button>>
    private var prevButton: Button? = null
    private val currWord = mutableListOf<Char>()

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
        binding.apply {
            buttonArray = arrayOf(
                arrayOf(button1, button2, button3, button4),
                arrayOf(button5, button6, button7, button8),
                arrayOf(button9, button10, button11, button12),
                arrayOf(button13, button14, button15, button16)
            )
            clearButton.setOnClickListener {
                buttonArray.forEach { row ->
                    row.forEach { button ->
                        button.isEnabled = true
                    }
                }
                prevButton = null
                currWord.clear()
                binding.currentText.text = currWord.toString()
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