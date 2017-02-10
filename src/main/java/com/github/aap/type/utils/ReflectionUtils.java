/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.aap.type.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Throwables;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author cdancy
 */
public class ReflectionUtils {
    
    private static final Object [] EMPTY_OBJECT_ARRAY = new Object[1];
    private static final Constructor OBJECT_CONSTRUCTOR = Object.class.getDeclaredConstructors()[0];    
    
    public static <T> T newInstance(Class<T> beanClass) {   
        checkNotNull(beanClass, "beanClass cannot be null");
        if (beanClass.isInterface()) {
            try {
                Constructor genericConstructor = sun.reflect.ReflectionFactory
                        .getReflectionFactory()
                        .newConstructorForSerialization(beanClass, OBJECT_CONSTRUCTOR);

                return beanClass.cast(genericConstructor.newInstance()); 
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {
                throw Throwables.propagate(ex);
            } 
        } else {
            try {
                Constructor noArgConstructor = beanClass.getDeclaredConstructors()[0];
                noArgConstructor.setAccessible(true);

                if (Number.class.isAssignableFrom(beanClass)) {
                    return beanClass.cast(noArgConstructor.newInstance(0));     
                } else {
                    return beanClass.cast(noArgConstructor.newInstance(EMPTY_OBJECT_ARRAY));     
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {
                
                // second attempt at creating generic object from class
                try {
                    return beanClass.newInstance();
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }     
        }
    }   
}
