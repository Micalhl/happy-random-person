package me.mical.hrp

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import taboolib.common.util.random
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Timer
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.timerTask

/**
 * happy-random-person
 * me.mical.hrp.Bootstrap
 *
 * @author xiaomu
 * @since 2023/2/23 7:59 PM
 */
@OptIn(ExperimentalUnitApi::class)
@Composable
@Preview
fun Update() {
    // TODO
}

@OptIn(ExperimentalUnitApi::class)
@Composable
@Preview
fun Settings() {
    // TODO
}

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
                //
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
                    students[students.size + 1] = getStudent(cheat)
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
                Spacer(Modifier.width(10.dp))
                Button(onClick = {
                    end = getNum(text)
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

fun main() {
    loadConfig()
    application {
        Window(onCloseRequest = ::exitApplication, title = "高一五班御用随机点名", icon = painterResource("icon_512x512.png")) {
            App()
        }
    }
}

fun getNum(name: String): Int {
    return students.filterKeys { it <= amount }.filterValues { it.contains(name) }.keys.first()
}

fun getStudent(num: Int): String {
    return (students[num] ?: "没有录入学生").let { it.split("/").random() }
}

val students = ConcurrentHashMap<Int, String>()
lateinit var title: List<String>
var cheat: Int = -1
var amount: Int = 0
var end: Int = -1 // initialize

private fun loadConfig() {
    val file = File(System.getProperty("user.home"), "hrp.json")
    if (!file.exists()) {
        file.createNewFile()
    }
    var content = ""
    var connection: HttpURLConnection? = null
    val remote = try {
        val timeout = 5000
        val url =
            URL("https://mcstarrysky.oss-cn-beijing.aliyuncs.com/School/hrp1.1.1.json")
        connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = timeout
        val buffer = StringBuilder(255)
        BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)).use { reader ->
            val buffer0 = CharArray(255)
            while (true) {
                val length = reader.read(buffer0)
                if (length == -1) break
                buffer.append(buffer0, 0, length)
            }
        }
        content = buffer.toString().trim { it <= ' ' }
        Configuration.loadFromString(content, type = Type.JSON)
    } catch (_: IOException) {
        Configuration.empty(type = Type.JSON)
    } finally {
        connection?.disconnect()
    }
    val config = Configuration.loadFromFile(file)
    if (remote.saveToString() != config.saveToString()) {
        if (remote.saveToString() == "{}") return
        remote.saveToFile(file)
        config.reload()
    }
    title = config.getStringList("title")
    for (key in config.getConfigurationSection("students")?.getKeys(false) ?: emptySet()) {
        val number = key.toIntOrNull() ?: continue
        students[number] = config.getString("students.$key") ?: continue
    }
    cheat = config.getInt("cheat", -1)
    amount = students.size
}