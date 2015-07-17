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

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.executor.vo.BLogicResult;
import org.springframework.context.ApplicationContext;

/**
 * ビジネスロジックの実行制御を行うインタフェース。<br>
 * @since 3.6
 */
public interface BLogicExecutor {

    /**
     * ビジネスロジックを実行し、実行結果戻り値を返却する。<br>
     *
     * @param applicationContext 業務用Bean定義によるアプリケーションコンテキスト
     * @param blogic ビジネスロジック
     * @param blogicParam ビジネスロジックの入力パラメータ
     * @param exceptionHandler ビジネスロジックの例外ハンドラ
     * @return ビジネスロジックの実行結果
     */
    BLogicResult execute(ApplicationContext applicationContext, BLogic blogic, BLogicParam blogicParam,
                         ExceptionHandler exceptionHandler);
}
