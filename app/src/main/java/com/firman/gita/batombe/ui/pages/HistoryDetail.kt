package com.firman.gita.batombe.ui.pages

import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.firman.gita.batombe.R
import com.firman.gita.batombe.ui.components.HistoryDetailCard
import com.firman.gita.batombe.ui.theme.*
import com.firman.gita.batombe.ui.viewmodel.HistoryViewModel
import com.firman.gita.batombe.utils.MediaUrlUtils
import com.firman.gita.batombe.utils.ResultState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    modifier: Modifier = Modifier,
    historyId: Int,
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val historyDetailState by viewModel.historyDetailState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var audioButton by remember { mutableStateOf(SSButtonState.IDLE) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSharing by remember { mutableStateOf(false) }

    LaunchedEffect(historyId) {
        viewModel.getHistoryById(historyId)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.history_detail_title),
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_white),
                            contentDescription = stringResource(R.string.back),
                            tint = whiteColor
                        )
                    }
                },
                actions = {
                    val state = historyDetailState
                    if (state is ResultState.Success && state.data != null) {
                        val historyData = state.data
                        IconButton(
                            enabled = !isSharing,
                            onClick = {
                                coroutineScope.launch {
                                    isSharing = true
                                    try {
                                        val sharedFile = withContext(Dispatchers.IO) {
                                            val audioUrl = MediaUrlUtils.buildMediaUrl(historyData.fileAudio.orEmpty())
                                            val tempFile = File(context.cacheDir, "shared_audio.mp3")
                                            URL(audioUrl).openStream().use { input ->
                                                FileOutputStream(tempFile).use { output ->
                                                    input.copyTo(output)
                                                }
                                            }
                                            tempFile
                                        }

                                        val authority = "${context.packageName}.provider"
                                        val audioUri = FileProvider.getUriForFile(context, authority, sharedFile)

                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "audio/mp3"
                                            putExtra(Intent.EXTRA_STREAM, audioUri)
                                            putExtra(Intent.EXTRA_TEXT, "Ini adalah Pantun Batombe dari saya:\n\n${historyData.pantunBatombe}")
                                            putExtra(Intent.EXTRA_SUBJECT, "Sebuah Pantun Batombe")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Bagikan audio via..."))
                                    } catch (e: Exception) {
                                        Log.e("HistoryDetailScreen", "Share failed", e)
                                        snackbarHostState.showSnackbar("Gagal membagikan audio")
                                    } finally {
                                        isSharing = false
                                    }
                                }
                            }
                        ) {
                            if (isSharing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = whiteColor,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = whiteColor
                                )
                            }
                        }
                    }
                }
            )
        },
        modifier = modifier,
        containerColor = batombeSecondary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = historyDetailState) {
                is ResultState.Loading, ResultState.Initial -> {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                    )
                }
                is ResultState.Success -> {
                    val historyData = state.data
                    if (historyData != null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp),
                            contentPadding = PaddingValues(vertical = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = batombeGray),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = stringResource(R.string.audio_recording),
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsSemiBold,
                                            color = textColor,
                                            modifier = Modifier.padding(bottom = 15.dp)
                                        )
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
                                                        audioPath = historyData.fileAudio ?: "",
                                                        mediaPlayer = mediaPlayer,
                                                        isPlaying = isPlaying,
                                                        onMediaPlayerChange = { mediaPlayer = it },
                                                        onPlayingStateChange = {
                                                            isPlaying = it
                                                            audioButton = if (it) SSButtonState.SUCCESS else SSButtonState.IDLE
                                                        },
                                                        onLoadingChange = {
                                                            audioButton = if (it) SSButtonState.LOADING else SSButtonState.IDLE
                                                        },
                                                        onErrorChange = {
                                                            errorMessage = it
                                                            if (it != null) audioButton = SSButtonState.FAILURE
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
                                                assetColor = whiteColor,
                                                text = when (audioButton) {
                                                    SSButtonState.LOADING -> stringResource(R.string.loading_audio)
                                                    SSButtonState.SUCCESS -> stringResource(R.string.pause_audio)
                                                    SSButtonState.FAILURE -> stringResource(R.string.failed_play_audio)
                                                    else -> stringResource(R.string.play_audio)
                                                }
                                            )
                                        }
                                        errorMessage?.let {
                                            Text(
                                                text = it,
                                                color = MaterialTheme.colorScheme.error,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                HistoryDetailCard(data = historyData)
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Detail histori tidak ditemukan.")
                        }
                    }
                }
                is ResultState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Terjadi kesalahan", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Text(
                            state.errorMessage,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = { viewModel.getHistoryById(historyId) },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Coba Lagi", color = whiteColor)
                        }
                    }
                }
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

        if (currentMediaPlayer == mediaPlayer && !currentMediaPlayer.isPlaying) {
            currentMediaPlayer.start()
            onPlayingStateChange(true)
            onLoadingChange(false)
        } else {
            currentMediaPlayer.apply {
                reset()
                setDataSource(fullAudioUrl)
                setOnPreparedListener {
                    onLoadingChange(false)
                    it.start()
                    onPlayingStateChange(true)
                }
                setOnCompletionListener {
                    onPlayingStateChange(false)
                    it.release()
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