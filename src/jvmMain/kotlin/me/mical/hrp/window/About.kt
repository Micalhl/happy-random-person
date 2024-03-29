package me.mical.hrp.window

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * happy-random-person
 * me.mical.hrp.window.About
 *
 * @author mical
 * @since 2023/5/17 12:01 AM
 */
@Composable
@Preview
fun About() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Text("2022届九年四班侯林制作，吕老师专用。")
            Text("Made it with ❤️.")
            Text("Copyright © 2022-2023 Mical. All Rights Reserved.")
            Text("Source code: https://github.com/Micalhl/happy-random-person")
            Text("Licence:")
            Text("""
                MIT License

                Copyright (c) 2022-2023 Mical

                Permission is hereby granted, free of charge, to any person obtaining a copy
                of this software and associated documentation files (the "Software"), to deal
                in the Software without restriction, including without limitation the rights
                to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
                copies of the Software, and to permit persons to whom the Software is
                furnished to do so, subject to the following conditions:

                The above copyright notice and this permission notice shall be included in all
                copies or substantial portions of the Software.

                THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
                IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
                FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
                AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
                LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
                OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
                SOFTWARE.
            """.trimIndent())
            Text(" ")
            Text("This project uses TabooLib open-source library, link: https://github.com/TabooLib/TabooLib")
            Text("This project uses Compose-MultiPlatform open-source library, link: https://github.com/jetbrains/compose-multiplatform/")
        }
    }
}
