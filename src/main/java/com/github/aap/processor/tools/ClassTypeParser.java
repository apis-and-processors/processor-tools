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
 * is a pseudo node/tree structure representing a given class, its parent
 * classes, and all interfaces attached therein on top of Type's applied to either.
 * 
 * <p>
 The algorithm involved here recursively climbs a given Classes `extend` hierarchy
 looking for classes and their potential types. Furthermore, and while we're
 climbing the `extend` hierarchy, we recursively climb each potential classes
 multiple interface hierarchy, and their potential extended interfaces, looking
 for types as well.

 Type's themselves, if not valid (i.e. not an actual java class), will be set
 to `java.lang.Object`.

 This ClassTypeParser has been heavily optimized to be as fast as possible with the least
 amount of comparisons required for a given `parse` whilst taking into account
 potential options the user may have requested. Please take heed when attempting
 to optimize and/or refactor this code due to what one might consider convoluted
 and/or overly complicated. Because classes can have N number of Type's we take
 special care to not do any unneeded computations as doing so can potentially
 slow down a process which needs to be as fast as possible.
 </p>
 *
 * @author dancc
 */
public class ClassTypeParser {

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @return instantiated ClassType.
     */
    public static ClassType parse(final Object parseToClassType) {
        return parseObject(parseToClassType, ClassTypeParserOptions.DEFAULT_PARSER_OPTIONS);
    }

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.) whilst
 supplying optional ClassTypeParserOptions (can be null).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    public static ClassType parse(final Object parseToClassType, final ClassTypeParserOptions options) {
        return parseObject(parseToClassType, options != null ? options : ClassTypeParserOptions.DEFAULT_PARSER_OPTIONS);
    }

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.) whilst
 supplying optional ClassTypeParserOptions (can be null). This is the internal version
 of the public method `parse` which does some initial parsing before attempting
 to recursively traverse the super-class/interface hierarchy.
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseObject(final Object parseToClassType,
            final ClassTypeParserOptions options) {

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

        return parseClass(potentialClazz, options);
    }

    /**
     * Parse a ClassType from a given Class. If applicable we will
     * optionally ignore generic types should they match a given regex.
     * 
     * @param clazz the Class to parse a ClassType from.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseClass(final Class clazz,
            final ClassTypeParserOptions options) {

        // 1.) init the parent ClassType and attach any parameters as child ClassType's.
        final ClassType parent = ClassType.instance(clazz.getName());
        if (options.classParamRegex != null) {
            for (final TypeVariable childVariable : clazz.getTypeParameters()) {
                final String properTypeName = properizeTypeName(childVariable.getTypeName());
                if (!properTypeName.matches(options.classParamRegex)) {
                    final ClassType child = ClassType.instance(properTypeName);
                    parent.child(child);
                }
            }
        } else {
            for (final TypeVariable childVariable : clazz.getTypeParameters()) {
                final String properTypeName = properizeTypeName(childVariable.getTypeName());
                final ClassType child = ClassType.instance(properTypeName);
                parent.child(child);
            }
        }

        // 2.) attach any interfaces and superclasses, recursively,
        //     as child ClassType's.
        parseInterfaces(clazz, parent, options);
        parseSuperClass(clazz, parent, options);

        return parent;
    }

    /**
     * Check passed type name and if valid return it otherwise return 'java.lang.Object'.
     * 
     * @param typeName String generally gotten from Type.getTypeName().
     * @return proper type name to use.
     */
    private static String properizeTypeName(final String typeName) {

        // the idea here is that if the passed String is a reserved java name
        // or it does NOT contain a package declaration (i.e. no periods) then
        // we know it's truly generic and thus have NO idea what it is and so
        // we MUST return a generic Object, otherwise use what is given and
        // parse as per usual.
        return (!SourceVersion.isName(typeName)
                || typeName.indexOf(PERIOD_CHAR) == -1)
                ? OBJECT_CLASS
                : typeName;
    }

    /**
     * Parse a ClassType from the passed Classes (e.g. `clazz`) super-class. If the passed
     * class has no super-class then this call amounts to a no-op. If applicable we will
     * optionally ignore super-classes should they match a given regex.
     *
     * @param clazz the Class from whose super-classes we will parse ClassType's from and insert as children.
     * @param parent the parent ClassType we will insert potential child ClassType's into.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     */
    private static void parseSuperClass(final Class clazz,
            final ClassType parent,
            final ClassTypeParserOptions options) {

        final Class superClass = clazz.getSuperclass();
        if (superClass != null
                && !superClass.getName().equals(OBJECT_CLASS)
                && (options.classRegex == null
                || !superClass.getName().matches(options.classRegex))) {

            final Type superType = clazz.getGenericSuperclass();
            final ClassType child = (superType instanceof ParameterizedType)
                    ? parseParameterizedType((ParameterizedType)superType, options)
                    : parseClass(superClass, options);

            parent.child(child);
        }
    }

    /**
     * Parse a ClassType from the passed Classes (e.g. `clazz`) interfaces. If the passed
     * class has no interfaces then this call amounts to a no-op. If applicable we will
     * optionally ignore interfaces should they match a given regex.
     *
     * @param clazz the Class from whose interfaces we will parse ClassType's from and insert as children.
     * @param parent the parent ClassType we will insert potential child ClassType's into.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     */
    private static void parseInterfaces(final Class clazz,
            final ClassType parent,
            final ClassTypeParserOptions options) {

        // we're iterating over all interfaces and checking whether or not
        // said Type is an toObject of ParamterizedType or just a normal Type
        // and adding them as child ClassType's as is appropriate.
        final Type[] childInterfaces = clazz.getGenericInterfaces();
        if (childInterfaces.length > 0) {
            if (options.interfaceRegex != null) {
                for (final Type childInterface : childInterfaces) {
                    if (childInterface instanceof ParameterizedType) {
                        final ParameterizedType childType = (ParameterizedType) childInterface;
                        final Class typeClass = (Class)childType.getRawType();
                        if (!typeClass.getName().matches(options.interfaceRegex)) {
                            final ClassType child = parseParameterizedType(childType, options);
                            parent.child(child);
                        }
                    } else {
                        final String properTypeName = properizeTypeName(childInterface.getTypeName());
                        if (!properTypeName.matches(options.interfaceRegex)) {
                            final ClassType child = ClassType.instance(properTypeName);
                            parent.child(child);
                        }
                    }
                }
            } else {
                for (final Type childInterface : childInterfaces) {
                    if (childInterface instanceof ParameterizedType) {
                        final ParameterizedType childType = (ParameterizedType) childInterface;
                        final ClassType child = parseParameterizedType(childType, options);
                        parent.child(child);
                    } else {
                        final String properTypeName = properizeTypeName(childInterface.getTypeName());
                        final ClassType child = ClassType.instance(properTypeName);
                        parent.child(child);
                    }
                }
            }
        }
    }

    /**
     * Parse a ClassType from a given ParameterizedType. If the passed
     * ParameterizedType has no type/args then this call amounts to a no-op.
     * If applicable we will optionally ignore param/arg Type's should they
     * match a given regex.
     *
     * @param type the ParameterizedType to parse a ClassType from.
     * @param options non-null ClassTypeParserOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseParameterizedType(final ParameterizedType type,
            final ClassTypeParserOptions options) {

        final ParameterizedType pType = (ParameterizedType)type;
        final Class clazz = (Class)pType.getRawType();
        final ClassType parent = ClassType.instance(clazz.getName());
        final Type[] childTypes = pType.getActualTypeArguments();
        if (childTypes.length > 0) {
            if (options.interfaceParamRegex != null) {
                for (final Type childArg : childTypes) {
                    if (childArg instanceof ParameterizedType) {
                        final ParameterizedType childType = (ParameterizedType)childArg;
                        final Class typeClass = (Class)childType.getRawType();
                        if (!typeClass.getName().matches(options.interfaceParamRegex)) {
                            final ClassType child = parseParameterizedType(childType, options);
                            parent.child(child);
                        }
                    } else {
                        final String properTypeName = properizeTypeName(childArg.getTypeName());
                        if (!properTypeName.matches(options.interfaceParamRegex)) {
                            final ClassType child = ClassType.instance(properTypeName);
                            parent.child(child);
                        }
                    }
                }
            } else {
                for (final Type childArg : childTypes) {
                    if (childArg instanceof ParameterizedType) {
                        final ParameterizedType childType = (ParameterizedType)childArg;
                        final ClassType child = parseParameterizedType(childType, options);
                        parent.child(child);
                    } else {
                        final String properTypeName = properizeTypeName(childArg.getTypeName());
                        final ClassType child = ClassType.instance(properTypeName);
                        parent.child(child);
                    }
                }
            }
        }

        parseInterfaces(clazz, parent, options);
        parseSuperClass(clazz, parent, options);

        return parent;
    }

    private ClassTypeParser() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
