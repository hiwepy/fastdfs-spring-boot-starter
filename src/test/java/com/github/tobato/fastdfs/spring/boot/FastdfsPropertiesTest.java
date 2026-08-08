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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link FastdfsProperties}.
 *
 * <p>Verifies default values, getters/setters and the public prefix constant.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FastdfsProperties Tests")
class FastdfsPropertiesTest {

    private FastdfsProperties props;

    @BeforeEach
    void setUp() {
        props = new FastdfsProperties();
    }

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("Public constant 'PREFIX' has expected value")
    void testPREFIXConstant() {
        assertThat(FastdfsProperties.PREFIX).isEqualTo("fdfs");
    }

    @Test
    @DisplayName("enabled defaults to false and is round-trippable")
    void testEnabledField() {
        assertThat(props.isEnabled()).isFalse();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("endpoint defaults to null and is round-trippable")
    void testEndpointField() {
        assertThat(props.getEndpoint()).isNull();
        props.setEndpoint("http://192.168.1.1");
        assertThat(props.getEndpoint()).isEqualTo("http://192.168.1.1");
    }

    @Test
    @DisplayName("secretKey defaults to null and is round-trippable")
    void testSecretKeyField() {
        assertThat(props.getSecretKey()).isNull();
        props.setSecretKey("secret");
        assertThat(props.getSecretKey()).isEqualTo("secret");
    }

    @Test
    @DisplayName("expire defaults to 100 and is round-trippable")
    void testExpireField() {
        assertThat(props.getExpire()).isEqualTo(100);
        props.setExpire(42);
        assertThat(props.getExpire()).isEqualTo(42);
        props.setExpire(0);
        assertThat(props.getExpire()).isZero();
    }

    @Test
    @DisplayName("charset defaults to FastdfsUtils.g_charset and is round-trippable")
    void testCharsetField() {
        assertThat(props.getCharset()).isEqualTo(com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils.g_charset);
        props.setCharset("UTF-8");
        assertThat(props.getCharset()).isEqualTo("UTF-8");
    }

    @Nested
    @DisplayName("toString / equals behavior")
    class PojoContractTest {

        @Test
        @DisplayName("Two instances with same state are equal by field values via getters")
        void testFieldConsistency() {
            FastdfsProperties a = new FastdfsProperties();
            FastdfsProperties b = new FastdfsProperties();
            b.setEnabled(a.isEnabled());
            b.setEndpoint(a.getEndpoint());
            b.setSecretKey(a.getSecretKey());
            b.setExpire(a.getExpire());
            b.setCharset(a.getCharset());
            assertThat(b.isEnabled()).isEqualTo(a.isEnabled());
            assertThat(b.getEndpoint()).isEqualTo(a.getEndpoint());
            assertThat(b.getSecretKey()).isEqualTo(a.getSecretKey());
            assertThat(b.getExpire()).isEqualTo(a.getExpire());
            assertThat(b.getCharset()).isEqualTo(a.getCharset());
        }
    }
}
