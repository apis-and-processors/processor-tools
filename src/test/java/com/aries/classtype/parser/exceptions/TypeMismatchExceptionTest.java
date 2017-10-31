/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aries.classtype.parser.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Test;

/**
 * Tests to exercise TypeMismtachException.
 * 
 * @author cdancy
 */
public class TypeMismatchExceptionTest {

    private static final String RANDOM_STRING = UUID.randomUUID().toString();

    @Test
    public void testCreateWithSource() {
        final TypeMismatchException tme = new TypeMismatchException(RANDOM_STRING, RANDOM_STRING);
        assertThat(tme).isNotNull();
    }

    @Test
    public void testExceptionOnNullSource() {
        try {
            throw new TypeMismatchException(RANDOM_STRING, null);
        } catch (final Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void testCreateWithSourceAndTarget() {
        final TypeMismatchException tme = new TypeMismatchException(RANDOM_STRING,
                RANDOM_STRING,
                RANDOM_STRING);
        assertThat(tme).isNotNull();
    }

    @Test
    public void testExceptionOnNullSourceAndTarget() {
        try {
            throw new TypeMismatchException(RANDOM_STRING, null, null);
        } catch (final Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void testCreateWithSourceAndTargetAndThrowable() {
        final TypeMismatchException tme = new TypeMismatchException(RANDOM_STRING,
                RANDOM_STRING,
                RANDOM_STRING,
                new Exception(RANDOM_STRING));
        assertThat(tme).isNotNull();
    }

    @Test
    public void testExceptionOnNullSourceAndTargetAndThrowable() {
        try {
            throw new TypeMismatchException(RANDOM_STRING, null, null, null);
        } catch (final Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }
}
