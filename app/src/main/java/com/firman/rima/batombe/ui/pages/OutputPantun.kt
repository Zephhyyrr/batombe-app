package com.firman.rima.batombe.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.firman.rima.batombe.R
import com.firman.rima.batombe.ui.navigation.Screen
import com.firman.rima.batombe.ui.theme.*
import com.firman.rima.batombe.ui.viewmodel.MeaningViewModel
import com.firman.rima.batombe.utils.ResultState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OutputPantunScreen(
    navController: NavController,
    pantunText: String = "",
    meaningViewModel: MeaningViewModel = hiltViewModel()
) {
    var buttonState by remember { mutableStateOf(SSButtonState.IDLE) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val meaningState by meaningViewModel.meaningState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(selectedTab) {
        if (selectedTab == 1) {
            if (meaningState !is ResultState.Success) {
                if (pantunText.isNotEmpty() && pantunText != "Pantun tidak ditemukan") {
                    meaningViewModel.getMeaning(pantunText)
                }
            }
        }
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = batombePrimary,
            darkIcons = false
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = batombePrimary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_batomve_1x1),
                    contentDescription = "Logo Pantun Batombe",
                    modifier = Modifier
                        .size(120.dp),
                    contentScale = ContentScale.FillBounds,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.iv_rumah_gadang_2),
                contentDescription = "Rumah Gadang",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = batombeSecondary),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 28.dp)) {

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { selectedTab = 0 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 0) batombeGray else Color.Transparent,
                                contentColor = if (selectedTab == 0) Color.Black else Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Output",
                                fontFamily = PoppinsSemiBold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { selectedTab = 1 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 1) batombeGray else Color.Transparent,
                                contentColor = if (selectedTab == 1) Color.Black else Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Terjemahan",
                                fontFamily = PoppinsSemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()
                            .height(250.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = batombeGray)
                    ) {
                        if (pantunText == "loading") {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LottieAnimation(
                                    modifier = Modifier.size(150.dp),
                                    composition = rememberLottieComposition(
                                        LottieCompositionSpec.RawRes(R.raw.loading_animation)
                                    ).value,
                                    iterations = LottieConstants.IterateForever,
                                    contentScale = ContentScale.Fit
                                )
                            }
                        } else {
                            if (selectedTab == 0) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = pantunText.replace("\\n", "\n"),
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .verticalScroll(rememberScrollState()),
                                        color = batombePrimary,
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsMedium,
                                        lineHeight = 24.sp
                                    )
                                }
                            } else {
                                when (val state = meaningState) {
                                    is ResultState.Loading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LottieAnimation(
                                                modifier = Modifier.size(150.dp),
                                                composition = rememberLottieComposition(
                                                    LottieCompositionSpec.RawRes(R.raw.loading_animation)
                                                ).value,
                                                iterations = LottieConstants.IterateForever,
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }

                                    is ResultState.Success -> {
                                        val makna = state.data.data?.makna_batombe
                                            ?: "Makna tidak ditemukan"
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = makna,
                                                modifier = Modifier
                                                    .padding(20.dp)
                                                    .verticalScroll(rememberScrollState()),
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsMedium,
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }

                                    is ResultState.Error -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "Gagal memuat arti",
                                                    color = Color.Red,
                                                    fontSize = 14.sp,
                                                    fontFamily = PoppinsMedium
                                                )
                                                Button(
                                                    onClick = {
                                                        meaningViewModel.getMeaning(
                                                            pantunText
                                                        )
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = batombePrimary
                                                    )
                                                ) {
                                                    Text("Coba Lagi", color = Color.White)
                                                }
                                            }
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SSJetPackComposeProgressButton(
                            type = SSButtonType.CIRCLE,
                            width = 400.dp,
                            height = 56.dp,
                            buttonState = buttonState,
                            onClick = {
                                coroutineScope.launch {
                                    buttonState = SSButtonState.LOADING
                                    delay(2000)
                                    buttonState = SSButtonState.SUCCESS
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            cornerRadius = 16,
                            assetColor = Color.White,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = batombePrimary,
                                contentColor = Color.White,
                                disabledContainerColor = batombePrimary,
                                disabledContentColor = Color.White
                            ),
                            text = "Login untuk buat batombe anda sendiri",
                            fontSize = 14.sp,
                            fontFamily = PoppinsSemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OutputPantunScreenPreview() {
    MaterialTheme {
        OutputPantunScreen(
            navController = rememberNavController(),
            pantunText = "Contoh pantun baris satu.\\nContoh pantun baris dua."
        )
    }
}