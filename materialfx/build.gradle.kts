buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath("org.glavo:module-info-compiler:1.2")
    }
}


plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()

    flatDir {
        dirs("${project(":demo").projectDir}/libs")
    }
}

val compileModuleInfo = tasks.create<org.glavo.mic.tasks.CompileModuleInfo>("compileModuleInfo") {
    sourceFile.set(file("src/main/module-info.java"))
    targetFile.set(buildDir.resolve("classes/java/module-info/module-info.class"))
}

tasks.compileJava {
    options.release.set(8)
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    compileOnly("javafx:jfx8")
    api(project(":VirtualizedFX"))
    api(project(":adapter"))
}

tasks.javadoc {
    exclude("**/*.html", "META-INF/**")

    (options as StandardJavadocDocletOptions).also {
        it.use(true)
        it.splitIndex(true)
        it.encoding("UTF-8")
        it.author(true)
        it.version(true)
        it.windowTitle("$project.name $project.version API")
        it.docTitle("$project.name $project.version API")
        it.links(
            "https://docs.oracle.com/en/java/javase/11/docs/api",
            "https://openjfx.io/javadoc/17"
        )
    }

}

tasks.create<Jar>("javadocJar") {
    dependsOn(tasks.javadoc)
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

tasks.create<Jar>("sourcesJarBuild") {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    (publications["maven"] as MavenPublication).apply {
        from(components["java"])
        artifact(tasks["javadocJar"])
        artifact(tasks["sourcesJarBuild"])
    }
}

tasks.jar {
    dependsOn(compileModuleInfo)

    from(compileModuleInfo.targetFile)

    manifest {
        attributes(
            "Bundle-Name" to project.name,
            "Bundle-Description" to "Material controls for JavaFX",
            "Bundle-SymbolicName" to "io.github.palexdev",
            "Export-Package" to "io.github.palexdev.materialfx.*, io.github.palexdev.materialfx.demo.*"
        )
    }
}

tasks.shadowJar {
    mergeServiceFiles()
    dependencies {
        include(project(":VirtualizedFX"))
        include(project(":adapter"))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

