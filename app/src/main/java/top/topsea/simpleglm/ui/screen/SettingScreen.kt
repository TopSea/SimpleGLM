package top.topsea.simpleglm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tencent.mmkv.MMKV
import top.topsea.simpleglm.settings.CSettings
import top.topsea.simpleglm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController
) {
    val setting = CSettings({ DefaultSettings() }, executeStage = 1, name = "Default")
    val settings = listOf(setting)
    Scaffold(
        topBar = { SettingTopBar(navController) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(paddingValues)
        ) {
            items(settings.size) {
                BasicSettingBlock(modifier = Modifier.padding(8.dp), setting = settings[it])
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SettingTopBar(
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color(dynamicLightColorScheme(context).primary.toArgb()))
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(32.dp)
                .clickable {
                    navController.navigate("main_screen") {
                        popUpTo("main_screen") { inclusive = true }
                    }
                },
            tint = Color.White
        )

        Text(
            text = "Settings",
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.Center),
            fontSize = TextUnit(value = 20F, type = TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun BasicSettingBlock(
    modifier: Modifier,
    setting: CSettings
) {
    Column(modifier = modifier.background(color = Color.White, shape = RoundedCornerShape(16.dp))) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 16.dp)) {
            Text(
                text = setting.commentName,
                fontSize = TextUnit(value = 16F, type = TextUnitType.Sp),
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .align(Alignment.CenterStart),
                fontWeight = FontWeight.Bold
            )

        }
        SplitLine(color = Color.Black)
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            setting.settingsUI()
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DefaultSettings() {
    val context = LocalContext.current

    val kv = MMKV.defaultMMKV()
    val serverIP: String = kv.decodeString(stringResource(id = R.string.server_ip), "http:192.168.0.107:8888")!!
    val chatHistory: Boolean = kv.decodeBool(stringResource(id = R.string.chat_history), true)
    var serverIPStr by remember { mutableStateOf(serverIP) }


    var enableServerIP by remember { mutableStateOf(true) }
    var enableHistory by remember { mutableStateOf(chatHistory) }
    var textColor by remember {
        mutableStateOf(Color.LightGray)
    }

    var btn by remember {
        mutableStateOf("修改")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Text(
                    text = "Server IP: ",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = TextUnit(value = 13F, type = TextUnitType.Sp)
                )
                BasicTextField(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    value = serverIPStr,
                    onValueChange = { serverIPStr = it },
                    enabled = true,
                    readOnly = enableServerIP,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = textColor)
                )
            }
            Button(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterEnd)
                    .height(30.dp),
                onClick = {
                    enableServerIP = !enableServerIP
                    if (!enableServerIP) {
                        textColor = Color.Black
                        btn ="确认"
                    } else {
                        textColor = Color.LightGray
                        btn ="修改"
                        kv.encode(context.resources.getString(R.string.server_ip), serverIPStr)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Text(text = btn)
            }
        }
        SplitLine(color = Color.LightGray)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "启用聊天历史",
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = TextUnit(value = 13F, type = TextUnitType.Sp)
            )

            Checkbox(
                checked = enableHistory,
                onCheckedChange = {
                    enableHistory = !enableHistory
                    kv.encode(context.resources.getString(R.string.chat_history), enableHistory)
                }
            )
        }
    }
}

@Composable
fun SplitLine(color: Color) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = color))
}