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

package com.github.aap.processor.tools;

import static org.testng.Assert.assertTrue;

import com.github.aap.processor.tools.domain.ClassType;
import com.github.aap.processor.tools.utils.Constants;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


import org.testng.annotations.Test;

/**
 * Tests for exercising ClassType parsing with options.
 * 
 * @author cdancy
 */
public class ClassTypeParserOptionsTest {

    class CustomInterfaceHandler implements Function<AtomicReference<Boolean>, String>, Serializable {

        @Override
        public String apply(final AtomicReference<Boolean> object) {
            return object.get().toString();
        }
    }

    class CustomClassHandlerOne <A> {

    }

    class CustomClassHandlerTwo <B> extends CustomClassHandlerOne {

    }

    class CustomClassHandlerThree <C> extends CustomClassHandlerTwo {

    }

    @Test
    public void testClassIgnored() throws Exception {
        final String regex = ".*" + CustomClassHandlerTwo.class.getSimpleName() + ".*";
        final ClassTypeParserOptions options = ClassTypeParserOptions.instance(regex, null, null, null);
        final ClassType classType = ClassTypeParser.parse(CustomClassHandlerThree.class, options);
        assertTrue(classType.childCount() == 1);
        assertTrue(classType.childAtIndex(0).name().equals(Constants.OBJECT_CLASS));
    }

    @Test
    public void testClassParamIgnored() throws Exception {
        final ClassTypeParserOptions options = ClassTypeParserOptions.instance(null, ".*Object.*", null, null);
        final ClassType classType = ClassTypeParser.parse(CustomClassHandlerThree.class, options);
        assertTrue(classType.childCount() == 1);
        assertTrue(classType.childAtIndex(0).name().contains(CustomClassHandlerTwo.class.getSimpleName()));
    }

    @Test
    public void testInterfaceIgnored() throws Exception {
        final String regex = ".*" + Serializable.class.getSimpleName() + ".*";
        final ClassTypeParserOptions options = ClassTypeParserOptions.instance(null, null, regex, null);
        final ClassType classType = ClassTypeParser.parse(CustomInterfaceHandler.class, options);
        assertTrue(classType.childCount() == 1);
        assertTrue(classType.childAtIndex(0).name().contains(Function.class.getSimpleName()));

        // check that Serializable interface does not exist.
        assertTrue(classType.childAtIndex(0).childCount() == 2);
        assertTrue(classType.childAtIndex(0).childAtIndex(0).name().contains(AtomicReference.class.getSimpleName()));
        assertTrue(classType.childAtIndex(0).childAtIndex(0).childCount() == 1);
        assertTrue(classType.childAtIndex(0).childAtIndex(0).childAtIndex(0).name().contains(Boolean.class.getSimpleName()));
        assertTrue(classType.childAtIndex(0).childAtIndex(1).name().contains(String.class.getSimpleName()));
    }

    @Test
    public void testInterfaceParamIgnored() throws Exception {
        final ClassTypeParserOptions options = ClassTypeParserOptions.instance(null, null, null, ".*" + String.class.getSimpleName() + ".*");
        final ClassType classType = ClassTypeParser.parse(CustomInterfaceHandler.class, options);
        assertTrue(classType.childCount() == 2);
        assertTrue(classType.childAtIndex(0).name().contains(Function.class.getSimpleName()));

        // check that String param within Function does not exist.
        assertTrue(classType.childAtIndex(0).childCount() == 1);
        assertTrue(classType.childAtIndex(0).childAtIndex(0).name().contains(AtomicReference.class.getSimpleName()));
        assertTrue(classType.childAtIndex(0).childAtIndex(0).childCount() == 2);
        assertTrue(classType.childAtIndex(0).childAtIndex(0).childAtIndex(0).name().equals(Boolean.class.getName()));
        assertTrue(classType.childAtIndex(0).childAtIndex(0).childAtIndex(1).name().equals(Serializable.class.getName()));
    }
}
