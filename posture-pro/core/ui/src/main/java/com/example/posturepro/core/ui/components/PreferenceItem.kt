package com.example.posturepro.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceItem(
    icon: ImageVector? = null,
    title: String,
    subtitle: String,
    value: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onItemClick: (() -> Unit)? = null
) {
    val modifier = if (onItemClick != null) {
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (trailing != null) {
            trailing()
        } else if (onItemClick != null) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview
@Composable
fun PreferenceItemPreview() {
    MaterialTheme {
        Column {
            PreferenceItem(
                title = "Preference Title",
                subtitle = "Preference description text",
                onItemClick = {}
            )
            PreferenceItem(
                title = "Preference With Value",
                subtitle = "Shows a value on the right",
                value = "Value",
                onItemClick = {}
            )
            PreferenceItem(
                title = "Preference With Switch",
                subtitle = "Toggleable preference",
                trailing = { Switch(checked = true, onCheckedChange = {}) }
            )
        }
    }
}