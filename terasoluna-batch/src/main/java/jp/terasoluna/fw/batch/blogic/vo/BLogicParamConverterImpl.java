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

package jp.terasoluna.fw.batch.blogic.vo;

import org.dozer.Mapper;
import org.springframework.util.Assert;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;

/**
 * ジョブパラメータをビジネスロジックの入力オブジェクトに変換するためのクラス<br>
 * @since 3.6
 */
public class BLogicParamConverterImpl implements BLogicParamConverter {

    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger.getLogger(BLogicParamConverterImpl.class);

    /**
     * Dozerのマッパーオブジェクト
     */
    protected Mapper beanMapper;

    /**
     * コンストラクタ<br>
     * @param beanMapper BatchJobDataからBLogicParamに変換するためのマッパーオブジェクト
     */
    public BLogicParamConverterImpl(Mapper beanMapper) {
        Assert.notNull(beanMapper, LOGGER.getLogMessage(LogId.EAL025086));
        this.beanMapper = beanMapper;
    }

    /**
     * ジョブパラメータである<code>BatchJobData</code>をビジネスロジックの入力オブジェクトに変換する。<br>
     *
     * @param batchJobData ジョブパラメータ
     * @return ビジネスロジックの入力オブジェクト
     */
    @Override
    public BLogicParam convertBLogicParam(BatchJobData batchJobData) {
        BLogicParam blogicParam = new BLogicParam();
        beanMapper.map(batchJobData, blogicParam);
        return blogicParam;
    }

}
