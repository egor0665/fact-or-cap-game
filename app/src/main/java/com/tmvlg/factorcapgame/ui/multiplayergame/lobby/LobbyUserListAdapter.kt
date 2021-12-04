package com.tmvlg.factorcapgame.ui.multiplayergame.lobby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tmvlg.factorcapgame.data.repository.firebase.Player
import com.tmvlg.factorcapgame.databinding.LobbyUserBinding

class LobbyUserListAdapter(
    var onClickListener: View.OnClickListener? = null
) : ListAdapter<Player, LobbyUserViewHolder>(LobbyUserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyUserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LobbyUserBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(onClickListener)
        return LobbyUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LobbyUserViewHolder, position: Int) {
        val player = getItem(position)
        with(holder) {
            binding.userName.text = player.name
        }
    }
}
