package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BakeryViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BakeryViewModel,
    modifier: Modifier = Modifier
) {
    val addressName by viewModel.addressName.collectAsState()
    val addressDetail by viewModel.addressDetail.collectAsState()

    Scaffold(
        modifier = modifier.background(CreamBackground),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Profil Saya",
                            fontWeight = FontWeight.Bold,
                            color = WarmBrown,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBackground)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar Circle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(WarmBrown),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MF",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // User Info
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Mhd Fauzan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.testTag("profile_user_name")
                )
                Text(
                    text = "ningsihendangsurya280@gmail.com",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.testTag("profile_user_email")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Settings Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileOptionRow(
                    icon = Icons.Outlined.LocationOn,
                    title = "Alamat Saya",
                    subtitle = "$addressName, $addressDetail",
                    onClick = { /* Edit address */ }
                )

                ProfileOptionRow(
                    icon = Icons.Outlined.ContactSupport,
                    title = "Hubungi Kami",
                    subtitle = "WhatsApp & Customer Service",
                    onClick = { /* Contact */ }
                )

                ProfileOptionRow(
                    icon = Icons.Outlined.Description,
                    title = "Syarat & Ketentuan",
                    subtitle = "Kebijakan privasi & layanan",
                    onClick = { /* Terms */ }
                )

                ProfileOptionRow(
                    icon = Icons.Outlined.Info,
                    title = "Tentang Cute Bakery",
                    subtitle = "Versi 1.0.0",
                    onClick = { /* About */ }
                )
            }
        }
    }
}

@Composable
fun ProfileOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = WarmBrown,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = "Arrow",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
