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

package com.github.aap.processor.tools.utils;

/**
 * Various constants used throughout library.
 * 
 * @author dancc
 */
public class Constants {
    
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

    private Constants() {
        throw new UnsupportedOperationException("Purposely not implemented");
    }
}
