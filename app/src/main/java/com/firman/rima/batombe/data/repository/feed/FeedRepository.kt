package com.firman.rima.batombe.data.repository.feed

import com.firman.rima.batombe.data.remote.models.*
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getFeeds(): Flow<ResultState<List<FeedResponse.FeedItem>>>
    fun getFeedById(id: Int): Flow<ResultState<FeedByIdResponse.FeedDetail>>
    fun publishFeed(historyId: Int): Flow<ResultState<PublishHistoryResponse.Data>>
    fun postComment(
        content: String,
        historyId: Int
    ): Flow<ResultState<PostCommentResponse.CommentData>>
    fun getComments(historyId: Int): Flow<ResultState<List<GetCommentsResponse.CommentData>>>
    fun likeFeed(historyId: Int): Flow<ResultState<LikeFeedResponse.Data>>
}