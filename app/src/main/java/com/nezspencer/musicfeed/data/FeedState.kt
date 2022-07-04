package com.nezspencer.musicfeed.data

sealed class FeedState {
    object Loading : FeedState()
    data class Success(val songs: List<Song>) : FeedState()
    data class Error(val errorType: ErrorType, val errorMessage: String) : FeedState()
}

enum class ErrorType {
    SERVER,
    NETWORK,
    HTTP,
    OTHERS
}