package com.firman.gita.batombe.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class FeedScreenStateHolder(
    val coroutineScope: CoroutineScope,
    context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    var currentlyPlayingId by mutableStateOf<Int?>(null)
        private set
    var audioButtonState by mutableStateOf(SSButtonState.IDLE)
        private set

    private var playerJob: Job? = null

    fun handlePlayPause(feedId: Int, audioPath: String) {
        playerJob?.cancel()

        if (currentlyPlayingId == feedId && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            audioButtonState = SSButtonState.IDLE
            return
        }

        if (currentlyPlayingId == feedId && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            audioButtonState = SSButtonState.SUCCESS
            return
        }

        mediaPlayer?.release()
        currentlyPlayingId = feedId
        audioButtonState = SSButtonState.LOADING

        try {
            val newPlayer = MediaPlayer().apply {
                setDataSource(MediaUrlUtils.buildMediaUrl(audioPath))
                setOnPreparedListener {
                    it.start()
                    audioButtonState = SSButtonState.SUCCESS
                }
                setOnCompletionListener {
                    cleanup()
                }
                setOnErrorListener { _, _, _ ->
                    Log.e("FeedPlayer", "MediaPlayer Error")
                    cleanup()
                    audioButtonState = SSButtonState.FAILURE
                    true
                }
            }
            newPlayer.prepareAsync()
            mediaPlayer = newPlayer
        } catch (e: Exception) {
            Log.e("FeedPlayer", "Error setting data source", e)
            cleanup()
            audioButtonState = SSButtonState.FAILURE
        }
    }

    fun cleanup() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentlyPlayingId = null
        audioButtonState = SSButtonState.IDLE
        playerJob = null
    }
}

@Composable
fun rememberFeedScreenState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current
): FeedScreenStateHolder {
    return remember(coroutineScope, context) {
        FeedScreenStateHolder(coroutineScope, context)
    }
}