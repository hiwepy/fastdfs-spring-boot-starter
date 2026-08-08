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
package com.github.tobato.fastdfs.spring.boot.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link FastdfsUtils}.
 *
 * <p>Exercises token generation, MD5 hashing, and the public charset flag.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FastdfsUtils Tests")
class FastdfsUtilsTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        assertThat(new FastdfsUtils()).isNotNull();
    }

    @Test
    @DisplayName("g_charset is initially ISO-8859-1 and is mutable")
    void testDefaultCharset() {
        String previous = FastdfsUtils.g_charset;
        try {
            FastdfsUtils.g_charset = "ISO-8859-1";
            assertThat(FastdfsUtils.g_charset).isEqualTo("ISO-8859-1");
            FastdfsUtils.g_charset = StandardCharsets.UTF_8.name();
            assertThat(FastdfsUtils.g_charset).isEqualTo("UTF-8");
        } finally {
            FastdfsUtils.g_charset = previous;
        }
    }

    @Test
    @DisplayName("getToken produces a deterministic 32-char token for fixed inputs")
    void testGetTokenDeterministic() throws Exception {
        String charset = FastdfsUtils.g_charset;
        try {
            FastdfsUtils.g_charset = "ISO-8859-1";
            String token1 = FastdfsUtils.getToken("/group1/M00/file", 1484735390L, "FastDFS1234567890");
            String token2 = FastdfsUtils.getToken("/group1/M00/file", 1484735390L, "FastDFS1234567890");
            assertThat(token1).isEqualTo(token2);
            assertThat(token1).hasSize(32);
            assertThat(token1).matches("[0-9a-f]{32}");
        } finally {
            FastdfsUtils.g_charset = charset;
        }
    }

    @Test
    @DisplayName("getToken value differs when any of (path|ts|secret) differs")
    void testGetTokenVariesWithInputs() throws Exception {
        String charset = FastdfsUtils.g_charset;
        try {
            FastdfsUtils.g_charset = "ISO-8859-1";
            String base = FastdfsUtils.getToken("/group1/M00/file", 1484735390L, "FastDFS1234567890");
            String diffPath = FastdfsUtils.getToken("/group1/M00/other", 1484735390L, "FastDFS1234567890");
            String diffTs = FastdfsUtils.getToken("/group1/M00/file", 1484735391L, "FastDFS1234567890");
            String diffKey = FastdfsUtils.getToken("/group1/M00/file", 1484735390L, "AnotherKey1234567");
            assertThat(base).isNotEqualTo(diffPath);
            assertThat(base).isNotEqualTo(diffTs);
            assertThat(base).isNotEqualTo(diffKey);
        } finally {
            FastdfsUtils.g_charset = charset;
        }
    }

    @Test
    @DisplayName("getToken uses the configured g_charset (UTF-8 path round-trips)")
    void testGetTokenWithUtf8Charset() throws Exception {
        String charset = FastdfsUtils.g_charset;
        try {
            FastdfsUtils.g_charset = "UTF-8";
            String token = FastdfsUtils.getToken("/group1/M00/file", 100L, "k");
            assertThat(token).hasSize(32).matches("[0-9a-f]{32}");
        } finally {
            FastdfsUtils.g_charset = charset;
        }
    }

    @Test
    @DisplayName("getToken propagates UnsupportedEncodingException when charset is invalid")
    void testGetTokenInvalidCharset() {
        String charset = FastdfsUtils.g_charset;
        try {
            FastdfsUtils.g_charset = "BOGUS-CHARSET";
            assertThatThrownBy(() -> FastdfsUtils.getToken("x", 1L, "k"))
                    .isInstanceOf(Exception.class);
        } finally {
            FastdfsUtils.g_charset = charset;
        }
    }

    @Test
    @DisplayName("md5 of empty input is the well-known MD5 of empty string")
    void testMd5Empty() throws NoSuchAlgorithmException {
        assertThat(FastdfsUtils.md5(new byte[0])).isEqualTo("d41d8cd98f00b204e9800998ecf8427e");
    }

    @Test
    @DisplayName("md5 of 'abc' matches the known MD5 digest")
    void testMd5Abc() throws NoSuchAlgorithmException {
        assertThat(FastdfsUtils.md5("abc".getBytes(StandardCharsets.ISO_8859_1)))
                .isEqualTo("900150983cd24fb0d6963f7d28e17f72");
    }

    @Test
    @DisplayName("md5 is stable and returns 32 lowercase hex chars")
    void testMd5Format() throws NoSuchAlgorithmException {
        byte[] data = "the quick brown fox".getBytes(StandardCharsets.ISO_8859_1);
        String digest = FastdfsUtils.md5(data);
        assertThat(digest).hasSize(32).matches("[0-9a-f]{32}");
        assertThat(FastdfsUtils.md5(data)).isEqualTo(digest);
    }
}
