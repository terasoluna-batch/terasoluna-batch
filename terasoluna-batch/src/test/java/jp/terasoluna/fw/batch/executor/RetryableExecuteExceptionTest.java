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

package jp.terasoluna.fw.batch.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * RetryabbleExecuteExceptionのテストケース。
 */
public class RetryableExecuteExceptionTest {

    /**
     * コンストラクタのテストケース。
     * 引数に渡される原因例外がnot nullのとき、getCause()により原因例外とそのメッセージが取得できること。
     */
    @Test
    public void testRetryableExecuteException01() {
        RetryableExecuteException exception = new RetryableExecuteException(new Exception("原因例外"));
        assertEquals("原因例外", exception.getCause().getMessage());
    }

    /**
     * コンストラクタのテストケース。
     * 引数に渡される原因例外がnullのとき、アサーションエラーが発生すること。
     */
    @Test
    public void testRetryableExecuteException02() {
        try {
            new RetryableExecuteException(null);
            fail("アサーションエラーが発生しません。");
        } catch (IllegalArgumentException e) {
        }
    }
}
