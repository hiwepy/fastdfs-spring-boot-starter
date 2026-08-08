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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;

/**
 * Unit tests for {@link FileStorePath}.
 *
 * <p>Covers all three constructors, the thumb getter/setter and the
 * {@code getFullThumb()} composition helper.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FileStorePath Tests")
class FileStorePathTest {

    @Test
    @DisplayName("Default constructor creates a non-null instance")
    void testDefaultConstructor() {
        assertThat(new FileStorePath()).isNotNull();
    }

    @Test
    @DisplayName("(StorePath, String) constructor copies group/path and sets thumb")
    void testStorePathThumbConstructor() {
        StorePath store = new StorePath("group1", "M00/00/00/file");
        FileStorePath path = new FileStorePath(store, "M00/00/00/thumb");
        assertThat(path.getGroup()).isEqualTo("group1");
        assertThat(path.getPath()).isEqualTo("M00/00/00/file");
        assertThat(path.getThumb()).isEqualTo("M00/00/00/thumb");
    }

    @Test
    @DisplayName("(group, path, thumb) constructor sets all three fields")
    void testGroupPathThumbConstructor() {
        FileStorePath path = new FileStorePath("group1", "M00/00/00/file", "M00/00/00/thumb");
        assertThat(path.getGroup()).isEqualTo("group1");
        assertThat(path.getPath()).isEqualTo("M00/00/00/file");
        assertThat(path.getThumb()).isEqualTo("M00/00/00/thumb");
    }

    @Test
    @DisplayName("Thumb getter/setter round-trip")
    void testThumbSetter() {
        FileStorePath path = new FileStorePath();
        path.setThumb("thumb-x");
        assertThat(path.getThumb()).isEqualTo("thumb-x");
    }

    @Test
    @DisplayName("getFullThumb concatenates group + '/' + thumb")
    void testGetFullThumb() {
        FileStorePath path = new FileStorePath("group1", "M00/00/00/file", "M00/00/00/thumb");
        assertThat(path.getFullThumb()).isEqualTo("group1/M00/00/00/thumb");
    }

    @Test
    @DisplayName("getFullPath (inherited) yields group + '/' + path")
    void testInheritedGetFullPath() {
        FileStorePath path = new FileStorePath("group2", "M00/00/01/file", "thumb");
        assertThat(path.getFullPath()).isEqualTo("group2/M00/00/01/file");
    }
}
