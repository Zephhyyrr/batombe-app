package com.firman.rima.batombe.ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firman.rima.batombe.R
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.ui.components.KamusCard
import com.firman.rima.batombe.ui.theme.PoppinsSemiBold
import com.firman.rima.batombe.ui.theme.batombePrimary
import com.firman.rima.batombe.ui.theme.batombeSecondary
import com.firman.rima.batombe.ui.theme.whiteColor
import com.firman.rima.batombe.ui.viewmodel.KamusViewModel
import com.firman.rima.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun KamusScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: KamusViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val kamusState by viewModel.articles.collectAsState()
    val currentPlayingUrl by viewModel.currentPlayingUrl
    val isPlaying by viewModel.isPlaying

    SideEffect {
        systemUiController.setStatusBarColor(
            color = batombePrimary,
            darkIcons = false
        )
    }

    KamusScreenContent(
        navController = navController,
        modifier = modifier,
        kamusState = kamusState,
        currentPlayingUrl = currentPlayingUrl,
        isPlaying = isPlaying,
        onPlayAudio = viewModel::toggleAudio,
        onDoneClick = viewModel::toggleKamusDone
    )
}

@Composable
fun KamusScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    kamusState: ResultState<List<KamusResponse.Data>> = ResultState.Initial,
    currentPlayingUrl: String? = null,
    isPlaying: Boolean = false,
    onPlayAudio: (String) -> Unit = {},
    onDoneClick: (Int) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = batombeSecondary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 60.dp,
                    bottomStart = 60.dp
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 21.dp),
                colors = CardDefaults.cardColors(containerColor = batombePrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_batombe_blue),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Rima Batombe",
                            style = TextStyle(
                                color = whiteColor,
                                fontSize = 20.sp,
                                fontFamily = PoppinsSemiBold,
                            )
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Baraja Minang",
                                    style = TextStyle(
                                        color = whiteColor,
                                        fontSize = 20.sp,
                                        fontFamily = PoppinsSemiBold
                                    )
                                )
                                Text(
                                    text = "Terus Belajar untuk Ahli bahasa Minang !",
                                    style = TextStyle(
                                        color = Color(0xFFFFC107),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .weight(0.35f)
                        .height(44.dp),
                    colors = CardDefaults.cardColors(containerColor = batombePrimary)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Kosa Kata",
                            style = TextStyle(
                                color = whiteColor,
                                fontFamily = PoppinsSemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                Card(
                    modifier = Modifier
                        .weight(0.65f)
                        .height(44.dp),
                    colors = CardDefaults.cardColors(containerColor = batombePrimary)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_volume_up),
                                contentDescription = "Speaker Icon",
                                tint = whiteColor,
                                modifier = Modifier.size(24.dp),
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "Tekan ikon speaker untuk mendengar pelafalan bahasa Minang",
                                style = TextStyle(
                                    color = whiteColor,
                                    fontFamily = PoppinsSemiBold,
                                    fontSize = 10.sp
                                ),
                                lineHeight = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (kamusState) {
                    is ResultState.Loading -> {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.loading_animation)
                        )
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(150.dp)
                        )
                    }

                    is ResultState.Success -> {
                        if (kamusState.data.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    val composition by rememberLottieComposition(
                                        LottieCompositionSpec.RawRes(
                                            R.raw.nodata_animation
                                        )
                                    )
                                    LottieAnimation(
                                        modifier = Modifier.size(400.dp),
                                        composition = composition,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit
                                    )
                                    Text(
                                        text = "Belum ada data kamus yang tersedia",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontFamily = PoppinsSemiBold
                                        )
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                items(kamusState.data) { item ->
                                    val isThisItemPlaying =
                                        (currentPlayingUrl == item.audio) && isPlaying
                                    KamusCard(
                                        data = item,
                                        isPlaying = isThisItemPlaying,
                                        onPlayAudio = onPlayAudio,
                                        onDoneClick = onDoneClick
                                    )
                                }
                            }
                        }
                    }

                    is ResultState.Error -> {
                        Text(
                            text = kamusState.errorMessage,
                            color = Color.Red,
                            style = TextStyle(fontFamily = PoppinsSemiBold)
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun KamusScreenPreview() {
    KamusScreenContent(
        navController = rememberNavController()
    )
}