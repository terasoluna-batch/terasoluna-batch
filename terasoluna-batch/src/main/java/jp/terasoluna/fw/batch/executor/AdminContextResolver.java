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

package jp.terasoluna.fw.batch.executor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * フレームワークが管理する{@code ApplicationContext}のインスタンス生成を解決するためのリゾルバ。
 *
 * @since 3.6
 */
public interface AdminContextResolver {

    /**
     * {@code ApplicationContext}のインスタンス生成を行う。
     *
     * @return フレームワーク管理の{@code ApplicationContext}
     * @throws BeansException {@code ApplicationContext}インスタンス生成失敗例外
     */
    ApplicationContext resolveAdminContext() throws BeansException;
}
