import java.io.RandomAccessFile

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
    implementation("fr.brouillard.oss:cssfx:11.5.1")
    implementation("org.kordamp.ikonli:ikonli-core:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.2.0")
    implementation(project(":VirtualizedFX"))
    implementation(project(":materialfx"))
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

tasks.compileJava {
    options.release.set(9)
    doLast {
        val tree = fileTree(destinationDirectory)
        tree.include("**/*.class")
        tree.exclude("module-info.class")
        tree.forEach {
            RandomAccessFile(it, "rw").use { rf ->
                rf.seek(7)   // major version
                rf.write(52)   // java 8
                rf.close()
            }
        }
    }
}

tasks.shadowJar {
    manifest {
        attributes("Multi-Release" to "true")
    }

    dependencies {
        exclude(dependency("org.openjfx:.*"))
    }
}

tasks.compileTestJava {
    extensions.configure<org.javamodularity.moduleplugin.extensions.CompileTestModuleOptions> {
        isCompileOnClasspath = true
    }
}

tasks.test {
    useJUnitPlatform()
    extensions.configure<org.javamodularity.moduleplugin.extensions.TestModuleOptions> {
        runOnClasspath = true
    }
}