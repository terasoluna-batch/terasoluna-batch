/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.validation.springmodules;

import org.springframework.validation.BindException;

/**
 * BindExceptionのスタブクラス。エラーありの状態。
 *
 */
public class BaseMultiFieldValidator_BindExceptionStub02 extends BindException {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = 2111159027023367043L;

    /**
     * コンストラクタ
     * @param target
     * @param objectName
     */
    public BaseMultiFieldValidator_BindExceptionStub02(
            Object target, String objectName) {
        super(target, objectName);
    }
    
    /**
     * エラーあり。
     */
    @Override
    public boolean hasErrors() {
        return true;
    }
}
