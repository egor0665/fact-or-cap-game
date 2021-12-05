package com.tmvlg.factorcapgame.ui.multiplayergame.lobby.find

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tmvlg.factorcapgame.FactOrCapApplication
import com.tmvlg.factorcapgame.R
import com.tmvlg.factorcapgame.data.repository.firebase.Lobby
import com.tmvlg.factorcapgame.databinding.FragmentFindLobbyBinding
import com.tmvlg.factorcapgame.databinding.LobbyBinding
import com.tmvlg.factorcapgame.ui.MainActivity
import com.tmvlg.factorcapgame.ui.menu.MenuFragment
import com.tmvlg.factorcapgame.ui.multiplayergame.lobby.LobbyFragment

class FindLobbyFragment : Fragment() {

    private val viewModel: FindLobbyViewModel by viewModels {
        // inits viewmodel
        val app = activity?.application as FactOrCapApplication
        return@viewModels FindLobbyViewModelFactory(
            app.firebaseLobbyRepository
        )
    }

    private var _binding: FragmentFindLobbyBinding? = null

    private val binding: FragmentFindLobbyBinding
        get() = _binding ?: throw IllegalStateException("null binding at $this")

    private var isEnabled: Boolean
        get() = binding.root.isEnabled
        set(value) {
            binding.root.isEnabled = value
        }

    private val lobbyListAdapter = LobbyListAdapter(
        object : LobbyListAdapter.OnSelectedListener {
            override fun onSelected(binding: LobbyBinding, isSelected: Boolean) {
                with(binding.lobbyItem) {
    private val lobbyListSection = ListSection<Lobby>()
    private val lobbyListAdapter = newLobbyListAdapter(
        object : LobbyBinder.OnLobbySelectedListener {
            override fun onLobbySelected(binding: LobbyBinding, isSelected: Boolean) {
                with(binding.bgLayout) {
                    Log.d("FindLobby", "OnSelect: $isSelected")
                    if (isSelected) {
                        setSelected(true)
                    } else {
                        setSelected(false)
                    }
                }
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindLobbyBinding.inflate(inflater, container, false)

        binding.pageContainer.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.fragment_change
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findLobbyRecyclerview.adapter = lobbyListAdapter
        binding.findLobbyRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.returnButton.isSoundEffectsEnabled = false
        binding.joinButton.isSoundEffectsEnabled = false

        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupListeners() {
        binding.returnButton.setOnClickListener {
            (activity as MainActivity).snapSEStart()
            viewModel.stopListenLobbies()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, MenuFragment())
                .commit()
        }
        binding.joinButton.setOnClickListener {
            (activity as MainActivity).snapSEStart()
            isEnabled = false
            val selectedPosition = lobbyListAdapter.selectedPosition
            if (selectedPosition == null) {
                Toast.makeText(context, "Select lobby!", Toast.LENGTH_SHORT).show()
                isEnabled = true
                return@setOnClickListener
            }

            viewModel.connectLobby(lobbyListAdapter.currentList[selectedPosition])
        }
        binding.findLobbyEdittext.addTextChangedListener { text ->
            filterText(textString = text?.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.lobbies.observe(viewLifecycleOwner) { lobbies ->
            filterText(lobbies)
        }
        viewModel.connectedLobbyId.observe(viewLifecycleOwner) { lobbyId ->
            if (lobbyId == null) {
                Toast.makeText(context, "Select lobby!", Toast.LENGTH_SHORT).show()
                return@observe
            }

            viewModel.stopListenLobbies()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_fragment_container,
                    LobbyFragment.newInstance(lobbyId)
                )
                .commit()
        }
    }

    private fun filterText(lobbyList: List<Lobby>? = null, textString: String? = null) {
        val text = textString ?: binding.findLobbyEdittext.text?.toString() ?: return
        val lobbies = lobbyList ?: viewModel.lobbies.value ?: return
        val regex = text.toRegex(RegexOption.IGNORE_CASE)
        lobbyListAdapter.submitList(
            lobbies.filter { lobby ->
                return@filter regex.containsMatchIn(lobby.name)
            }
        )
    }

    companion object {
        fun newInstance(): FindLobbyFragment {
            return FindLobbyFragment()
        }
    }
}
