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

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

/**
 * 業務用DIコンテナの生成を行う。<br>
 * 本クラスでは業務用DIコンテナの親としてフレームワークのDIコンテナを指定する。<br>
 * <p>
 * 業務用DIコンテナのBean定義ファイルが格納されるディレクトリのクラスパスとして
 * {@code batch.properties}のプロパティ{@code beanDefinition.business.classpath}
 * の値を記述しておくこと。<br>
 * 指定がない場合、Bean定義ファイルパスはクラスパス直下にファイルが
 * 格納されているものとみなされる。
 * </p>
 * <p>
 * 配置ディレクトリはプロパティファイル経由で指定するが、このディレクトリパスで
 * {@code ${jobAppCd}}という文字列を埋め込むことで、実行対象のジョブ業務コードに
 * 置換される。<br>
 * これを利用してジョブ単位に業務用DIコンテナのBean定義ファイルの配置ディレクトリ
 * を分割することができる。<br>
 * なお、業務用DIコンテナのBean定義ファイルは「ジョブ業務コード({@code jobAppCode})
 * + &quot;.xml&quot;」というファイル名が固定で使用される。
 * </p>
 *
 * @since 3.6
 */
public class BLogicApplicationContextResolverImpl
        implements BLogicApplicationContextResolver, ApplicationContextAware {

    /**
     * 業務用DIコンテナの親として使用されるフレームワークのDIコンテナ。<br>
     */
    protected ApplicationContext parent;

    /**
     * Bean定義ファイル名のXMLファイル拡張子。<br>
     */
    protected static final String PROPERTY_BEAN_FILENAME_SUFFIX = ".xml";

    /**
     * 置換文字列接頭語。<br>
     */
    protected static final String REPLACE_STRING_PREFIX = "${";

    /**
     * プロパティファイルからインジェクションされる業務用Bean定義ファイルのディレクトリパス。<br>
     */
    @Value("${beanDefinition.business.classpath:}")
    protected String classpath;

    /**
     * SpELパーサー。<br>
     */
    protected SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * SpELパーサー内で使用される、置換文字列の接頭辞:&quot;${&quot; 接尾辞:&quot;}&quot;を規定するパーサーのコンテキスト。<br>
     */
    protected TemplateParserContext parserContext = new TemplateParserContext(
            "${", "}");

    /**
     * フレームワークのDIコンテナである{@code ApplicationCotext}を設定する。<br>
     * {@code ApplicationContextAware}によりDIコンテナ生成時にコールバックされる。
     *
     * @param applicationContext フレームワークのDIコンテナとなるアプリケーションコンテキスト
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.parent = applicationContext;
    }

    /**
     * 業務用DIコンテナとなるアプリケーションコンテキストを取得する。<br>
     * フレームワークのDIコンテナは業務用DIコンテナの親となる。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return 業務用DIコンテナ
     * @throws BeansException DIコンテナの生成に失敗した例外
     */
    @Override
    public ApplicationContext resolveApplicationContext(
            BatchJobData batchJobData) throws BeansException {
        String bLogicBeanDefinitionName = getBeanFileName(batchJobData);

        return new ClassPathXmlApplicationContext(
                new String[] { bLogicBeanDefinitionName }, parent);
    }

    /**
     * ジョブ業務コードを元に、業務用Bean定義ファイルのパスを取得する。<br>
     *
     * @param jobRecord ジョブパラメータ
     * @return 業務用Bean定義ファイルのパス
     */
    protected String getBeanFileName(BatchJobData jobRecord) {
        Assert.notNull(jobRecord);
        StringBuilder str = new StringBuilder();
        String jobAppCd =
                jobRecord.getJobAppCd() == null ? "" : jobRecord.getJobAppCd();

        if (classpath == null || classpath.length() == 0) {
            str.append(jobAppCd).append(PROPERTY_BEAN_FILENAME_SUFFIX);
            return str.toString();
        }

        String pathSource = str.append(classpath).append(jobAppCd)
                .append(PROPERTY_BEAN_FILENAME_SUFFIX).toString();
        if (!classpath.contains(REPLACE_STRING_PREFIX)) {
            return pathSource;
        }

        StandardEvaluationContext eval = new StandardEvaluationContext(
                jobRecord);
        Expression exp = parser.parseExpression(pathSource, parserContext);
        return String.class.cast(exp.getValue(eval));
    }

    /**
     * 業務用DIコンテナをクローズする。<br>
     * 親コンテナは本メソッドではクローズされない点に注意すること。<br>
     *
     * @param applicationContext 業務用Bean定義のアプリケーションコンテキスト
     */
    @Override
    public void closeApplicationContext(ApplicationContext applicationContext) {
        if (applicationContext instanceof AbstractApplicationContext) {
            AbstractApplicationContext aac = AbstractApplicationContext.class
                    .cast(applicationContext);
            aac.close();
            aac.destroy();
        }
    }
}
