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

import com.github.aap.processor.tools.ReflectionUtils;
import com.github.aap.processor.tools.utils.Constants;
import com.github.aap.processor.tools.types.GenericTypes;
import static com.google.common.base.Preconditions.checkNotNull;

import com.github.aap.processor.tools.domain.Unknown;
import com.github.aap.processor.tools.exceptions.TypeMismatchException;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import java.util.List;

/**
 * ClassType representing some Type and potential child Type(s).
 * 
 * @author dancc
 */
public class ClassType implements Comparable<ClassType> {

    private final String name;
    private final ClassType parent;
    private final List<ClassType> subTypes = Lists.newArrayList();

    public ClassType(String name, ClassType parent) {
        this.name = checkNotNull(name, "ClassType name cannot be null");
        this.parent = parent;
    }  

    /**
     * Add a child subType to this ClassType.
     * 
     * @param classType ClassType to add as a child.
     * @return this ClassType.
     */
    public ClassType add(ClassType classType) {
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
    public ClassType subTypeAtIndex(int index) {
        return subTypes.get(index);
    }
    
    /**
     * Find first subType matching the passed regex.
     * 
     * @param regex the regular expression used to match.
     * @return found ClassType or null if regex is null or none found.
     */
    public ClassType firstSubTypeMatching(String regex) {
        ClassType classType = null;
        if (regex != null) {
            for (int i = 0; i < subTypes.size(); i++) {
                classType = subTypes.get(i);
                if (classType.name().matches(regex)) {
                    break;
                }
            }
        }
        return classType;
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
     * @param compareTo ClassType to compare this ClassType to.
     * @return value representing comparison.
     */
    @Override
    public int compareTo(ClassType compareTo) {
        try {
            return compare(this, compareTo);
        } catch (NullPointerException | TypeMismatchException e) {
            return -1;
        }
    }
    
    /**
     * Helper method to compare 2 ClassType's against each other. Throws 
     * RuntimeException if 2 types are not equal and can't be massaged into 
     * one or the other (i.e. java.lang.Integer into java.lang.Object).
     * 
     * <p>
     * 1 == source has unknown types
     * 2 == target has unknown types
     * 3 == source and target have unknown types
     * </p>
     * 
     * @param source ClassType to act as source.
     * @param target ClassType to act as target to compare against.
     * @return value representing comparison.
     */
    private static int compare(ClassType source, ClassType target) {      
        if (source.name.equals(target.name)) {      
            int sourceSize = source.subTypes().size();
            int targetSize = target.subTypes().size();
            if (sourceSize == targetSize) {
                int counter = 0;
                for (int i = 0; i < sourceSize; i++) {
                    int localCount = compare(source.subTypes().get(i), target.subTypes().get(i));
                    switch (localCount) {
                    case 0:
                        break;
                    case 1:
                        if (counter == 0 || counter == 2) {
                            counter ++;
                        }
                        break;
                    case 2:
                        if (counter == 0 || counter == 1) {
                            counter ++;
                        }
                        break;
                    default:
                        break;
                    }
                }
                return counter;
            } else {
                throw new TypeMismatchException("Source type '" 
                    + source.name + "' has " + sourceSize + " subTypes while '" 
                    + target.name + "' has " + targetSize + " subTypes", 
                        source.name, target.name); 
            }
        } else {
            if (isTypeUnknown(source.name)) {
                return 1;
            } else if (isTypeUnknown(target.name)) {
                return 2;
            } else {
                throw new TypeMismatchException("Source type '" 
                    + source.name + "' does not match target type '" 
                    + target.name + "'", 
                        source.name, target.name);
            }
        }        
    }
    
    /**
     * Helper method to determine if the String is of a known Type or not.
     * 
     * @param possiblyUnknownType String representation of the Type
     * @return true if type is unknown false otherwise
     */
    private static boolean isTypeUnknown(String possiblyUnknownType) {
        if (!possiblyUnknownType.equals(Constants.OBJECT_CLASS)) {
            try {
                GenericTypes.valueOf(possiblyUnknownType);                
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Helper method to recursively print this ClassType, and all 
     * potential children, into a StringBuilder.
     * 
     * @param classType ClassType to print
     * @param builder StringBuilder to write ClassType data into
     */
    private static void print(ClassType classType, StringBuilder builder) {
        builder.append(classType.name);
        if (classType.subTypes().size() > 0) {
            builder.append(Constants.GREATER_THAN);
            int size = classType.subTypes().size();
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
        return ReflectionUtils.newInstance(toClass());
    }
    
    /**
     * Get the Class of the backing ClassType.
     * 
     * @return Class or instance of Unknown if type is generic (e.g. T or V).
     */
    public Class toClass() {
        try {
            Class clazz = Class.forName(name());
            Reflection.initialize(clazz);
            return clazz;
        } catch (ClassNotFoundException ex) {
            return Unknown.INSTANCE.getClass();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        print(this, builder);
        return builder.toString();
    }
}
