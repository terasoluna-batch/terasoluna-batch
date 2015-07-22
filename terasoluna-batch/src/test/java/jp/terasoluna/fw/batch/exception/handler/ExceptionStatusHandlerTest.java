/*
 * Copyright (c) 2015 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.exception.handler;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * ExceptionStatusHandlerのテストケースクラス
 */
public class ExceptionStatusHandlerTest {

    /**
     * testHandleExceptionテスト 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・ステータスコード255が返却されていること
     * </pre>
     */
    @Test
    public void testHandleException() {
        ExceptionStatusHandler exceptionStatusHandler = new ExceptionStatusHandlerImpl();

        // テスト実施
        // 結果検証
        assertEquals(255, exceptionStatusHandler
                .handleException(new Exception("test exception.")));
    }

}
