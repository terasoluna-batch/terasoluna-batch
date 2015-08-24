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
 *
 */

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.batch.executor.worker.BLogicExecutor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * {@code ThreadGroupManagementAspect}よるジョインポイントの対象となるモッククラス。
 */
public class MockBLogicExecutor implements BLogicExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public BLogicResult execute(ApplicationContext applicationContext,
            BLogic blogic, BLogicParam blogicParam,
            ExceptionHandler exceptionHandler) {

        assertSame(applicationContext, ThreadGroupApplicationContextHolder
                .getCurrentThreadGroupApplicationContext());
        assertEquals("mocked message.", MessageUtil.getMessage("code"));

        BLogicResult result = new BLogicResult();
        result.setBlogicStatus(100);
        return result;
    }
}
