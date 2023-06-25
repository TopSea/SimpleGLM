/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.topsea.simpleglm.chat

import top.topsea.simpleglm.chat.EMOJIS.EMOJI_CLOUDS
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_FLAMINGO
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_MELTING
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_PINK_HEART
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_POINTS

private val initialChatMessages = listOf(
    ChatMessage(
        "me",
        "Check it out!",
        "8:07 PM"
    ),
    ChatMessage(
        "me",
        "Thank you!$EMOJI_PINK_HEART",
        "8:06 PM",
    ),
    ChatMessage(
        "Taylor Brooks",
        "You can use all the same stuff",
        "8:05 PM"
    ),
    ChatMessage(
        "Taylor Brooks",
        "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
        "8:05 PM"
    ),
    ChatMessage(
        "John Glenn",
        "Compose newbie as well $EMOJI_FLAMINGO, have you looked at the JetNews sample? " +
            "Most blog posts end up out of date pretty fast but this sample is always up to " +
            "date and deals with async data loading (it's faked but the same idea " +
            "applies) $EMOJI_POINTS https://goo.gle/jetnews",
        "8:04 PM"
    ),
    ChatMessage(
        "me",
        "Compose newbie: I’ve scourged the internet for tutorials about async data " +
            "loading but haven’t found any good ones $EMOJI_MELTING $EMOJI_CLOUDS. " +
            "What’s the recommended way to load async data and emit composable widgets?",
        "8:03 PM"
    )
)

val exampleUiState = ConversationUiState(
    initialChatMessages = initialChatMessages,
)


object EMOJIS {
    // EMOJI 15
    const val EMOJI_PINK_HEART = "\uD83E\uDE77"

    // EMOJI 14 🫠
    const val EMOJI_MELTING = "\uD83E\uDEE0"

    // ANDROID 13.1 😶‍🌫️
    const val EMOJI_CLOUDS = "\uD83D\uDE36\u200D\uD83C\uDF2B️"

    // ANDROID 12.0 🦩
    const val EMOJI_FLAMINGO = "\uD83E\uDDA9"

    // ANDROID 12.0  👉
    const val EMOJI_POINTS = " \uD83D\uDC49"
}
