import java.io.RandomAccessFile

val java8SourceSet = sourceSets.create("java8") {
    java {
        srcDirs("src/main/java8")
    }
}

repositories {
    flatDir {
        dirs("${project(":adapter").projectDir}/libs")
    }
    mavenCentral()
}

dependencies {
    "java8CompileOnly"("javafx:jfx8")
}


javafx {
    configuration = "compileOnly"
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

tasks.named<JavaCompile>("compileJava8Java") {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.create<Jar>("multiJar") {
    archiveClassifier.set(null as String?)

    dependsOn("compileJava", "compileJava8Java")

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