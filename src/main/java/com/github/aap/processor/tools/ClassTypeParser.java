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

import static com.github.aap.processor.tools.utils.Constants.OBJECT_CLASS;
import static com.github.aap.processor.tools.utils.Constants.PERIOD_CHAR;

import com.github.aap.processor.tools.domain.ClassType;
import com.github.aap.processor.tools.types.PrimitiveTypes;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.lang.model.SourceVersion;

/**
 * Parse a ClassType from an arbitrary Object (e.g. Class, Type, etc.). A ClassType
 * is a pseudo node data-structure representing a given class, its parent
 * classes, and all interfaces attached therein.
 * 
 * <p>
 * This class has exactly 1 exposed entry-point `parse` which is static and 
 * the work-horse of this entire project. The idea is that you can pass in 
 * whatever you want and we'll give you back an instance of ClassType.
 * 
 * The algorithm involved here recursively climbs a given Classes `extend` hierarchy 
 * looking for classes and their potential types. Furthermore, and while we're
 * climbing the `extend` hierarchy, we recursively climb each potential classes 
 * multiple interface hierarchy, and their potential extended interfaces, looking 
 * for types.
 * 
 * Type's themselves, if not valid (i.e. not an actual java class), will be set
 * to `java.lang.Object`.
 * 
 * </p>
 *
 * @author dancc
 */
public class ClassTypeParser {
 
    /**
     * Parse an instance of ClassType from some arbitrary Object (e.g. Class, Type, etc.).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @return instantiated ClassType.
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

    /**
     * Parse a ClassType from a given Class object.
     * 
     * @param clazz arbitrary Class object.
     * @return instantiated ClassType.
     */
    private static ClassType parseClass(final Class clazz) {
        
        // 1.) init the parent ClassType
        final ClassType parentClassType = ClassType.newInstance(clazz.getName());
        for (final TypeVariable<?> varType: clazz.getTypeParameters()) {
            parentClassType.add(fromTypeName(varType.getTypeName()));
        }
        
        // 2.) attach any interfaces as child ClassType's
        if (clazz.getGenericInterfaces().length > 0) {
            for (final Type interfaceType : clazz.getGenericInterfaces()) {
                parentClassType.add(parseType(interfaceType));
            }
        }

        // 3.) If we have a valid generic super class then recursively
        //     parse it until we reach null or hit an instance of type Object.
        final Class superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.getName().equals(OBJECT_CLASS)) {
            final Type superType = clazz.getGenericSuperclass();
            final ClassType childType = (superType instanceof ParameterizedType)
                    ? parseType(superType)
                    : parseClass(superClass);
            parentClassType.add(childType);
        }
        
        return parentClassType;
    }
    
    /**
     * Parse a ClassType from a given Type or ParameterizedType.
     * 
     * @param type instance of either Type or ParameterizedType.
     * @return instantiated ClassType.
     */
    private static ClassType parseType(final Type type) {
        
        // if we have an instance of ParameterizedType chances are
        // we'll be able to actually parse out a valid Type, otherwise
        // we'll have to fall back to the value returned by Type.getTypeName()
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType)type;
            final Class originClass = (Class)pType.getRawType();
            final ClassType classType = ClassType.newInstance(originClass.getName());
            for (final Type typeArg : pType.getActualTypeArguments()) {
                classType.add(fromTypeName(typeArg.getTypeName()));
            }
            if (originClass.getGenericInterfaces().length > 0) {
                for (final Type interfaceType : originClass.getGenericInterfaces()) {
                    final ClassType childType = (interfaceType instanceof ParameterizedType)
                            ? parseType(interfaceType)
                            : fromTypeName(interfaceType.getTypeName());
                    classType.add(childType);
                }
            }
            
            // if this Type has a valid super class then start recursively climbing
            // upwards checking for Type's.
            final Class superClass = originClass.getSuperclass();
            if (superClass != null && !superClass.getName().equals(OBJECT_CLASS)) {
                final Type superType = originClass.getGenericSuperclass();
                final ClassType childType = (superType instanceof ParameterizedType)
                        ? parseType(superType)
                        : parseClass(superClass);
                classType.add(childType);
            }
            
            return classType;
        } else {
            return fromTypeName(type.getTypeName());
        }
    }
    
    /**
     * Parse a ClassType from a given string generally gotten from Type.getTypeName().
     * 
     * @param typeName String generally gotten from Type.getTypeName().
     * @return instantiated ClassType.
     */
    private static ClassType fromTypeName(final String typeName) {
        
        // the idea here is that if the passed String is a reserved java name
        // or it does NOT contain a package declaration (i.e. no periods) then
        // we know it's truly generic and thus have NO idea what it is and so
        // we MUST return a generic Object, otherwise use what is given and
        // parse as per usual.
        return (!SourceVersion.isName(typeName) 
                || typeName.indexOf(PERIOD_CHAR) == -1) 
                ? ClassType.newInstance(OBJECT_CLASS)
                : ClassType.newInstance(typeName);
    }
    
    private ClassTypeParser() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
