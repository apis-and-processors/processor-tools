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

package com.github.aap.processor.tools.utils;

import com.github.aap.processor.tools.domain.Null;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Tests for invoking ReflectionMagic.
 * 
 * @author cdancy
 */
public class ReflectionMagicTest {

    private class InnerTestClass implements Comparable {
        public void noop() {}

        @Override
        public int compareTo(final Object object) {
            return 0;
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
        
        final Integer integerObj = ReflectionMagic.instance(int.class);
        assertNotNull(integerObj);
        assertTrue(integerObj == 0);

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
        assertNull(voidObj);
        
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
        assertNull(voidObj);
        
        final Null nullObj = ReflectionMagic.instance(Null.class);
        assertNotNull(nullObj);
    }
    
    @Test 
    public void initDataStructureInterfaces() {
        
        final Map<String, String> map = ReflectionMagic.instance(Map.class);
        assertNotNull(map);
        map.put("hello1", "world1");
        assertTrue(map.size() == 1);
        
        final List<String> list = ReflectionMagic.instance(List.class);
        assertNotNull(list);
        list.add("hello2");
        assertTrue(list.size() == 1);
        
        final Set<String> set = ReflectionMagic.instance(Set.class);
        assertNotNull(set);
        set.add("hello3");
        assertTrue(map.size() == 1);
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
}
