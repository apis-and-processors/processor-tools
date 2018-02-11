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

import static com.aries.classtype.parser.utils.Constants.NULL_STRING;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.aries.classtype.parser.ClassType;
import com.aries.classtype.parser.ReflectionMagic;
import com.aries.classtype.parser.domain.Null;
import java.util.UUID;
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
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Short instance = Short.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("SHORT");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.SHORT);
        final PrimitiveTypes second = PrimitiveTypes.from("short");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.SHORT);
    }

    @Test
    public void intToInteger() throws Exception {

        final ClassType clazzType = ClassType.parse(int.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.INT.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.INT.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Integer instance = Integer.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("INT");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.INT);
        final PrimitiveTypes second = PrimitiveTypes.from("int");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.INT);
    }

    @Test
    public void longToLong() throws Exception {

        final ClassType clazzType = ClassType.parse(long.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.LONG.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.LONG.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Long instance = Long.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("LONG");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.LONG);
        final PrimitiveTypes second = PrimitiveTypes.from("long");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.LONG);
    }

    @Test
    public void floatToFloat() throws Exception {

        final ClassType clazzType = ClassType.parse(float.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.FLOAT.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.FLOAT.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Float instance = Float.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("FLOAT");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.FLOAT);
        final PrimitiveTypes second = PrimitiveTypes.from("float");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.FLOAT);
    }

    @Test
    public void doubleToDouble() throws Exception {

        final ClassType clazzType = ClassType.parse(double.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.DOUBLE.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.DOUBLE.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Double instance = Double.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("DOUBLE");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.DOUBLE);
        final PrimitiveTypes second = PrimitiveTypes.from("double");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.DOUBLE);
    }

    @Test
    public void byteToByte() throws Exception {

        final ClassType clazzType = ClassType.parse(byte.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BYTE.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.BYTE.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Byte instance = Byte.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == 0);

        final PrimitiveTypes first = PrimitiveTypes.from("BYTE");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.BYTE);
        final PrimitiveTypes second = PrimitiveTypes.from("byte");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.BYTE);
    }

    @Test
    public void charToCharacter() throws Exception {

        final ClassType clazzType = ClassType.parse(char.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.CHAR.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.CHAR.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Character instance = Character.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == '\u0000');

        final PrimitiveTypes first = PrimitiveTypes.from("CHAR");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.CHAR);
        final PrimitiveTypes second = PrimitiveTypes.from("char");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.CHAR);
    }

    @Test
    public void booleanToBoolean() throws Exception {

        final ClassType clazzType = ClassType.parse(boolean.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.name().equals(PrimitiveTypes.BOOLEAN.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.BOOLEAN.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Boolean instance = Boolean.class.cast(parsedObject);
        assertNotNull(instance);
        assertTrue(instance == false);

        final PrimitiveTypes first = PrimitiveTypes.from("BOOLEAN");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.BOOLEAN);
        final PrimitiveTypes second = PrimitiveTypes.from("boolean");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.BOOLEAN);

        final PrimitiveTypes third = PrimitiveTypes.from("BooLEAn");
        assertThat(third).isNotNull();
        assertThat(third).isEqualTo(PrimitiveTypes.BOOLEAN);
        final PrimitiveTypes fourth = PrimitiveTypes.from("boOleaN");
        assertThat(fourth).isNotNull();
        assertThat(fourth).isEqualTo(PrimitiveTypes.BOOLEAN);
    }

    @Test
    public void voidToVoid() throws Exception {

        final ClassType clazzType = ClassType.parse(void.class);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.toString().equals(PrimitiveTypes.VOID.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.VOID.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Void instance = Void.class.cast(parsedObject);
        assertThat(instance).isNotNull();

        final PrimitiveTypes first = PrimitiveTypes.from("VOID");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.VOID);
        final PrimitiveTypes second = PrimitiveTypes.from("void");
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.VOID);
    }

    @Test
    public void nullToNull() throws Exception {

        final ClassType clazzType = ClassType.parse(null);
        assertNotNull(clazzType);
        assertTrue(clazzType.name().equals(PrimitiveTypes.NULL.getBoxedClass().getName()));
        assertTrue(clazzType.clazz().equals(PrimitiveTypes.NULL.getBoxedClass()));
        final Object parsedObject = ReflectionMagic.instance(clazzType.clazz());

        final Null instance = Null.class.cast(parsedObject);
        assertNotNull(instance);

        final PrimitiveTypes first = PrimitiveTypes.from("NULL");
        assertThat(first).isNotNull();
        assertThat(first).isEqualTo(PrimitiveTypes.NULL);
        final PrimitiveTypes second = PrimitiveTypes.from(NULL_STRING);
        assertThat(second).isNotNull();
        assertThat(second).isEqualTo(PrimitiveTypes.NULL);
        final PrimitiveTypes third = PrimitiveTypes.from(NULL_STRING + "     ");
        assertThat(third).isNotNull();
        assertThat(third).isEqualTo(PrimitiveTypes.NULL);
    }

    @Test
    public void fromNulltoNull() {
        final PrimitiveTypes type = PrimitiveTypes.from(null);
        assertNotNull(type);
        assertThat(type).isEqualTo(PrimitiveTypes.NULL);
        assertThat(type).isEqualByComparingTo(PrimitiveTypes.NULL);
        assertThat(type.getName()).isEqualTo(NULL_STRING);
        assertThat(type.isNullable()).isTrue();
    }

    @Test
    public void fromNullStringtoNull() {
        final PrimitiveTypes type = PrimitiveTypes.from(NULL_STRING);
        assertNotNull(type);
        assertThat(type).isEqualByComparingTo(PrimitiveTypes.NULL);
        assertThat(type.getName()).isEqualTo(NULL_STRING);
        assertThat(type.isNullable()).isTrue();
    }

    @Test
    public void testPrimitiveClassesOfPrimitiveTypes() {
        final PrimitiveTypes possibleShort = PrimitiveTypes.SHORT;
        assertThat(possibleShort.getPrimitveClass()).isEqualTo(short.class);
        final PrimitiveTypes possibleInt = PrimitiveTypes.INT;
        assertThat(possibleInt.getPrimitveClass()).isEqualTo(int.class);
        final PrimitiveTypes possibleLong = PrimitiveTypes.LONG;
        assertThat(possibleLong.getPrimitveClass()).isEqualTo(long.class);

        final PrimitiveTypes possibleFloat = PrimitiveTypes.FLOAT;
        assertThat(possibleFloat.getPrimitveClass()).isEqualTo(float.class);
        final PrimitiveTypes possibleDouble = PrimitiveTypes.DOUBLE;
        assertThat(possibleDouble.getPrimitveClass()).isEqualTo(double.class);
        final PrimitiveTypes possibleByte = PrimitiveTypes.BYTE;
        assertThat(possibleByte.getPrimitveClass()).isEqualTo(byte.class);

        final PrimitiveTypes possibleChar = PrimitiveTypes.CHAR;
        assertThat(possibleChar.getPrimitveClass()).isEqualTo(char.class);
        final PrimitiveTypes possibleBoolean = PrimitiveTypes.BOOLEAN;
        assertThat(possibleBoolean.getPrimitveClass()).isEqualTo(boolean.class);
        final PrimitiveTypes possibleVoid = PrimitiveTypes.VOID;
        assertThat(possibleVoid.getPrimitveClass()).isEqualTo(void.class);
    }

    @Test
    public void testPrimitiveTypesFromErroneousData() {
        final PrimitiveTypes type = PrimitiveTypes.from(UUID.randomUUID().toString());
        assertThat(type).isNull();;
    }
}
