/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.aap.type.utils;

import com.github.aap.type.utils.domain.Null;

/**
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
    
    private PrimitiveTypes(String name, Object defaultValue, Class primitiveClass, Class boxedClass, boolean nullable) {
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
    
    public static PrimitiveTypes from(Object obj) {
        return obj == null ? PrimitiveTypes.NULL : from(obj instanceof Class ? ((Class)obj).getName() : obj.toString());
    }
        
    public static PrimitiveTypes from(String name) {
        PrimitiveTypes possibleType = null;
        if (name == null || name.trim().equalsIgnoreCase(TypeUtilsConstants.NULL_STRING)) {
            possibleType = PrimitiveTypes.NULL;
        } else {

            try {
                possibleType = PrimitiveTypes.valueOf(name.toUpperCase());
            } catch (Exception e) {
                // ignore 
            }
            
            if (possibleType == null) {
                for (int i = 0; i < PrimitiveTypes.values().length; i++) {
                    PrimitiveTypes iteration = PrimitiveTypes.values()[i];
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