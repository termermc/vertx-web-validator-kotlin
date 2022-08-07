import java.net.URI

plugins {
    kotlin("jvm") version "1.7.10"

    id("org.jetbrains.dokka") version "1.7.10"
    id("maven-publish")
    id("signing")

    `java-library`
}

group = "net.termer.vertx.kotlin.validation"
version = "2.0.0"

val archivesBaseName = "vertx-web-validator-kotlin"
val projVersion = version as String
val vertxVersion = "4.3.2"

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Vert.x
    compileOnly("io.vertx:vertx-core:$vertxVersion")
    compileOnly("io.vertx:vertx-web:$vertxVersion")
    testImplementation("io.vertx:vertx-core:$vertxVersion")
    testImplementation("io.vertx:vertx-web:$vertxVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    // Docs
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.10")
}

tasks.test {
    useJUnitPlatform()
}

// Set JVM version
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("8"))
    }
}
tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.create("kdoc") {
    dependsOn("dokkaHtml")
}
tasks.javadoc {
    dependsOn("dokkaJavadoc")
}

tasks.create("javadocJar", org.gradle.jvm.tasks.Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from("build/dokka/javadoc")
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.dokkaHtml {
    moduleName.set(archivesBaseName)
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class) {
            groupId = "net.termer.vertx.kotlin.validation"
            artifactId = archivesBaseName
            version = projVersion

            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set(archivesBaseName)
                description.set("A simple Kotlin library for null-safe Vert.x Web request validation")
                url.set("https://github.com/termermc/vertx-web-validator-kotlin")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("termer")
                        name.set("Michael Termer")
                        email.set("termer@termer.net")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/termermc/vertx-web-validator-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/termermc/vertx-web-validator-kotlin.git")
                    url.set("https://github.com/termermc/vertx-web-validator-kotlin")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = URI.create("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = properties["nexusUsername"] as String
                password = properties["nexusPassword"] as String
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}