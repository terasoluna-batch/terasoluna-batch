package jp.terasoluna.fw.batch.unit.testcase.junit4.loader;

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

import java.util.List;

import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * 抽象コンテキストローダー拡張。
 * 
 * <p>
 * コンテキストに読み込む設定ファイルにデフォルトBean定義ファイルを追加します。<br>
 * また、コンテキストルートをクラスパスに追加します。
 * </p>
 */
public abstract class AbstractTerasolunaUnitContextLoader extends
                                                         GenericXmlContextLoader {
    /**
     * ロガー。
     */
    private static final Log logger = LogFactory
            .getLog(AbstractTerasolunaUnitContextLoader.class);

    /**
     * デフォルトBean定義ファイルパスを返します。
     * 
     * @return デフォルトBean定義ファイルパス。
     */
    protected abstract List<String> getDefaultConfigs();

    /*
     * (non-Javadoc)
     * @see org.springframework.test.context.support.AbstractContextLoader#isGenerateDefaultLocations()
     */
    @Override
    protected boolean isGenerateDefaultLocations() {
        // 設定ファイルが指定されていなかった場合に、デフォルトファイルは読み込まない。
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.test.context.support.AbstractContextLoader#modifyLocations(java.lang.Class, java.lang.String[])
     */
    @Override
    protected String[] modifyLocations(Class<?> clazz, String... locations) {
        ClassLoaderUtils.addContextRootToClassPath();
        List<String> configLocations = getDefaultConfigs();

        for (String location : super.modifyLocations(clazz, locations)) {
            configLocations.add(location);
        }
        logger.info(clazz + " " + configLocations);
        return configLocations.toArray(new String[configLocations.size()]);
    }
}
