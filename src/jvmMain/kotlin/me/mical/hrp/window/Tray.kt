package me.mical.hrp.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberTrayState

/**
 * happy-random-person
 * me.mical.hrp.window.Tray
 *
 * @author mical
 * @since 2023/5/16 11:48 PM
 */
@Composable
fun ApplicationScope.MainTray(
    isMainWindowVisible: MutableState<Boolean>,
    isAboutWindowVisible: MutableState<Boolean>
) {
    Tray(
        state = rememberTrayState(),
        icon = painterResource("icon_512x512.png"),
        menu = {
            Item(
                "打开",
                onClick = {
                    isMainWindowVisible.value = true
                }
            )
            Item(
                "关于",
                onClick = {
                    isAboutWindowVisible.value = true
                }
            )
            Separator()
            Item(
                "退出",
                onClick = ::exitApplication
            )
        }
    )
}