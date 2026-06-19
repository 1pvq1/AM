package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.local.ChatSession
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for the chat history drawer content.
 */
@Composable
fun ChatHistoryDrawerContent(
    allSessions: List<ChatSession>,
    selectedSessionId: String? = null,
    onSessionSelect: (ChatSession) -> Unit,
    onCreateNewSession: () -> Unit,
    onDeleteSession: (ChatSession) -> Unit,
    onRenameSession: (ChatSession) -> Unit = {},
    onPinSession: (ChatSession) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = "character_interaction"),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onCreateNewSession,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = "new_chat"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allSessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = "no_sessions"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(allSessions) { session ->
                    SessionItem(
                        session = session,
                        isSelected = session.id == selectedSessionId,
                        onSelect = { onSessionSelect(session) },
                        onDelete = { onDeleteSession(session) },
                        onRename = { onRenameSession(session) },
                        onPin = { onPinSession(session) }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SessionItem(
    session: ChatSession,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onRename: () -> Unit,
    onPin: () -> Unit
) {
    var isActionsExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = onSelect,
                onLongClick = { isActionsExpanded = !isActionsExpanded }
            ),
        shape = MaterialTheme.shapes.small,
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )

                if (session.isPinned) {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        modifier = Modifier.size(24.dp).padding(horizontal = 4.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }

                if (isSelected) {
                    IconButton(
                        onClick = { isActionsExpanded = !isActionsExpanded },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Actions",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isActionsExpanded) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onRename, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Rename",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onPin, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = "Pin",
                            modifier = Modifier.size(18.dp),
                            tint = if (session.isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = stringResource(id = "delete_session"),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatHistoryDrawerContentPreview() {
    val sampleSessions = listOf(
        ChatSession("1", "Project Discussion", 1717750000000L, isPinned = true),
        ChatSession("2", "Shopping List", 1717751000000L),
        ChatSession("3", "Vacation Planning", 1717752000000L)
    )
    AppTheme {
        Surface {
            ChatHistoryDrawerContent(
                allSessions = sampleSessions,
                selectedSessionId = "2",
                onSessionSelect = {},
                onCreateNewSession = {},
                onDeleteSession = {}
            )
        }
    }
}
