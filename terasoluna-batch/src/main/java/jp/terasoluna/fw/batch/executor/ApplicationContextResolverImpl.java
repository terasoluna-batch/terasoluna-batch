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

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
 * 本クラスで生成される業務用DIコンテナの親をsetterインジェクションを用い
 * Bean定義ファイルパスとして指定することができる。<br>
 * 親コンテナは本クラスの初期化時にロード・生成の後フィールドに保持されるため、
 * ライフサイクルはシステム用アプリケーションコンテキスト内で
 * 管理される本クラスのスコープ指定と同一となる。
 * <p>
 * 業務用DIコンテナのBean定義ファイルが格納されるクラスパス直下から
 * Bean定義ファイルの格納ディレクトリまでのパスとして{@code batch.properties}の
 * プロパティ{@code beanDefinition.business.classpath}の値を記述しておくこと。<br>
 * 指定がない場合、Bean定義ファイルパスはクラスパス直下にファイルが
 * 格納されているものとみなされる。
 * </p>
 * <p>
 * ※システム用アプリケーションコンテキストのBean定義ファイル（AdminContext.xml）記述例：
 * <code><pre>
 * &lt;bean id=&quot;blogicContextResolver&quot; class=&quot;jp.terasoluna.fw.batch.executor.ApplicationContextResolverImpl&quot;&gt;
 *   &lt;!-- 共通コンテキストを業務コンテキストの親とする場合、commonContextClassPathでBean定義ファイルのクラスパスを記述する。(複数指定時はカンマ区切り) --&gt;
 *   &lt;property name=&quot;commonContextClassPath&quot; value=&quot;beansDef/commonContext.xml,beansDef/dataSource.xml&quot;/&gt;
 * &lt;/bean&gt;
 * </pre></code>
 * ※プロパティファイル（batch.properties）の記述例：
 * <code><pre>
 * #
 * # 業務用Bean定義ファイルを配置するクラスパス.
 * #
 * beanDefinition.business.classpath=beansDef/
 * </pre></code>
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
public class ApplicationContextResolverImpl
        implements ApplicationContextResolver, InitializingBean,
                   DisposableBean {

    /**
     * ロガー
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(ApplicationContextResolverImpl.class);

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
     * 管理用Bean定義ファイルを配置するクラスパス.
     */
    protected static final String BEAN_DEFINITION_ADMIN_CLASSPATH = "beanDefinition.admin.classpath";

    /**
     * 管理用Bean定義（基本部）
     */
    protected static final String BEAN_DEFINITION_DEFAULT = "beanDefinition.admin.default";

    /**
     * 管理用Bean定義（データソース部）
     */
    protected static final String BEAN_DEFINITION_DATASOURCE = "beanDefinition.admin.dataSource";

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
     * 共通コンテキストとなるXMLBean定義ファイルのクラスパス
     */
    protected String[] commonContextClassPath;

    /**
     * プロパティからシステム用アプリケーションコンテキストを取得する。
     *
     * @return システム用アプリケーションコンテキスト
     */
    @Override
    public ApplicationContext resolveApplicationContext() {
        return new ClassPathXmlApplicationContext(
                concatBeanDefinitionFilePath(BEAN_DEFINITION_ADMIN_CLASSPATH,
                        BEAN_DEFINITION_DEFAULT),
                concatBeanDefinitionFilePath(BEAN_DEFINITION_ADMIN_CLASSPATH,
                        BEAN_DEFINITION_DATASOURCE));
    }

    /**
     * 業務用DIコンテナとなるアプリケーションコンテキストを取得する。<br>
     * 親コンテナが指定されている場合は、業務用DIコンテナの親としてコンテナが生成される。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return 業務用DIコンテナ
     */
    @Override
    public ApplicationContext resolveApplicationContext(
            BatchJobData batchJobData) {
        String blogicBeanDefinitionName = getBeanFileName(batchJobData);
        if (parent != null) {
            return new ClassPathXmlApplicationContext(new String[]{ blogicBeanDefinitionName }, parent);
        }
        return new ClassPathXmlApplicationContext(blogicBeanDefinitionName);
    }

    /**
     * プロパティファイルのクラスパス、Bean定義ファイル名を表すキーから
     * Bean定義ファイルのクラスパスとして連結し、取得する。
     *
     * @param classPathKey クラスパスを表すプロパティキー
     * @param fileNameKey  Bean定義ファイル名を表すプロパティキー
     * @return Bean定義ファイルを表すクラスパス
     */
    protected String concatBeanDefinitionFilePath(String classPathKey,
            String fileNameKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(PropertyUtil.getProperty(classPathKey, ""))
                .append(PropertyUtil.getProperty(fileNameKey, ""));
        String beanDefinitionFileClasspath = sb.toString();
        LOGGER.debug(LogId.DAL025020, beanDefinitionFileClasspath);

        if ("".equals(beanDefinitionFileClasspath)) {
            throw new BeanInstantiationException(ApplicationContext.class,
                    LOGGER.getLogMessage(LogId.EAL025003));
        }
        return beanDefinitionFileClasspath;
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
        }
    }

    /**
     * 共有コンテキストのクラスパスがプロパティとして設定されている時、
     * Bean初期化処理として業務コンテキストの親コンテキストを指定する。
     */
    @Override
    public void afterPropertiesSet() {
        if (this.commonContextClassPath == null || this.commonContextClassPath.length == 0) {
            return;
        }
        this.parent = new ClassPathXmlApplicationContext(this.commonContextClassPath);
    }

    /**
     * 共通コンテキストとなるXMLBean定義ファイルのクラスパスを指定する。
     *
     * @param commonContextClassPath 共通コンテキストとなるXMLBean定義ファイルのクラスパス
     */
    public void setCommonContextClassPath(String[] commonContextClassPath) {
        this.commonContextClassPath = commonContextClassPath;
    }

    /**
     * DIコンテナの破棄時、フィールドで保持されている共有コンテナの破棄を行う。
     */
    @Override
    public void destroy() {
        if (this.parent == null) {
            return;
        }
        closeApplicationContext(this.parent);
    }
}
