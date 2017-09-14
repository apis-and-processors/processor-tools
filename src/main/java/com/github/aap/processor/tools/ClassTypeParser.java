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

import static com.github.aap.processor.tools.utils.StringUtils.replaceFirstSubStringIfAppearsMoreThanOnce;
import static com.github.aap.processor.tools.utils.Constants.EXTENDS_CLASS_SEPARATOR;
import static com.github.aap.processor.tools.utils.StringUtils.toBuilder;

import com.github.aap.processor.tools.utils.Constants;
import com.github.aap.processor.tools.domain.ClassType;
import com.github.aap.processor.tools.types.PrimitiveTypes;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.lang.model.SourceVersion;

/**
 * Parse a ClassType from an arbitrary Object (e.g. Class, Type, etc.). 
 * 
 * <p>
 * This class has exactly 1 exposed entry-point `parse` which is static and 
 * the work-horse of this entire project. The idea is that you can pass in 
 * whatever you want and we'll give you back an instance of ClassType.
 * </p>
 *
 * @author dancc
 */
public class ClassTypeParser {
   
    private static final Field[] VALUE_FIELD = new Field[1];
    
    static {
        try {
            VALUE_FIELD[0] = String.class.getDeclaredField("value");
            VALUE_FIELD[0].setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            // ignore as we know the field exists
        }
    }
 
    /**
     * Parse an instance of ClassType from some arbitrary Object (e.g. Class, Type, etc.).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @return instance of ClassType
     */
    public static ClassType parse(final Object parseToClassType) {
        Class potentialClazz;
        if (parseToClassType != null) {
            if (parseToClassType instanceof Class) {
                potentialClazz = (Class)parseToClassType;
                if (potentialClazz.isPrimitive()) {
                    potentialClazz = PrimitiveTypes.from(potentialClazz.toGenericString()).getBoxedClass();
                } 
            } else {  
                potentialClazz = parseToClassType.getClass();
            }
        } else {
            potentialClazz = PrimitiveTypes.from(parseToClassType).getBoxedClass();
        }
        
        return parseClass(potentialClazz);
    }

    private static ClassType parseClass(final Class clazz) {
        if (clazz.getGenericSuperclass() != null) {
            
            // check if generic string already returns type info: public class java.util.ArrayList<E>
            final boolean isAlreadyGeneric = clazz.toGenericString().matches(Constants.CLASS_REGEX);
            if (isAlreadyGeneric) {
                return parseGenericString(clazz);
            } else {
                        
                if (clazz.getGenericSuperclass().getTypeName().equals(Constants.OBJECT_CLASS)) {
                    return attachInterfaceClassTypes(parseParts(clazz.getName(), null, null), clazz);
                } else {
                    final StringBuilder builder = toBuilder(clazz.getGenericSuperclass().toString());
                    final String declaringClass = clazz.getDeclaringClass() != null 
                            ? clazz.getDeclaringClass().getName() 
                            : null;
                    replaceFirstSubStringIfAppearsMoreThanOnce(builder, declaringClass);
                    if (builder.charAt(0) == '.') {
                        builder.deleteCharAt(0)
                                .insert(0, EXTENDS_CLASS_SEPARATOR)
                                .insert(0, clazz.getName());
                    }
                    
                    return parseParts(builder.toString(), null, null);
                }       
            }
        } else {
            return attachInterfaceClassTypes(parseGenericString(clazz), clazz);            
        }
    }

    private static ClassType parseGenericString(final Class clazz) {
        final String [] parts = clazz.toGenericString().split(Constants.SPACE_STRING);
        return parseParts(parts[parts.length - 1], null, null);
    }

    private static ClassType parseParts(final String clazzAndTypes, final ClassType parentType, StringBuilder builder) {

        // Special case to catch generally parameter types (e.g. K, V, R)
        // but could potentially be classes as well (though that's a long shot).
        // We make a best case effort here and say that if no `.` character
        // exists (i.e. no package is present) then it must be a generic
        // type and so we just return the Object class.
        if (SourceVersion.isName(clazzAndTypes) 
                && clazzAndTypes.indexOf(Constants.PERIOD_CHAR) == -1) {
            return new ClassType(Constants.OBJECT_CLASS, parentType);
        }
        
        final int index = clazzAndTypes.indexOf(Constants.GREATER_THAN);
        if (index == -1) {
            return new ClassType(clazzAndTypes, parentType);
        } 
        
        final ClassType classType = new ClassType(clazzAndTypes.substring(0, index), parentType);
        try {
                        
            if (builder == null) {
                builder = new StringBuilder();
            }
                        
            final char[] chars = (char[]) VALUE_FIELD[0].get(clazzAndTypes);
            final int stopPoint = chars.length - 2;
            int lessThanEncountered = 0;
            for (int i = index + 1; i < chars.length - 1; i++) {
                
                if (chars[i] != Constants.SPACE_CHAR) {
                    builder.append(chars[i]);
                    
                    switch (chars[i]) {
                    case Constants.GREATER_THAN_CHAR:
                        lessThanEncountered += 1;
                        break;
                    case Constants.LESS_THAN_CHAR:
                        lessThanEncountered -= 1;
                        if (i == stopPoint) {
                            final String foundType = builder.toString();  
                            builder.setLength(0);
                            classType.add(parseParts(foundType, classType, builder));
                        }   
                        break;
                    case Constants.COMMA_CHAR:
                        if (lessThanEncountered == 0) {
                            builder.deleteCharAt(builder.length() - 1);
                            final String foundType = builder.toString();
                            builder.setLength(0);
                            classType.add(parseParts(foundType, classType, builder));                                
                        } 
                        break;
                    default:
                        if (i == stopPoint) {
                            final String foundType = builder.toString();
                            builder.setLength(0);
                            classType.add(parseParts(foundType, classType, builder));  
                        } 
                        break;
                    }
                }                
            }            
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }

        return classType;  
    }

    private static ClassType attachInterfaceClassTypes(final ClassType clazzType, final Class clazz) {
        for (final Type possibleType : clazz.getGenericInterfaces()) {
            final StringBuilder builder = toBuilder(possibleType.getTypeName());
            final String declaringClassName = (clazz.getDeclaringClass() != null) 
                    ? clazz.getDeclaringClass().getName() 
                    : null;
            replaceFirstSubStringIfAppearsMoreThanOnce(builder, declaringClassName);
            if (builder.charAt(0) == '.') {
                builder.deleteCharAt(0);
            }
            clazzType.add(parseParts(builder.toString(), clazzType, null));
        }
        return clazzType;
    }
    
    private ClassTypeParser() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
