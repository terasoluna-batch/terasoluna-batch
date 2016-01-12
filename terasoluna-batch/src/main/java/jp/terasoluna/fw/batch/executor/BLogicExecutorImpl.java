/*
 * Copyright (c) 2016 NTT DATA Corporation
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

import org.springframework.context.ApplicationContext;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import jp.terasoluna.fw.logger.TLogger;

/**
 * ビジネスロジックの実行を行い、その結果をハンドリングする。
 * @since 3.6
 */
public class BLogicExecutorImpl implements BLogicExecutor {
    
    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(BLogicExecutorImpl.class);
    
    
    /**
     * {@inheritDoc}
     * <p>
     * この実装では、引数のNULLチェックを行わないため呼出し側でチェックする必要がある。<br>
     * ビジネスロジックで例外が発生した場合は、以下を行う。
     * <ul>
     * <li>例外ハンドラが指定されていれば例外処理を委譲し、その結果を返却値{@code BLogicResult}のステータスに設定する。<br>
     * 指定されていなければ発生した例外のログ出力のみを行う。ステータスは変更しないため、デフォルトの255のまま返却する。</li>
     * <li>返却値{@code BLogicResult}に発生した例外を設定する。</li>
     * </ul>
     * </p>
     */
    @Override
    public BLogicResult execute(ApplicationContext applicationContext,
            BLogic blogic, BLogicParam blogicParam,
            ExceptionHandler exceptionHandler) {

        BLogicResult result = new BLogicResult();

        try {
            int blogicStatus = blogic.execute(blogicParam);
            result.setBlogicStatus(blogicStatus);
        } catch (Throwable th) {
            result.setBlogicThrowable(th);
            if (exceptionHandler != null) {
                result.setBlogicStatus(exceptionHandler
                        .handleThrowableException(th));
            } else {
                LOGGER.error(LogId.EAL025057, th);
            }
        }
        return result;
    }
}
