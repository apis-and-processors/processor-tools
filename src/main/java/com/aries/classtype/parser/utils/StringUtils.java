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

import static com.aries.classtype.parser.utils.Preconditions.failIfNull;

/**
 * Static utility methods aiding in string manipulation.
 * 
 * @author cdancy
 */
public class StringUtils {

    /**
     * Get the first occurrence of a subString but ONLY if the subString
     * appears more than once.
     * 
     * @param targetString String to search for occurrences of subString in.
     * @param subString String that possibly exists within targetString.
     * @return index of the first occurrence of subString, otherwise -1.
     */
    public static int firstOccurence(final StringBuilder targetString, final String subString) {
        failIfNull(targetString, "targetString cannot be null".intern());

        int count = 0;
        int firstOccurence = -1;
        if (subString != null) {
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = targetString.indexOf(subString, lastIndex);
                if (lastIndex != -1) {
                    if (firstOccurence == -1) {
                        firstOccurence = lastIndex;
                    }
                    count++;
                    lastIndex += subString.length();
                }
            }
        }
        return count > 1 ? firstOccurence : -1;
    }

    /**
     * Replace the first occurrence of subString, within targetString, but ONLY if
     * subString appears more than once within targetString.
     * 
     * @param targetString String to replace first occurrence of subString.
     * @param subString String that possibly exists within targetString.
     * @return targetString with first occurrence of subString replaced.
     */
    public static StringBuilder replaceFirstSubStringIfAppearsMoreThanOnce(final StringBuilder targetString, final String subString) {
        final int index = firstOccurence(targetString, subString);
        if (index != -1) {
            targetString.replace(index, subString.length(), "");
        }
        return targetString;
    }
}
