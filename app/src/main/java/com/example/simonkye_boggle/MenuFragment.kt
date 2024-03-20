package com.example.simonkye_boggle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.simonkye_boggle.databinding.FragmentBoggleMenuBinding

class MenuFragment : Fragment() {
    private var _binding: FragmentBoggleMenuBinding? = null
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

        val sharedViewModel : SharedViewModel by activityViewModels()
        sharedViewModel.word.observe(viewLifecycleOwner) { word ->
            if (word != "-1") {
                Toast.makeText(requireContext(), "Received word: $word", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Deducting Points", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}