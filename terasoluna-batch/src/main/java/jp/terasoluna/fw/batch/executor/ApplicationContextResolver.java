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

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import org.springframework.context.ApplicationContext;

/**
 * {@code ApplicationContext}のインスタンス生成と破棄を行うリゾルバ。
 *
 * @since 3.6
 */
public interface ApplicationContextResolver {

    /**
     * {@code ApplicationContext}のインスタンス生成を行う。<br>
     *
     * @return {@code ApplicationContext}のインスタンス
     */
    ApplicationContext resolveApplicationContext();

    /**
     * {@code ApplicationContext}のインスタンス生成を行う。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return {@code ApplicationContext}のインスタンス
     */
    ApplicationContext resolveApplicationContext(BatchJobData batchJobData);

    /**
     * 業務用Bean定義のアプリケーションコンテキストをクローズする。<br>
     *
     * @param applicationContext 業務用Bean定義のアプリケーションコンテキスト
     */
    void closeApplicationContext(ApplicationContext applicationContext);
}
