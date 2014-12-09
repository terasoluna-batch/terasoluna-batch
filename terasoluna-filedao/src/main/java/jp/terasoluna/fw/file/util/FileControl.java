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

package jp.terasoluna.fw.file.util;

import java.util.List;

/**
 * ファイル操作機能で提供する処理のインタフェース.
 * @see FileControlImpl
 * @see FileUtility
 */
public interface FileControl {

    /**
     * ファイルのコピー.
     * @param srcFile コピー元のファイルのパス
     * @param newFile コピー先のファイルのパス
     */
    void copyFile(String srcFile, String newFile);

    /**
     * ファイルの削除.
     * @param srcFile 削除するファイルのパス
     */
    void deleteFile(String srcFile);

    /**
     * ファイル結合.
     * @param fileList 結合するファイルのリスト
     * @param newFile 結合してできるファイルのパス
     */
    void mergeFile(List<String> fileList, String newFile);

    /**
     * ファイル名の変更･ファイルの移動.
     * @param srcFile 移動前のパス
     * @param newFile 移動後のパス
     */
    void renameFile(String srcFile, String newFile);

}
