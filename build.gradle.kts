import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  java
  id("com.gradleup.shadow") version "8.3.0"
  id("co.uzzu.dotenv.gradle") version "4.0.0"
  `maven-publish`
}

group = "pl.miloszgilga"
version = "1.0.1"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

repositories {
  mavenCentral()
}

// prevent loading noop slf4j logger for test server (enable logging with logback)
configurations.configureEach {
  if (name.startsWith("test")) {
    exclude(group = "org.slf4j", module = "slf4j-nop")
  }
}

object Versions {
  const val FLEXMARK = "0.64.8"
  const val GRAALVM = "25.4.4.1.1"
  const val SLF4J = "2.0.20"
  const val ASM = "9.11.1"
  const val JETTY = "12.1.14"
  const val JACKSON = "3.2.4"
  const val FREEMARKER = "2.3.35"
  const val HTMLCOMPRESSOR = "1.5.2"
  const val SLUGIFY = "4.0.1"
  const val MINIFIER = "1.5"
  const val WEBP_IMAGEIO = "0.1.6"
  const val COMMONS_CLI = "1.11.0"
  const val DOTENV = "3.2.0"
  const val PROGRESSBAR = "0.10.2"
  const val JSOUP = "1.23.2"
  const val CLOSURE_COMPILER = "v20261021"
  const val JAVALIN = "7.2.4"
  const val LOGBACK = "1.6.6"
}

dependencies {
  implementation("com.vladsch.flexmark:flexmark:${Versions.FLEXMARK}")
  implementation("com.vladsch.flexmark:flexmark-ext-tables:${Versions.FLEXMARK}")
  implementation("com.vladsch.flexmark:flexmark-ext-gitlab:${Versions.FLEXMARK}")

  implementation("org.graalvm.polyglot:polyglot:${Versions.GRAALVM}")
  implementation("org.graalvm.polyglot:js:${Versions.GRAALVM}")

  implementation("org.slf4j:slf4j-nop:${Versions.SLF4J}")
  implementation("org.slf4j:slf4j-api:${Versions.SLF4J}")

  implementation("tools.jackson.dataformat:jackson-dataformat-yaml:${Versions.JACKSON}")
  implementation("org.freemarker:freemarker:${Versions.FREEMARKER}")
  implementation("com.googlecode.htmlcompressor:htmlcompressor:${Versions.HTMLCOMPRESSOR}")
  implementation("com.github.slugify:slugify:${Versions.SLUGIFY}")
  implementation("net.logicsquad:minifier:${Versions.MINIFIER}")
  implementation("org.sejda.imageio:webp-imageio:${Versions.WEBP_IMAGEIO}")
  implementation("commons-cli:commons-cli:${Versions.COMMONS_CLI}")
  implementation("io.github.cdimascio:dotenv-java:${Versions.DOTENV}")
  implementation("me.tongfei:progressbar:${Versions.PROGRESSBAR}")
  implementation("org.jsoup:jsoup:${Versions.JSOUP}")
  implementation("com.google.javascript:closure-compiler:${Versions.CLOSURE_COMPILER}")

  testImplementation("io.javalin:javalin:${Versions.JAVALIN}")
  testImplementation("ch.qos.logback:logback-classic:${Versions.LOGBACK}")

  constraints {
    testImplementation("org.ow2.asm:asm:${Versions.ASM}")
    testImplementation("org.ow2.asm:asm-commons:${Versions.ASM}")
    testImplementation("org.eclipse.jetty.ee10.websocket:jetty-ee10-websocket-jetty-server:${Versions.JETTY}")
    testImplementation("org.eclipse.jetty:jetty-annotations:${Versions.JETTY}")
  }
}

tasks.jar {
  manifest {
    attributes["Main-Class"] = "pl.miloszgilga.jargonaut.SsgMain"
    attributes["Multi-Release"] = "true" // for multi-release support (fixing GraalVM issues on FatJar)
  }
}

tasks.withType<ShadowJar> {
  archiveBaseName.set("jargonaut")
  archiveClassifier.set("")
  archiveVersion.set(project.version.toString())
  mergeServiceFiles() // merge GraalVM and SLF4J in META-INF/services/
}

publishing {
  publications {
    create<MavenPublication>("jargonautExecutable") {
      artifact(tasks.named("shadowJar"))
      artifactId = "jargonaut"
      pom {
        name.set("JArgonaut SSG")
        description.set("Java-based portfolio static site generator with CLI and configuration management.")
        url.set("https://github.com/milosz08/milosz08")
        inceptionYear.set("2024")
        licenses {
          license {
            name.set("The GNU General Public License, Version 3.0")
            url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
            distribution.set("repo")
          }
        }
        developers {
          developer {
            id.set("milosz08")
            name.set("Miłosz Gilga")
            email.set("miloszgilga@gmail.com")
            url.set("https://miloszgilga.pl")
            roles.set(listOf("Architect", "Developer"))
            timezone.set("Europe/Warsaw")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/milosz08/milosz08.git")
          developerConnection.set("scm:git:ssh://git@github.com:milosz08/milosz08.git")
          url.set("https://github.com/milosz08/milosz08")
        }
        issueManagement {
          system.set("GitHub Issues")
          url.set("https://github.com/milosz08/milosz08/issues")
        }
      }
    }
  }
  repositories {
    maven {
      name = "M2"
      url = uri("https://m2.miloszgilga.pl/releases")
      credentials {
        username = env.fetchOrNull("OSS_M2_USER")
        password = env.fetchOrNull("OSS_M2_PASSWORD")
      }
    }
  }
}

val buildVersionlessJar by tasks.registering(Copy::class) {
  group = "build"
  description = "Create jar copy without version in file name"

  val shadowTask = tasks.named<ShadowJar>("shadowJar")
  dependsOn(shadowTask)

  from(shadowTask.flatMap { it.archiveFile })
  into(layout.buildDirectory.dir("libs"))

  rename { "jargonaut.jar" }
}
