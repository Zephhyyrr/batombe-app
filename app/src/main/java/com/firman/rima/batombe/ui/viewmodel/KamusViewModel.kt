package com.firman.rima.batombe.ui.viewmodel

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.data.repository.kamus.KamusRepository
import com.firman.rima.batombe.utils.MediaUrlUtils
import com.firman.rima.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KamusViewModel @Inject constructor(
    private val kamusRepository: KamusRepository
) : ViewModel() {
    private val _kamus =
        MutableStateFlow<ResultState<List<KamusResponse.Data>>>(ResultState.Initial)
    val articles: StateFlow<ResultState<List<KamusResponse.Data>>> = _kamus

    private var mediaPlayer: MediaPlayer? = null
    private val _currentPlayingUrl = mutableStateOf<String?>(null)
    val currentPlayingUrl: State<String?> = _currentPlayingUrl
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    init {
        getAllKamus()
    }

    fun getAllKamus() {
        viewModelScope.launch {
            kamusRepository.getAllKamus().collect { result ->
                _kamus.value = when (result) {
                    is ResultState.Success -> ResultState.Success(
                        data = result.data,
                        successMessage = result.successMessage
                    )

                    is ResultState.Error -> ResultState.Error(result.errorMessage)
                    is ResultState.Loading -> ResultState.Loading
                    is ResultState.Initial -> ResultState.Initial
                }
            }
        }
    }

    fun toggleKamusDone(id: Int) {
        viewModelScope.launch {
            kamusRepository.kamusDone(id.toString()).collect { result ->
                if (result is ResultState.Success) {
                    val currentState = _kamus.value
                    if (currentState is ResultState.Success) {
                        val updatedList = currentState.data.map { item ->
                            if (item.id == id) {
                                item.copy(isDone = result.data.isDone)
                            } else {
                                item
                            }
                        }
                        _kamus.value = ResultState.Success(updatedList, currentState.successMessage)
                    }
                }
            }
        }
    }

    fun toggleAudio(url: String) {
        if (_currentPlayingUrl.value == url) {
            if (_isPlaying.value) {
                mediaPlayer?.pause()
                _isPlaying.value = false
            } else {
                mediaPlayer?.start()
                _isPlaying.value = true
            }
        } else {
            playNewAudio(url)
        }
    }

    private fun playNewAudio(url: String) {
        try {
            val fullAudioUrl = MediaUrlUtils.buildMediaUrl(url)
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(fullAudioUrl)
                prepareAsync()
                setOnPreparedListener { mp ->
                    mp.start()
                    _currentPlayingUrl.value = url
                    _isPlaying.value = true
                }
                setOnCompletionListener {
                    _isPlaying.value = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}