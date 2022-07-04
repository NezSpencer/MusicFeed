package com.nezspencer.musicfeed

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezspencer.musicfeed.data.ErrorType
import com.nezspencer.musicfeed.repository.FeedRepository
import com.nezspencer.musicfeed.data.FeedState
import com.nezspencer.musicfeed.data.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: FeedRepository) : ViewModel() {

    var screenData by mutableStateOf(FeedDashboardData())

    private val queryDebounceCountDown = object : CountDownTimer(DEBOUNCE_TIME_IN_MILLS, 100L) {
        override fun onFinish() {
            screenData = screenData.copy(
                showSearchProgress = true,
                clearExistingSongs = true,
                showError = false
            )
            getFeed(query = screenData.query)
        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    fun updateSearchFilter(query: String) {
        screenData = screenData.copy(query = query)
        queryDebounceCountDown.cancel()
        queryDebounceCountDown.start()
    }

    fun loadMore() {
        screenData = screenData.copy(showFooterProgress = true, showError = false)
        getFeed(screenData.query)
    }

    fun initialize() {
        getFeed(screenData.query)
    }

    private fun getFeed(query: String) = viewModelScope.launch {
        val response = repository.getSongs(query)
        if (response is FeedState.Success) {
            val updatedSongs = mutableListOf<Song>()
            if (screenData.clearExistingSongs) {
                updatedSongs.addAll(response.songs)
            } else {
                val existingSongs = screenData.songs
                updatedSongs.addAll(existingSongs)
                updatedSongs.addAll(response.songs)
            }
            screenData = screenData.copy(
                songs = updatedSongs,
                clearExistingSongs = false,
                showSearchProgress = false,
                showFooterProgress = false,
                showScreenProgress = false
            )
        } else {
            val errorResponse = response as FeedState.Error
            screenData = screenData.copy(
                clearExistingSongs = false,
                showSearchProgress = false,
                showFooterProgress = false,
                showScreenProgress = false,
                showError = errorResponse.errorType == ErrorType.NETWORK,
                errorMessage = errorResponse.errorMessage
            )

        }
    }

    companion object {
        private const val DEBOUNCE_TIME_IN_MILLS = 300L
    }

}