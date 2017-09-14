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

import com.github.aap.processor.tools.utils.Constants;
import com.github.aap.processor.tools.domain.Null;

/**
 * Java primitive types, and some of our own, along with various attributes
 * surrounding there use.
 * 
 * @author dancc
 */
public enum PrimitiveTypes {
    
    // numbers
    SHORT("short", (short)0, short.class, Short.class, false),
    INT("int", (int)0, int.class, Integer.class, false),
    LONG("long", (long)0L, long.class, Long.class, false),
    FLOAT("float", (float)0.0f, float.class, Float.class, false),
    DOUBLE("double", (double)0.0d, double.class, Double.class, false),
    BYTE("byte", (byte)0, byte.class, Byte.class, false),
    
    // non-numbers
    CHAR("char", '\u0000', char.class, Character.class, false),
    BOOLEAN("boolean", false,  boolean.class, Boolean.class, false),
    VOID("void", null, void.class, Void.class, true),
    
    // special custom primitives
    NULL("null", null, Null.class, Null.class, true);

    private final String name;
    private final Object defaultValue;
    private final Class primitiveClass;
    private final Class boxedClass;
    private final boolean nullable;
    
    private PrimitiveTypes(final String name, 
            final Object defaultValue, 
            final Class primitiveClass, 
            final Class boxedClass, 
            final boolean nullable) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.primitiveClass = primitiveClass;
        this.boxedClass = boxedClass;
        this.nullable = nullable;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Object getDefaultValue() {
        return this.defaultValue;
    }
    
    public Class getPrimitveClass() {
        return this.primitiveClass;
    }
    
    public Class getBoxedClass() {
        return this.boxedClass;
    }

    public boolean isNullable() {
        return this.nullable;
    }
    
    /**
     * Get the corresponding PrimitiveType of given Object.
     * 
     * @param obj Object to infer PrimitiveType from.
     * @return PrimitiveType.
     */
    public static PrimitiveTypes from(final Object obj) {
        return obj == null ? PrimitiveTypes.NULL : from(obj instanceof Class ? ((Class)obj).getName() : obj.toString());
    }
    
    private static PrimitiveTypes from(final String name) {
        PrimitiveTypes possibleType = null;
        if (name == null || name.trim().equalsIgnoreCase(Constants.NULL_STRING)) {
            possibleType = PrimitiveTypes.NULL;
        } else {

            try {
                possibleType = PrimitiveTypes.valueOf(name.toUpperCase());
            } catch (Exception e) {
                // ignore 
            }
            
            if (possibleType == null) {
                for (int i = 0; i < PrimitiveTypes.values().length; i++) {
                    final PrimitiveTypes iteration = PrimitiveTypes.values()[i];
                    if (name.equalsIgnoreCase(iteration.getPrimitveClass().getName()) || name.equalsIgnoreCase(iteration.getBoxedClass().getName())) {
                        possibleType = iteration;
                        break;
                    }
                }
            }
        }
        return possibleType;
    }
}