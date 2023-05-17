import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "me.mical"
version = "1.2.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("http://ptms.ink:8081/repository/releases/")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    commonMainImplementation("io.izzel.taboolib:common:6.0.10-95")
    commonMainImplementation("io.izzel.taboolib:common-5:6.0.10-95")
    commonMainImplementation("io.izzel.taboolib:module-configuration:6.0.10-95")
    commonMainImplementation("com.electronwill.night-config:json:3.6.6")
    commonMainImplementation("com.electronwill.night-config:toml:3.6.6")
    commonMainImplementation("org.apache.commons:commons-lang3:3.12.0")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "me.mical.hrp.BootstrapKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "happy-random-person"
            packageVersion = "1.2.0"
            windows.shortcut = true
            windows {
                iconFile.set(project.file("launcher/icon.ico"))
            }
            macOS {
                iconFile.set(project.file("launcher/icon.icns"))
            }
        }
    }
}
