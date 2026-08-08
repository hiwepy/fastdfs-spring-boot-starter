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
import org.junit.jupiter.api.Test;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;

/**
 * Unit tests for {@link FastdfsTemplate}.
 *
 * <p>Covers endpoint normalization (with/without trailing slash),
 * access-URL generation for raw (group,path), {@link StorePath} and
 * {@link FileStorePath} (thumbnail) variants, and the charset-reset branch.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FastdfsTemplate Tests")
class FastdfsTemplateTest {

    private FastdfsProperties props;
    private FastdfsTemplate template;

    @BeforeEach
    void setUp() {
        props = new FastdfsProperties();
        props.setEndpoint("http://192.168.1.1");
        props.setSecretKey("FastDFS1234567890");
        props.setExpire(60);
        props.setCharset("ISO8859-1");
        template = new FastdfsTemplate(props);
    }

    @Test
    @DisplayName("Constructor stores the given properties")
    void testInstantiation() {
        assertThat(new FastdfsTemplate(props)).isNotNull();
    }

    @Test
    @DisplayName("getEndpoint appends a trailing slash when missing")
    void testGetEndpointAppendsSlash() {
        props.setEndpoint("http://host");
        assertThat(template.getEndpoint()).isEqualTo("http://host/");
    }

    @Test
    @DisplayName("getEndpoint does not double the trailing slash when already present")
    void testGetEndpointKeepsSingleSlash() {
        props.setEndpoint("http://host/");
        assertThat(template.getEndpoint()).isEqualTo("http://host/");
    }

    @Test
    @DisplayName("getAccsssURL(group, path) builds a url with ts and 32-char token")
    void testGetAccsssURLByGroupAndPath() throws Exception {
        String url = template.getAccsssURL("group1", "M00/00/00/file");
        assertThat(url)
                .startsWith("http://192.168.1.1/")
                .contains("group1/M00/00/00/file")
                .contains("ts=")
                .contains("token=");
        int tokenIdx = url.indexOf("token=");
        assertThat(url.substring(tokenIdx + "token=".length())).hasSize(32);
    }

    @Test
    @DisplayName("getAccsssURL(StorePath) builds a url using storePath.getFullPath")
    void testGetAccsssURLByStorePath() throws Exception {
        StorePath storePath = new StorePath("group1", "M00/00/00/file");
        String url = template.getAccsssURL(storePath);
        assertThat(url)
                .startsWith("http://192.168.1.1/")
                .contains("group1/M00/00/00/file")
                .contains("ts=")
                .contains("token=");
    }

    @Test
    @DisplayName("getThumbAccsssURL builds a url using the thumbnail full path")
    void testGetThumbAccsssURL() throws Exception {
        FileStorePath storePath = new FileStorePath("group1", "M00/00/00/file", "M00/00/00/file_100x100");
        String url = template.getThumbAccsssURL(storePath);
        assertThat(url)
                .startsWith("http://192.168.1.1/")
                .contains("group1/M00/00/00/file_100x100")
                .contains("ts=")
                .contains("token=");
    }

    @Test
    @DisplayName("Charset branch resets g_charset when configured charset differs")
    void testCharsetResetBranch() throws Exception {
        String previous = com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils.g_charset;
        try {
            props.setCharset("UTF-8");
            String url = template.getAccsssURL("group1", "M00/00/00/file");
            assertThat(com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils.g_charset).isEqualTo("UTF-8");
            assertThat(url).contains("token=");
        } finally {
            com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils.g_charset = previous;
        }
    }

    @Test
    @DisplayName("Expire lower bound: minimum 5 seconds is enforced in the generated ts")
    void testExpireMinimumFiveSeconds() throws Exception {
        props.setExpire(1);
        long before = System.currentTimeMillis() / 1000;
        String url = template.getAccsssURL("group1", "path");
        long after = System.currentTimeMillis() / 1000;
        int tsIdx = url.indexOf("ts=");
        long ts = Long.parseLong(url.substring(tsIdx + 3, url.indexOf('&', tsIdx)));
        // ts must be in [before+5, after+5]
        assertThat(ts).isBetween(before + 5, after + 5);
    }
}
