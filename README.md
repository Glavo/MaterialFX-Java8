# MaterialFX Java 8

This repository merges [MaterialFX](https://github.com/palexdev/MaterialFX) and [VirtualizedFX](https://github.com/palexdev/VirtualizedFX),
port them for compatibility with Java 8.

This is the version it is based on:

* [MaterialFX](https://github.com/palexdev/MaterialFX): v11.13.5 (a98d9e998d92948a0edcf4ba7a5f3ad7acfb41a8)
* [VirtualizedFX](https://github.com/palexdev/VirtualizedFX): v11.12.6 (cce95f44ea43056b88078a7c5ae2fe6f291e9fe3)

I have provided a multi release jar as an adapter to make it compatible with both JavaFX 8 and JavaFX 9+,
So it is compatible with Java 8 or later.
However, it currently has some known issues when it runs on Java 8, see the [issue list](https://github.com/Glavo/MaterialFX-Java8/issues).

## Usage

Maven:
```xml
<dependency>
  <groupId>org.glavo.materialfx</groupId>
  <artifactId>materialfx</artifactId>
  <version>11.13.5</version>
</dependency>
```

Gradle:
```kotlin
dependencies {
    implementation("org.glavo.materialfx:materialfx:11.13.5")
}
```

## Incompatibility

While I've barely modified the API when porting, this has one exception:
Since `PopupWindow` in JavaFX 8 contains a `ObservableList<Node> getContent()` method, 
which conflicts with the `getContent` method signature in `MFXPopup`, 
I had to rename the `MFXPopup::contentProperty()` to `contentNodeProperty`,
and renamed the relevant getter/setter methods.

