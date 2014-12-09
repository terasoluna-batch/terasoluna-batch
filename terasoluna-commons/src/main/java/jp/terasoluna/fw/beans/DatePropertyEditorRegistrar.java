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

package jp.terasoluna.fw.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Date型のプロパティエディタを生成するクラス。
 *
 */
public class DatePropertyEditorRegistrar 
    implements PropertyEditorRegistrar {
    
    /**
     * このプロパティエディタが使用する日付フォーマット。
     */
    private DateFormat dateFormat = DEFAULT_DATE_FORMAT;
    
    /**
     * デフォルトの日付フォーマット。YYYY/MM/DD形式。
     */
    private static final DateFormat DEFAULT_DATE_FORMAT 
        = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 日付フォーマットを設定する。
     * @param dateFormat 日付フォーマット
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    /**
     * カスタムプロパティエディタを生成する。
     * @param registry プロパティエディタを保持するオブジェクト
     */
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Date.class, 
                new CustomDateEditor((DateFormat) dateFormat.clone(), false));
    }
}