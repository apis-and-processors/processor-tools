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

package com.aries.classtype.parser.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.aries.classtype.parser.ReflectionMagic;

import com.aries.classtype.parser.domain.Null;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.Test;

/**
 * Tests for exercising ReflectionMagic.
 * 
 * @author cdancy
 */
public class ReflectionMagicTest {

    private static class InnerTestClass implements Comparable {
        public void noop() {}

        @Override
        public int compareTo(final Object object) {
            return 0;
        }

        @Override
        public boolean equals(final Object object) {
            return object != this; // just to shut-up warnings
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + Objects.hashCode(this);
            return hash;
        }
    }

    @Test
    public void createInnerClasses() {
        final InnerTestClass int1 = new InnerTestClass();

        assertNotNull(int1);
        final InnerTestClass int2 = ReflectionMagic.instance(InnerTestClass.class);
        assertNotNull(int2);
    }

    @Test
    public void initPrimitiveClasses() throws Exception {

        final Short shortObj = ReflectionMagic.instance(short.class);
        assertNotNull(shortObj);
        assertTrue(shortObj == 0);

        int integerObj = ReflectionMagic.instance(int.class);
        assertNotNull(integerObj);
        assertTrue(integerObj == 0);
        integerObj = 78;

        final Long longObj = ReflectionMagic.instance(long.class);
        assertNotNull(longObj);
        assertTrue(longObj == 0);

        final Float floatObj = ReflectionMagic.instance(float.class);
        assertNotNull(floatObj);
        assertTrue(floatObj == 0);

        final Double doubleObj = ReflectionMagic.instance(double.class);
        assertNotNull(doubleObj);
        assertTrue(doubleObj == 0);

        final Byte byteObj = ReflectionMagic.instance(byte.class);
        assertNotNull(byteObj);
        assertTrue(byteObj == 0);

        final Character charObj = ReflectionMagic.instance(char.class);
        assertNotNull(charObj);
        assertTrue(charObj == '\u0000');

        final Boolean booleanObj = ReflectionMagic.instance(boolean.class);
        assertNotNull(booleanObj);
        assertTrue(booleanObj == false);

        final Void voidObj = ReflectionMagic.instance(void.class);
        assertThat(voidObj).isNull();

        final Null nullObj = ReflectionMagic.instance(Null.class);
        assertNotNull(nullObj);
    }

    @Test
    public void initBoxedPrimitiveClasses() throws Exception {

        final Short shortObj = ReflectionMagic.instance(Short.class);
        assertNotNull(shortObj);
        assertTrue(shortObj == 0);

        final Integer integerObj = ReflectionMagic.instance(Integer.class);
        assertNotNull(integerObj);
        assertTrue(integerObj == 0);

        final Long longObj = ReflectionMagic.instance(Long.class);
        assertNotNull(longObj);
        assertTrue(longObj == 0);

        final Float floatObj = ReflectionMagic.instance(Float.class);
        assertNotNull(floatObj);
        assertTrue(floatObj == 0);

        final Double doubleObj = ReflectionMagic.instance(Double.class);
        assertNotNull(doubleObj);
        assertTrue(doubleObj == 0);

        final Byte byteObj = ReflectionMagic.instance(Byte.class);
        assertNotNull(byteObj);
        assertTrue(byteObj == 0);

        final Character charObj = ReflectionMagic.instance(Character.class);
        assertNotNull(charObj);
        assertTrue(charObj == '\u0000');

        final Boolean booleanObj = ReflectionMagic.instance(Boolean.class);
        assertNotNull(booleanObj);
        assertTrue(booleanObj == false);

        final Void voidObj = ReflectionMagic.instance(Void.class);
        assertThat(voidObj).isNotNull();

        final Null nullObj = ReflectionMagic.instance(Null.class);
        assertNotNull(nullObj);
    }

    @Test
    public void initDataStructureTypes() {

        final Map<String, String> map = ReflectionMagic.instance(HashMap.class);
        assertNotNull(map);
        map.put("hello4", "world2");
        assertTrue(map.size() == 1);

        final List<String> list = ReflectionMagic.instance(ArrayList.class);
        assertNotNull(list);
        list.add("hello5");
        assertTrue(list.size() == 1);

        final Set<String> set = ReflectionMagic.instance(HashSet.class);
        assertNotNull(set);
        set.add("hello6");
        assertTrue(map.size() == 1);

    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        final Constructor<ReflectionMagic> constructor = ReflectionMagic.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (final Exception e) {
            assertThat(e).isInstanceOf(InvocationTargetException.class);
        }
    }
}
