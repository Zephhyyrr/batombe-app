package com.firman.rima.batombe.ui.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.firman.rima.batombe.R
import com.firman.rima.batombe.ui.navigation.Screen
import com.firman.rima.batombe.ui.theme.PoppinsSemiBold
import com.firman.rima.batombe.ui.theme.batombePrimary
import com.firman.rima.batombe.ui.theme.batombeSecondary
import com.firman.rima.batombe.ui.theme.whiteColor
import com.firman.rima.batombe.ui.viewmodel.GeneratePantunViewModel
import com.firman.rima.batombe.utils.ResultState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.launch

@Composable
fun GeneratePantunLoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GeneratePantunViewModel = hiltViewModel()
) {
    var jumlahBaris by remember { mutableStateOf("") }
    var tema by remember { mutableStateOf("") }
    var emosi by remember { mutableStateOf("") }
    var buttonState by remember { mutableStateOf(SSButtonState.IDLE) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.generatePantunState.collectAsStateWithLifecycle()

    // Flag untuk mengunci navigasi agar hanya terjadi sekali per sukses
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ResultState.Success -> {
                if (!hasNavigated) {
                    buttonState = SSButtonState.SUCCESS

                    // Ambil data pantun dengan aman.
                    // Pastikan field 'pantun' sesuai dengan Data Class Response API Anda.
                    // Jika API mengembalikan null, kita gunakan string kosong dulu untuk debug, jangan langsung "Tidak Ditemukan"
                    val rawPantun = state.data.data?.pantun

                    if (!rawPantun.isNullOrBlank()) {
                        navController.navigate(Screen.OutputPantunLogin.createRoute(rawPantun))
                        hasNavigated = true
                        // PENTING: Jangan panggil viewModel.resetState() di sini agar data tidak hilang saat transisi
                    } else {
                        Toast.makeText(context, "Pantun berhasil dibuat namun datanya kosong.", Toast.LENGTH_LONG).show()
                        buttonState = SSButtonState.IDLE
                    }
                }
            }

            is ResultState.Error -> {
                buttonState = SSButtonState.FAILURE
                Toast.makeText(context, "Gagal: ${state.errorMessage}", Toast.LENGTH_LONG).show()
                // Reset manual tombol agar user bisa coba lagi
                buttonState = SSButtonState.IDLE
                hasNavigated = false
                viewModel.resetState()
            }

            is ResultState.Loading -> {
                buttonState = SSButtonState.LOADING
            }

            else -> {
                buttonState = SSButtonState.IDLE
                hasNavigated = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(batombeSecondary)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_batomve_1x1),
            contentDescription = "Logo Pantun Batombe",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.rumah_gadang_1),
            contentDescription = "Rumah Gadang",
            modifier = Modifier
                .fillMaxWidth()
                .height(328.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Generate Batombe",
            fontSize = 24.sp,
            fontFamily = PoppinsSemiBold,
            color = Color(0xFF2C1810),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            label = "JUMLAH BARIS",
            value = jumlahBaris,
            onValueChange = { jumlahBaris = it },
            placeholder = "4"
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "TEMA",
            value = tema,
            onValueChange = { tema = it },
            placeholder = "Nasihat Pernikahan"
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "EMOSI",
            value = emosi,
            onValueChange = { emosi = it },
            placeholder = "BAHAGIA"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 75.dp)
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            SSJetPackComposeProgressButton(
                type = SSButtonType.CIRCLE,
                width = 400.dp,
                height = 56.dp,
                buttonBorderColor = Color.Transparent,
                buttonBorderWidth = 0.dp,
                buttonState = buttonState,
                onClick = {
                    val jumlahBarisInt = jumlahBaris.toIntOrNull()
                    if (jumlahBarisInt == null) {
                        Toast.makeText(context, "Jumlah baris harus berupa angka.", Toast.LENGTH_SHORT).show()
                    } else if (tema.isBlank() || emosi.isBlank()) {
                        Toast.makeText(context, "Tema dan emosi tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                    } else {
                        hasNavigated = false // Reset flag sebelum request baru
                        coroutineScope.launch {
                            viewModel.generatePantun(jumlahBarisInt, tema, emosi)
                        }
                    }
                },
                cornerRadius = 16,
                assetColor = Color.White,
                successIconPainter = null,
                failureIconPainter = null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = batombePrimary,
                    contentColor = whiteColor,
                    disabledContainerColor = batombePrimary,
                    disabledContentColor = whiteColor
                ),
                text = "GENERATE ✨",
                textModifier = Modifier,
                fontSize = 16.sp,
                fontFamily = PoppinsSemiBold
            )
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp, fontFamily = PoppinsSemiBold, color = batombePrimary),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFFBDBDBD)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = batombePrimary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = batombePrimary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(color = batombePrimary, fontSize = 16.sp, fontFamily = PoppinsSemiBold),
            singleLine = true
        )
    }
}