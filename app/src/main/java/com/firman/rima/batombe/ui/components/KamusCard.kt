package com.firman.rima.batombe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firman.rima.batombe.R
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.ui.theme.PoppinsSemiBold

@Composable
fun KamusCard(
    modifier: Modifier = Modifier,
    data: KamusResponse.Data,
    isPlaying: Boolean,
    onPlayAudio: (String) -> Unit,
    onDoneClick: (Int) -> Unit
) {
    val isDone = data.isDone == true
    val checklistColor = if (isDone) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.5f)
    val iconColor = if (isDone) Color.White else Color.Black

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // --- LOGIC CHECKLIST DI SINI ---
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = 2.dp,
                        color = if (isDone) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .background(
                        color = if (isDone) Color(0xFF4CAF50) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        data.id?.let { onDoneClick(it) }
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Mark as Done",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = data.word ?: "", style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = PoppinsSemiBold,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = data.meaning ?: "", style = TextStyle(
                        fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Normal
                    )
                )
            }

            IconButton(
                onClick = {
                    data.audio?.let { onPlayAudio(it) }
                }) {
                if (isPlaying) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pause),
                        contentDescription = "Pause Audio",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_volume_up),
                        contentDescription = "Play Audio",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}