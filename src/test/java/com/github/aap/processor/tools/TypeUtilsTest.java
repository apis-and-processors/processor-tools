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

import com.github.aap.processor.tools.domain.ClassType;
import com.github.aap.processor.tools.domain.Null;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Random tests to catch potential quirks in using TypeUtils.
 * 
 * @author cdancy
 */
public class TypeUtilsTest {
    
    @Test
    public void testNullType() {
        
        final ClassType instance = TypeUtils.parseClassType(null);
        assertNotNull(instance);
        assertTrue(instance.toClass() == Null.class);
        assertTrue(instance.toInstance().toString().equals("null"));
    }
    
    @Test
    public void testEmptyStringType() {
        
        final ClassType instance = TypeUtils.parseClassType("");
        assertNotNull(instance);
        assertTrue(instance.toClass() == String.class);
        assertTrue(instance.toInstance().toString().equals(""));
    }
    
    @Test
    public void testPrimitiveType() {
        
        final ClassType instance = TypeUtils.parseClassType(123);
        assertNotNull(instance);
        assertTrue(instance.toClass() == Integer.class);
        assertTrue(instance.toInstance().toString().equals("0"));
    }
}
