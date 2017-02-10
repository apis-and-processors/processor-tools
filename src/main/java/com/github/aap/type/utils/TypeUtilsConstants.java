/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.aap.type.utils;

/**
 *
 * @author dancc
 */
public class TypeUtilsConstants {
    
    public static final String OBJECT_CLASS = "java.lang.Object";

    public static final String GREATER_THAN = "<";
    public static final String LESS_THAN = ">";
    public static final String COMMA_SPACE = ", ";
    
    public static final char GREATER_THAN_CHAR = '<';
    public static final char LESS_THAN_CHAR = '>';
    public static final char COMMA_CHAR = ',';
    public static final char SPACE_CHAR = ' '; 
    
    public static final String CLASS_REGEX = "^(public|private|protected) .+";
    public static final String SPACE_STRING = " ";
    public static final String NULL_STRING = "null";

    private TypeUtilsConstants() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
