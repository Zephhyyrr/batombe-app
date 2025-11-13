package com.firman.rima.batombe.ui.pages

import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.firman.rima.batombe.ui.navigation.Screen
import com.firman.rima.batombe.ui.theme.PoppinsSemiBold
import com.firman.rima.batombe.ui.theme.batombePrimary
import com.firman.rima.batombe.ui.theme.batombeSecondary
import com.firman.rima.batombe.ui.viewmodel.HistoryViewModel
import com.firman.rima.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import com.firman.rima.batombe.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordResultScreen(
    navController: NavController,
    pantunText: String,
    audioFileName: String,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    var buttonState by remember { mutableStateOf(SSButtonState.IDLE) }
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val voicePlayer = remember { MediaPlayer() }
    val snackbarHostState = remember { SnackbarHostState() }
    val saveHistoryState by viewModel.saveHistoryState.collectAsState()

    LaunchedEffect(saveHistoryState) {
        when (val result = saveHistoryState) {
            is ResultState.Loading -> buttonState = SSButtonState.LOADING
            is ResultState.Success -> {
                navController.navigate(Screen.History.route) {
                    popUpTo(Screen.RecordResult.route) { inclusive = true }
                }
                viewModel.resetSaveState()
            }
            is ResultState.Error -> {
                buttonState = SSButtonState.FAILURE
                snackbarHostState.showSnackbar("Gagal: ${result.errorMessage}")
                delay(2000)
                buttonState = SSButtonState.IDLE
                viewModel.resetSaveState()
            }
            is ResultState.Initial -> buttonState = SSButtonState.IDLE
        }
    }

    LaunchedEffect(audioFileName) {
        val audioFile = File(context.cacheDir, audioFileName)
        if (audioFile.exists()) {
            try {
                voicePlayer.setDataSource(audioFile.absolutePath)
                voicePlayer.prepare()
            } catch (e: Exception) {
                Log.e("RecordResultScreen", "Error: ${e.message}")
            }
        }
        voicePlayer.setOnCompletionListener { isPlaying = false }
    }

    DisposableEffect(Unit) {
        onDispose {
            voicePlayer.release()
            viewModel.resetSaveState()
        }
    }

    SideEffect {
        systemUiController.setStatusBarColor(color = batombePrimary, darkIcons = false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = batombePrimary,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iv_logo_batombe_2),
                    contentDescription = "Pantun Batombe Logo",
                    modifier = Modifier
                        .height(92.dp)
                        .width(210.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.iv_batombe_ilus_sitting),
                contentDescription = "Character",
                modifier = Modifier
                    .width(337.dp)
                    .height(247.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = batombeSecondary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali", tint = batombePrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kembali", color = batombePrimary, fontWeight = FontWeight.Medium)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = pantunText.replace("/n", "\n"),
                            fontSize = 18.sp,
                            fontFamily = PoppinsSemiBold,
                            color = batombePrimary,
                            lineHeight = 28.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    val audioFile = File(context.cacheDir, audioFileName)
                                    if (audioFile.exists()) {
                                        val authority = "${context.packageName}.provider"
                                        val audioUri = FileProvider.getUriForFile(context, authority, audioFile)
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "audio/*"
                                            putExtra(Intent.EXTRA_STREAM, audioUri)
                                            putExtra(Intent.EXTRA_TEXT, pantunText)
                                            putExtra(Intent.EXTRA_SUBJECT, "Sebuah Pantun Batombe")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        val chooser = Intent.createChooser(shareIntent, "Bagikan via...")
                                        context.startActivity(chooser)
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = batombePrimary, modifier = Modifier.size(28.dp))
                            }
                            Button(
                                onClick = {
                                    if (isPlaying) {
                                        voicePlayer.pause()
                                    } else {
                                        if (!voicePlayer.isPlaying && voicePlayer.duration > 0 && voicePlayer.currentPosition >= voicePlayer.duration - 100) {
                                            voicePlayer.seekTo(0)
                                        }
                                        voicePlayer.start()
                                    }
                                    isPlaying = !isPlaying
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = batombePrimary),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(if (isPlaying) "Jeda Audio" else "Putar Audio", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                                    contentDescription = if (isPlaying) "Pause" else "Play"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SSJetPackComposeProgressButton(
                            type = SSButtonType.CIRCLE,
                            width = 400.dp,
                            height = 56.dp,
                            buttonState = buttonState,
                            onClick = {
                                if (buttonState == SSButtonState.IDLE) {
                                    val audioFile = File(context.cacheDir, audioFileName)
                                    if (audioFile.exists()) {
                                        viewModel.saveHistory(audioFile, pantunText)
                                    } else {
                                        Log.e("RecordResultScreen", "File audio tidak ditemukan!")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = batombePrimary,
                                contentColor = Color.White,
                                disabledContainerColor = batombePrimary,
                                disabledContentColor = Color.White
                            ),
                            text = "Simpan History",
                            cornerRadius = 16,
                            assetColor = Color.White,
                            fontSize = 16.sp,
                            fontFamily = PoppinsSemiBold
                        )
                    }
                }
            }
        }
    }
}