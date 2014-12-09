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

package jp.terasoluna.fw.file.dao;

/**
 * ファイルから行のデータを読み取る際に発生した例外をラップするクラス。<br>
 * エラーの情報として以下を持つ。<br>
 * <ul>
 * <li>エラーメッセージ</li>
 * <li>原因例外</li>
 * <li>エラーが発生したデータのデータ部内行番号</li>
 * <li>エラーが発生したデータのカラム名</li>
 * <li>エラーが発生したデータのカラムインデックス</li>
 * </ul>
 * <b>※注意事項</b> FileLineExceptionに格納される行番号情報はファイルに対する行番号ではなく データ部内の行番号です。<br>
 * ヘッダ部が存在する場合はファイルの行番号と合わない為気をつけること。<br>
 */
public class FileLineException extends FileException {

    /**
     * シリアルバージョンUID。
     */
    private static final long serialVersionUID = 2922540035720279260L;

    /**
     * エラーが発生したデータのカラム名。<br>
     * 未設定時は<code>null</code>を持つ。<br>
     */
    private String columnName = null;

    /**
     * エラーが発生したデータのカラム番号。<br>
     * 未設定時は<code>-1</code>を持つ。<br>
     */
    private int columnIndex = -1;

    /**
     * エラーが発生したデータのデータ部内行番号。<br>
     * ファイルの行番号ではなくデータ部内の行番号です。<br>
     * 未設定時は<code>-1</code>を持つ。<br>
     */
    private int lineNo = -1;

    /**
     * コンストラクタ。
     * @param e 原因例外
     */
    public FileLineException(Exception e) {
        super(e);
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public FileLineException(String message) {
        super(message);
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param e 原因例外
     */
    public FileLineException(String message, Exception e) {
        super(message, e);
    }

    /**
     * コンストラクタ。
     * @param e 原因例外
     * @param fileName ファイル名
     * @param lineNo エラーが発生したデータのデータ部内行番号
     */
    public FileLineException(Exception e, String fileName, int lineNo) {
        super(e, fileName);
        this.lineNo = lineNo;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param e 原因例外
     * @param fileName ファイル名
     * @param lineNo エラーが発生したデータのデータ部内行番号
     */
    public FileLineException(String message, Exception e, String fileName,
            int lineNo) {
        super(message, e, fileName);
        this.lineNo = lineNo;
    }

    /**
     * コンストラクタ。
     * @param e 原因例外
     * @param fileName ファイル名
     * @param lineNo エラーが発生したデータのデータ部内行番号
     * @param columnName カラム名
     * @param columnIndex エラーが発生したカラム番号
     */
    public FileLineException(Exception e, String fileName, int lineNo,
            String columnName, int columnIndex) {
        super(e, fileName);
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.lineNo = lineNo;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param e 原因例外
     * @param fileName ファイル名
     * @param lineNo エラーが発生したデータのデータ部内行番号
     * @param columnName カラム名
     * @param columnIndex エラーが発生したカラム番号
     */
    public FileLineException(String message, Exception e, String fileName,
            int lineNo, String columnName, int columnIndex) {
        super(message, e, fileName);
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.lineNo = lineNo;
    }

    /**
     * カラム名を取得する。
     * @return カラム名
     */
    public String getColumnName() {
        return this.columnName;
    }

    /**
     * エラーが発生した行の行番号を取得する。
     * @return エラーが発生した行の行番号
     */
    public int getLineNo() {
        return this.lineNo;
    }

    /**
     * エラーが発生したカラムのカラム番号を取得する。
     * @return エラーが発生したカラムのカラム番号（0から開始）
     */
    public int getColumnIndex() {
        return columnIndex;
    }

}
