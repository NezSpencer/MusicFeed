package com.nezspencer.musicfeed.data

import com.squareup.moshi.Json

data class FeedResponse(@Json(name = "data") val data: FeedData)

data class FeedData(@Json(name = "sessions") val sessions: List<Song>)

data class Song(
    @Json(name = "name") val name: String,
    @Json(name = "listener_count") val listenerCount: Long,
    @Json(name = "genres") val genres: List<String>,
    @Json(name = "current_track") val currentTrack: CurrentTrack
)

data class CurrentTrack(
    @Json(name = "title") val title: String,
    @Json(name = "artwork_url") val artworkUrl: String
)

data class ErrorResponse(@Json(name = "message") val errorMessage: String)