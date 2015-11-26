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
import jp.terasoluna.fw.logger.TLogger;
import jp.terasoluna.fw.util.PropertyUtil;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 管理用Bean定義ファイル、および管理用Bean定義（データソース部）ファイルを指定し、
 * フレームワーク管理の{@code ApplicationContext}を生成する。
 * この２つのBean定義ファイルは{@code PropertyUtil}を用い、以下のプロパティより
 * クラスパスを組み立てた上で{@code ApplicationContext}の生成が行われる。
 * <table>
 * <tr>
 * <th>{@code PropertyUtil}を使用して読み取られるプロパティ</td>
 * <th>説明</th>
 * <th>プロパティ無指定時のデフォルト値</th>
 * </tr>
 * <tr>
 * <td>beanDefinition.admin.classpath</td>
 * <td>管理用Bean定義ファイルを配置するクラスパス</td>
 * <td>(空白)</td>
 * </tr>
 * <tr>
 * <td>beanDefinition.admin.default</td>
 * <td>管理用Bean定義（基本部）ファイル</td>
 * <td>(空白)</td>
 * </tr>
 * <tr>
 * <td>beanDefinition.admin.dataSource</td>
 * <td>管管理用Bean定義（データソース部）ファイル</td>
 * <td>(空白)</td>
 * </tr>
 * </table>
 *
 * @since 3.6
 */
public class DefaultAdminContextResolver implements AdminContextResolver {

    /**
     * ログ
     */
    private static final TLogger LOGGER = TLogger.getLogger(
            DefaultAdminContextResolver.class);

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
     * {@inheritDoc}
     */
    @Override
    public ApplicationContext resolveAdminContext() throws BeansException {
        return new ClassPathXmlApplicationContext(
                concatBeanDefinitionFilePath(BEAN_DEFINITION_ADMIN_CLASSPATH,
                        BEAN_DEFINITION_DEFAULT),
                concatBeanDefinitionFilePath(BEAN_DEFINITION_ADMIN_CLASSPATH,
                        BEAN_DEFINITION_DATASOURCE));
    }

    /**
     * プロパティファイルのクラスパス、Bean定義ファイル名を表すキーから
     * Bean定義ファイルのクラスパスとして連結し、取得する。
     *
     * @param classPathKey クラスパスを表すプロパティキー
     * @param fileNameKey  Bean定義ファイル名を表すプロパティキー
     * @return Bean定義ファイルを表すクラスパス
     * @throws BeanInstantiationException クラスパスを表すプロパティが見つからない場合にスローする。
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
}
