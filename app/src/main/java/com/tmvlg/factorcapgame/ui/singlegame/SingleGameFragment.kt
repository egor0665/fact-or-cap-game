package com.tmvlg.factorcapgame.ui.singlegame

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tmvlg.factorcapgame.FactOrCapApplication
import com.tmvlg.factorcapgame.R
import com.tmvlg.factorcapgame.databinding.FragmentSingleGameBinding

class SingleGameFragment : Fragment() {

    private lateinit var viewModel: SingleGameViewModel

    private var _binding: FragmentSingleGameBinding? = null
    private val binding: FragmentSingleGameBinding
        get() = _binding ?: throw IllegalStateException("null binding at $this")

    val singleGameViewModelFactory by lazy {
        SingleGameViewModelFactory(
            (activity?.application as FactOrCapApplication).gameRepository,
            (activity?.application as FactOrCapApplication).factRepository,
            (activity?.application as FactOrCapApplication).userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleGameBinding.inflate(inflater, container, false)

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
            singleGameViewModelFactory
        )[SingleGameViewModel::class.java]

        observeViewModel()

        binding.agreeButton.setOnClickListener {
            viewModel.sendAnswer(true)
        }

        binding.disagreeButton.setOnClickListener {
            viewModel.sendAnswer(false)
        }
    }

    private fun observeViewModel() {

        viewModel.gameFinished.observe(viewLifecycleOwner) {
            Log.d("1", "observeViewModel: game finished")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, SingleGameFinishedFragment.newInstance())
                .commit()
        }
        viewModel.fact.observe(viewLifecycleOwner) {
            binding.tvFact.text = it.text
        }
        viewModel.rightAnswersCount.observe(viewLifecycleOwner) {
            binding.tvScore.text = it.toString()
        }
    }
}
