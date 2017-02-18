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

package com.github.aap.processor.utils;

import com.github.aap.processor.utils.types.PrimitiveTypes;
import com.github.aap.processor.utils.domain.ClassType;
import com.github.aap.processor.utils.TypeUtils;
import com.github.aap.processor.utils.domain.Null;
import com.github.aap.processor.utils.domain.Unknown;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Various tests for parsing and using PrimitiveTypes.
 * 
 * @author cdancy
 */
public class PrimitiveTypesTest {

    @Test
    public void shortToShort() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(short.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.SHORT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.SHORT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.SHORT.getBoxedClass()));
        Short instance = Short.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void intToInteger() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(int.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.INT.getBoxedClass()));
        Integer instance = Integer.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void longToLong() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(long.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.LONG.getBoxedClass()));
        Long instance = Long.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void floatToFloat() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(float.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.FLOAT.getBoxedClass()));
        Float instance = Float.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void doubleToDouble() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(double.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.DOUBLE.getBoxedClass()));
        Double instance = Double.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void byteToByte() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(byte.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.BYTE.getBoxedClass()));
        Byte instance = Byte.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    
    @Test
    public void charToCharacter() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(char.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.CHAR.getBoxedClass()));
        Character instance = Character.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == '\u0000');
    }
    
    @Test
    public void booleanToBoolean() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(boolean.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.BOOLEAN.getBoxedClass()));
        Boolean instance = Boolean.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == false);
    }
    
    @Test
    public void voidToVoid() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(void.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.VOID.getBoxedClass()));
        Void instance = Void.class.cast(clazzType.toInstance());
        assertNull(instance);
    }
    
    @Test
    public void nullToNull() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(null);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.NULL.getBoxedClass()));
        Null instance = Null.class.cast(clazzType.toInstance());
        assertNull(instance);
    }
    
    @Test
    public void unknownToUnknown() throws Exception {
        
        ClassType clazzType = TypeUtils.parseClassType(Unknown.INSTANCE);
        assertNotNull(clazzType);
        Unknown instance = Unknown.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance.toString().equals(""));
    }
}
