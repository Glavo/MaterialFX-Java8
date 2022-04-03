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