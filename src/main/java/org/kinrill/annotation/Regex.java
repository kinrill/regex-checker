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

package org.kinrill.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use annotation <code>{@literal @}Regex</code> to mark a constant
 * string used as a regular expression.
 *
 * <p>This allows an annotation processor to ensure the constant is a
 * valid regular expression string at compile time.</p>
 *
 * <p>Here is an example:</p>
 * <pre><code>
 * {@literal @}Regex private final String EMAIL_REX = "\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}\b"
 * </code>
 * </pre>
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Regex {
}
