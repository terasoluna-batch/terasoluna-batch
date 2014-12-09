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

package jp.terasoluna.fw.collector.validate;

import jp.terasoluna.fw.collector.vo.DataValueObject;

import org.springframework.validation.Errors;

public interface ValidationErrorHandler {
    /**
     * 入力チェックエラー時の処理.<br>
     * @param dataValueObject DataValueObject
     * @param errors Errors
     * @return ValidateStatus
     */
    ValidateErrorStatus handleValidationError(DataValueObject dataValueObject,
            Errors errors);
}
