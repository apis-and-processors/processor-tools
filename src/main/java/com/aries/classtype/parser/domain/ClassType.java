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

package com.aries.classtype.parser.domain;

import com.aries.classtype.parser.utils.ReflectionMagic;
import com.aries.classtype.parser.utils.Constants;

import com.aries.classtype.parser.exceptions.TypeMismatchException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassType representing an arbitrary Class with potentially
 * X number of child Classes/Types. This data-structure closely
 * resembles that of a typical Node.
 * 
 * @author dancc
 */
public class ClassType implements Comparable<ClassType> {

    private final Class clazz;
    private final List<ClassType> children = new ArrayList<>();

    public ClassType(final Class clazz) {
        this.clazz = clazz;
    }

    public static ClassType instance(final Class clazz) {
        return new ClassType(clazz);
    }

    /**
     * Add a child subType to this ClassType.
     * 
     * @param classType ClassType to child as a child.
     * @return this ClassType.
     */
    public ClassType child(final ClassType classType) {
        if (classType != null) {
            children.add(classType);
        }
        return this;
    }

    /**
     * Get the class representing this ClassType.
     * 
     * @return Class
     */
    public Class clazz() {
        return clazz;
    }

    /**
     * Qualified Class name of this ClassType (e.g. java.lang.Integer).
     * 
     * @return name of this ClassType
     */
    public String name() {
        return clazz.getName();
    }

    /**
     * List of children.
     * 
     * @return list of child children or empty list if no children.
     */
    public List<ClassType> children() {
        return children;
    }

    /**
     * Find first ClassType matching the passed regex.
     * 
     * @param regex the regular expression used to match.
     * @return found ClassType or null if regex is null or none found.
     */
    public ClassType firstChildMatching(final String regex) {
        return (regex != null) ? firstSubTypeMatching(regex, this) : null;
    }

    /**
     * Inner helper method used for recursively iterating through all potential
     * types to find a match.
     * 
     * @param regex the regular expression used to match.
     * @param classType ClassType to check it, and its children, for match.
     * @return found ClassType or null if regex is null or none found.
     */
    private ClassType firstSubTypeMatching(final String regex, final ClassType classType) {
        if (classType.name().matches(regex)) {
            return classType;
        } else {
            for (int i = 0; i < classType.children().size(); i++) {
                final ClassType innerClassType = firstSubTypeMatching(regex, classType.children().get(i));
                if (innerClassType != null) {
                    return innerClassType;
                }
            }
            return null;
        }
    }

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
    public int compare(final ClassType target) {
        if (target != null) {
            return compare(this, target);
        } else {
            throw new TypeMismatchException("Source type '" + this.name()
                    + "' cannot be comapred to NULL target type", this.name());
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
    private static int compare(final ClassType source, final ClassType target) {
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
                    final int localCount = compare(source.children().get(i), target.children().get(i));
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
                        subTypesMessage.append(source.children.get(index).name());
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
                        subTypesMessage.append(target.children.get(index).name());
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

    /**
     * Helper method to recursively print this ClassType, and all
     * potential children, into a StringBuilder.
     * 
     * @param classType ClassType to print
     * @param builder StringBuilder to write ClassType data into
     */
    private static void print(final ClassType classType, final StringBuilder builder) {
        builder.append(classType.name());
        if (classType.children().size() > 0) {
            builder.append(Constants.GREATER_THAN);
            final int size = classType.children().size();
            for (int i = 0; i < size; i++) {
                print(classType.children().get(i), builder);
                if (size > 0 && i != (size - 1)) {
                    builder.append(Constants.COMMA_SPACE);
                }
            }
            builder.append(Constants.LESS_THAN);
        }
    }

    /**
     * Get an toObject of the backing ClassType.
     * 
     * @return new toObject from backing ClassType
     */
    public Object toObject() {
        return ReflectionMagic.instance(clazz());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        print(this, builder);
        return builder.toString();
    }
}
