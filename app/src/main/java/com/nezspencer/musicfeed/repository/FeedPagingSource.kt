package com.nezspencer.musicfeed.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nezspencer.musicfeed.data.Api
import com.nezspencer.musicfeed.data.Song

class FeedPagingSource(private val api: Api, private val query: String) :
    PagingSource<Int, Song>() {
    private var currentQuery: String = ""

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val nextPageIndex = params.key ?: START_INDEX
            if (nextPageIndex > LAST_PAGE_INDEX) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = LAST_PAGE_INDEX,
                    nextKey = null
                )
            } else {
                //val feedList = filterResult(currentQuery, api.getFeed().data.sessions)
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (nextPageIndex == START_INDEX) null else nextPageIndex - 1,
                    nextKey = if (emptyList<Song>().isEmpty()) null else nextPageIndex + 1
                )
            }

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private fun filterResult(query: String, unfiltered: List<Song>): List<Song> =
        unfiltered.filter { song ->
            song.currentTrack.title.startsWith(query) ||
                    song.genres.any { genre -> genre.startsWith(query) } ||
                    song.name.startsWith(query)
        }

    companion object {
        private const val START_INDEX = 1
        private const val LAST_PAGE_INDEX = 5
    }
}