![Build Status](https://api.travis-ci.org/kinrill/regex-checker.svg?branch=master)

# Regex checker

This repository contains an annotation checker that verifies (at
compile time) a Java String used to define a regular expression is
valid.

## Usage

Within your code, annotate a String constant you want to use as a
regular expression with `@Regex`.

```java
@Regex private final static String EMAIL_REX =
  "\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}\b";
```

You'll now get a compile time error if the regular expression is
invalid.

**Note**: The annotation is only retained in the source, so there is no
increase in the size of your compiled code.

## Gradle instructions for Java projects

Configure classpath and dependencies in `build.gradle`:

```groovy
buildscript {
   dependencies {
    classpath 'net.ltgt.gradle:gradle-apt-plugin:0.9'
  }
}

....

repositories {
  jcenter()
}

...

dependencies {
  compileOnly 'org.kinrill.annotation.regex:regex:0.1'
  apt 'org.kinrill.annotation.regex:regex:0.1'
}
```


## Gradle instructions for Android projects

If you are using an Android gradle plugin version 2.2.0 or above, you
can just use `annotationProcessor` in the compiler dependency.

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'org.kinrill.annotation.regex:regex:0.1'
  annotationProcessor 'org.kinrill.annotation.regex:regex:0.1'
}

```

If you are using an Android gradle plugin version below 2.2.0, you
need to include the `android-apt` plugin first.

```groovy
buildscript {
  repositories {
    mavenCentral()
   }
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}
```

Apply the `android-apt` plugin in your module level `build.gradle` and
add the Regex checker dependencies:

```groovy

repositories {
  jcenter()
}

apply plugin: 'android-apt'

android {
  ...
}

dependencies {
  compile 'org.kinrill.annotation.regex:regex:0.1'
  apt 'org.kinrill.annotation.regex:regex:0.1'
}
```
