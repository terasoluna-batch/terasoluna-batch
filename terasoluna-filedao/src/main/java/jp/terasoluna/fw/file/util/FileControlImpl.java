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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.file.dao.FileException;

/**
 * FileControlインタフェースを実装するクラス.
 * <p>
 * このクラスは、ファイル操作処理を実行するFileUtilityクラスをラップしている。<br>
 * FileControlクラスはDIコンテナによってビジネスロジックを生成する際、ビジネスロジックの属性として設定する。<br>
 * FileControlクラスは属性に、ファイル操作を行う際に基準となるパス(基準パス)を持つ。<br>
 * FileUtilityクラスは以下の機能を実装している。
 * <ul>
 * <li>ファイル名の変更･ファイルの移動</li>
 * <li>ファイルのコピー</li>
 * <li>ファイルの削除</li>
 * <li>ファイルの結合</li>
 * </ul>
 * なお、ファイル機能で使用するパスは相対パス、絶対パスの両方を指す。
 * </p>
 * @see FileUtility
 */
public class FileControlImpl implements FileControl {

    /**
     * 基準パス.
     * <p>
     * 基準パスを使用することにより、ファイルアクセス時に発生するファイルパスの環境依存の問題を回避することができる。
     * </p>
     */
    private String basePath = "";

    /**
     * ファイルのコピー。
     * @param srcFile コピー元のファイルのパス
     * @param newFile コピー先のファイルのパス
     */
    @Override
    public void copyFile(String srcFile, String newFile) {

        FileUtility
                .copyFile(getAbsolutePath(srcFile), getAbsolutePath(newFile));
    }

    /**
     * ファイルの削除。
     * @param srcFile 削除するファイルのパス
     */
    @Override
    public void deleteFile(String srcFile) {

        FileUtility.deleteFile(getAbsolutePath(srcFile));
    }

    /**
     * ファイル結合。
     * @param fileList 結合するファイルのリスト
     * @param newFile 結合してできるファイルのパス
     */
    @Override
    public void mergeFile(List<String> fileList, String newFile) {

        List<String> srcFileList = new ArrayList<String>();
        for (String fileName : fileList) {
            srcFileList.add(getAbsolutePath(fileName));
        }

        FileUtility.mergeFile(srcFileList, getAbsolutePath(newFile));
    }

    /**
     * ファイル名の変更･ファイルの移動。
     * @param srcFile 移動前のパス
     * @param newFile 移動後のパス
     */
    @Override
    public void renameFile(String srcFile, String newFile) {

        FileUtility.renameFile(getAbsolutePath(srcFile),
                getAbsolutePath(newFile));
    }

    /**
     * FileControlで使用するファイル名を絶対パスかどうかを確認して、絶対パスを返却する。
     * @param fileName 各メソッドの引数として受けたファイル名
     * @return fileNameの絶対パス
     * @throws ファイル機能例外
     */
    private String getAbsolutePath(String fileName) {

        if (fileName == null) {
            throw new FileException("File name is not set.", fileName);
        }

        File newFileObject = new File(fileName);

        if (!newFileObject.isAbsolute()) {
            StringBuilder newFilePath = new StringBuilder(basePath);
            return newFilePath.append(fileName).toString();
        } else {
            return fileName;
        }
    }

    /**
     * 基準パスを取得する。
     * @return 基準パス
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * 基準パスを設定する。
     * @param basePath 基準パス
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * FileUtilityのファイルの上書きフラグを設定する。
     * @param checkFileExist 上書きフラグ
     */
    public void setCheckFileExist(boolean checkFileExist) {
        FileUtility.setCheckFileExist(checkFileExist);
    }

}
