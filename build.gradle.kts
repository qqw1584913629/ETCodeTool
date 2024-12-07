plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    // 阿里云镜像
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
    
    // 华为云镜像
    maven { url = uri("https://repo.huaweicloud.com/repository/maven") }
    
    // 原始仓库作为备份
    mavenCentral()
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
    maven { url = uri("https://cache-redirector.jetbrains.com/intellij-repository/releases") }
    maven { url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies") }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    type.set("RD") // Rider IDE
    version.set("2023.3.3")
    
    // 移除 plugins 配置
    plugins.set(listOf())
}

dependencies {
    // 使用对应版本的依赖
    implementation("com.jetbrains.rd:rd-core:2023.3.3")
    implementation("com.jetbrains.rd:rd-framework:2023.3.3")
    implementation("com.jetbrains.rd:rd-swing:2023.3.3")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        // 支持从较早版本开始
        sinceBuild.set("221") // 对应 2022.1
        // 不设置 untilBuild，这样就可以支持所有更新的版本
        untilBuild.set("")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
