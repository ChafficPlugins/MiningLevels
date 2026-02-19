# Developer API

MiningLevels can be used as a dependency in your own plugins.

## Maven (JitPack)

Add the JitPack repository and the MiningLevels dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.github.ChafficPlugins</groupId>
        <artifactId>MiningLevels</artifactId>
        <version>LATEST</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Replace `LATEST` with a specific version tag from [GitHub Releases](https://github.com/ChafficPlugins/MiningLevels/releases) for reproducible builds.

## Gradle (JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ChafficPlugins:MiningLevels:LATEST'
}
```

## Usage

Add MiningLevels as a dependency or soft-dependency in your `plugin.yml`:

```yaml
depend: [MiningLevels]
# or
softdepend: [MiningLevels]
```

For available API classes and methods, refer to the [source code](https://github.com/ChafficPlugins/MiningLevels).

<!-- TODO: Document specific API events and hooks once they are formally documented in the wiki -->
