package com.example.simonkye_boggle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.simonkye_boggle.databinding.FragmentBoggleMenuBinding

class MenuFragment : Fragment() {
    private var _binding: FragmentBoggleMenuBinding? = null
    private lateinit var scoreTextView: TextView
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
            FragmentBoggleMenuBinding.inflate(layoutInflater, container, false)
        binding.apply {
            scoreTextView = scoreText
        }
        val sharedViewModel : SharedViewModel by activityViewModels()
        sharedViewModel.score.observe(viewLifecycleOwner) { score ->
            scoreTextView.text = getString(R.string.score, score)
        }

        binding.newGameButton.setOnClickListener {
            sharedViewModel.generateNewBoard()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}