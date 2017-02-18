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

package com.github.aap.type.utils.exceptions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when comparing 2 ClassTypes and either the source or target don't match.
 * 
 * @author cdancy
 */
public class TypeMismatchException extends RuntimeException {
    
    public final String source;
    public final String target;
    
    /**
     * Create TypeMismatchException.
     * 
     * @param message message of exception.
     * @param source non-null source type.
     * @param target non-null target type.
     */
    public TypeMismatchException(String message, String source, String target) {
        super(message);
        this.source = checkNotNull(source, "source cannot be null");
        this.target = checkNotNull(target, "target cannot be null");
    }
    
    /**
     * Create TypeMismatchException.
     * 
     * @param message message of exception.
     * @param source non-null source type.
     * @param target non-null target type.
     * @param throwable the source exception this exception came from.
     */
    public TypeMismatchException(String message, String source, String target, Throwable throwable) {
        super(message, throwable);
        this.source = checkNotNull(source, "source cannot be null");
        this.target = checkNotNull(target, "target cannot be null");
    }
}
