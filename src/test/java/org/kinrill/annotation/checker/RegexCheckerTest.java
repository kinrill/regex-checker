/**
 * Copyright 2017 Kin Yeung
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kinrill.annotation.checker;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

public class RegexCheckerTest {

  @Test
  public void verifyClassTarget() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "@Regex",
            "final class HelloWorld {",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile()
        .withErrorContaining(
            "@Regex can only be applied to a field String declaration");
  }

  @Test
  public void verifyMethodTarget() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  @Regex",
            "  String foo() {",
            "    return null;",
            "  }",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile()
        .withErrorContaining(
            "@Regex can only be applied to a field String declaration");
  }

  @Test
  public void verifyLocalVariableTarget() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  String foo() {",
            "    @Regex final String bar = \"x\";",
            "    return null;",
            "  }",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile();
    // annotation processing on local variables is a bit hit-or-miss,
    // so we simply look for a compilation error but not whether we or
    // javac found the issue.
  }

  @Test
  public void verifyIntFieldTarget() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  @Regex final static int BAD = 0;",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile()
        .withErrorContaining("@Regex can only be used on a String");
  }

  @Test
  public void verifyFinalFieldTarget() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  @Regex static String BAD = \"abc\";",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile()
        .withErrorContaining("@Regex can only be used on a final String");
  }

  @Test
  public void verifyBadRegex() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  @Regex final static String BAD_REGEX = \"abc[\";",
            "}"))
        .processedWith(new RegexChecker())
        .failsToCompile()
        .withErrorContaining("Invalid regular expression: ");
  }

  @Test
  public void verifyGoodRegex() {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceLines(
            "HelloWorld",
            "import org.kinrill.annotation.Regex;",
            "final class HelloWorld {",
            "  @Regex final static String GOOD_REGEX = \"abc[a-z]+\";",
            "}"))
        .processedWith(new RegexChecker())
        .compilesWithoutWarnings();
  }

}
