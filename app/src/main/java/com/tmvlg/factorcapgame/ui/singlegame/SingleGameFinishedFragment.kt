package com.tmvlg.factorcapgame.ui.singlegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tmvlg.factorcapgame.FactOrCapApplication
import com.tmvlg.factorcapgame.databinding.FragmentSingleGameBinding
import com.tmvlg.factorcapgame.databinding.FragmentSingleGameFinishedBinding

class SingleGameFinishedFragment : Fragment() {

    private lateinit var viewModel: SingleGameViewModel

    private var _binding: FragmentSingleGameFinishedBinding? = null
    private val binding: FragmentSingleGameFinishedBinding
        get() = _binding ?: throw IllegalStateException("null binding at $this")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleGameFinishedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            (activity?.application as FactOrCapApplication).singleGameViewModelFactory
        )[SingleGameViewModel::class.java]
    }

    companion object {

        fun newInstance(): SingleGameFinishedFragment {
            return SingleGameFinishedFragment()
        }
    }
}
