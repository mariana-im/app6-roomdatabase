package com.example.tareasapp

import androidx.compose.material3.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember {
        SimpleDateFormat("dd/MM HH:mm")
    }
    val fechaTexto = remember(task.creado_en) {
        dateFormat.format(Date(task.creado_en))
    }
    ListItem(
        leadingContent = {
            Checkbox(
                checked = task.completado,
                onCheckedChange = { onToggleCompleted() }
            )
        },
        headlineContent = {
            Text(
                text = task.titulo,
                textDecoration = if (task.completado)
                    TextDecoration.LineThrough else null,
                color = if (task.completado)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            Text(text = fechaTexto)
        },
    )
}