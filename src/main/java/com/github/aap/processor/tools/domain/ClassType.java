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

package com.github.aap.processor.tools.domain;

import static com.github.aap.processor.tools.Preconditions.failIfNull;

import com.github.aap.processor.tools.ReflectionMagic;
import com.github.aap.processor.tools.utils.Constants;

import com.github.aap.processor.tools.exceptions.TypeMismatchException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassType representing some Type and potential child Type(s).
 * 
 * @author dancc
 */
public class ClassType implements Comparable<ClassType> {

    private final String name;
    private final ClassType parent;
    private final List<ClassType> subTypes = new ArrayList<>();

    public ClassType(final String name, final ClassType parent) {
        this.name = failIfNull(name, "ClassType name cannot be null").intern();
        this.parent = parent;
    }  

    /**
     * Add a child subType to this ClassType.
     * 
     * @param classType ClassType to add as a child.
     * @return this ClassType.
     */
    public ClassType add(final ClassType classType) {
        if (classType != null) {
            subTypes.add(classType);
        }
        return this;
    }
    
    /**
     * Qualified Class name of this ClassType (e.g. java.lang.Integer).
     * 
     * @return name of this ClassType
     */
    public String name() {
        return name;
    }
       
    /**
     * Parent of this ClassType.
     * 
     * @return parent ClassType or null if has no parent.
     */
    public ClassType parent() {
        return parent;
    }
        
    /**
     * List of child subTypes.
     * 
     * @return list of child subTypes or empty list if no children.
     */
    public List<ClassType> subTypes() {
        return subTypes;    
    }
    
    /**
     * Get sub ClassType at specified index.
     * 
     * @param index index of subType.
     * @return ClassType at specified index.
     */
    public ClassType subTypeAtIndex(final int index) {
        return subTypes.get(index);
    }
    
    /**
     * Find first ClassType matching the passed regex.
     * 
     * @param regex the regular expression used to match.
     * @return found ClassType or null if regex is null or none found.
     */
    public ClassType firstSubTypeMatching(final String regex) {
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
            for (int i = 0; i < classType.subTypes().size(); i++) {
                final ClassType innerClassType = firstSubTypeMatching(regex, classType.subTypes().get(i));
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
        if (source.name.equals(target.name)) {
            
            // All generic types get converted to 'java.lang.Object' thus if 
            // we encounter one, or in this case 2 because of the match, then 
            // return 3 as don't really know what exactly these Objects are.
            if (source.name.equals(Constants.OBJECT_CLASS)) {
                return 3;
            }
            
            final int sourceSize = source.subTypes().size();
            final int targetSize = target.subTypes().size();
            if (sourceSize == targetSize) {
                int counter = 0;
                for (int i = 0; i < sourceSize; i++) {
                    final int localCount = compare(source.subTypes().get(i), target.subTypes().get(i));
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
                        .append(source.name)
                        .append("' has ")
                        .append(sourceSize)
                        .append(" subTypes ");
                if (sourceSize > 0) {
                    subTypesMessage.append('(');
                    for (int index = 0; index < sourceSize; index++) {
                        subTypesMessage.append(source.subTypes.get(index).name);
                        if (index != sourceSize - 1) {
                            subTypesMessage.append(", ");
                        }
                    }
                    subTypesMessage.append(") while ");
                }
                
                subTypesMessage.append(target.name)
                        .append("' has ")
                        .append(targetSize)
                        .append(" subTypes");
                if (targetSize > 0) {
                    subTypesMessage.append(" (");
                    for (int index = 0; index < targetSize; index++) {
                        subTypesMessage.append(target.subTypes.get(index).name);
                        if (index != targetSize - 1) {
                            subTypesMessage.append(", ");
                        }
                    }
                    subTypesMessage.append(')');
                } 
                
                throw new TypeMismatchException(subTypesMessage.toString(), 
                        source.name, target.name); 
            }
        } else {
            if (source.name.equals(Constants.OBJECT_CLASS)) {
                return 1;
            } else if (target.name.equals(Constants.OBJECT_CLASS)) {
                return 2;
            } else {
                throw new TypeMismatchException("Source type '" 
                    + source.name + "' does not match target type '" 
                    + target.name + "'", source.name, target.name);
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
        builder.append(classType.name);
        if (classType.subTypes().size() > 0) {
            builder.append(Constants.GREATER_THAN);
            final int size = classType.subTypes().size();
            for (int i = 0; i < size; i++) {
                print(classType.subTypes().get(i), builder);
                if (size > 0 && i != (size - 1)) {
                    builder.append(Constants.COMMA_SPACE);
                }
            }
            builder.append(Constants.LESS_THAN);
        }
    }
        
    /**
     * Get an instance of the backing ClassType.
     * 
     * @return new instance from backing ClassType
     */
    public Object toInstance() {
        return ReflectionMagic.newInstance(toClass());
    }
    
    /**
     * Get the Class of the backing ClassType.
     * 
     * @return Class or instance of Unknown if type is generic (e.g. T or V).
     */
    public Class toClass() {
        try {
            return Class.forName(name());
        } catch (ClassNotFoundException ex) {
            return Unknown.INSTANCE.getClass();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        print(this, builder);
        return builder.toString();
    }
}
