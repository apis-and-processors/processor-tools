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
import com.github.aap.processor.tools.domain.ClassType;
import com.github.aap.processor.tools.domain.Null;
import com.github.aap.processor.tools.domain.Unknown;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Tests for parsing and using PrimitiveTypes.
 * 
 * @author cdancy
 */
public class PrimitiveTypesTest {

    @Test
    public void shortToShort() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(short.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.SHORT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.SHORT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.SHORT.getBoxedClass()));
        final Short instance = Short.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void intToInteger() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(int.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.INT.getBoxedClass()));
        final Integer instance = Integer.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void longToLong() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(long.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.LONG.getBoxedClass()));
        final Long instance = Long.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void floatToFloat() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(float.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.FLOAT.getBoxedClass()));
        final Float instance = Float.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void doubleToDouble() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(double.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.DOUBLE.getBoxedClass()));
        final Double instance = Double.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    @Test
    public void byteToByte() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(byte.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.BYTE.getBoxedClass()));
        final Byte instance = Byte.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }
    
    
    @Test
    public void charToCharacter() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(char.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.CHAR.getBoxedClass()));
        final Character instance = Character.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == '\u0000');
    }
    
    @Test
    public void booleanToBoolean() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(boolean.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.BOOLEAN.getBoxedClass()));
        final Boolean instance = Boolean.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance == false);
    }
    
    @Test
    public void voidToVoid() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(void.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.VOID.getBoxedClass()));
        final Void instance = Void.class.cast(clazzType.toInstance());
        assertNull(instance);
    }
    
    @Test
    public void nullToNull() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(null);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.toClass().equals(PrimitiveTypes.NULL.getBoxedClass()));
        final Null instance = Null.class.cast(clazzType.toInstance());
        assertNotNull(instance);
    }
    
    @Test
    public void unknownToUnknown() throws Exception {
        
        final ClassType clazzType = ClassTypeParser.parse(Unknown.INSTANCE);
        assertNotNull(clazzType);
        final Unknown instance = Unknown.class.cast(clazzType.toInstance());
        assertNotNull(instance);
        assertTrue(instance.toString().equals(""));
    }
}
