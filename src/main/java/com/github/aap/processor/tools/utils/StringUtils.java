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

import static com.github.aap.processor.tools.Preconditions.failIfNull;

/**
 * Random static methods aiding in string manipulation. These methods
 * can probably be found elsewhere at the expense of importing/depending 
 * on external libraries.
 * 
 * @author cdancy
 */
public class StringUtils {
    
    /**
     * Count how many times subString appears within targetString.
     * 
     * @param targetString String to search for occurrences of subString in.
     * @param subString String that possibly exists within targetString.
     * @return number of times subString appears within targetString.
     */
    public static int countSubString(final String targetString, final String subString) {
        failIfNull(targetString, "targetString cannot be null".intern());
        
        int count = 0;
        if (subString != null) {   
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = targetString.indexOf(subString, lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += subString.length();
                }
            }
        }
        return count;
    }
    
    /**
     * Replace the first occurrence of subString, within targetString, but ONLY if
     * subString appears more than once within targetString.
     * 
     * @param targetString String to replace first occurrence of subString.
     * @param subString String that possibly exists within targetString.
     * @return targetString with first occurrence of subString replaced.
     */
    public static String replaceFirstSubStringIfAppearsMoreThanOnce(String targetString, final String subString) {
        failIfNull(targetString, "targetString cannot be null".intern());

        final int count = countSubString(targetString, subString);
        if (count > 1) {
            targetString = targetString.replaceFirst(subString, "");
        }
        return targetString;
    }
}
