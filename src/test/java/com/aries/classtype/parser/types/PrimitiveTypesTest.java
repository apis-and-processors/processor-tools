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

package com.aries.classtype.parser.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.aries.classtype.parser.ClassType;
import com.aries.classtype.parser.domain.Null;
import org.junit.Test;

/**
 * Tests for parsing and using PrimitiveTypes.
 * 
 * @author cdancy
 */
public class PrimitiveTypesTest {

    @Test
    public void shortToShort() throws Exception {

        final ClassType clazzType = ClassType.parse(short.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.SHORT.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.SHORT.getBoxedClass()));
        final Short instance = Short.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void intToInteger() throws Exception {

        final ClassType clazzType = ClassType.parse(int.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.INT.getBoxedClass()));
        final Integer instance = Integer.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void longToLong() throws Exception {

        final ClassType clazzType = ClassType.parse(long.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.LONG.getBoxedClass()));
        final Long instance = Long.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void floatToFloat() throws Exception {

        final ClassType clazzType = ClassType.parse(float.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.FLOAT.getBoxedClass()));
        final Float instance = Float.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void doubleToDouble() throws Exception {

        final ClassType clazzType = ClassType.parse(double.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.DOUBLE.getBoxedClass()));
        final Double instance = Double.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void byteToByte() throws Exception {

        final ClassType clazzType = ClassType.parse(byte.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.BYTE.getBoxedClass()));
        final Byte instance = Byte.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == 0);
    }

    @Test
    public void charToCharacter() throws Exception {

        final ClassType clazzType = ClassType.parse(char.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.CHAR.getBoxedClass()));
        final Character instance = Character.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == '\u0000');
    }

    @Test
    public void booleanToBoolean() throws Exception {

        final ClassType clazzType = ClassType.parse(boolean.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.BOOLEAN.getBoxedClass()));
        final Boolean instance = Boolean.class.cast(clazzType.toObject());
        assertNotNull(instance);
        assertTrue(instance == false);
    }

    @Test
    public void voidToVoid() throws Exception {

        final ClassType clazzType = ClassType.parse(void.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.VOID.getBoxedClass()));
        final Void instance = Void.class.cast(clazzType.toObject());
        assertThat(instance).isNotNull();
    }

    @Test
    public void nullToNull() throws Exception {

        final ClassType clazzType = ClassType.parse(null);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.NULL.getBoxedClass()));
        final Null instance = Null.class.cast(clazzType.toObject());
        assertNotNull(instance);
    }

    @Test
    public void fromNulltoNull() {
        final PrimitiveTypes type = PrimitiveTypes.from(null);
        assertNotNull(type);
        assertThat(type).isEqualByComparingTo(PrimitiveTypes.NULL);
        assertThat(type.getName()).isEqualTo("null");
        assertThat(type.isNullable()).isTrue();
    }

    @Test
    public void fromNullStringtoNull() {
        final PrimitiveTypes type = PrimitiveTypes.from("null");
        assertNotNull(type);
        assertThat(type).isEqualByComparingTo(PrimitiveTypes.NULL);
        assertThat(type.getName()).isEqualTo("null");
        assertThat(type.isNullable()).isTrue();
    }
}
