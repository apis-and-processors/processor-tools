/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.type.utils;

import com.github.aap.type.utils.ClassType;
import com.github.aap.type.utils.TypeUtils;
import com.github.aap.type.utils.ReflectionUtils;
import com.google.common.base.Function;
import org.testng.annotations.Test;

/**
 *
 * @author cdancy
 */
public class TypeUtilsTest {
    
    private static interface BEARS <G, Q> {
        
    }

    private static interface FISH <Avb, RTV> {
        
    }
    
    class Handler3 implements Function<Object, Character>, Comparable<String> {
        @Override
        public Character apply(Object object) {
            return 'c';
        }

        @Override
        public int compareTo(String o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } // com.github.type.utils.TypeUtilsTest$Handler3<java.lang.Comparable, com.google.common.base.Function<java.lang.Object, java.lang.Character>>
    }
        
    @Test
    public void testSomeLibrary() {
        
        Handler3 obj = ReflectionUtils.newInstance(Handler3.class);
        ClassType clazzType = TypeUtils.parseClassType(BEARS.class);
        int here = clazzType.compareTo(TypeUtils.parseClassType(int.class));
        System.out.println("END:  " + TypeUtils.parseClassType(int.class));
    }
}
