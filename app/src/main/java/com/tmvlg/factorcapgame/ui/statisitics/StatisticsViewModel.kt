package com.tmvlg.factorcapgame.ui.statisitics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.annotation.SuppressLint
import android.app.Application
import com.tmvlg.factorcapgame.data.preferences.PreferenceProvider
import com.tmvlg.factorcapgame.data.repository.user.Statistics
import com.tmvlg.factorcapgame.data.repository.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsViewModel(userRepository: UserRepository) : ViewModel() {
    
    private val _totalGames = MutableLiveData<Int>()
    val totalGames = _totalGames.map { it }

    private val _highestScore = MutableLiveData<Int>()
    val highestScore = _highestScore.map { it }

    private val _lastScore = MutableLiveData<Int>()
    val lastScore = _lastScore.map { it }

    private val _averageScore = MutableLiveData<Int>()
    val averageScore = _averageScore.map { it }

    private val _allScores = MutableLiveData<Int>()
    val allScores = _allScores.map { it }

    init {
        viewModelScope.launch {
            val statistics = userRepository.getStats()
            _totalGames.postValue(statistics.total_games)
            _highestScore.postValue(statistics.highest_score)
            _lastScore.postValue(statistics.last_score)
            _averageScore.postValue(statistics.average_score)
            _allScores.postValue(statistics.all_scores)
        }
    }
}
