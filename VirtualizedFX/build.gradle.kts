plugins {
    `java-library`
    id("org.openjfx.javafxplugin")
    id("org.javamodularity.moduleplugin")
}

group = "io.github.palexdev"
version = "11.2.6"

repositories {
    mavenCentral()

    flatDir {
        dirs("libs")
    }
}

tasks.compileJava {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

javafx {
    configuration = "compileOnly"
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    testImplementation("org.testfx:testfx-core:4.0.16-alpha")
    testImplementation("org.testfx:testfx-junit5:4.0.16-alpha")
    testImplementation("scenicview:scenicview")
    testImplementation("org.fxmisc.flowless:flowless:0.6.9")
    testImplementation("org.kordamp.ikonli:ikonli-core:12.3.0")
    testImplementation("org.kordamp.ikonli:ikonli-javafx:12.3.0")
    testImplementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.0")
    testImplementation(project(":materialfx"))
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
            "Bundle-Description" to "Alternative VirtualFlows for JavaFX",
            "Bundle-SymbolicName" to "io.github.palexdev",
            "Export-Package" to "io.github.palexdev.virtualizedfx.*"
        )
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