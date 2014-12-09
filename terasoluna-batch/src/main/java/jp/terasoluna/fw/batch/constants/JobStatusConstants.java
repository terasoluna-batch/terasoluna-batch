/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.constants;

/**
 * ジョブステータス用定数定義クラス。
 */
public class JobStatusConstants {

    /**
     * ジョブステータス：未実施.
     */
    public static final String JOB_STATUS_UNEXECUTION = "0";

    /**
     * ジョブステータス：実行中.
     */
    public static final String JOB_STATUS_EXECUTING = "1";

    /**
     * ジョブステータス：処理済.
     */
    public static final String JOB_STATUS_PROCESSED = "2";

    /**
     * ジョブステータス：失敗.
     */
    public static final String JOB_STATUS_FAILURE = "3";

}
