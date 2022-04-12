buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.glavo:module-info-compiler:1.2")
    }
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()

    flatDir {
        dirs("${project(":demo").projectDir}/libs")
    }
}

dependencies {
    testImplementation("org.testfx:testfx-core:4.0.16-alpha")
    testImplementation("org.testfx:testfx-junit5:4.0.16-alpha")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("scenicview:scenicview")
    implementation("org.fxmisc.cssfx:cssfx:1.1.1")
    implementation("org.kordamp.ikonli:ikonli-core:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.2.0")
    implementation(project(":VirtualizedFX"))
    implementation(project(":materialfx"))

    compileOnly("javafx:jfx8")
}

application {
    mainModule.set("MaterialFX.Demo")
    val main = project.findProperty("chooseMain").toString()
    if (main != "null" && main.trim().isNotEmpty()) {
        mainClass.set(main)
    } else {
        mainClass.set("io.github.palexdev.materialfx.demo.Demo")
    }
}

val compileModuleInfo = tasks.create<org.glavo.mic.tasks.CompileModuleInfo>("compileModuleInfo") {
    sourceFile.set(file("src/main/module-info.java"))
    targetFile.set(buildDir.resolve("classes/java/module-info/module-info.class"))
}

tasks.compileJava {
    options.release.set(8)
}

tasks.jar {
    dependsOn(compileModuleInfo)

    from(compileModuleInfo.targetFile)
}

tasks.shadowJar {
    manifest {
        attributes("Multi-Release" to "true")
    }

    dependencies {
        exclude(dependency("org.openjfx:.*"))
    }
}

tasks.test {
    useJUnitPlatform()
}