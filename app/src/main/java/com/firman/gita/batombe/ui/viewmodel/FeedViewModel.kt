package com.firman.gita.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.gita.batombe.data.remote.models.*
import com.firman.gita.batombe.data.repository.feed.FeedRepository
import com.firman.gita.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val _feedsState =
        MutableStateFlow<ResultState<List<FeedResponse.FeedItem>>>(ResultState.Initial)
    val feedsState: StateFlow<ResultState<List<FeedResponse.FeedItem>>> = _feedsState.asStateFlow()

    private val _feedDetailState =
        MutableStateFlow<ResultState<FeedByIdResponse.FeedDetail>>(ResultState.Initial)
    val feedDetailState: StateFlow<ResultState<FeedByIdResponse.FeedDetail>> =
        _feedDetailState.asStateFlow()

    private val _publishState =
        MutableStateFlow<ResultState<PublishHistoryResponse.Data>>(ResultState.Initial)
    val publishState: StateFlow<ResultState<PublishHistoryResponse.Data>> =
        _publishState.asStateFlow()

    private val _commentsState =
        MutableStateFlow<ResultState<List<GetCommentsResponse.CommentData>>>(ResultState.Initial)
    val commentsState: StateFlow<ResultState<List<GetCommentsResponse.CommentData>>> =
        _commentsState.asStateFlow()

    private val _postCommentState =
        MutableStateFlow<ResultState<PostCommentResponse.CommentData>>(ResultState.Initial)
    val postCommentState: StateFlow<ResultState<PostCommentResponse.CommentData>> =
        _postCommentState.asStateFlow()

    private val _likeState =
        MutableStateFlow<ResultState<LikeFeedResponse.Data>>(ResultState.Initial)
    val likeState: StateFlow<ResultState<LikeFeedResponse.Data>> =
        _likeState.asStateFlow()

    init {
        getFeeds()
    }

    private fun <T> launchApiCall(
        stateFlow: MutableStateFlow<ResultState<T>>,
        apiCall: Flow<ResultState<T>>
    ) {
        viewModelScope.launch {
            apiCall.collect { result ->
                stateFlow.value = result
            }
        }
    }

    fun getFeeds() {
        launchApiCall(_feedsState, feedRepository.getFeeds())
    }

    fun getFeedById(id: Int) {
        launchApiCall(_feedDetailState, feedRepository.getFeedById(id))
    }

    fun publishFeed(historyId: Int) {
        launchApiCall(_publishState, feedRepository.publishFeed(historyId))
    }

    fun getComments(historyId: Int) {
        launchApiCall(_commentsState, feedRepository.getComments(historyId))
    }

    fun postComment(historyId: Int, content: String) {
        viewModelScope.launch {
            feedRepository.postComment(content, historyId).collect { result ->
                _postCommentState.value = result
                if (result is ResultState.Success) {
                    getComments(historyId)
                }
            }
        }
    }

    fun likeFeed(historyId: Int) {
        viewModelScope.launch {
            feedRepository.likeFeed(historyId).collect { result ->
                _likeState.value = result
                if (result is ResultState.Success) {
                    val updatedLikeData = result.data

                    _feedsState.update { currentState ->
                        if (currentState is ResultState.Success) {
                            val updatedList = currentState.data.map { feedItem ->
                                if (feedItem.id == historyId) {
                                    feedItem.copy(
                                        isLiked = updatedLikeData.isLiked,
                                        like = updatedLikeData.like
                                    )
                                } else {
                                    feedItem
                                }
                            }
                            ResultState.Success(updatedList)
                        } else {
                            currentState
                        }
                    }

                    _feedDetailState.update { detailState ->
                        if (detailState is ResultState.Success && detailState.data.id == historyId) {
                            ResultState.Success(
                                detailState.data.copy(
                                    isLiked = updatedLikeData.isLiked,
                                    like = updatedLikeData.like
                                )
                            )
                        } else {
                            detailState
                        }
                    }
                }
            }
        }
    }

    fun resetPublishState() {
        _publishState.value = ResultState.Initial
    }

    fun resetPostCommentState() {
        _postCommentState.value = ResultState.Initial
    }

    fun resetLikeState() {
        _likeState.value = ResultState.Initial
    }

    fun clearFeedDetail() {
        _feedDetailState.value = ResultState.Initial
    }
}