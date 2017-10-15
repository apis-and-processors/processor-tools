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

package com.github.aap.processor.tools;

/**
 * Options available when parsing a ClassType using the `Parser.parse` method.
 * 
 * @author cdancy
 */
public class ClassTypeParserOptions {

    public static final ClassTypeParserOptions DEFAULT_PARSER_OPTIONS = ClassTypeParserOptions.instance(null, null, null, null);

    public final String classRegex;
    public final String classParamRegex;
    public final String interfaceRegex;
    public final String interfaceParamRegex;

    /**
     * Create a new ParserOptions for use within `Parser.parse()` calls.
     *
     * @param classRegex the super-classes to ignore.
     * @param classParamRegex the super-classes params/args to ignore.
     * @param interfaceRegex the interfaces to ignore.
     * @param interfaceParamRegex the interface params/args to ignore.
     */
    public ClassTypeParserOptions(final String classRegex,
            final String classParamRegex,
            final String interfaceRegex,
            final String interfaceParamRegex) {

        this.classRegex = classRegex;
        this.classParamRegex = classParamRegex;
        this.interfaceRegex = interfaceRegex;
        this.interfaceParamRegex = interfaceParamRegex;
    }

    public static ClassTypeParserOptions instance(final String classRegex,
            final String classParamRegex,
            final String interfaceRegex,
            final String interfaceParamRegex) {
        return new ClassTypeParserOptions(classRegex, classParamRegex, interfaceRegex, interfaceParamRegex);
    }
}
