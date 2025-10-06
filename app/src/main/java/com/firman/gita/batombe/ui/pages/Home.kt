package com.firman.gita.batombe.ui.pages

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.*
import com.firman.gita.batombe.R
import com.firman.gita.batombe.data.remote.models.CurrentUserResponse
import com.firman.gita.batombe.data.remote.models.ExampleVideoResponse
import com.firman.gita.batombe.ui.components.CardArticle
import com.firman.gita.batombe.ui.theme.PoppinsSemiBold
import com.firman.gita.batombe.ui.theme.batombePrimary
import com.firman.gita.batombe.ui.theme.batombeSecondary
import com.firman.gita.batombe.ui.theme.textColor
import com.firman.gita.batombe.ui.viewmodel.HomeViewModel
import com.firman.gita.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.core.net.toUri

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }

    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val exampleVideosState by viewModel.exampleVideosState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
        viewModel.getAllExampleVideos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(batombePrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(batombePrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 16.dp)
            ) {
                when (val state = userState) {
                    is ResultState.Loading -> {
                        UserHeaderSkeleton()
                    }

                    is ResultState.Success -> {
                        UserHeader(state.data)
                    }

                    is ResultState.Error -> {
                        UserHeaderError(state.errorMessage ?: "Error loading user")
                    }

                    is ResultState.Initial -> {
                        Log.d("HomeScreen", "User initial state")
                        UserHeaderSkeleton()
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = batombeSecondary,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(top = 40.dp, start = 15.dp, end = 15.dp)
        ) {
            ExampleVideoSection(exampleVideosState)
        }
    }
}

@Composable
private fun UserHeader(user: CurrentUserResponse) {
    Column(
        modifier = Modifier
            .padding(start = 15.dp, top = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.data?.profileImage)
                .crossfade(true)
                .build(),
            contentDescription = "Foto Profil",
            placeholder = painterResource(R.drawable.unknownperson),
            error = painterResource(R.drawable.unknownperson),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Selamat Datang,",
            color = Color.White,
            fontSize = 14.sp
        )

        Text(
            text = user.data?.name ?: "Pengguna",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun UserHeaderSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun UserHeaderError(errorMessage: String) {
    Column {
        Text(
            text = "Error",
            color = Color.White,
            fontSize = 14.sp
        )
        Text(
            text = errorMessage,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun ExampleVideoSection(state: ResultState<ExampleVideoResponse>) {
    when (val currentState = state) {
        is ResultState.Loading -> {
            Log.d("HomeScreen", "Showing videos loading state")
            LoadingAnimation()
        }

        is ResultState.Success -> {
            val videos = currentState.data.data ?: emptyList()
            Log.d("HomeScreen", "Showing videos success state: ${videos.size} videos")

            if (videos.isEmpty()) {
                NoDataAnimation("Belum ada contoh video")
            } else {
                Column {
                    Text(
                        text = "Contoh Video Batombe: ",
                        style = TextStyle(
                            color = textColor,
                            fontSize = 18.sp,
                            fontFamily = PoppinsSemiBold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(videos) { video ->
                            val context = LocalContext.current

                            CardArticle(
                                title = video.title ?: "Tanpa Judul",
                                content = "",
                                imageUrl = video.imageUrl ?: "",
                                onClick = {
                                    val videoUrl = video.url
                                    if (!videoUrl.isNullOrBlank()) {
                                        val intent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
                                        context.startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        is ResultState.Error -> {
            Log.e("HomeScreen", "Videos error state: ${currentState.errorMessage}")
            NoDataAnimation(currentState.errorMessage ?: "Terjadi kesalahan")
        }

        is ResultState.Initial -> {
            Log.d("HomeScreen", "Videos initial state")
        }
    }
}

@Composable
private fun LoadingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
private fun NoDataAnimation(message: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.nodata_animation))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = message,
            style = TextStyle(color = textColor, fontSize = 14.sp, fontFamily = PoppinsSemiBold)
        )
    }
}