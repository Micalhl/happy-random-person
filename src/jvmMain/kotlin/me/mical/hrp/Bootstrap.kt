package me.mical.hrp

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.mical.hrp.window.About
import me.mical.hrp.window.App
import me.mical.hrp.window.MainTray
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.exitProcess

/**
 * happy-random-person
 * me.mical.hrp.Bootstrap
 *
 * @author xiaomu
 * @since 2023/2/23 7:59 PM
 */
val file = File(System.getProperty("user.home"), "hrp.lock")

fun checkEnvironment() {
    if (file.exists()) {
        file.writeText(System.currentTimeMillis().toString())
    } else {
        file.createNewFile()
    }
}

fun main() {
    checkEnvironment()
    try {
        socket = ServerSocket(socketPort)
    } catch (ex: IOException) {
        if (ex.message?.contains("Address already in use") == true) {
            exitProcess(0)
        }
    }
    loadConfig()
    application {
        val isMainWindowVisible = remember { mutableStateOf(true) }
        val isAboutWindowVisible = remember { mutableStateOf(false) }
        FileWatcher.INSTANCE.addSimpleListener(file) {
            if (!isMainWindowVisible.value) {
                isMainWindowVisible.value = true
            }
        }
        MainTray(isMainWindowVisible, isAboutWindowVisible)
        Window(onCloseRequest = { isMainWindowVisible.value = false }, title = "随机点名", icon = painterResource("icon_512x512.png"), visible = isMainWindowVisible.value) {
            App()
        }
        Window(onCloseRequest = { isAboutWindowVisible.value = false }, title = "关于随机点名", icon = painterResource("icon_512x512.png"), visible = isAboutWindowVisible.value) {
            About()
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
var socketPort: Int = 10086 // 学校电脑用, 不需要考虑太多
lateinit var socket: ServerSocket

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
            URL("https://mcstarrysky.oss-cn-beijing.aliyuncs.com/School/zhou-yan-chen-gao3-A1.json")
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
