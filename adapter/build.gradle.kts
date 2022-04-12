buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.glavo:module-info-compiler:1.2")
    }
}

plugins {
    id("org.openjfx.javafxplugin") version "0.0.12"
}

val java8SourceSet = sourceSets.create("java8") {
    java {
        srcDirs("src/main/java8")
    }
}

dependencies {
    "java8CompileOnly"("javafx:jfx8")
}

tasks.create<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}

tasks.create<Jar>("sourcesJarBuild") {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

javafx {
    version = "18"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media", "javafx.swing", "javafx.web")
    configuration = "compileOnly"
}

tasks.compileJava {
    options.release.set(8)
}

val compileModuleInfo = tasks.create<org.glavo.mic.tasks.CompileModuleInfo>("compileModuleInfo") {
    sourceFile.set(file("src/main/module-info.java"))
    targetFile.set(buildDir.resolve("classes/java/module-info/module-info.class"))
}


tasks.named<JavaCompile>("compileJava8Java") {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.create<Jar>("multiJar") {
    archiveClassifier.set(null as String?)

    dependsOn("compileJava", "compileJava8Java", compileModuleInfo)

    from(compileModuleInfo.targetFile)
    from(java8SourceSet.output)
    into("META-INF/versions/9") {
        from(sourceSets["main"].output)
    }

    manifest.attributes(
        "Multi-Release" to "true"
    )
}

tasks.jar {
    dependsOn(tasks["multiJar"])

    enabled = false
}

publishing {
    (publications["maven"] as MavenPublication).apply {
        artifact(tasks["multiJar"])
        artifact(tasks["javadocJar"])
        artifact(tasks["sourcesJarBuild"])
    }
}