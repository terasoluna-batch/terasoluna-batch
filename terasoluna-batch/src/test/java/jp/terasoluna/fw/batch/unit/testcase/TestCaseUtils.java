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
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.CONTEXTFILE_DIR;
import static jp.terasoluna.fw.batch.unit.common.PropertyKeys.APPLICATIONCONTEXT_FILE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

public class TestCaseUtils {

    /**
     * {@link DaoTestCase}における設定ファイルのリストを返却します。
     * 
     * <pre>
     * {@link DaoTestCase}におけるデフォルトで追加の設定ファイルは
     * 
     * ・beanDefs/AdminContext.xml
     * 
     * です。修正する際は、それぞれ
     * beanDefs -> contextfile.dir
     * applicationContext.xml -> applicationcontext.file
     * のキーに対して設定値を記述してください。
     * </pre>
     * 
     * @param configLocations 元の設定ファイルパス配列
     * @return 追加後の設定ファイルパスリスト
     */
    public static List<String> getConfigLocationsForDaoTestCase(
            String[] configLocations) {
        // beanDefs/AdminDataSource.xmlを追加します。
        List<String> addFiles = Arrays.asList(getValue(CONTEXTFILE_DIR) + "/"
                + getValue(APPLICATIONCONTEXT_FILE));
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
