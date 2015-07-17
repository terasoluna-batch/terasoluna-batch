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

package jp.terasoluna.fw.batch.exception.handler;

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.logger.TLogger;

import org.springframework.stereotype.Component;

/**
 * フレームワーク内部例外ハンドラの実装クラス。<br>
 *
 * @since 3.6
 * @see ExceptionStatusHandler
 */
@Component("exceptionStatusHandler")
public class ExceptionStatusHandlerImpl implements ExceptionStatusHandler {

	private static final TLogger LOGGER = TLogger
			.getLogger(ExceptionStatusHandlerImpl.class);

	/**
	 * フレームワーク機能内部で発生した例外を元にログを出力し、終了ステータスを返却する。<br>
	 *
	 * @param e
	 *            フレームワーク機能内部で発生した例外
	 * @return ジョブの終了ステータス
	 */
	@Override
	public int handleException(Exception e) {
		LOGGER.error(LogId.EAL025031, e);
		
		// TODO リテラルでよいのか確認
		return 100;
	}

}
