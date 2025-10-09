package com.firman.gita.batombe.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.firman.gita.batombe.R
import com.firman.gita.batombe.ui.navigation.Screen
import com.firman.gita.batombe.ui.theme.PoppinsMedium
import com.firman.gita.batombe.ui.theme.PoppinsSemiBold
import com.firman.gita.batombe.ui.theme.batombeGray
import com.firman.gita.batombe.ui.theme.batombePrimary
import com.firman.gita.batombe.ui.theme.batombeSecondary
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutputPantunScreen(navController: NavController, pantunText: String = "") {
    var buttonState by remember { mutableStateOf(SSButtonState.IDLE) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = batombePrimary,
            darkIcons = false
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = batombePrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iv_logo_batombe_2),
                    contentDescription = "Logo Pantun Batombe",
                    modifier = Modifier
                        .height(92.dp)
                        .width(210.dp),
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
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = batombeSecondary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 28.dp, horizontal = 15.dp)
                ) {
                    Text(
                        text = "Output",
                        fontSize = 20.sp,
                        fontFamily = PoppinsSemiBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .height(250.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = batombeGray
                        )
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
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = pantunText.replace("/n", "\n"),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .verticalScroll(rememberScrollState()),
                                    color = batombePrimary,
                                    fontSize = 16.sp,
                                    fontFamily = PoppinsMedium,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    SSJetPackComposeProgressButton(
                        type = SSButtonType.CIRCLE,
                        width = 400.dp,
                        height = 56.dp,
                        buttonBorderColor = Color.Transparent,
                        buttonBorderWidth = 0.dp,
                        padding = it,
                        buttonState = buttonState,
                        onClick = {
                            coroutineScope.launch {
                                buttonState = SSButtonState.LOADING
                                delay(2000)
                                buttonState = SSButtonState.SUCCESS
                                navController.graph.startDestinationRoute?.let { route ->
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        cornerRadius = 16,
                        assetColor = Color.White,
                        successIconPainter = null,
                        failureIconPainter = null,
                        colors = buttonColors(
                            containerColor = batombePrimary,
                            contentColor = Color.White,
                            disabledContainerColor = batombePrimary,
                            disabledContentColor = Color.White
                        ),
                        text = "Login untuk buat batombe anda sendiri",
                        textModifier = Modifier,
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold
                    )
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
            pantunText = "Sample pantun text for preview"
        )
    }
}
