package com.firman.rima.batombe.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.firman.rima.batombe.R
import com.firman.rima.batombe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeaningScreen(
    navController: NavController,
    pantun: String,
    meaning: String
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Makna Filosofis",
                        fontSize = 14.sp,
                        fontFamily = PoppinsSemiBold,
                        color = whiteColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = batombePrimary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_white),
                            contentDescription = stringResource(R.string.back),
                            tint = whiteColor
                        )
                    }
                }
            )
        },
        containerColor = batombeSecondary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pantun Batombe",
                fontFamily = PoppinsSemiBold,
                fontSize = 14.sp,
                color = textColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = batombeGray),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = pantun,
                    fontFamily = PoppinsMedium,
                    fontSize = 14.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bahasa Indonesia",
                fontFamily = PoppinsSemiBold,
                fontSize = 14.sp,
                color = textColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = meaning,
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp,
                    color = textColor,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}