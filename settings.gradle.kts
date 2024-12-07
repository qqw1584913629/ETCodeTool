pluginManagement {
    repositories {
        // 阿里云镜像
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        
        // 华为云镜像
        maven { url = uri("https://repo.huaweicloud.com/repository/maven") }
        
        // JetBrains 的镜像
        maven { url = uri("https://mirrors.huaweicloud.com/repository/maven/com/jetbrains/intellij/") }
        
        // 原始仓库作为备份
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
        maven { url = uri("https://cache-redirector.jetbrains.com/intellij-repository/releases") }
        maven { url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies") }
    }
}

rootProject.name = "demo"