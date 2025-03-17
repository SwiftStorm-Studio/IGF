# IGF - Inventory GUI Framework

Latest version: ![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https://repo1.maven.org/maven2/net/ririfa/igf/maven-metadata.xml&style=plastic&logo=sonatype&label=Central&color=00FF87)

IGF (Inventory GUI Framework) is a lightweight library for creating intuitive and powerful Inventory GUIs in Minecraft plugins. It simplifies the process of building and managing custom GUI interfaces using the native Minecraft inventory system, making it ideal for developers who want to enhance their plugins with an interactive user experience.

## Features
- Easy-to-use API for creating Inventory GUIs.
- Support for different event handlers (click, open, close).
- Built-in utility functions for managing complex GUIs.(I'll implement this in the future)

## Getting Started

### Installation
To include IGF in your project, add the following dependency to your `pom.xml` if you're using Maven:

```xml
<dependency>
    <groupId>net.ririfa</groupId>
    <artifactId>igf</artifactId>
    <version>{version}</version> <!-- Replace {version} with the latest version -->
</dependency>
```

For Gradle, include this in your `build.gradle`:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'net.ririfa:igf:{version}' // Replace {version} with the latest version
}
```

For kotlin DSL:
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("net.ririfa:igf:{version}") // Replace {version} with the latest version
}
```

Hint: You can always find the latest version on [Maven Central](https://central.sonatype.com/artifact/net.rk4z/igf).

### License
This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.