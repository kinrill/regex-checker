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

import org.kinrill.annotation.Regex;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


public class RegexChecker extends AbstractProcessor {

  private static final String WRONG_TARGET_MESSAGE =
      "@Regex can only be applied to a field String declaration";
  private static final String WRONG_TYPE_MESSAGE = "@Regex can only be used on a String";
  private static final String WRONG_MODIFIER_MESSAGE =
      "@Regex can only be used on a final String";
  private static final String NON_CONSTANT_MESSAGE =
      "@Regex can only be used with a constant expression";
  private static final String REGEX_ERROR_MESSAGE = "Invalid regular expression: %s";

  private Types types;
  private Elements elements;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    types = env.getTypeUtils();
    elements = env.getElementUtils();
    messager = env.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

    for (Element e : env.getElementsAnnotatedWith(Regex.class)) {
      ElementKind kind = e.getKind();
      if (kind != ElementKind.FIELD) {
        error(WRONG_TARGET_MESSAGE, e);
        continue;
      }

      if (!Utils.isStringType(e.asType())) {
        error(WRONG_TYPE_MESSAGE, e);
        continue;
      }

      if (!e.getModifiers().contains(Modifier.FINAL)) {
        error(WRONG_MODIFIER_MESSAGE, e);
        continue;
      }

      VariableElement varElement = Utils.asVariableElement(e);
      Object object = varElement.getConstantValue();
      if (object == null) {
        error(NON_CONSTANT_MESSAGE, e);
        continue;
      }

      if (!(object instanceof String)) {
        warn("Unexpected constant type", e);
        continue;
      }

      String value = (String) object;
      if (!validateRegex(e, value)) {
        continue;
      }
    }
    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton("org.kinrill.annotation.Regex");
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  private void error(String message, Element target) {
    messager.printMessage(Diagnostic.Kind.ERROR, message, target);
  }

  private void warn(String message, Element target) {
    messager.printMessage(Diagnostic.Kind.WARNING, message, target);
  }

  private boolean validateRegex(Element element, String value) {
    try {
      Pattern.compile(value);
      return true;
    } catch (PatternSyntaxException pse) {
      error(String.format(REGEX_ERROR_MESSAGE, pse.getMessage()), element);
      return false;
    }
  }

}
