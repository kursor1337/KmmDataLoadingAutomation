import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.signing
import java.util.*


apply(plugin = "maven-publish")
apply(plugin = "signing")


// Stub secrets to let the project sync and build without the publication values set up
extra["signing.keyId"] = null
extra["signing.password"] = null
extra["signing.secretKeyRingFile"] = null
extra["ossrhUsername"] = null
extra["ossrhPassword"] = null


// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")

if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        extra[name.toString()] = value
    }
} else {
    extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    extra["signing.password"] = System.getenv("SIGNING_PASSWORD")
    extra["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    extra["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    extra["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}


fun getExtraString(name: String) = extra[name]?.toString()


configure<PublishingExtension> {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("KmmDataLoadingAutomation")
            description.set("Sample Kotlin Multiplatform library that helps automate network requests and data storage")
            url.set("https://github.com/kursor1337/KmmDataLoadingAutomation")

            licenses {
                license {
                    name.set("The MIT License")
                    url.set("https://github.com/kursor1337/KmmDataLoadingAutomation/blob/master/LICENSE")
                }
            }

            developers {
                developer {
                    id.set("kursor1337")
                    name.set("Sergey Kurochkin")
                    email.set("skurochkin298@gmail.com")
                }
            }

            scm {
                url.set("https://github.com/kursor1337/KmmDataLoadingAutomation")
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

configure<SigningExtension> {
    sign(the<PublishingExtension>().publications)
}