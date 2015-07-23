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

package jp.terasoluna.fw.batch.blogic;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import jp.terasoluna.fw.batch.annotation.util.GenericBeanFactoryAccessorEx;
import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * ビジネスロジックのインスタンス解決の実装クラス。<br>
 * @since 3.6
 * @see BLogicResolver
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
     * JobComponentアノテーション有効化フラグ
     */
    @Value("${enableJobComponentAnnotation}")
    protected boolean enableJobComponentAnnotation = false;

    /**
     * 実行対象のビジネスロジックインスタンスを取得する。<br>
     * @param ctx インスタンス取得対象となるアプリケーションコンテキスト
     * @param jobAppCd ジョブ業務コード
     * @return ビジネスロジック
     */
    @Override
    public BLogic resolveBLogic(ApplicationContext ctx, String jobAppCd) {
        BLogic bLogic = null;
        if (enableJobComponentAnnotation) {
            bLogic = resolveFromAnnotation(ctx, jobAppCd);
            if (bLogic != null) {
                return bLogic;
            }
        }

        String bLogicBeanName = getBLogicBeanName(jobAppCd);
        // ビジネスロジックのBeanが存在するか確認
        if (ctx.containsBean(bLogicBeanName)) {
            return ctx.getBean(bLogicBeanName, BLogic.class);
        }

        String decapitalizedName = Introspector.decapitalize(bLogicBeanName);
        try {
            bLogic = ctx.getBean(decapitalizedName, BLogic.class);
        } catch (BeansException e) {
            LOGGER.error(LogId.EAL025009, decapitalizedName);
            throw e;
        }
        return bLogic;
    }

    /**
     * アノテーションからビジネスロジックを取得する。<br>
     * @param ctx インスタンス取得対象となるアプリケーションコンテキスト
     * @param jobAppCd ジョブアプリケーションコード
     * @return ビジネスロジック
     */
    protected BLogic resolveFromAnnotation(ApplicationContext ctx,
            String jobAppCd) {
        GenericBeanFactoryAccessorEx gbfa = new GenericBeanFactoryAccessorEx(ctx);
        Map<String, Object> jobMap = gbfa
                .getBeansWithAnnotation(JobComponent.class);
        if (jobMap == null) {
            throw new NoSuchBeanDefinitionException("can't find @JobComponent on BLogic in applicationContext.");
        }
        final Set<Map.Entry<String, Object>> entries = jobMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object obj = entry.getValue();
            JobComponent jobComponent = AnnotationUtils.findAnnotation(obj
                    .getClass(), JobComponent.class);
            if (jobComponent.jobId() == null
                    || !jobComponent.jobId().equals(jobAppCd)) {
                continue;
            }
            return BLogic.class.cast(entry.getValue());
        }
        return null;
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
