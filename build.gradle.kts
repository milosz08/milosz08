plugins {
  java
}

group = "pl.miloszgilga"
version = "1.0.0"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.vladsch.flexmark:flexmark:0.64.8")
  implementation("com.vladsch.flexmark:flexmark-ext-tables:0.64.8")
  implementation("com.vladsch.flexmark:flexmark-ext-gitlab:0.64.8")
  implementation("tools.jackson.dataformat:jackson-dataformat-yaml:3.2.4")
  implementation("org.freemarker:freemarker:2.3.35")
  implementation("com.googlecode.htmlcompressor:htmlcompressor:1.5.2")
  implementation("com.github.slugify:slugify:4.0.1")
  implementation("net.logicsquad:minifier:1.5")
  implementation("org.sejda.imageio:webp-imageio:0.1.6")
  implementation("commons-cli:commons-cli:1.11.0")
  implementation("io.github.cdimascio:dotenv-java:3.2.0")
  implementation("org.graalvm.polyglot:polyglot:25.4.4.1.1")
  implementation("org.graalvm.polyglot:js:25.4.4.1.1")
  implementation("me.tongfei:progressbar:0.10.2")
  implementation("org.slf4j:slf4j-nop:2.0.20")
  implementation("org.jsoup:jsoup:1.23.2")
  implementation("com.google.javascript:closure-compiler:v20261021")
}
