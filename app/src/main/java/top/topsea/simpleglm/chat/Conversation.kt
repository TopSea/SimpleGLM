package top.topsea.simpleglm.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.topsea.simpleglm.settings.droid
import top.topsea.simpleglm.settings.me

const val ConversationTestTag = "ConversationTestTag"

@Composable
fun ChatMessages(
    chatMessages: List<ChatMessage>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in chatMessages.indices) {
                val prevAuthor = chatMessages.getOrNull(index - 1)?.author
                val nextAuthor = chatMessages.getOrNull(index + 1)?.author
                val content = chatMessages[index]
                val firstMessageFromMe = prevAuthor == me
                val lastMessageFromMe = nextAuthor == me

                if (index == 0) {
                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                // Hardcode day dividers for simplicity
//                if (index == chatMessages.size - 1) {
//                    item {
//                        DayHeader("20 Aug")
//                    }
//                } else if (index == 2) {
//                    item {
//                        DayHeader("Today")
//                    }
//                }

                if (content.author == me) {
                    item {
                        SelfMessageItem(
                            onAuthorClick = { name -> navigateToProfile(name) },
                            msg = content,
                            isUserMe = true,
                            firstMessageFromMe = firstMessageFromMe,
                            lastMessageFromMe = lastMessageFromMe
                        )
                    }
                } else {
                    item {
                        DroidMessageItem(
                            onAuthorClick = { name -> navigateToProfile(name) },
                            msg = content,
                            isUserMe = false,
                            firsMessageFromDroid = !firstMessageFromMe,
                            lastMessageFromDroid = !lastMessageFromMe
                        )
                    }
                }
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                    scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DroidMessageItem(
    onAuthorClick: (String) -> Unit,
    msg: ChatMessage,
    isUserMe: Boolean,
    firsMessageFromDroid: Boolean,
    lastMessageFromDroid: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (lastMessageFromDroid) Modifier.padding(top = 8.dp) else Modifier.padding(top = 16.dp)
    Row(modifier = spaceBetweenAuthors) {
        if (!lastMessageFromDroid) {
            // Avatar
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(msg.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = msg.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        DroidMessage(
            msg = msg,
            isUserMe = isUserMe,
            firsMessageFromDroid = firsMessageFromDroid,
            lastMessageFromDroid = lastMessageFromDroid,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}


@Composable
fun SelfMessageItem(
    onAuthorClick: (String) -> Unit,
    msg: ChatMessage,
    isUserMe: Boolean,
    firstMessageFromMe: Boolean,
    lastMessageFromMe: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (lastMessageFromMe) Modifier.padding(top = 8.dp) else Modifier.padding(top = 16.dp)
    Row(modifier = spaceBetweenAuthors) {
        SelfMessage(
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = firstMessageFromMe,
            isLastMessageByAuthor = lastMessageFromMe,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        if (!lastMessageFromMe) {
            // Avatar
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(msg.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = msg.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
    }
}

@Composable
fun DroidMessage(
    msg: ChatMessage,
    isUserMe: Boolean,
    firsMessageFromDroid: Boolean,
    lastMessageFromDroid: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (!lastMessageFromDroid) {
            DroidTimestamp(msg)
        }
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)

//        if (lastMessageFromDroid) {
//            // Last bubble before next author
//            Spacer(modifier = Modifier.height(8.dp))
//        } else {
//            // Between bubbles
//            Spacer(modifier = Modifier.height(4.dp))
//        }
    }
}

@Composable
fun SelfMessage(
    msg: ChatMessage,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (!isLastMessageByAuthor) {
            SelfTimestamp(msg)
        }
        SelfChatItemBubble(msg, isUserMe, authorClicked = authorClicked)

//        if (isLastMessageByAuthor) {
//            // Last bubble before next author
//            Spacer(modifier = Modifier.height(8.dp))
//        } else {
//            // Between bubbles
//            Spacer(modifier = Modifier.height(4.dp))
//        }
    }
}

@Composable
private fun DroidTimestamp(msg: ChatMessage) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SelfTimestamp(msg: ChatMessage) {
    // Combine author and timestamp for a11y.
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            Text(
                text = msg.timestamp,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alignBy(LastBaseline),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = msg.author,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .alignBy(LastBaseline)
                    .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
            )
        }
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

private val SelfChatBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun ChatItemBubble(
    chatMessage: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                chatMessage = chatMessage,
                isUserMe = isUserMe,
                authorClicked = authorClicked
            )
        }
    }
}

@Composable
fun SelfChatItemBubble(
    chatMessage: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.CenterEnd)) {
            Surface(
                color = backgroundBubbleColor,
                shape = SelfChatBubbleShape
            ) {
                ClickableMessage(
                    chatMessage = chatMessage,
                    isUserMe = isUserMe,
                    authorClicked = authorClicked
                )
            }
        }
    }
}

@Composable
fun ClickableMessage(
    chatMessage: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = chatMessage.content.value,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

private val JumpToBottomThreshold = 56.dp
