package com.firman.rima.batombe.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firman.rima.batombe.R
import com.firman.rima.batombe.ui.theme.PoppinsMedium
import com.firman.rima.batombe.ui.theme.batombePrimary
import com.firman.rima.batombe.ui.theme.batombeSecondary
import com.firman.rima.batombe.ui.theme.textColor

@Composable
fun BottomAppBarWithFab(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntryState by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntryState?.destination?.route

    val barItems = items.filter { !it.isMainFeature }
    val fabItem = items.find { it.isMainFeature }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                barItems.forEach { item ->
                    BottomNavItemComponent(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        fabItem?.let { item ->
            val isSelected = currentRoute == item.route
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-55).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    modifier = Modifier
                        .size(70.dp)
                        .border(
                            width = 8.dp,
                            color = batombeSecondary,
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    containerColor = batombePrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                ) {
                    item.icon?.let {
                        Icon(
                            painter = it,
                            contentDescription = item.title,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title ?: "",
                    fontSize = 10.sp,
                    color = batombePrimary,
                    fontFamily = PoppinsMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BottomNavItemComponent(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedBackground = batombePrimary.copy(alpha = 0.2f)
    val iconColor = if (isSelected) batombePrimary else textColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxHeight()
            .clickable(enabled = item.isEnabled) { onClick() }
            .clip(RoundedCornerShape(50))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(width = 56.dp, height = 31.dp)
                        .background(
                            color = selectedBackground,
                            shape = RoundedCornerShape(50)
                        )
                )
            }

            item.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = item.title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = item.title ?: "",
            fontSize = 10.sp,
            color = iconColor,
            fontFamily = PoppinsMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarWithFabPreview() {
    val items = listOf(
        BottomNavItem("home", "Home", painterResource(id = R.drawable.ic_home)),
        BottomNavItem("post", "Postingan", painterResource(id = R.drawable.ic_post)),
        BottomNavItem(
            "generate",
            "",
            painterResource(id = R.drawable.ic_star),
            isMainFeature = true
        ),
        BottomNavItem("baraja", "Baraja", painterResource(id = R.drawable.ic_books)),
        BottomNavItem("history", "History", painterResource(id = R.drawable.ic_history)),
        BottomNavItem("profile", "Profile", painterResource(id = R.drawable.ic_profile))
    )

    BottomAppBarWithFab(
        items = items,
        navController = rememberNavController()
    )
}