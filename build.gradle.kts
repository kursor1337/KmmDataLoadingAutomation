 plugins {
     //trick: for the same plugin versions in all sub-modules
     id("com.android.library").version("7.4.1").apply(false)
     kotlin("multiplatform").version("1.8.10").apply(false)
     id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false

 }
 apply(from = "publish.gradle.kts")