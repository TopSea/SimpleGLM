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

import androidx.compose.runtime.mutableStateOf
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_CLOUDS
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_FLAMINGO
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_MELTING
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_PINK_HEART
import top.topsea.simpleglm.chat.EMOJIS.EMOJI_POINTS

private val initialChatMessages = ArrayList<ChatMessage>()

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
