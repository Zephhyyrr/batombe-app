package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.models.FeedByIdResponse
import com.firman.gita.batombe.data.remote.models.FeedResponse
import com.firman.gita.batombe.data.remote.models.GetCommentsResponse
import com.firman.gita.batombe.data.remote.models.LikeFeedResponse
import com.firman.gita.batombe.data.remote.models.PostCommentResponse
import com.firman.gita.batombe.data.remote.request.PostCommentRequest
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FeedService {

    @GET(ApiConstant.FEED_LIST)
    suspend fun getFeeds(
        @Header("Authorization") token: String
    ): FeedResponse

    @GET(ApiConstant.FEED_BY_ID)
    suspend fun getFeedById(
        @Header("Authorization") token: String,
        @Path("id") feedId: Int
    ): FeedByIdResponse

    @POST(ApiConstant.POST_COMMENT)
    suspend fun postComment(
        @Header("Authorization") token: String,
        @Body request: PostCommentRequest
    ): PostCommentResponse

    @GET(ApiConstant.GET_COMMENTS)
    suspend fun getComments(
        @Header("Authorization") token: String,
        @Path("historyId") historyId: Int
    ): GetCommentsResponse

    @POST(ApiConstant.LIKE_FEED)
    suspend fun likeFeed(
        @Header("Authorization") token: String,
        @Path("id") feedId: Int
    ): LikeFeedResponse
}