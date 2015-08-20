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

package jp.terasoluna.fw.batch.executor.worker;

import org.springframework.context.ApplicationContext;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;

/**
 * ビジネスロジックの実行を行い、その結果をハンドリングする。
 * @since 3.6
 */
public class BLogicExecutorImpl implements BLogicExecutor {

    /**
     * {@inheritDoc}
     * <p>
     * この実装では、引数のNULLチェックを行わないため呼出し側でチェックする必要がある。<br>
     * ビジネスロジックで例外が発生した場合は、例外ハンドラが指定されていれば例外処理を委譲する。 指定されていなければ何も行わない。<br>
     * この時、返却値{@code BLogicResult}には発生した例外と例外ハンドリング後のステータスが設定される。
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
            }
        }
        return result;
    }
}
