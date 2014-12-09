/*
 * Copyright (c) 2014 NTT DATA Corporation
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

import org.springframework.util.Assert;

/**
 * 非同期バッチエグゼキュータ内のリトライ可能例外。<br>
 * <br>
 * 非同期バッチエグゼキュータ内でメインメソッド内でリトライ可能なエラーとして捕捉可能
 * であることを示すための例外である。<br>
 * ロギング方針としては互換性を考慮し、本例外クラスで原因例外をラップしてスローする際はログ出力せず、
 * メインメソッド内で原因例外に対して出力すること。<br>
 * 
 * @see jp.terasoluna.fw.batch.executor.AbstractJobBatchExecutor
 * @see jp.terasoluna.fw.batch.executor.AsyncBatchExecutor
 */
public class RetryableExecuteException extends RuntimeException {

    /**
     * シリアルバージョンUID。
     */
    private static final long serialVersionUID = -298806643234717470L;

    /**
     * コンストラクタ。
     * 非同期バッチエグゼキュータのメインメソッドでリトライ可能な例外を渡すこと。<br>
     * 
     * @param cause リトライ可能な原因例外
     * @see jp.terasoluna.fw.batch.executor.AsyncBatchExecutor#main(String[])
     */
    public RetryableExecuteException(Throwable cause) {
        super(cause);
        Assert.notNull(cause);
    }
}
