package com.firman.rima.batombe.data.repository.feed

import android.util.Log
import com.firman.rima.batombe.data.local.datastore.AuthPreferences
import com.firman.rima.batombe.data.remote.models.FeedByIdResponse
import com.firman.rima.batombe.data.remote.models.FeedResponse
import com.firman.rima.batombe.data.remote.models.GetCommentsResponse
import com.firman.rima.batombe.data.remote.models.LikeFeedResponse
import com.firman.rima.batombe.data.remote.models.PostCommentResponse
import com.firman.rima.batombe.data.remote.models.PublishHistoryResponse
import com.firman.rima.batombe.data.remote.request.PostCommentRequest
import com.firman.rima.batombe.data.remote.service.FeedService
import com.firman.rima.batombe.data.remote.service.PublishService
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val feedService: FeedService,
    private val publishService: PublishService
) : FeedRepository {

    override fun getFeeds(): Flow<ResultState<List<FeedResponse.FeedItem>>> = flow {
        emit(ResultState.Loading)
        val token = authPreferences.authToken.first() ?: ""
        val response = feedService.getFeeds("Bearer $token")
        if (response.success && response.data != null) {
            emit(ResultState.Success(response.data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to fetch feeds"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
    }

    override fun getFeedById(id: Int): Flow<ResultState<FeedByIdResponse.FeedDetail>> =
        flow {
            emit(ResultState.Loading)
            val token = authPreferences.authToken.first() ?: ""
            val response = feedService.getFeedById("Bearer $token", id)
            if (response.success && response.data != null) {
                emit(ResultState.Success(response.data))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to fetch feed detail"))
            }
        }.catch { e ->
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }

    override fun publishFeed(historyId: Int): Flow<ResultState<PublishHistoryResponse.Data>> =
        flow {
            emit(ResultState.Loading)
            val token = authPreferences.authToken.first() ?: ""
            val response = publishService.publishFeed("Bearer $token", historyId)
            if (response.success && response.data != null) {
                emit(ResultState.Success(response.data))
            } else {
                emit(ResultState.Error(response.message ?: "Failed to publish feed"))
            }
        }.catch { e ->
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }

    override fun postComment(
        content: String,
        historyId: Int
    ): Flow<ResultState<PostCommentResponse.CommentData>> = flow {
        emit(ResultState.Loading)
        val token = authPreferences.authToken.first() ?: ""
        val request = PostCommentRequest(content = content, historyId = historyId)
        val response = feedService.postComment("Bearer $token", request)

        if (response.success && response.data != null) {
            emit(ResultState.Success(response.data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to post comment"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
    }

    override fun getComments(historyId: Int): Flow<ResultState<List<GetCommentsResponse.CommentData>>> =
        flow {
            emit(ResultState.Loading)
            val token = "Bearer ${authPreferences.authToken.first() ?: ""}"
            try {
                val response = feedService.getComments(token, historyId)

                if (response.success) {
                    emit(ResultState.Success(response.data ?: emptyList()))
                } else {
                    emit(ResultState.Error(response.message ?: "Failed to fetch comments"))
                }
            } catch (e: Exception) {
                Log.e("FeedRepository", "GetComments Exception", e)
                emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
            }
        }

    override fun likeFeed(historyId: Int): Flow<ResultState<LikeFeedResponse.Data>> =
        flow {
            emit(ResultState.Loading)
            val token = "Bearer ${authPreferences.authToken.first() ?: ""}"

            try {
                val response = feedService.likeFeed(token, historyId)

                if (response.success && response.data != null) {
                    emit(ResultState.Success(response.data))
                } else {
                    emit(ResultState.Error(response.message ?: "Failed to like feed"))
                }
            } catch (e: Exception) {
                Log.e("FeedRepository", "LikeFeed Exception", e)
                emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
            }
        }
}