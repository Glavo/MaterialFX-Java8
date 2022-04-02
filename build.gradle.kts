import org.openjfx.gradle.JavaFXOptions

plugins {
    `java-library`
    id("org.openjfx.javafxplugin") version "0.0.12" apply false
    id("org.javamodularity.moduleplugin") version "1.8.10" apply false
}

group = "io.github.palexdev"
version = "11.13.4"

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin("org.openjfx.javafxplugin")
    }

    configure<JavaFXOptions> {
        version = "18"
        modules = listOf("javafx.controls", "javafx.fxml", "javafx.media", "javafx.swing", "javafx.web")
    }
}





