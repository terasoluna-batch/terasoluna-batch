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
 * ファイルアクセス機能で発生した例外をラップするクラス。
 * <p>
 * ファイルアクセス機能の初期化時に発生した例外をラップするクラス。
 * </p>
 */
public class FileException extends RuntimeException {

    /**
     * シリアルバージョンUID。
     */
    private static final long serialVersionUID = 3532998688369543117L;

    /**
     * ファイル名。
     */
    private final String fileName;

    /**
     * コンストラクタ。
     * @param e 原因例外
     */
    public FileException(Exception e) {
        super(e);
        fileName = null;
    }

    /**
     * コンストラクタ。
     * @param e 原因例外
     * @param fileName ファイル名
     */
    public FileException(Exception e, String fileName) {
        super(e);
        this.fileName = fileName;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public FileException(String message) {
        super(message);
        fileName = null;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param fileName ファイル名
     */
    public FileException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param e 原因例外
     */
    public FileException(String message, Exception e) {
        super(message, e);
        fileName = null;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param e 原因例外
     * @param fileName ファイル名
     */
    public FileException(String message, Exception e, String fileName) {
        super(message, e);
        this.fileName = fileName;
    }

    /**
     * ファイル名を取得する。
     * @return ファイル名
     */
    public String getFileName() {
        return fileName;
    }
}
