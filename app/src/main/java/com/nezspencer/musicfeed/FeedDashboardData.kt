package com.nezspencer.musicfeed

import com.nezspencer.musicfeed.data.Song

data class FeedDashboardData(
    val songs: List<Song> = listOf(),
    val showScreenProgress: Boolean = true,
    val clearExistingSongs: Boolean = false,
    val showFooterProgress: Boolean = false,
    val showSearchProgress: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String = "",
    val triggerScreenRefresh: Boolean = false,
    val query: String = ""
)