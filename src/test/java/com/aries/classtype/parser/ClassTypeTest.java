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

package com.aries.classtype.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.aries.classtype.parser.domain.Null;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import org.junit.Test;

/**
 * Tests for exercising ClassType.
 * 
 * @author cdancy
 */
public class ClassTypeTest {

    private static final String COMPARABLE_REGEX = ".*Comparable.*";
    private static final String FUNCTION_REGEX = ".*Function.*";

    // inner classes used primarily for testing purposes
    abstract static class HelloWorld implements Function<Integer, Boolean>, Comparable<String> {
        @Override
        public Boolean apply(final Integer instance) {
            return true;
        }
    }

    abstract static class HelloWorld2 implements Comparable<String>, Function<Integer, Boolean> {
        @Override
        public Boolean apply(final Integer instance) {
            return true;
        }
    }

    abstract static class HelloWorld3 implements Comparable<Character>, Function<Object, Boolean> {
        @Override
        public Boolean apply(final Object instance) {
            return true;
        }
    }

    abstract static class HelloWorld4 implements Comparable<Character>, Function<String, Object> {
        @Override
        public Object apply(final String instance) {
            return null;
        }
    }

    interface GenericInterface<T> {
        void bears(final T obj);
    }

    interface ExtendingGenericInterface<U> extends GenericInterface<String> {

    }

    static class GenericClass<R> {

    }

    static class SecondGenericClass<T, V> extends GenericClass {

    }

    static class TestGenericClass extends GenericClass<String> {

    }

    static class TestGenericInterface implements GenericInterface<String> {

        @Override
        public void bears(final String obj){}
    }

    static class TestMultipleImplements implements GenericInterface<String>, Comparable<String> {

        @Override
        public void bears(final String obj){}

        @Override
        public int compareTo(final String object) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    static class TestMultipleExtends extends SecondGenericClass<String, Integer> {

    }

    @Test
    public void testNullType() {

        final ClassType instance = ClassType.parse(null);
        assertNotNull(instance);

        final Object parsedObject = ReflectionMagic.instance(instance.clazz());
        assertTrue(instance.clazz() == Null.class);
        assertTrue(parsedObject.toString().equals("null"));
    }

    @Test
    public void testFirstChildMatchingWithNullRegexReturnsNull() {

        final ClassType instance = ClassType.parse(String.class);
        assertThat(instance.firstChildMatching(null)).isNull();
    }

    @Test
    public void testAddingChild() {

        final ClassType instance = ClassType.parse(String.class);
        final ClassType childInstance = ClassType.parse(Integer.class);
        assertThat(instance.child(childInstance)).isEqualTo(instance);
        assertThat(instance.child(null)).isEqualTo(instance);
    }

    @Test
    public void testUniqueHashCodes() {

        final ClassType first = ClassType.parse(String.class);
        final ClassType second = ClassType.parse(Integer.class);
        assertThat(first.hashCode()).isGreaterThan(0);
        assertThat(second.hashCode()).isGreaterThan(0);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }

    @Test
    public void testBasicEqualityTest() {

        final ClassType first = ClassType.parse(String.class);
        final ClassType second = ClassType.parse(Integer.class);
        assertThat(first.equals(second)).isFalse();
        assertThat(first.equals(ClassType.parse(String.class))).isTrue();
    }

    @Test
    public void testEmptyStringType() {

        final ClassType instance = ClassType.parse("");
        assertNotNull(instance);
        assertTrue(instance.clazz() == String.class);
        final Object parsedObject = ReflectionMagic.instance(instance.clazz());
        assertTrue(parsedObject.toString().equals(""));
    }

    @Test
    public void testPrimitiveType() {

        final ClassType instance = ClassType.parse(123);
        assertNotNull(instance);
        assertTrue(instance.clazz() == Integer.class);
        final Object parsedObject = ReflectionMagic.instance(instance.clazz());
        assertTrue(parsedObject.toString().equals("0"));
    }

    @Test
    public void testToStringWithChildren() {

        final ClassType first = ClassType.parse(String.class);
        final ClassType second = ClassType.parse(Integer.class);
        first.child(second);
        assertThat(first.toString()).isNotNull();
    }

    @Test
    public void testJavaDataStructuresToClassTypes() {

        String fullyQualifiedName = "java.util.HashSet";
        Object instance = new HashSet<>();
        ClassType type = ClassType.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 5);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.compareTo(ClassType.parse(instance)) == 3);

        fullyQualifiedName = "java.util.ArrayList";
        instance = new ArrayList<>();
        type = ClassType.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 6);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.compareTo(ClassType.parse(instance)) == 3);

        fullyQualifiedName = "java.util.Properties";
        instance = new Properties();
        type = ClassType.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 1);
        assertTrue(type.compareTo(ClassType.parse(instance)) == 3);

        fullyQualifiedName = "java.util.HashMap";
        instance = new HashMap<>();
        type = ClassType.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 6);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.children().get(1).clazz() == Object.class);
        assertTrue(type.compareTo(ClassType.parse(instance)) == 3);
    }

    @Test
    public void testDataStructuresToClassTypes() {

        final ClassType helloWorld = ClassType.parse(HelloWorld.class);
        assertNotNull(helloWorld);
        assertThat(helloWorld.firstChildMatching(".*NonExistentType.*")).isNull();
        assertTrue(helloWorld.name().equals(HelloWorld.class.getName()));
        assertTrue(helloWorld.children().size() == 2);

        final ClassType functionType = helloWorld.firstChildMatching(FUNCTION_REGEX);
        assertNotNull(functionType);
        assertThat(functionType.firstChildMatching(".*NonExistentType.*")).isNull();
        assertTrue(functionType.children().size() == 2);

        final ClassType functionTypeBoolean = functionType.firstChildMatching(".*Boolean.*");
        assertNotNull(functionTypeBoolean);
        assertTrue(functionTypeBoolean.children().size() == 0);

        final ClassType functionTypeInteger = functionType.firstChildMatching(".*Integer.*");
        assertNotNull(functionTypeInteger);
        assertTrue(functionTypeInteger.children().size() == 0);

        final ClassType comparableType = helloWorld.firstChildMatching(COMPARABLE_REGEX);
        assertNotNull(comparableType);
        assertThat(helloWorld.firstChildMatching(".*NonExistentType.*")).isNull();
        assertTrue(comparableType.children().size() == 1);

        final ClassType comparableTypeString = comparableType.firstChildMatching(".*String.*");
        assertNotNull(comparableTypeString);
        assertTrue(comparableTypeString.children().size() == 0);
    }

    @Test
    public void testDataStructuresComparison() {

        final ClassType helloWorld = ClassType.parse(HelloWorld.class);
        final ClassType helloWorld2 = ClassType.parse(HelloWorld2.class);
        final ClassType helloWorld3 = ClassType.parse(HelloWorld3.class);
        final ClassType helloWorld4 = ClassType.parse(HelloWorld4.class);

        assertTrue(helloWorld.compareTo(helloWorld2) == -1);
        assertTrue(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(helloWorld3.firstChildMatching(COMPARABLE_REGEX)) == -1);
        assertTrue(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(helloWorld2.firstChildMatching(COMPARABLE_REGEX)) == 0);
        assertTrue(helloWorld3.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld2.firstChildMatching(FUNCTION_REGEX)) == 1);
        assertTrue(helloWorld2.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld3.firstChildMatching(FUNCTION_REGEX)) == 2);
        assertTrue(helloWorld3.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld4.firstChildMatching(FUNCTION_REGEX)) == 3);
    }

    @Test
    public void testThrowsExceptionOnCompare() {
        final ClassType helloWorld = ClassType.parse(HelloWorld.class);
        final ClassType helloWorld3 = ClassType.parse(HelloWorld3.class);
        assertThat(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(helloWorld3.firstChildMatching(COMPARABLE_REGEX))).isEqualTo(-1);
    }

    @Test
    public void testThrowsExceptionOnCompareWithNull() {
        final ClassType helloWorld = ClassType.parse(HelloWorld.class);
        assertThat(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(null)).isEqualTo(-1);
    }

    @Test
    public void testGeneric() {
        final ClassType classType = ClassType.parse(GenericClass.class);
        assertTrue(classType.name().equalsIgnoreCase(GenericClass.class.getName()));
        assertTrue(classType.children().size() == 1);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(Object.class.getName()));
    }

    @Test
    public void testMultipleExtends() {
        final ClassType classType = ClassType.parse(TestMultipleExtends.class);
        assertTrue(classType.name().equalsIgnoreCase(TestMultipleExtends.class.getName()));
        assertTrue(classType.children().size() == 1);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(SecondGenericClass.class.getName()));
        assertTrue(classType.children().get(0).children().size() == 3);
        assertTrue(classType.children().get(0).children().get(0).name().equalsIgnoreCase(String.class.getName()));
        assertTrue(classType.children().get(0).children().get(1).name().equalsIgnoreCase(Integer.class.getName()));
        assertTrue(classType.children().get(0).children().get(2).name().equalsIgnoreCase(GenericClass.class.getName()));
    }

    @Test
    public void testMultipleImplements() {
        final ClassType classType = ClassType.parse(TestMultipleImplements.class);
        assertTrue(classType.name().equalsIgnoreCase(TestMultipleImplements.class.getName()));
        assertTrue(classType.children().size() == 2);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(GenericInterface.class.getName()));
        assertTrue(classType.children().get(0).children().size() == 1);
        assertTrue(classType.children().get(0).children().get(0).name().equalsIgnoreCase(String.class.getName()));
        assertTrue(classType.children().get(1).name().equalsIgnoreCase(Comparable.class.getName()));
        assertTrue(classType.children().get(1).children().size() == 1);
        assertTrue(classType.children().get(1).children().get(0).name().equalsIgnoreCase(String.class.getName()));
    }

    @Test
    public void testExtendingGenericInterface() {
        final ClassType classType = ClassType.parse(ExtendingGenericInterface.class);
        assertTrue(classType.name().equalsIgnoreCase(ExtendingGenericInterface.class.getName()));
        assertTrue(classType.children().size() == 2);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(Object.class.getName()));
        assertTrue(classType.children().get(0).children().isEmpty());
        assertTrue(classType.children().get(1).name().equalsIgnoreCase(GenericInterface.class.getName()));
        assertTrue(classType.children().get(1).children().size() == 1);
        assertTrue(classType.children().get(1).children().get(0).name().equalsIgnoreCase(String.class.getName()));
    }
}
