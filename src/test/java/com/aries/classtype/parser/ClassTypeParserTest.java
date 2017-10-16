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

import com.aries.classtype.parser.domain.ClassType;
import com.aries.classtype.parser.domain.Null;
import com.aries.classtype.parser.exceptions.TypeMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.function.Function;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Tests for exercising ClassTypeParser.
 * 
 * @author cdancy
 */
public class ClassTypeParserTest {

    private static final String COMPARABLE_REGEX = ".*Comparable.*";
    private static final String FUNCTION_REGEX = ".*Function.*";

    // inner classes used primarily for testing purposes
    abstract class HelloWorld implements Function<Integer, Boolean>, Comparable<String> {
        @Override
        public Boolean apply(final Integer instance) {
            return null;
        }
    }

    abstract class HelloWorld2 implements Comparable<String>, Function<Integer, Boolean> {
        @Override
        public Boolean apply(final Integer instance) {
            return null;
        }
    }

    abstract class HelloWorld3 implements Comparable<Character>, Function<Object, Boolean> {
        @Override
        public Boolean apply(final Object instance) {
            return null;
        }
    }

    abstract class HelloWorld4 implements Comparable<Character>, Function<String, Object> {
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

    class GenericClass<R> {

    }

    class SecondGenericClass<T, V> extends GenericClass {

    }

    class TestGenericClass extends GenericClass<String> {

    }

    class TestGenericInterface implements GenericInterface<String> {

        @Override
        public void bears(final String obj){}
    }

    class TestMultipleImplements implements GenericInterface<String>, Comparable<String> {

        @Override
        public void bears(final String obj){}

        @Override
        public int compareTo(final String object) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    class TestMultipleExtends extends SecondGenericClass<String, Integer> {

    }

    @Test
    public void testNullType() {

        final ClassType instance = ClassTypeParser.parse(null);
        assertNotNull(instance);
        assertTrue(instance.clazz() == Null.class);
        assertTrue(instance.toObject().toString().equals("null"));
    }

    @Test
    public void testEmptyStringType() {

        final ClassType instance = ClassTypeParser.parse("");
        assertNotNull(instance);
        assertTrue(instance.clazz() == String.class);
        assertTrue(instance.toObject().toString().equals(""));
    }

    @Test
    public void testPrimitiveType() {

        final ClassType instance = ClassTypeParser.parse(123);
        assertNotNull(instance);
        assertTrue(instance.clazz() == Integer.class);
        assertTrue(instance.toObject().toString().equals("0"));
    }

    @Test
    public void testJavaDataStructuresToClassTypes() {

        String fullyQualifiedName = "java.util.HashSet";
        Object instance = new HashSet<>();
        ClassType type = ClassTypeParser.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 5);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.compareTo(ClassTypeParser.parse(instance)) == 3);

        fullyQualifiedName = "java.util.ArrayList";
        instance = new ArrayList<>();
        type = ClassTypeParser.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 6);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.compareTo(ClassTypeParser.parse(instance)) == 3);

        fullyQualifiedName = "java.util.Properties";
        instance = new Properties();
        type = ClassTypeParser.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 1);
        assertTrue(type.compareTo(ClassTypeParser.parse(instance)) == 3);

        fullyQualifiedName = "java.util.HashMap";
        instance = new HashMap<>();
        type = ClassTypeParser.parse(instance);
        assertNotNull(type);
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.children().size() == 6);
        assertTrue(type.children().get(0).clazz() == Object.class);
        assertTrue(type.children().get(1).clazz() == Object.class);
        assertTrue(type.compareTo(ClassTypeParser.parse(instance)) == 3);
    }

    @Test
    public void testDataStructuresToClassTypes() {

        final ClassType helloWorld = ClassTypeParser.parse(HelloWorld.class);
        assertNotNull(helloWorld);
        assertNull(helloWorld.firstChildMatching(".*NonExistentType.*"));
        assertTrue(helloWorld.name().equals(HelloWorld.class.getName()));
        assertTrue(helloWorld.children().size() == 2);

        final ClassType functionType = helloWorld.firstChildMatching(FUNCTION_REGEX);
        assertNotNull(functionType);
        assertNull(functionType.firstChildMatching(".*NonExistentType.*"));
        assertTrue(functionType.children().size() == 2);

        final ClassType functionTypeBoolean = functionType.firstChildMatching(".*Boolean.*");
        assertNotNull(functionTypeBoolean);
        assertTrue(functionTypeBoolean.children().size() == 0);

        final ClassType functionTypeInteger = functionType.firstChildMatching(".*Integer.*");
        assertNotNull(functionTypeInteger);
        assertTrue(functionTypeInteger.children().size() == 0);

        final ClassType comparableType = helloWorld.firstChildMatching(COMPARABLE_REGEX);
        assertNotNull(comparableType);
        assertNull(helloWorld.firstChildMatching(".*NonExistentType.*"));
        assertTrue(comparableType.children().size() == 1);

        final ClassType comparableTypeString = comparableType.firstChildMatching(".*String.*");
        assertNotNull(comparableTypeString);
        assertTrue(comparableTypeString.children().size() == 0);
    }

    @Test (dependsOnMethods = "testDataStructuresToClassTypes")
    public void testDataStructuresComparison() {

        final ClassType helloWorld = ClassTypeParser.parse(HelloWorld.class);
        final ClassType helloWorld2 = ClassTypeParser.parse(HelloWorld2.class);
        final ClassType helloWorld3 = ClassTypeParser.parse(HelloWorld3.class);
        final ClassType helloWorld4 = ClassTypeParser.parse(HelloWorld4.class);

        assertTrue(helloWorld.compareTo(helloWorld2) == -1);
        assertTrue(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(helloWorld3.firstChildMatching(COMPARABLE_REGEX)) == -1);
        assertTrue(helloWorld.firstChildMatching(COMPARABLE_REGEX).compareTo(helloWorld2.firstChildMatching(COMPARABLE_REGEX)) == 0);
        assertTrue(helloWorld3.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld2.firstChildMatching(FUNCTION_REGEX)) == 1);
        assertTrue(helloWorld2.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld3.firstChildMatching(FUNCTION_REGEX)) == 2);
        assertTrue(helloWorld3.firstChildMatching(FUNCTION_REGEX).compareTo(helloWorld4.firstChildMatching(FUNCTION_REGEX)) == 3);
    }

    @Test (expectedExceptions = TypeMismatchException.class)
    public void testThrowsExceptionOnCompare() {
        final ClassType helloWorld = ClassTypeParser.parse(HelloWorld.class);
        final ClassType helloWorld3 = ClassTypeParser.parse(HelloWorld3.class);
        helloWorld.firstChildMatching(COMPARABLE_REGEX).compare(helloWorld3.firstChildMatching(COMPARABLE_REGEX));
    }

    @Test (expectedExceptions = TypeMismatchException.class)
    public void testThrowsExceptionOnCompareWithNull() {
        final ClassType helloWorld = ClassTypeParser.parse(HelloWorld.class);
        helloWorld.firstChildMatching(COMPARABLE_REGEX).compare(null);
    }

    @Test
    public void testGeneric() {
        final ClassType classType = ClassTypeParser.parse(GenericClass.class);
        assertTrue(classType.name().equalsIgnoreCase(GenericClass.class.getName()));
        assertTrue(classType.children().size() == 1);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(Object.class.getName()));
    }

    @Test
    public void testMultipleExtends() {
        final ClassType classType = ClassTypeParser.parse(TestMultipleExtends.class);
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
        final ClassType classType = ClassTypeParser.parse(TestMultipleImplements.class);
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
        final ClassType classType = ClassTypeParser.parse(ExtendingGenericInterface.class);
        assertTrue(classType.name().equalsIgnoreCase(ExtendingGenericInterface.class.getName()));
        assertTrue(classType.children().size() == 2);
        assertTrue(classType.children().get(0).name().equalsIgnoreCase(Object.class.getName()));
        assertTrue(classType.children().get(0).children().size() == 0);
        assertTrue(classType.children().get(1).name().equalsIgnoreCase(GenericInterface.class.getName()));
        assertTrue(classType.children().get(1).children().size() == 1);
        assertTrue(classType.children().get(1).children().get(0).name().equalsIgnoreCase(String.class.getName()));
    }
}
