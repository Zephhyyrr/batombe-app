package com.firman.gita.batombe.ui.components

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firman.gita.batombe.R
import com.firman.gita.batombe.data.remote.models.HistoryResponse
import com.firman.gita.batombe.ui.theme.*
import com.firman.gita.batombe.utils.FormatDateUtils
import com.firman.gita.batombe.utils.MediaUrlUtils
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton

@Composable
fun HistoryCard(
    historyItem: HistoryResponse.Data,
    onClick: () -> Unit,
) {
    val (dayMonth, year) = FormatDateUtils.formatDate(historyItem.createdAt.orEmpty())
    val pantunText = historyItem.pantunBatombe.orEmpty()
    val audioPath = historyItem.fileAudio.orEmpty()

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var audioButton by remember { mutableStateOf(SSButtonState.IDLE) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp, top = 4.dp)
        ) {
            Text(text = dayMonth, fontSize = 14.sp, fontFamily = PoppinsSemiBold, color = textColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = year, fontSize = 14.sp, fontFamily = PoppinsSemiBold, color = textColor)
            Spacer(modifier = Modifier.width(8.dp))
            Divider(modifier = Modifier.weight(1f).height(1.dp), color = textColor.copy(alpha = 0.5f))
        }

        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = batombeGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.pantun_text_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = pantunText,
                        fontSize = 12.sp,
                        color = textColor,
                        lineHeight = 18.sp,
                        fontFamily = PoppinsRegular,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    errorMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontFamily = PoppinsRegular,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SSJetPackComposeProgressButton(
                            type = SSButtonType.CIRCLE,
                            width = 323.dp,
                            height = 45.dp,
                            buttonState = audioButton,
                            onClick = {
                                handlePlayPause(
                                    audioPath = audioPath,
                                    mediaPlayer = mediaPlayer,
                                    isPlaying = isPlaying,
                                    onMediaPlayerChange = { mediaPlayer = it },
                                    onPlayingStateChange = { playing ->
                                        isPlaying = playing
                                        audioButton = if (playing) SSButtonState.SUCCESS else SSButtonState.IDLE
                                    },
                                    onLoadingChange = { isLoading ->
                                        audioButton = if (isLoading) SSButtonState.LOADING else SSButtonState.IDLE
                                    },
                                    onErrorChange = { error ->
                                        errorMessage = error
                                        if (error != null) audioButton = SSButtonState.FAILURE
                                    }
                                )
                            },
                            cornerRadius = 100,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = batombePrimary,
                                contentColor = Color.White,
                                disabledContainerColor = batombePrimary,
                                disabledContentColor = Color.White
                            ),
                            assetColor = Color.White,
                            text = when (audioButton) {
                                SSButtonState.LOADING -> stringResource(R.string.loading_audio)
                                SSButtonState.SUCCESS -> stringResource(R.string.pause_audio)
                                SSButtonState.FAILURE -> stringResource(R.string.failed_play_audio)
                                else -> stringResource(R.string.play_audio)
                            }
                        )
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = stringResource(R.string.go_detail_description),
                    tint = textColor,
                    modifier = Modifier.size(30.dp).padding(end = 12.dp)
                )
            }
        }
    }
}

private fun handlePlayPause(
    audioPath: String,
    mediaPlayer: MediaPlayer?,
    isPlaying: Boolean,
    onMediaPlayerChange: (MediaPlayer?) -> Unit,
    onPlayingStateChange: (Boolean) -> Unit,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String?) -> Unit
) {
    try {
        if (isPlaying) {
            mediaPlayer?.pause()
            onPlayingStateChange(false)
            return
        }

        if (audioPath.isBlank()) {
            onErrorChange("Path audio tidak valid.")
            return
        }

        onLoadingChange(true)
        onErrorChange(null)

        val fullAudioUrl = MediaUrlUtils.buildMediaUrl(audioPath)
        val currentMediaPlayer = mediaPlayer ?: MediaPlayer()

        if (currentMediaPlayer == mediaPlayer) {
            currentMediaPlayer.start()
            onPlayingStateChange(true)
            onLoadingChange(false)
        } else {
            currentMediaPlayer.apply {
                setDataSource(fullAudioUrl)
                setOnPreparedListener { mp ->
                    onLoadingChange(false)
                    mp.start()
                    onPlayingStateChange(true)
                }
                setOnCompletionListener { mp ->
                    onPlayingStateChange(false)
                    mp.release()
                    onMediaPlayerChange(null)
                }
                setOnErrorListener { _, _, _ ->
                    onLoadingChange(false)
                    onErrorChange("Gagal memutar audio.")
                    onPlayingStateChange(false)
                    onMediaPlayerChange(null)
                    true
                }
                prepareAsync()
                onMediaPlayerChange(this)
            }
        }
    } catch (e: Exception) {
        Log.e("AudioPlayer", "Playback error", e)
        onErrorChange("Gagal memutar audio: ${e.message}")
        onPlayingStateChange(false)
        onLoadingChange(false)
    }
}