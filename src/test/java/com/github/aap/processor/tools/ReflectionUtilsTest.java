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

import com.github.aap.processor.tools.ReflectionUtils;
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
 * Various tests for invoking ReflectionUtils.
 * 
 * @author cdancy
 */
public class ReflectionUtilsTest {

    @Test
    public void initPrimitiveClasses() throws Exception {

        Short shortObj = ReflectionUtils.newInstance(short.class);
        assertNotNull(shortObj);
        assertTrue(shortObj == 0);
        
        Integer integerObj = ReflectionUtils.newInstance(int.class);
        assertNotNull(integerObj);
        assertTrue(integerObj == 0);

        Long longObj = ReflectionUtils.newInstance(long.class);
        assertNotNull(longObj);
        assertTrue(longObj == 0);
        
        Float floatObj = ReflectionUtils.newInstance(float.class);
        assertNotNull(floatObj);
        assertTrue(floatObj == 0);
        
        Double doubleObj = ReflectionUtils.newInstance(double.class);
        assertNotNull(doubleObj);
        assertTrue(doubleObj == 0);
        
        Byte byteObj = ReflectionUtils.newInstance(byte.class);
        assertNotNull(byteObj);
        assertTrue(byteObj == 0);
        
        Character charObj = ReflectionUtils.newInstance(char.class);
        assertNotNull(charObj);
        assertTrue(charObj == '\u0000');
        
        Boolean booleanObj = ReflectionUtils.newInstance(boolean.class);
        assertNotNull(booleanObj);
        assertTrue(booleanObj == false);
        
        Void voidObj = ReflectionUtils.newInstance(void.class);
        assertNull(voidObj);
        
        Null nullObj = ReflectionUtils.newInstance(Null.class);
        assertNull(nullObj);
    }
    
    @Test
    public void initBoxedPrimitiveClasses() throws Exception {

        Short shortObj = ReflectionUtils.newInstance(Short.class);
        assertNotNull(shortObj);
        assertTrue(shortObj == 0);
        
        Integer integerObj = ReflectionUtils.newInstance(Integer.class);
        assertNotNull(integerObj);
        assertTrue(integerObj == 0);

        Long longObj = ReflectionUtils.newInstance(Long.class);
        assertNotNull(longObj);
        assertTrue(longObj == 0);
        
        Float floatObj = ReflectionUtils.newInstance(Float.class);
        assertNotNull(floatObj);
        assertTrue(floatObj == 0);
        
        Double doubleObj = ReflectionUtils.newInstance(Double.class);
        assertNotNull(doubleObj);
        assertTrue(doubleObj == 0);
        
        Byte byteObj = ReflectionUtils.newInstance(Byte.class);
        assertNotNull(byteObj);
        assertTrue(byteObj == 0);
        
        Character charObj = ReflectionUtils.newInstance(Character.class);
        assertNotNull(charObj);
        assertTrue(charObj == '\u0000');
        
        Boolean booleanObj = ReflectionUtils.newInstance(Boolean.class);
        assertNotNull(booleanObj);
        assertTrue(booleanObj == false);
        
        Void voidObj = ReflectionUtils.newInstance(Void.class);
        assertNull(voidObj);
        
        Null nullObj = ReflectionUtils.newInstance(Null.class);
        assertNull(nullObj);
    }
    
    @Test
    public void initDataStructureInterfaces() {
        
        Map<String, String> map = ReflectionUtils.newInstance(Map.class);
        assertNotNull(map);
        map.put("hello", "world");
        assertTrue(map.size() == 1);
        
        List<String> list = ReflectionUtils.newInstance(List.class);
        assertNotNull(list);
        list.add("hello");
        assertTrue(list.size() == 1);
        
        Set<String> set = ReflectionUtils.newInstance(Set.class);
        assertNotNull(set);
        set.add("hello");
        assertTrue(map.size() == 1);
    }
    
    @Test
    public void initDataStructureTypes() {
        
        Map<String, String> map = ReflectionUtils.newInstance(HashMap.class);
        assertNotNull(map);
        map.put("hello", "world");
        assertTrue(map.size() == 1);
        
        List<String> list = ReflectionUtils.newInstance(ArrayList.class);
        assertNotNull(list);
        list.add("hello");
        assertTrue(list.size() == 1);
        
        Set<String> set = ReflectionUtils.newInstance(HashSet.class);
        assertNotNull(set);
        set.add("hello");
        assertTrue(map.size() == 1);
        
    }
}
