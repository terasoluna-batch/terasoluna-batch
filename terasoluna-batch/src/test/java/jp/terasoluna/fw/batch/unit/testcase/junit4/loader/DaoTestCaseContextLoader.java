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

import jp.terasoluna.fw.batch.unit.testcase.TestCaseUtils;

/**
 * DaoTestCase用コンテキストローダ。
 * 
 * <p>
 * {@link DaoTestCaseJunit4}用の{@link AbstractTerasolunaUnitContextLoader}拡張で、<br>
 * デフォルトBean定義ファイルとして{@link TestCaseUtils#getConfigLocationsForDaoTestCase(String[])}で返却される パスを返却します。
 * </p>
 * 
 */
public class DaoTestCaseContextLoader extends
                                     AbstractTerasolunaUnitContextLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> getDefaultConfigs() {
        return TestCaseUtils.getConfigLocationsForDaoTestCase(null);
    }

}
