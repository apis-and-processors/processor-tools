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

package com.github.aap.processor.tools.types;

import com.github.aap.processor.tools.ClassTypeParser;
import com.github.aap.processor.tools.ClassTypeParserOptions;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


import org.testng.annotations.Test;

/**
 * Tests for exercising class and interface ignoring when parsing a ClassType.
 * 
 * @author cdancy
 */
public class IgnoreHierarchyTest {

    class Handler1 implements Function<Boolean, String> {

        @Override
        public String apply(final Boolean object) {
            return object ? "hello" : "world";
        }
    }

    class CustomHandler implements Function<String, AtomicReference<Integer>> {

        @Override
        public AtomicReference<Integer> apply(final String object) {
            return null;
        }
    }

    @Test
    public void booleanToBoolean() throws Exception {
        final String regex = ".*Serial.*";
        final ClassTypeParserOptions options = ClassTypeParserOptions.instance(null, null, regex, regex);
        System.out.println("parse function: " + ClassTypeParser.parse(CustomHandler.class, options));
    }
}
