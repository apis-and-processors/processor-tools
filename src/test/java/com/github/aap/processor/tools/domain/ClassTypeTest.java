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

package com.github.aap.processor.tools.domain;

import com.github.aap.processor.tools.TypeUtils;
import com.github.aap.processor.tools.utils.Constants;
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
 * Tests for ClassType.
 * 
 * @author cdancy
 */
public class ClassTypeTest {
    
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
    
    @Test 
    public void testJavaDataStructuresToClassTypes() {
        
        String fullyQualifiedName = "java.util.HashSet";
        Object instance = new HashSet<>();
        ClassType type = TypeUtils.parseClassType(instance);
        assertNotNull(type);
        assertNull(type.parent());
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.subTypes().size() == 1);
        assertTrue(type.subTypeAtIndex(0).name().equals(Constants.OBJECT_CLASS));
        assertTrue(type.subTypeAtIndex(0).parent().name().equals(fullyQualifiedName));
        assertTrue(type.compareTo(TypeUtils.parseClassType(instance)) == 3);
        
        fullyQualifiedName = "java.util.ArrayList";
        instance = new ArrayList<>();
        type = TypeUtils.parseClassType(instance);
        assertNotNull(type);
        assertNull(type.parent());
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.subTypes().size() == 1);
        assertTrue(type.subTypeAtIndex(0).name().equals(Constants.OBJECT_CLASS));
        assertTrue(type.subTypeAtIndex(0).parent().name().equals(fullyQualifiedName));
        assertTrue(type.compareTo(TypeUtils.parseClassType(instance)) == 3);

        fullyQualifiedName = "java.util.Properties";
        instance = new Properties();
        type = TypeUtils.parseClassType(instance);
        assertNotNull(type);
        assertNull(type.parent());
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.subTypes().isEmpty());
        assertTrue(type.compareTo(TypeUtils.parseClassType(instance)) == 0);
        
        fullyQualifiedName = "java.util.HashMap";
        instance = new HashMap<>();
        type = TypeUtils.parseClassType(instance);
        assertNotNull(type);
        assertNull(type.parent());
        assertTrue(type.name().equals(fullyQualifiedName));
        assertTrue(type.subTypes().size() == 2);
        assertTrue(type.subTypeAtIndex(0).name().equals(Constants.OBJECT_CLASS));
        assertTrue(type.subTypeAtIndex(0).parent().name().equals(fullyQualifiedName));
        assertTrue(type.subTypeAtIndex(1).name().equals(Constants.OBJECT_CLASS));
        assertTrue(type.subTypeAtIndex(1).parent().name().equals(fullyQualifiedName));
        assertTrue(type.compareTo(TypeUtils.parseClassType(instance)) == 3);
    }
    
    @Test
    public void testDataStructuresToClassTypes() {

        final ClassType helloWorld = TypeUtils.parseClassType(HelloWorld.class);
        assertNotNull(helloWorld);
        assertNull(helloWorld.parent());
        assertNull(helloWorld.firstSubTypeMatching(".*NonExistentType.*"));
        assertTrue(helloWorld.name().equals(HelloWorld.class.getName()));
        assertTrue(helloWorld.subTypes().size() == 2);
        
        final ClassType functionType = helloWorld.firstSubTypeMatching(FUNCTION_REGEX);
        assertNotNull(functionType);
        assertNotNull(functionType.parent());
        assertNull(functionType.firstSubTypeMatching(".*NonExistentType.*"));
        assertTrue(functionType.parent().name().equals(HelloWorld.class.getName()));
        assertTrue(functionType.subTypes().size() == 2);
        
        final ClassType functionTypeBoolean = functionType.firstSubTypeMatching(".*Boolean.*");
        assertNotNull(functionTypeBoolean);
        assertNotNull(functionTypeBoolean.parent());
        assertTrue(functionTypeBoolean.parent().name().equals(functionType.name()));
        assertTrue(functionTypeBoolean.subTypes().isEmpty());
        
        final ClassType functionTypeInteger = functionType.firstSubTypeMatching(".*Integer.*");
        assertNotNull(functionTypeInteger);
        assertNotNull(functionTypeInteger.parent());
        assertTrue(functionTypeInteger.parent().name().equals(functionType.name()));
        assertTrue(functionTypeInteger.subTypes().isEmpty());
        
        final ClassType comparableType = helloWorld.firstSubTypeMatching(COMPARABLE_REGEX);
        assertNotNull(comparableType);
        assertNotNull(comparableType.parent());
        assertNull(helloWorld.firstSubTypeMatching(".*NonExistentType.*"));
        assertTrue(comparableType.parent().name().equals(HelloWorld.class.getName()));
        assertTrue(comparableType.subTypes().size() == 1);
        
        final ClassType comparableTypeString = comparableType.firstSubTypeMatching(".*String.*");
        assertNotNull(comparableTypeString);
        assertNotNull(comparableTypeString.parent());
        assertTrue(comparableTypeString.parent().name().equals(comparableType.name()));
        assertTrue(comparableTypeString.subTypes().isEmpty());
    }
    
    @Test (dependsOnMethods = "testDataStructuresToClassTypes")
    public void testDataStructuresComparison() {
        
        final ClassType helloWorld = TypeUtils.parseClassType(HelloWorld.class);
        final ClassType helloWorld2 = TypeUtils.parseClassType(HelloWorld2.class);
        final ClassType helloWorld3 = TypeUtils.parseClassType(HelloWorld3.class);
        final ClassType helloWorld4 = TypeUtils.parseClassType(HelloWorld4.class);

        assertTrue(helloWorld.compareTo(helloWorld2) == -1);
        assertTrue(helloWorld.firstSubTypeMatching(COMPARABLE_REGEX).compareTo(helloWorld3.firstSubTypeMatching(COMPARABLE_REGEX)) == -1);
        assertTrue(helloWorld.firstSubTypeMatching(COMPARABLE_REGEX).compareTo(helloWorld2.firstSubTypeMatching(COMPARABLE_REGEX)) == 0);
        assertTrue(helloWorld3.firstSubTypeMatching(FUNCTION_REGEX).compareTo(helloWorld2.firstSubTypeMatching(FUNCTION_REGEX)) == 1);
        assertTrue(helloWorld2.firstSubTypeMatching(FUNCTION_REGEX).compareTo(helloWorld3.firstSubTypeMatching(FUNCTION_REGEX)) == 2);
        assertTrue(helloWorld3.firstSubTypeMatching(FUNCTION_REGEX).compareTo(helloWorld4.firstSubTypeMatching(FUNCTION_REGEX)) == 3);
    }
}
