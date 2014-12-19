/*
 * Copyright (c) 2011 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.exception;

import junit.framework.TestCase;

/**
 * IllegalClassTypeExceptionのテストケース。
 */
public class IllegalClassTypeExceptionTest extends TestCase {

    /**
     * {@see IllegalClassTypeException#IllegalClassTypeException(java.lang.String)}
     * のテストケース。
     */
    public void testIllegalClassTypeExceptionString() {
        IllegalClassTypeException e = new IllegalClassTypeException("message");
        assertEquals("message", e.getMessage());
    }

    /**
     * {@see IllegalClassTypeException#IllegalClassTypeException(java.lang.Throwable)}
     * のテストケース。
     */
    public void testIllegalClassTypeExceptionThrowable() {
        IllegalClassTypeException e = new IllegalClassTypeException(new RuntimeException("cause message"));
        assertEquals("cause message", e.getCause().getMessage());
    }

    /**
     * {@see IllegalClassTypeException#IllegalClassTypeException(java.lang.String, java.lang.Throwable)}
     * のテストケース。
     */
    public void testIllegalClassTypeExcetpionStringThrowable() {
        IllegalClassTypeException e = new IllegalClassTypeException("message", new RuntimeException("cause message"));
        assertTrue(e.getMessage().startsWith("message"));
        assertEquals("cause message", e.getCause().getMessage());
    }
}