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

    // DIPERBAIKI: Menggunakan helper `launchApiCall` agar konsisten
    fun getFeeds() {
        launchApiCall(_feedsState, feedRepository.getFeeds())
    }

    // DIHAPUS: suspend
    fun getFeedById(id: Int) {
        launchApiCall(_feedDetailState, feedRepository.getFeedById(id))
    }

    // DIHAPUS: suspend
    fun publishFeed(historyId: Int) {
        launchApiCall(_publishState, feedRepository.publishFeed(historyId))
    }

    // DIHAPUS: suspend
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

    fun resetPublishState() {
        _publishState.value = ResultState.Initial
    }

    fun resetPostCommentState() {
        _postCommentState.value = ResultState.Initial
    }

    fun clearFeedDetail() {
        _feedDetailState.value = ResultState.Initial
    }
}