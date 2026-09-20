package com.creative.isitvegan.ui.components.v2

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.isitvegan.ui.viewmodels.AppInfoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransparencyInfoSheet(
    viewModel: AppInfoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val nextResetTime by viewModel.nextResetTime.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .testTag("transparency_sheet_container")
    ) {
        Text(
            text = "Privacy & Transparency",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("transparency_title")
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TransparencyItem(
            icon = Icons.Default.MoneyOff,
            title = "100% Free & Ad-Free",
            desc = "GreenCheck is a passion project. I don't charge money, show ads, or monetize your usage in any way.",
            modifier = Modifier.testTag("transparency_item_free")
        )
        
        TransparencyItem(
            icon = Icons.Default.Shield,
            title = "Strictly Local Experience",
            desc = "No accounts, no cloud sync, and no data collection. Your scan history and settings stay entirely on your device.",
            modifier = Modifier.testTag("transparency_item_local")
        )
        
        TransparencyItem(
            icon = Icons.Default.DeviceUnknown,
            title = "Per-Device Usage Limits",
            desc = "To ensure the Open Food Facts API remains available for everyone, we enforce a small daily lookup limit strictly on your device. Your next quota refresh is scheduled for ${formatResetTime(nextResetTime)}.",
            modifier = Modifier.testTag("transparency_item_limits")
        )
        
        TransparencyItem(
            icon = Icons.Default.Code,
            title = "Open Source Integrity",
            desc = "Transparency is core to our mission. You can audit our code on GitHub to verify how we handle (or rather, don't handle) your data.",
            modifier = Modifier.testTag("transparency_item_open_source")
        )
        
        TransparencyItem(
            icon = Icons.Default.Shield,
            title = "Data & Licensing",
            desc = "All product data is retrieved from Open Food Facts and is governed by the Open Database License (ODbL). You can use and redistribute this data according to the license terms.",
            modifier = Modifier.testTag("transparency_item_licensing")
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rajbirsehmi/GreenCheck"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("button_audit_code"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    modifier = Modifier.testTag("button_audit_code_icon")
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Audit Code",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("button_audit_code_text")
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://opendatacommons.org/licenses/odbl/1-0/"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("button_odbl_license"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text(
                    text = "ODbL License",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("button_odbl_license_text")
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TransparencyItem(
    icon: ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                .testTag("transparency_item_icon_container_${title.lowercase().replace(" ", "_")}"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(20.dp)
                    .testTag("transparency_item_icon_${title.lowercase().replace(" ", "_")}")
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("transparency_item_title_${title.lowercase().replace(" ", "_")}")
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                modifier = Modifier.testTag("transparency_item_desc_${title.lowercase().replace(" ", "_")}")
            )
        }
    }
}

private fun formatResetTime(timestamp: Long): String {
    if (timestamp <= 0L) return "soon"
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
