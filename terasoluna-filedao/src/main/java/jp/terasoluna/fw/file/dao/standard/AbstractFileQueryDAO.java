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

import jp.terasoluna.fw.file.dao.FileLineIterator;
import jp.terasoluna.fw.file.dao.FileQueryDAO;

/**
 * ファイル読取用のFileLineIterator生成用のクラス。
 * <p>
 * ファイルアクセス(データ取得)を行う3つのクラス(CSV、固定長、可変長) に共通する処理をまとめた抽象クラス。 ファイルの種類に対応するサブクラスが処理を行う。<br>
 * 設定例は{@link jp.terasoluna.fw.file.dao.FileQueryDAO}を参照のこと。
 * </p>
 * @see jp.terasoluna.fw.file.dao.FileQueryDAO
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileQueryDAO
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileQueryDAO
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileQueryDAO
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileQueryDAO
 */
public abstract class AbstractFileQueryDAO implements FileQueryDAO {

    /**
     * カラムパーサーを格納するマップ。
     */
    private Map<String, ColumnParser> columnParserMap = null;

    /**
     * ファイル名を指定して、<code>FileLineIterator</code>を取得する。
     * @param <T> 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @return ファイル行オブジェクト生成用のイテレータ
     */
    public abstract <T> FileLineIterator<T> execute(String fileName,
            Class<T> clazz);

    /**
     * カラムパーサーを格納するマップを取得する。
     * @return カラムパーサーを格納するマップ
     */
    protected Map<String, ColumnParser> getColumnParserMap() {
        return columnParserMap;
    }

    /**
     * カラムパーサーを格納するマップを設定する。
     * @param columnParserMap カラムパーサーを格納するマップ
     */
    public void setColumnParserMap(Map<String, ColumnParser> columnParserMap) {
        this.columnParserMap = columnParserMap;
    }

}
