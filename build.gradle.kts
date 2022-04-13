import java.util.Properties

plugins {
    `java-library`
    id("org.javamodularity.moduleplugin") version "1.8.10" apply false
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "io.github.palexdev"
version = "11.13.5"

loadMavenPublishProperties()

allprojects {
    repositories {
        flatDir {
            dirs("${project(":adapter").projectDir}/libs")
        }

        mavenCentral()
    }

}


subprojects {
    if (project.hasProperty("POM_ARTIFACT_ID")) {
        apply {
            plugin("maven-publish")
            plugin("signing")
        }

        tasks.withType<GenerateModuleMetadata>().configureEach {
            enabled = false
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    groupId = project.property("GROUP").toString()
                    version = project.property("VERSION_NAME").toString()
                    artifactId = project.property("POM_ARTIFACT_ID").toString()

                    pom {
                        name.set(project.property("POM_NAME").toString())
                        description.set(project.property("POM_DESCRIPTION").toString())
                        url.set("https://github.com/Glavo/MaterialFX-Java8")

                        licenses {
                            license {
                                name.set("GNU LGPLv3")
                                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                            }
                        }

                        developers {
                            developer {
                                id.set("palexdev")
                                name.set("Alessandro Parisi")
                                url.set("https://github.com/palexdev")
                            }
                            developer {
                                id.set("glavo")
                                name.set("Glavo")
                                email.set("zjx001202@gmail.com")
                            }
                        }

                        scm {
                            url.set("https://github.com/Glavo/kala-common")
                        }
                    }
                }
            }
        }

        if (rootProject.ext.has("signing.key")) {
            signing {
                useInMemoryPgpKeys(
                    rootProject.ext["signing.keyId"].toString(),
                    rootProject.ext["signing.key"].toString(),
                    rootProject.ext["signing.password"].toString(),
                )
                sign(publishing.publications["maven"])
            }
        }
    }
}


fun loadMavenPublishProperties() {
    var secretPropsFile = project.rootProject.file("gradle/maven-central-publish.properties")
    if (!secretPropsFile.exists()) {
        secretPropsFile =
            file(System.getProperty("user.home")).resolve(".gradle").resolve("maven-central-publish.properties")
    }

    if (secretPropsFile.exists()) {
        // Read local.properties file first if it exists
        val p = Properties()
        secretPropsFile.reader().use {
            p.load(it)
        }

        p.forEach { (name, value) ->
            rootProject.ext[name.toString()] = value
        }
    }

    listOf(
        "sonatypeUsername" to "SONATYPE_USERNAME",
        "sonatypePassword" to "SONATYPE_PASSWORD",
        "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
        "signing.keyId" to "SIGNING_KEY_ID",
        "signing.password" to "SIGNING_PASSWORD",
        "signing.key" to "SIGNING_KEY"
    ).forEach { (p, e) ->
        if (!rootProject.ext.has(p)) {
            rootProject.ext[p] = System.getenv(e)
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}


