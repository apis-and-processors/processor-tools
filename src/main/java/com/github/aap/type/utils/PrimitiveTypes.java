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
    SHORT("short", (short)0, short.class, Short.class),
    INT("int", (int)0, int.class, Integer.class),
    LONG("long", (long)0L, long.class, Long.class),
    FLOAT("float", (float)0.0f, float.class, Float.class),
    DOUBLE("double", (double)0.0d, double.class, Double.class),
    BYTE("byte", (byte)0, byte.class, Byte.class),
    
    // non-numbers
    CHAR("char", '\u0000', char.class, Character.class),
    BOOLEAN("boolean", false,  boolean.class, Boolean.class),
    VOID("void", null, void.class, Void.class),
    
    // special custom primitives
    NULL("null", null, Null.class, Null.class);

    private final String name;
    private final Object defaultValue;
    private final Class primitiveClass;
    private final Class boxedClass;
    
    private PrimitiveTypes(String name, Object defaultValue, Class primitiveClass, Class boxedClass) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.primitiveClass = primitiveClass;
        this.boxedClass = boxedClass;
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

    public static PrimitiveTypes from(Object obj) {
        return obj == null ? PrimitiveTypes.NULL : from(obj instanceof Class ? ((Class)obj).getName() : obj.toString());
    }
        
    public static PrimitiveTypes from(String name) {
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