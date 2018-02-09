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

package com.aries.classtype.parser.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aries.classtype.parser.utils.Constants;
import org.junit.Test;

/**
 * Test to exercise the Null object.
 */
public class NullTest {

    @Test
    public void testThatToStringIsExpectedValue() {
        assertThat(Null.INSTANCE.toString()).isEqualTo(Constants.NULL_STRING);
    }

    @Test
    public void testThatValueOfInstanceReturnsActualNullInstance() {
        assertThat(Null.valueOf("INSTANCE")).isEqualTo(Null.INSTANCE);
    }

    @Test
    public void testThatValuesContainsExactlyOneValue() {
        assertThat(Null.values().length).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatExceptionIsThrownWhenGettingIllegalValue() {
        Null.valueOf("HELLOWWORLD");
    }
}
