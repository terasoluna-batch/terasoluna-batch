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

package jp.terasoluna.fw.file.dao.standard;

import java.util.Map;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import jp.terasoluna.fw.file.dao.FileUpdateDAO;

/**
 * ファイル書き込み用のFileLineWriter生成クラス。
 * <p>
 * ファイルアクセス(データ書込)を行う3つのクラス(CSV、固定長、可変長) に共通する処理をまとめた抽象クラス。 ファイルの種類に対応するサブクラスが処理を行う。<br>
 * 設定例は{@link jp.terasoluna.fw.file.dao.FileUpdateDAO}を参照のこと。
 * </p>
 * @see jp.terasoluna.fw.file.dao.FileUpdateDAO
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileUpdateDAO
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileUpdateDAO
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileUpdateDAO
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileUpdateDAO
 */
public abstract class AbstractFileUpdateDAO implements FileUpdateDAO {

    /**
     * 文字フォーマット処理マップ。
     */
    private Map<String, ColumnFormatter> columnFormatterMap = null;

    /**
     * ファイル名を指定して、<code>FileLineWriter</code> を取得する。
     * @param <T> 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @return ファイル出力用Writer
     */
    @Override
    public abstract <T> FileLineWriter<T> execute(String fileName,
            Class<T> clazz);

    /**
     * 文字フォーマット処理マップを取得する。
     * @return 文字フォーマット処理マップ
     */
    public Map<String, ColumnFormatter> getColumnFormatterMap() {
        return columnFormatterMap;
    }

    /**
     * 文字フォーマット処理マップを設定する。
     * @param columnFormatterMap 文字フォーマット処理マップ
     */
    public void setColumnFormatterMap(
            Map<String, ColumnFormatter> columnFormatterMap) {
        this.columnFormatterMap = columnFormatterMap;
    }
}
