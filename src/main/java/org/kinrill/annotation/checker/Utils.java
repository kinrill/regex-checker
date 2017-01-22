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

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

final class Utils {

  static boolean isTypeElement(Element element) {
    return element.getKind().isClass() || element.getKind().isInterface();
  }

  static TypeElement asTypeElement(Element base) {
    return base.accept(TypeElementVisitor.INSTANCE, null);
  }

  static VariableElement asVariableElement(Element base) {
    return base.accept(VariableElementVisitor.INSTANCE, null);
  }

  static boolean isStringType(TypeMirror typeMirror) {
    return typeMirror.accept(IsDeclaredTypeMirrorVisitor.IS_STRING, null);
  }


  private static final class IsDeclaredTypeMirrorVisitor
      extends SimpleTypeVisitor6<Boolean, Void> {

    private static final IsDeclaredTypeMirrorVisitor IS_STRING =
        new IsDeclaredTypeMirrorVisitor(String.class);

    private final String requiredName;

    private IsDeclaredTypeMirrorVisitor(Class<?> clazz) {
      super(Boolean.FALSE);
      requiredName = clazz.getCanonicalName();
    }

    @Override
    public Boolean visitDeclared(DeclaredType declaredType, Void ignore) {
      Element element = declaredType.asElement();
      if (!isTypeElement(element)) {
        return Boolean.FALSE;
      }
      return asTypeElement(element).getQualifiedName().contentEquals(requiredName);
    }
  }


  private static final class VariableElementVisitor
      extends SimpleElementVisitor6<VariableElement,Void> {

    private static final VariableElementVisitor INSTANCE = new VariableElementVisitor();

    @Override
    public VariableElement defaultAction(Element element, Void ignore) {
      throw new IllegalArgumentException("Not a VariableElement:" + element);
    }

    @Override
    public VariableElement visitVariable(VariableElement variableElement, Void ignore) {
      return variableElement;
    }
  }


  private static final class TypeElementVisitor
      extends SimpleElementVisitor6<TypeElement,Void> {

    private static final TypeElementVisitor INSTANCE = new TypeElementVisitor();

    @Override
    public TypeElement defaultAction(Element element, Void ignore) {
      throw new IllegalArgumentException("Not a TypeElement:" + element);
    }

    @Override
    public TypeElement visitType(TypeElement typeElement, Void ignore) {
      return typeElement;
    }
  }

}
