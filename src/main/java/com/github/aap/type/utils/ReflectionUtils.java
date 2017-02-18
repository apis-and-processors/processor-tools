/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.aap.type.utils;

import com.github.aap.type.utils.domain.Unknown;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Throwables;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Various static utilities surrounding the use of reflection.
 * 
 * @author cdancy
 */
public class ReflectionUtils {
    
    private static final Object [] EMPTY_OBJECT_ARRAY = new Object[1];
    private static final Constructor OBJECT_CONSTRUCTOR = Object.class.getDeclaredConstructors()[0];    
    
    public static <T> T newInstance(Class<T> clazz) {   
        checkNotNull(clazz, "clazz cannot be null");
        if (clazz.isInterface()) {
            try {
                
                if (Map.class.isAssignableFrom(clazz)) {
                    return (T) new HashMap();
                } else if (List.class.isAssignableFrom(clazz)) {
                    return (T) new ArrayList();
                } else if (Set.class.isAssignableFrom(clazz)) {
                    return (T) new HashSet();
                }
                
                Constructor genericConstructor = sun.reflect.ReflectionFactory
                        .getReflectionFactory()
                        .newConstructorForSerialization(clazz, OBJECT_CONSTRUCTOR);

                return clazz.cast(genericConstructor.newInstance()); 
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {
                throw Throwables.propagate(ex);
            } 
        } else {
            try {

                if (clazz.isAssignableFrom(Unknown.class)) {
                    return (T) Unknown.INSTANCE;
                }
                
                PrimitiveTypes found = PrimitiveTypes.from(clazz);
                if (found != null) {
                    return (T) found.getDefaultValue();
                } else {
                    Constructor noArgConstructor = clazz.getDeclaredConstructors()[0];
                    noArgConstructor.setAccessible(true);
                    return clazz.cast(noArgConstructor.newInstance(EMPTY_OBJECT_ARRAY));
                }
                
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | IllegalArgumentException ex) {
                
                // second attempt at creating generic object from class
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }     
        }
    }       
}
