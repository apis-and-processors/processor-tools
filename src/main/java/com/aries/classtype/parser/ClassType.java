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

import com.aries.classtype.parser.utils.Constants;

import com.aries.classtype.parser.exceptions.TypeMismatchException;
import java.util.List;

/**
 * ClassType representing an arbitrary Class with potentially
 * X number of child Classes/Types. This data-structure closely
 * resembles that of a typical Node or Tree.
 * 
 * @author dancc
 */
public class ClassType extends AbstractClassType {

    protected ClassType(final Class clazz) {
        super(clazz);
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

    @Override
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

    @Override
    public int compare(final ClassType target) {
        if (target != null) {
            return compareTypes(this, target);
        } else {
            throw new TypeMismatchException("Source type '" + this.name()
                    + "' cannot be comapred to NULL target type", this.name());
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        print(this, builder);
        return builder.toString();
    }
}
