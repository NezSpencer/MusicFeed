package com.nezspencer.musicfeed.repository

import android.util.Log
import com.haroldadmin.cnradapter.NetworkResponse
import com.nezspencer.musicfeed.data.Api
import com.nezspencer.musicfeed.data.ErrorType
import com.nezspencer.musicfeed.data.FeedState
import com.nezspencer.musicfeed.data.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface FeedRepository {
    suspend fun getSongs(filter: String): FeedState
}

class FeedRepositoryImpl @Inject constructor(private val api: Api) : FeedRepository {
    private var currentPage: Int = 1
    private var currentFilter = ""

    override suspend fun getSongs(filter: String): FeedState = withContext(Dispatchers.IO) {
        if (currentPage > LAST_PAGE_INDEX && currentFilter == filter) {
            FeedState.Success(emptyList())
        } else {
            when (val response = api.getFeed()) {
                is NetworkResponse.Success -> {
                    if (currentFilter == filter) {
                        currentPage++
                    } else {
                        currentFilter = filter
                        currentPage = 1
                    }
                    FeedState.Success(filterResult(filter, response.body.data.sessions))
                }
                is NetworkResponse.ServerError -> {
                    // This ideally should be logged to a reporting platform,
                    // e.g Firebase crashlytics as a non-fatal error, for further review
                    //I am choosing to ignore this error. The user can just retry the request by scrolling
                    FeedState.Error(ErrorType.SERVER, "")
                }
                is NetworkResponse.NetworkError -> {
                    // This is usually due to network latency. Here, I want to show the user this error
                    // because they can take action to rectify this
                    FeedState.Error(
                        ErrorType.NETWORK,
                        "Please check your internet connection and try again"
                    )
                }
                else -> {
                    // We are not sure what is wrong here, so I am going to ignore this. This like others
                    // should also be logged to a reporting platform for further analysis
                    FeedState.Error(
                        ErrorType.OTHERS,
                        ""
                    )
                }
            }
        }
    }

    private fun filterResult(query: String, unfiltered: List<Song>): List<Song> =
        unfiltered.filter { song ->
            song.currentTrack.title.startsWith(query) ||
                    song.genres.any { genre -> genre.startsWith(query) } ||
                    song.name.startsWith(query)
        }

    companion object {
        private const val LAST_PAGE_INDEX = 5
    }
}