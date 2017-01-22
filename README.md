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
