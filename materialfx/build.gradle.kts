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

tasks.compileJava {
    sourceCompatibility = "9"
    targetCompatibility = "9"

    modularity.inferModulePath.set(true)
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    api(project(":VirtualizedFX"))
    api(project(":adapter"))
}

javafx {
    configuration = "compileOnly"
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

artifacts {
    archives(tasks["javadocJar"])
    archives(tasks["sourcesJarBuild"])
    archives(tasks["jar"])
}

tasks.jar {
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
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

