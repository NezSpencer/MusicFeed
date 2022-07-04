package com.nezspencer.musicfeed.data

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET

interface Api {

    //https://www.mocky.io/v2/5df79a3a320000f0612e0115
    @GET("v2/5df79a3a320000f0612e0115")
    suspend fun getFeed(): NetworkResponse<FeedResponse, ErrorResponse>
}