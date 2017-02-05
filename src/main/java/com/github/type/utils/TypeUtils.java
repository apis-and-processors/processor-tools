/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.type.utils;

import com.google.common.base.Throwables;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

/**
 *
 * @author dancc
 */
public class TypeUtils {
   
    private static final Field[] VALUE_FIELD = new Field[1];
    static {
        try {
            VALUE_FIELD[0] = String.class.getDeclaredField("value");
            VALUE_FIELD[0].setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            // ignore as we know the field exists
        }
    }
    
    public static ClassType parseClassType(Object clazz) {
        Class potentialClazz;
        if (clazz != null) {
            if (clazz instanceof Class) {
                potentialClazz = (Class)clazz;
                if (potentialClazz.isPrimitive()) {
                    potentialClazz = PrimitiveTypes.fromName(potentialClazz.toGenericString()).getRawClass();
                } 
            } else {
                potentialClazz = clazz.getClass();
            }
        } else {
            potentialClazz = PrimitiveTypes.fromName(clazz).getRawClass();
        }
        
        return parseClassType(potentialClazz);
    }
    
    private static ClassType parseClassType(Class clazz) {
        if (clazz.getGenericSuperclass() != null) {
            
            // check if generic string already returns type info: public class java.util.ArrayList<E>
            boolean isAlreadyGeneric = clazz.toGenericString().matches(Constants.CLASS_REGEX);
            if (isAlreadyGeneric) {
                String [] parts = clazz.toGenericString().split(Constants.SPACE_STRING);
                return parseClassType(parts[parts.length - 1], null, null);
            } else {
                
                if (clazz.getGenericSuperclass().getTypeName().equals(Constants.OBJECT_CLASS)) {
                    ClassType clazzType = parseClassType(clazz.getName());
                    if (clazz.getInterfaces().length > 0) {
                        Set<TypeToken> tt = TypeToken.of(clazz).getTypes().interfaces();
                        for (TypeToken type : tt) {
                            clazzType.add(parseClassType(type.getType()));
                        }
                    }
                    return clazzType;
                } else {
                    return parseClassType(clazz.getGenericSuperclass());
                }       
            }
        } else {
            String [] parts = clazz.toGenericString().split(Constants.SPACE_STRING);
            return parseClassType(parts[parts.length - 1], null, null);
        }
    }
    
    private static ClassType parseClassType(Type type) {
        return TypeUtils.parseClassType(type.getTypeName());
    }
        
    private static ClassType parseClassType(String clazz) {
        return parseClassType(clazz, null, null);
    }
        
    private static ClassType parseClassType(String clazzAndTypes, ClassType genericTypes, StringBuilder builder) {

        int index = clazzAndTypes.indexOf(Constants.GREATER_THAN);
        if (index == -1) {
            if (genericTypes != null) {
                return new ClassType(clazzAndTypes, genericTypes);
            } else {
                return new ClassType(clazzAndTypes, null);
            }
        }
        
        ClassType types = new ClassType(clazzAndTypes.substring(0, index), (genericTypes != null ? genericTypes : null));
        try {
                        
            if (builder == null) {
                builder = new StringBuilder();
            }
                        
            char[] chars = (char[]) VALUE_FIELD[0].get(clazzAndTypes);
            int stopPoint = chars.length - 2;
            int lessThanEncountered = 0;
            for (int i = index + 1; i < chars.length -1; i++) {
                
                if (chars[i] != Constants.SPACE_CHAR) {
                    builder.append(chars[i]);
                    
                    switch (chars[i]) {
                        case Constants.GREATER_THAN_CHAR:
                            lessThanEncountered += 1;
                            break;
                        case Constants.LESS_THAN_CHAR:
                            lessThanEncountered -= 1;
                            if (i == stopPoint) {
                                String foundType = builder.toString();  
                                builder.setLength(0);
                                ClassType type = parseClassType(foundType, types, builder);
                                types.add(type);
                            }   
                            break;
                        case Constants.COMMA_CHAR:
                            if (lessThanEncountered == 0) {
                                builder.deleteCharAt(builder.length() - 1);
                                String foundType = builder.toString();
                                builder.setLength(0);
                                ClassType type = parseClassType(foundType, types, builder);
                                types.add(type);                                
                            } 
                            break;
                        default:
                            if (i == stopPoint) {
                                String foundType = builder.toString();
                                builder.setLength(0);
                                ClassType type = parseClassType(foundType, types, builder);
                                types.add(type);  
                            } 
                            break;
                    }
                }                
            }            
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw Throwables.propagate(ex);
        }

        return types;  
    }
    
    private TypeUtils() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
