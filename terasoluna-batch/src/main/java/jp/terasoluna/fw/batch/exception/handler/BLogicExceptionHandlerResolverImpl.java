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

import java.beans.Introspector;

import org.springframework.context.ApplicationContext;

/**
 * ビジネスロジックの例外ハンドラを解決するリゾルバインタフェース。<br>
 * @since 3.6
 */
public class BLogicExceptionHandlerResolverImpl implements
                                               BLogicExceptionHandlerResolver {

    /**
     * 例外ハンドラのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX = "ExceptionHandler";

    /**
     * デフォルトの例外ハンドラのBean名.
     */
    protected static final String DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME = "defaultExceptionHandler";

    /**
     * {@inheritDoc}
     */
    @Override
    public ExceptionHandler resolveExceptionHandler(ApplicationContext ctx,
            String jobAppCd) {
        String exceptionHandlerBeanName = getExceptionHandlerBeanName(jobAppCd);
        if (ctx.containsBean(exceptionHandlerBeanName)) {
            return ctx
                    .getBean(exceptionHandlerBeanName, ExceptionHandler.class);
        } else if (ctx.containsBean(Introspector
                .decapitalize(exceptionHandlerBeanName))) {
            return ctx.getBean(Introspector
                    .decapitalize(exceptionHandlerBeanName),
                    ExceptionHandler.class);
        }
        if (ctx.containsBean(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME)) {
            return ctx.getBean(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME,
                    ExceptionHandler.class);
        }
        return null;
    }

    /**
     * 実行するExceptionHandlerのBean名を取得する。<br>
     * @param jobAppCd ジョブアプリケーションコード
     * @return ExceptionHandlerのBean名
     */
    protected String getExceptionHandlerBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_EXCEPTION_HANDLER_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }

}
