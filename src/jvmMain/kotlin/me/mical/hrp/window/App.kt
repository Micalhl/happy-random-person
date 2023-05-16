package me.mical.hrp.window

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import me.mical.hrp.*
import taboolib.common.util.random
import java.util.*
import kotlin.concurrent.timerTask

/**
 * happy-random-person
 * me.mical.hrp.window.App
 *
 * @author mical
 * @since 2023/5/16 11:47 PM
 */
@OptIn(ExperimentalUnitApi::class)
@Composable
@Preview
fun App() {
    var tasking = false
    var text by remember { mutableStateOf(title.random()) }
    var button by remember { mutableStateOf("开始点名") }
    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(200.dp))
            Text(text, style = TextStyle(fontSize = TextUnit(5f, TextUnitType.Em)))
            Spacer(Modifier.height(150.dp))
            Button(onClick = {
                tasking = !tasking
                if (end != -1) {
                    text = getStudent(end)
                }
                button = if (tasking) "停止点名" else "开始点名"
            }) {
                Text(
                    text = button
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    end = getNum(text)
                }) {
                    Text(
                        text = "千万别点"
                    )
                }
                Spacer(Modifier.width(10.dp))
                Button(onClick = {
                    val delete = hashSetOf<Int>()
                    for (key in students.keys()) {
                        if (key > amount) {
                            delete.add(key)
                        }
                    }
                    delete.forEach { students.remove(it) }
                    end = -1
                }) {
                    Text(
                        text = "千万别点"
                    )
                }
            }
        }
    }
    Timer().schedule(timerTask {
        if (tasking) {
            text = getStudent(random(1, students.size))
        }
    }, 0, 50)
}