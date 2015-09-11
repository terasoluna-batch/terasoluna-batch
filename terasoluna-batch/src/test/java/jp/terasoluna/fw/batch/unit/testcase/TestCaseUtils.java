package jp.terasoluna.fw.batch.unit.testcase;

/*
 * Copyright (c) 2012 NTT DATA Corporation
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

import static jp.terasoluna.fw.batch.unit.common.DefaultProperties.getValue;
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.APPLICATIONCONTEXT_FILE;
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.MOCKDAOBEANS_FILE;
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.MODULECONTEXT_FILE;
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.WEBINF_DIR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

public class TestCaseUtils {

    /**
     * {@link DaoTestCase}におけるデフォルトの設定ファイルを追加した設定ファイルのリストを返却します。
     * 
     * <pre>
     * {@link DaoTestCase}におけるデフォルトで追加の設定ファイルは
     * 
     * ・WebContext/WEB-INF/applicationContext.xml
     * ・WebContext/WEB-INF/moduleContext.xml
     * 
     * です。
     * これを上書きする場合は、
     * terasoluna-unit-override.properties
     * に設定します。
     * 
     * それぞれ
     * WebContext -> webapp.path
     * WEB-INF -> webinf.dir
     * applicationContext.xml -> applicationcontext.file
     * moduleContext.xml -> modulecontext.file
     * のキーに対して設定値を記述してください。
     * </pre>
     * 
     * @param configLocations 元の設定ファイルパス配列
     * @return 追加後の設定ファイルパスリスト
     */
    public static List<String> getConfigLocationsForDaoTestCase(
            String[] configLocations) {
        // WEB-INF/applicationContext.xml,WEB-INF/moduleContext.xmlを追加します。
        List<String> addFiles = Arrays.asList(getValue(WEBINF_DIR) + "/"
                + getValue(APPLICATIONCONTEXT_FILE), getValue(WEBINF_DIR) + "/"
                + getValue(MODULECONTEXT_FILE));
        return getDefaultAddedConfigLocations(configLocations, addFiles);
    }

    /**
     * {@link MockDaoInjectedTestCase}におけるデフォルトの設定ファイルを追加した設定ファイルのリストを返却します。
     * 
     * <pre>
     * {@link MockDaoInjectedTestCase}におけるデフォルトで追加の設定ファイルは
     * 
     * ・WEB-INF/applicationContext.xml
     * ・mockDaoBeans.xml
     * 
     * です。
     * これを上書きする場合は、
     * terasoluna-unit-override.properties
     * に設定します。
     * 
     * それぞれ
     * WEB-INF -> webinf.dir
     * applicationContext.xml -> applicationcontext.file
     * mockDaoBeans.xml -> mockdaobeans.file
     * のキーに対して設定値を記述してください。
     * </pre>
     * 
     * @param configLocations 元の設定ファイルパス配列
     * @return 追加後の設定ファイルパスリスト
     */
    public static List<String> getConfigLocationsForMockDaoInjectedTestCase(
            String[] configLocations) {
        // WEB-INF/moduleContext.xml,mockDaoBeans.xmlを追加します。
        List<String> addFiles = Arrays.asList(getValue(WEBINF_DIR) + "/"
                + getValue(MODULECONTEXT_FILE), getValue(MOCKDAOBEANS_FILE));
        return getDefaultAddedConfigLocations(configLocations, addFiles);
    }

    /**
     * デフォルトで追加する設定ファイルのリストを返却します。
     * 
     * @param configLocations 元の設定ファイルパス配列
     * @param addFiles 追加する設定ファイルパスリスト
     * @return 追加後の設定ファイルパスリスト
     */
    public static List<String> getDefaultAddedConfigLocations(
            String[] configLocations, List<String> addFiles) {
        List<String> result = new ArrayList<String>();

        if (configLocations != null) {
            Collections.addAll(result, configLocations);
        }
        // デフォルトで追加するファイルに関しては存在チェックします
        ClassLoaderUtils.addPathIfExists(result, addFiles);
        return result;
    }
}
