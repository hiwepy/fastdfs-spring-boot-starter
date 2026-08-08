/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.tobato.fastdfs.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link EnableFastdfs}.
 *
 * <p>Verifies the {@code @EnableFastdfs} annotation meta-attributes
 * (target, retention, inheritance) and that it imports the FastDFS client config.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("EnableFastdfs Tests")
class EnableFastdfsTest {

    @Test
    @DisplayName("EnableFastdfs is an annotation type")
    void testIsAnnotation() {
        assertThat(EnableFastdfs.class.isAnnotation()).isTrue();
    }

    @Test
    @DisplayName("EnableFastdfs is annotated @Target(TYPE)")
    void testTargetTypeOnly() {
        Target target = EnableFastdfs.class.getAnnotation(Target.class);
        assertThat(target).isNotNull();
        assertThat(target.value()).containsExactly(ElementType.TYPE);
    }

    @Test
    @DisplayName("EnableFastdfs is annotated @Retention(RUNTIME)")
    void testRetentionRuntime() {
        Retention retention = EnableFastdfs.class.getAnnotation(Retention.class);
        assertThat(retention).isNotNull();
        assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("EnableFastdfs is annotated @Documented and @Inherited")
    void testDocumentedAndInherited() {
        assertThat(EnableFastdfs.class.isAnnotationPresent(Documented.class)).isTrue();
        assertThat(EnableFastdfs.class.isAnnotationPresent(Inherited.class)).isTrue();
    }

    @Test
    @DisplayName("EnableFastdfs has no declared methods")
    void testNoDeclaredMethods() {
        Method[] methods = EnableFastdfs.class.getDeclaredMethods();
        assertThat(methods).isEmpty();
    }

    @Test
    @DisplayName("EnableFastdfs can be applied to a sample type and read back via reflection")
    void testAppliedAnnotationIsReadable() {
        Annotation[] annotations = Sample.class.getAnnotations();
        assertThat(annotations).anySatisfy(annotation ->
                assertThat(annotation).isInstanceOf(EnableFastdfs.class));
    }

    @EnableFastdfs
    static class Sample {
        // marker type used solely to validate annotation retention
    }
}
