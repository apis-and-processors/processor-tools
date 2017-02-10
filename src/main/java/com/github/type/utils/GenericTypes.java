/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.type.utils;

/**
 * Handful of OOTB java generic types used.
 * 
 * @author dancc
 */
public enum GenericTypes {

    E("Element"),
    K("Key"),
    N("Number"),
    R("Return"),
    T("Type"),
    V("Value");

    private final String name;
    
    private GenericTypes(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}