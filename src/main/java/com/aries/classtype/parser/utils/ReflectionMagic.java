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

package com.aries.classtype.parser.utils;

import com.aries.classtype.parser.domain.Null;
import com.aries.classtype.parser.types.PrimitiveTypes;
import com.aries.classtype.parser.domain.Unknown;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Various static utilities surrounding the use of reflection.
 * 
 * @author cdancy
 */
public class ReflectionMagic {

    private static final Class [] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Object [] BEAN_ARG_OBJECT_ARRAY = new Object[0];
    private static final Object [] EMPTY_OBJECT_ARRAY = new Object[1];
    private static final Constructor OBJECT_CONSTRUCTOR = Object.class.getDeclaredConstructors()[0];

    /**
     * Create a new instance from some arbitrary class type.
     *
     * @param <T> arbitrary Type.
     * @param clazz arbitrary Class.
     * @return new instance of arbitrary class.
     */
    public static <T> T instance(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz cannot be null");
        if (clazz.isInterface()) {
            try {

                if (Map.class.isAssignableFrom(clazz)) {
                    return (T) new HashMap();
                } else if (List.class.isAssignableFrom(clazz)) {
                    return (T) new ArrayList();
                } else if (Set.class.isAssignableFrom(clazz)) {
                    return (T) new HashSet();
                }

                final Constructor genericConstructor = sun.reflect.ReflectionFactory
                        .getReflectionFactory()
                        .newConstructorForSerialization(clazz, OBJECT_CONSTRUCTOR);

                return clazz.cast(genericConstructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {

                if (clazz.isAssignableFrom(Unknown.class)) {
                    return (T) Unknown.INSTANCE;
                } else if (clazz.isAssignableFrom(Null.class)) {
                    return (T) Null.INSTANCE;
                }

                final PrimitiveTypes found = PrimitiveTypes.from(clazz);
                if (found != null) {
                    return (T) found.getDefaultValue();
                } else {
                    final Constructor noArgConstructor = clazz.getDeclaredConstructors()[clazz.getDeclaredConstructors().length - 1];
                    noArgConstructor.setAccessible(true);
                    return clazz.cast(noArgConstructor.newInstance(EMPTY_OBJECT_ARRAY));
                }

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {

                // second attempt at creating generic object from class
                try {
                    return (T) clazz.getDeclaredConstructor(EMPTY_CLASS_ARRAY).newInstance(BEAN_ARG_OBJECT_ARRAY);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex2) {

                    final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                    for (int i = 0; i < constructors.length; i++) {
                        final Constructor possibleConstructor = constructors[i];
                        if (possibleConstructor.getParameterCount() == 1) {
                            possibleConstructor.setAccessible(true);
                            try {
                                return (T) possibleConstructor.newInstance(EMPTY_OBJECT_ARRAY);
                            } catch (IllegalAccessException | InstantiationException | InvocationTargetException ite) {
                                throw new RuntimeException(ite);
                            }
                        }
                    }
                    throw new RuntimeException(ex2);
                }
            }
        }
    }

    private ReflectionMagic() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
