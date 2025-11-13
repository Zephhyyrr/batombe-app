package com.firman.rima.batombe.ui.pages

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.firman.rima.batombe.ui.navigation.Screen
import com.firman.rima.batombe.ui.theme.*
import java.io.File
import java.io.IOException
import java.util.UUID
import com.firman.rima.batombe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    pantunText: String,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    var isRecording by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    val backgroundMusicPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            backgroundMusicPlayer.release()
        }
    }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    fun startRecording(context: Context) {
        val fileName = "voice_${UUID.randomUUID()}.mp3"
        val file = File(context.cacheDir, fileName)
        audioFile = file

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            try {
                prepare()
                start()
                isRecording = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        try {
            backgroundMusicPlayer.reset()
            val descriptor = context.resources.openRawResourceFd(R.raw.music_latar)
            backgroundMusicPlayer.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            descriptor.close()
            backgroundMusicPlayer.prepare()
            backgroundMusicPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        recorder?.apply {
            try {
                stop()
                release()
            } catch (e: Exception) { e.printStackTrace() }
        }
        if (backgroundMusicPlayer.isPlaying) {
            backgroundMusicPlayer.stop()
        }
        recorder = null
        isRecording = false

        audioFile?.let { file ->
            navController.navigate(
                Screen.RecordResult.createRoute(
                    pantunText = pantunText,
                    audioFileName = file.name
                )
            )
        }
    }

    val handleRecordingClick = remember {
        {
            if (!hasAudioPermission) {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return@remember
            }
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            if (isRecording) {
                stopRecording()
            } else {
                startRecording(context)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Rekam Batombe Anda", fontSize = 14.sp, fontFamily = PoppinsSemiBold, color = whiteColor) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary)
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = batombeGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = pantunText.replace("/n", "\n"),
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = batombePrimary,
                                fontFamily = PoppinsMedium,
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 24.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = when {
                        !hasAudioPermission -> stringResource(R.string.audio_permission)
                        isRecording -> stringResource(R.string.recording_in_progress)
                        else -> stringResource(R.string.record_instructions)
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = when {
                            !hasAudioPermission -> MaterialTheme.colorScheme.error
                            isRecording -> Color.Red
                            else -> textColor
                        },
                        fontFamily = PoppinsMedium
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(if (isRecording) R.raw.stop_animation else R.raw.mic_animation)
                )
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever
                )
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            onClick = handleRecordingClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition,
                        { progress },
                        modifier = Modifier.size(350.dp)
                    )
                }
            }
        }
    }
}