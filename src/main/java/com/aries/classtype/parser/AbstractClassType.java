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

package com.aries.classtype.parser;

import static com.aries.classtype.parser.utils.Constants.PERIOD_CHAR;

import com.aries.classtype.parser.exceptions.TypeMismatchException;

import com.aries.classtype.parser.types.PrimitiveTypes;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import javax.lang.model.SourceVersion;

/**
 * Parse a ClassType from an arbitrary Object (e.g. Class, Type, etc.). A ClassType
 * is a pseudo node/tree structure representing a given class, its parent
 * classes, and all interfaces attached therein on top of Type's applied to either.
 * 
 * <p>
 The algorithm involved here recursively climbs a given classes `extend` hierarchy
 looking for classes and their potential types. Furthermore, and while we're
 climbing the `extend` hierarchy, we recursively climb each potential classes
 multiple interface hierarchy, and their potential extended interfaces, looking
 for types as well.

 Type's themselves, if not valid (i.e. not an actual java class), will be set
 to `java.lang.Object`.

 This AbstractClassType has been heavily optimized to be as fast as possible with the least
 amount of comparisons required for a given `parse` whilst taking into account
 potential options the user may have requested. Please take heed when attempting
 to optimize this code due to what one might consider convoluted
 and/or overly complicated. Because classes can have N number of Type's we take
 special care to not do any unneeded computations as doing so can potentially
 slow down a process which needs to be as fast as possible.
 </p>
 *
 * @author dancc
 */
public abstract class AbstractClassType implements Comparable<ClassType> {

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @return instantiated ClassType.
     */
    public static ClassType parse(final Object parseToClassType) {
        return parseObject(parseToClassType, ParseOptions.DEFAULT_PARSER_OPTIONS);
    }

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.) whilst
     * supplying optional ParseOptions (can be null).
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @param options non-null ParseOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    public static ClassType parse(final Object parseToClassType, final ParseOptions options) {
        return parseObject(parseToClassType, options != null ? options : ParseOptions.DEFAULT_PARSER_OPTIONS);
    }

    /**
     * Parse a ClassType from some arbitrary Object (e.g. Class, Type, etc.) whilst
     * supplying optional ParseOptions (can be null). This is the internal version
     * of the public method `parse` which does some initial parsing before attempting
     * to recursively traverse the super-class/interface hierarchy.
     * 
     * @param parseToClassType arbitrary Object to parse a ClassType from.
     * @param options non-null ParseOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseObject(final Object parseToClassType,
            final ParseOptions options) {

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
            potentialClazz = PrimitiveTypes.NULL.getBoxedClass();
        }

        return parseClass(potentialClazz, options);
    }

    /**
     * Parse a ClassType from a given Class. If applicable we will
     * optionally ignore generic types should they match a given regex.
     * 
     * @param clazz the Class to parse a ClassType from.
     * @param options non-null ParseOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseClass(final Class clazz,
            final ParseOptions options) {

        // 1.) init the parent ClassType and attach any parameters as child ClassType's.
        final ClassType parent = ClassType.instance(clazz);
        if (options.classParamRegex != null) {
            for (final TypeVariable childVariable : clazz.getTypeParameters()) {
                final Class properTypeName = parseClassFromTypeName(childVariable.getTypeName());
                if (!properTypeName.getName().matches(options.classParamRegex)) {
                    final ClassType child = ClassType.instance(properTypeName);
                    parent.child(child);
                }
            }
        } else {
            for (final TypeVariable childVariable : clazz.getTypeParameters()) {
                final Class properTypeName = parseClassFromTypeName(childVariable.getTypeName());
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
     * Parse a Class object from the passed type name. If the passed
     * String is truly generic then we'll return an instance of
     * 'java.lang.Object' otherwise we'll load a class from its value.
     * 
     * @param typeName String generally gotten from Type.getTypeName().
     * @return Class parsed from type name.
     */
    private static Class parseClassFromTypeName(final String typeName) {

        // the idea here is that if the passed String is a reserved java name
        // or it does NOT contain a package declaration (i.e. no periods) then
        // we know it's truly generic and thus have NO idea what it is and so
        // we MUST return a generic Object, otherwise use what is given and
        // parse as per usual.
        try {
            return (!SourceVersion.isName(typeName)
                    || typeName.indexOf(PERIOD_CHAR) == -1)
                    ? Object.class
                    : Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse a ClassType from the passed Classes (e.g. `clazz`) super-class. If the passed
     * class has no super-class then this call amounts to a no-op. If applicable we will
     * optionally ignore super-classes should they match a given regex.
     *
     * @param clazz the Class from whose super-classes we will parse ClassType's from and insert as children.
     * @param parent the parent ClassType we will insert potential child ClassType's into.
     * @param options non-null ParseOptions the user may have optionally requested.
     */
    private static void parseSuperClass(final Class clazz,
            final ClassType parent,
            final ParseOptions options) {

        final Class superClass = clazz.getSuperclass();
        if (superClass != null
                && !(superClass == Object.class)
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
     * @param options non-null ParseOptions the user may have optionally requested.
     */
    private static void parseInterfaces(final Class clazz,
            final ClassType parent,
            final ParseOptions options) {

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
                        final Class properTypeName = parseClassFromTypeName(childInterface.getTypeName());
                        if (!properTypeName.getName().matches(options.interfaceRegex)) {
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
                        final Class properTypeName = parseClassFromTypeName(childInterface.getTypeName());
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
     * @param options non-null ParseOptions the user may have optionally requested.
     * @return instantiated ClassType.
     */
    private static ClassType parseParameterizedType(final ParameterizedType type,
            final ParseOptions options) {

        final ParameterizedType pType = (ParameterizedType)type;
        final Class clazz = (Class)pType.getRawType();
        final ClassType parent = ClassType.instance(clazz);
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
                        final Class properTypeName = parseClassFromTypeName(childArg.getTypeName());
                        if (!properTypeName.getName().matches(options.interfaceParamRegex)) {
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
                        final Class properTypeName = parseClassFromTypeName(childArg.getTypeName());
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

    /**
     * Get the list of child ClassType's this ClassType has.
     * 
     * @return list of ClassType's or empty list if no ClassType's defined.
     */
    abstract List<ClassType> children();

    /**
     * Get the name of this ClassType.
     * 
     * @return name of this ClassType.
     */
    abstract String name();

    /**
     * Compare this ClassType to another ClassType.
     * 
     * <p>
     * -1 == source and target do not match
     * 0 == source and target match
     * 1 == source has unknown Type
     * 2 == target has unknown Type
     * 3 == source and target both have unknown Types
     * </p>
     * 
     * @param target ClassType to compare this ClassType to.
     * @return value representing comparison.
     * @throws TypeMismatchException if target is null or any 2 types cannot be compared
     */
    abstract int compare(final ClassType target);

    /**
     * Compare this ClassType to another ClassType.
     * 
     * <p>
     * -1 == source and target do not match
     * 0 == source and target match
     * 1 == source has unknown Type
     * 2 == target has unknown Type
     * 3 == source and target both have unknown Types
     * </p>
     * 
     * @param target ClassType to compare this ClassType to.
     * @return value representing comparison.
     */
    @Override
    public int compareTo(final ClassType target) {
        try {
            return compare(target);
        } catch (TypeMismatchException e) {
            return -1;
        }
    }

    /**
     * Helper method to compare 2 ClassType's against each other. Throws
     * RuntimeException if 2 types are not equal and can't be massaged into
     * one or the other (i.e. java.lang.Integer into java.lang.Object).
     * 
     * @param source ClassType to act as source.
     * @param target ClassType to act as target to compare against.
     * @return value representing comparison.
     */
    protected static int compareTypes(final ClassType source, final ClassType target) {
        if (source.clazz() == target.clazz()) {

            // All generic types get converted to 'java.lang.Object' thus if
            // we encounter one, or in this case 2 because of the match, then
            // return 3 as don't really know what exactly these Objects are.
            if (source.clazz() == Object.class) {
                return 3;
            }

            final int sourceSize = source.children().size();
            final int targetSize = target.children().size();
            if (sourceSize == targetSize) {
                int counter = 0;
                for (int i = 0; i < sourceSize; i++) {
                    final int localCount = compareTypes(source.children().get(i), target.children().get(i));
                    switch (localCount) {
                    case 0:
                        break;
                    case 1:
                        if (counter == 0 || counter == 2) {
                            counter ++;
                        }
                        break;
                    case 2:
                        if (counter < 2) {
                            counter += 2;
                        }
                        break;
                    case 3:
                        counter = 3;
                        break;
                    default:
                        break;
                    }
                }
                return counter;
            } else {

                final StringBuilder subTypesMessage = new StringBuilder("Source type '")
                        .append(source.name())
                        .append("' has ")
                        .append(sourceSize)
                        .append(" subTypes ");
                if (sourceSize > 0) {
                    subTypesMessage.append('(');
                    for (int index = 0; index < sourceSize; index++) {
                        subTypesMessage.append(source.children().get(index).name());
                        if (index != sourceSize - 1) {
                            subTypesMessage.append(", ");
                        }
                    }
                    subTypesMessage.append(") while '");
                }

                subTypesMessage.append(target.name())
                        .append("' has ")
                        .append(targetSize)
                        .append(" subTypes");
                if (targetSize > 0) {
                    subTypesMessage.append(" (");
                    for (int index = 0; index < targetSize; index++) {
                        subTypesMessage.append(target.children().get(index).name());
                        if (index != targetSize - 1) {
                            subTypesMessage.append(", ");
                        }
                    }
                    subTypesMessage.append(')');
                }

                throw new TypeMismatchException(subTypesMessage.toString(),
                        source.name(), target.name());
            }
        } else {
            if (source.clazz() == Object.class) {
                return 1;
            } else if (target.clazz() == Object.class) {
                return 2;
            } else {
                throw new TypeMismatchException("Source type '"
                    + source.name() + "' does not match target type '"
                    + target.name() + "'", source.name(), target.name());
            }
        }
    }

    @Override
    public abstract boolean equals(final Object classType);

    @Override
    public abstract int hashCode();
}
