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

package jp.terasoluna.fw.file.annotation;

import org.apache.commons.lang3.StringUtils;

/**
 * 大文字変換処理クラス。
 * <p>
 * String型のカラムについて大文字変換を行う。<br>
 * </p>
 */
public class StringConverterToUpperCase implements StringConverter {

    /**
     * 大文字変換を行う。
     * @param column 変換前の文字列
     * @return 大文字に変換された文字列
     */
    public String convert(String column) {
        return StringUtils.upperCase(column);
    }

}
