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

package jp.terasoluna.fw.batch.blogic;

import java.beans.Introspector;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * ビジネスロジックのインスタンスを解決する実装クラス。<br>
 * @since 3.6
 */
public class BLogicResolverImpl implements BLogicResolver {

    /**
     * ロガー。
     */
    private static TLogger LOGGER = TLogger.getLogger(BLogicResolverImpl.class);

    /**
     * BLogicのBean名に付与する接尾語.
     */
    protected static final String DEFAULT_BLOGIC_BEAN_NAME_SUFFIX = "BLogic";

    /**
     * 実行対象のビジネスロジックインスタンスを取得する。<br>
     * @param ctx インスタンス取得対象となるアプリケーションコンテキスト
     * @param jobAppCd ジョブ業務コード
     * @return ビジネスロジック
     */
    @Override
    public BLogic resolveBLogic(ApplicationContext ctx, String jobAppCd) {
        BLogic blogic = null;

        String blogicBeanName = getBLogicBeanName(jobAppCd);
        // ビジネスロジックのBeanが存在するか確認
        if (ctx.containsBean(blogicBeanName)) {
            return ctx.getBean(blogicBeanName, BLogic.class);
        }

        String decapitalizedName = Introspector.decapitalize(blogicBeanName);
        try {
            blogic = ctx.getBean(decapitalizedName, BLogic.class);
        } catch (BeansException e) {
            LOGGER.error(LogId.EAL025009, decapitalizedName);
            throw e;
        }
        return blogic;
    }

    /**
     * 実行するBLogicのBean名を取得する。<br>
     * @param jobAppCd ジョブアプリケーションコード
     * @return BLogicのBean名
     */
    protected String getBLogicBeanName(String jobAppCd) {
        StringBuilder str = new StringBuilder();

        if (jobAppCd != null && jobAppCd.length() != 0) {
            str.append(jobAppCd);
            str.append(DEFAULT_BLOGIC_BEAN_NAME_SUFFIX);
        }

        return str.toString();
    }

}
