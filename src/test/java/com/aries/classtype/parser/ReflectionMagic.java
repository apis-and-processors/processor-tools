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

import com.aries.classtype.parser.types.PrimitiveTypes;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Various static utilities surrounding the use of reflection.
 * 
 * @author cdancy
 */
public class ReflectionMagic {

    private static final Class [] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Object [] BEAN_ARG_OBJECT_ARRAY = new Object[0];
    private static final Constructor OBJECT_CONST;

    static {
        try {
            OBJECT_CONST = Object.class.getDeclaredConstructor(EMPTY_CLASS_ARRAY);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Create a new instance from some arbitrary class type.
     *
     * @param <T> arbitrary Type.
     * @param clazz arbitrary Class.
     * @return new instance of arbitrary class.
     */
    public static <T> T instance(final Class<T> clazz) {

        // if non-boxed primitive then return default value
        if (clazz.isPrimitive()) {
            return (T) PrimitiveTypes.from(clazz).getDefaultValue();
        } else {

            Constructor constructor;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException nsme) {
                constructor = sun.reflect.ReflectionFactory
                        .getReflectionFactory()
                        .newConstructorForSerialization(clazz, OBJECT_CONST);
            }

            try {
                return (T) constructor.newInstance(BEAN_ARG_OBJECT_ARRAY);
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ReflectionMagic() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
