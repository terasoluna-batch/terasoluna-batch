/*
 * Copyright (c) 2016 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.executor.controller;

import java.io.File;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * 非同期バッチ起動プロセスの終了判定を行う実装クラス。<br>
 * <p>
 * 終了ファイルの有無によって、非同期バッチ起動プロセスを終了するかどうか判定する。<br>
 * 終了ファイルのパスはプロパティファイルの{@code executor.endMonitoringFile}にて指定する。
 * </p>
 * 
 * @since 3.6
 */
public class EndFileStopper implements AsyncBatchStopper, InitializingBean {

    @Value("${executor.endMonitoringFile:}")
    protected String endMonitoringFileName;

    /**
     * ロガー。
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(EndFileStopper.class);

    /**
     * ファイルの有無によってプロセスの終了判定を行う。<br>
     * @return 非同期バッチ起動プロセスの終了条件（<code>true</code>返却時に終了する）
     */
    @Override
    public boolean canStop() {
        File f = new File(endMonitoringFileName);

        // ファイルが存在した場合、INFOログを出力する
        boolean isExist = f.exists();
        if (isExist) {
            LOGGER.info(LogId.IAL025022, endMonitoringFileName);
        }
        return isExist;
    }

    /**
     * プロパティの設定後にファイルパスが設定されているかどうかの確認を行う。<br>
     * @throws IllegalStateException プロパティが未設定、あるいは、空文字である場合
     */
    @Override
    public void afterPropertiesSet() throws IllegalStateException {
        Assert.state(!"".equals(endMonitoringFileName), LOGGER.getLogMessage(
                LogId.EAL025056, this.getClass().getSimpleName(),
                "executor.endMonitoringFile"));

        File f = new File(endMonitoringFileName);
        LOGGER.info(LogId.IAL025025, endMonitoringFileName, f.exists());
    }
}
