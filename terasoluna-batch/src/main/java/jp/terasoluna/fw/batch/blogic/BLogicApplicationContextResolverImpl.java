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
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

/**
 * 業務用DIコンテナの生成を行う。<br>
 * 本クラスでは業務用DIコンテナの親DIコンテナを指定することができる。<br>
 * <p>
 * 業務用DIコンテナのBean定義ファイルが格納されるクラスパス直下から
 * Bean定義ファイルの格納ディレクトリまでのパスとして{@code batch.properties}の
 * プロパティ{@code beanDefinition.business.classpath}の値を記述しておくこと。<br>
 * 指定がない場合、Bean定義ファイルパスはクラスパス直下にファイルが
 * 格納されているものとみなされる。
 * </p>
 * <p>
 * 配置ディレクトリはプロパティファイル経由で指定するが、ディレクトリパスに対して
 * ${jobAppCd}のように${}内にBatchJobDataのプロパティの名前を埋め込むことで、
 * {@code BatchJobData}のプロパティの値に置換される。<br>
 * これを利用してジョブ単位に業務用DIコンテナのBean定義ファイルの配置ディレクトリを
 * ジョブ毎の業務用Bean定義ファイルのパスとして分割することができる。<br>
 * なお、業務用DIコンテナのBean定義ファイル名は「ジョブ業務コード({@code jobAppCode})
 * + &quot;.xml&quot;」というファイル名が固定で使用される。
 * </p>
 *
 * @since 3.6
 */
public class BLogicApplicationContextResolverImpl
        implements BLogicApplicationContextResolver {

    /**
     * ロガー
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(BLogicApplicationContextResolverImpl.class);

    /**
     * 業務用DIコンテナの親として使用されるDIコンテナ。<br>
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
     * 置換文字列：ジョブ業務コード（大文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_UPPER = "\\$\\{jobAppCdUpper\\}";

    /**
     * 置換文字列：ジョブ業務コード（大文字）の置換後EL式
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_UPPER_REPLACE = "\\$\\{jobAppCd.toUpperCase()\\}";

    /**
     * 置換文字列：ジョブ業務コード（小文字）
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_LOWER = "\\$\\{jobAppCdLower\\}";

    /**
     * 置換文字列：ジョブ業務コード（小文字）の置換後EL式
     */
    protected static final String REPLACE_STRING_JOB_APP_CD_LOWER_REPLACE = "\\$\\{jobAppCd.toLowerCase()\\}";

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
     * 業務用DIコンテナとなるアプリケーションコンテキストを取得する。<br>
     * 親コンテナが指定されている場合は、業務用DIコンテナの親としてコンテナが生成される。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return 業務用DIコンテナ
     * @throws BeansException DIコンテナの生成に失敗した例外
     */
    @Override
    public ApplicationContext resolveApplicationContext(
            BatchJobData batchJobData) throws BeansException {
        String bLogicBeanDefinitionName = getBeanFileName(batchJobData);
        if (parent != null) {
            return new ClassPathXmlApplicationContext(new String[]{ bLogicBeanDefinitionName }, parent);
        }
        return new ClassPathXmlApplicationContext(bLogicBeanDefinitionName);
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

        str.append(classpath);
        if (!classpath.contains(REPLACE_STRING_PREFIX)) {
            return str.append(jobAppCd).append(PROPERTY_BEAN_FILENAME_SUFFIX)
                    .toString();
        }

        String pathSource = str.toString();
        // ${jobAppCdUpper}, ${jobAppCdLower}のEL式置換
        pathSource = pathSource.replaceAll(REPLACE_STRING_JOB_APP_CD_UPPER,
                REPLACE_STRING_JOB_APP_CD_UPPER_REPLACE);
        pathSource = pathSource.replaceAll(REPLACE_STRING_JOB_APP_CD_LOWER,
                REPLACE_STRING_JOB_APP_CD_LOWER_REPLACE);

        StandardEvaluationContext eval = new StandardEvaluationContext(
                jobRecord);

        Expression exp;
        try {
            exp = parser.parseExpression(pathSource, parserContext);
            str = new StringBuilder(String.class.cast(exp.getValue(eval)));
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    LOGGER.getLogMessage(LogId.EAL025092,
                            "beanDefinition.business.classpath"), e);
        }
        return str.append(jobAppCd).append(PROPERTY_BEAN_FILENAME_SUFFIX)
                .toString();
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

    /**
     * 生成対象となる業務コンテキストの親DIコンテナを設定する。
     *
     * @param parent 親DIコンテナとなる{@code ApplicationContext}
     */
    public void setParent(ApplicationContext parent) {
        this.parent = parent;
    }
}
