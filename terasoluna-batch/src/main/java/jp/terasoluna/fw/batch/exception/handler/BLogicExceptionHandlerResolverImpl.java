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

package jp.terasoluna.fw.batch.exception.handler;

import java.beans.Introspector;

import org.springframework.context.ApplicationContext;

/**
 * ビジネスロジックの例外ハンドラを解決するリゾルバを実装するクラス。<br>
 * @since 3.6
 */
public class BLogicExceptionHandlerResolverImpl implements
                                                BLogicExceptionHandlerResolver {

    /**
     * 例外ハンドラのBean名に付与する接尾語.
     */
    protected static final String EXCEPTION_HANDLER_BEAN_NAME_SUFFIX = "ExceptionHandler";

    /**
     * デフォルトの例外ハンドラのBean名.
     */
    protected static final String DEFAULT_EXCEPTION_HANDLER_BEAN_NAME = "defaultExceptionHandler";

    /**
     * {@inheritDoc}
     * <p>
     * 本実装では、以下の順序でハンドラを解決する。
     * <ol>
     * <li>ジョブ業務コードに対応する例外ハンドラを取得しようと試みる</li>
     * <li>解決できなかった場合にはデフォルトの例外ハンドラを取得しようと試みる</li>
     * <li>それでも解決できなかった場合はNULLを返却する</li>
     * </ol>
     * </p>
     */
    @Override
    public ExceptionHandler resolveExceptionHandler(ApplicationContext ctx,
            String jobAppCd) {

        if (jobAppCd == null || jobAppCd.length() == 0) {
            return null;
        }
        
        if (ctx == null) {
            return null;
        }

        String handlerName = jobAppCd + EXCEPTION_HANDLER_BEAN_NAME_SUFFIX;
        String decapitalizedName = Introspector.decapitalize(handlerName);

        if (ctx.containsBean(handlerName)) {
            return ctx.getBean(handlerName, ExceptionHandler.class);
        }
        if (ctx.containsBean(decapitalizedName)) {
            return ctx.getBean(decapitalizedName, ExceptionHandler.class);
        }
        if (ctx.containsBean(DEFAULT_EXCEPTION_HANDLER_BEAN_NAME)) {
            return ctx.getBean(DEFAULT_EXCEPTION_HANDLER_BEAN_NAME,
                    ExceptionHandler.class);
        }
        return null;
    }
}
