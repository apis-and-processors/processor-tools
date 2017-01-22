/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.type.utils;

import com.google.common.base.Function;
import org.testng.annotations.Test;

/**
 *
 * @author cdancy
 */
public class TypeUtilsTest {
    
    class Handler3 implements Function<Object, Character>, Comparable {
        @Override
        public Character apply(Object object) {
            return 'c';
        }

        @Override
        public int compareTo(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
        
    @Test
    public void testSomeLibrary() {
        Handler3 obj = ReflectionUtils.newInstance(Handler3.class);
        ClassType clazzType = TypeUtils.parseClassType(obj);
        System.out.println(clazzType);
    }
}
