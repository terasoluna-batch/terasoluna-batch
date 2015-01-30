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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;

import jp.terasoluna.fw.file.dao.FileException;

/**
 * ファイル操作機能を実装するクラス。
 * <p>
 * このクラスは、ビジネスロジックから直接利用することも可能である。<br>
 * FileUtilityクラスは以下の機能を実装している。
 * <ul>
 * <li>ファイル名の変更･ファイルの移動</li>
 * <li>ファイルのコピー</li>
 * <li>ファイルの削除</li>
 * <li>ファイルの結合</li>
 * </ul>
 * なお、ファイル機能で使用するパスは相対パス、絶対パスの両方を利用可能だが、<br>
 * 本クラスを直接利用する場合に限り絶対パスのみしか利用できない。<br>
 * 相対パスを利用したい場合は、<code>FileControlImpl</code>クラスにより 本クラスをラップして利用すること。
 * </p>
 */
public class FileUtility {

    /**
     * ファイルの存在するかどうかを示すフラグ
     */
    private static boolean checkFileExist = false;

    /**
     * ファイルをコピーする。
     * <p>
     * コピー元のファイルのパスを受け取り、 コピー先のパスにファイルをコピーする。<br>
     * コピー先にファイルが存在する場合、そのファイルを削除した後、 ファイルのコピーを実行する。<br>
     * コピー元のパスにファイルが存在しない場合、非検査例外をスローする。<br>
     * ファイルのコピーに失敗した場合、非検査例外をスローする。
     * </p>
     * @param srcFile コピー元のファイルのパス
     * @param newFile コピー先のファイルのパス
     * @throws ファイル機能例外
     */
    public static void copyFile(String srcFile, String newFile) {

        checkAbsolutePath(srcFile);
        checkAbsolutePath(newFile);

        File srcFileObject = new File(srcFile);
        // コピー元のパスにファイルが存在しない場合、エラーを投げて処理を終了する。
        if (!srcFileObject.exists()) {
            throw new FileException(srcFile + " is not exist.", srcFile);
        }

        File newFileObject = new File(newFile);
        // 移動先のファイルが存在する場合、そのファイルを削除する。
        if (newFileObject.exists() && checkFileExist) {
            boolean result = newFileObject.delete();
            if (!result) {
                throw new FileException("File control operation was failed.",
                        newFile);
            }
        } else if (newFileObject.exists() && !checkFileExist) {
            throw new FileException(newFile + " is exist.", newFile);
        }

        FileOutputStream fos = null;
        FileChannel outputFileChannel = null;
        FileInputStream ios = null;
        FileChannel inputFileChannel = null;
        FileLock inputFileLock = null;
        FileLock outputFileLock = null;
        try {
            fos = new FileOutputStream(newFileObject, true);
            outputFileChannel = fos.getChannel();

            ios = new FileInputStream(srcFileObject);
            inputFileChannel = ios.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            inputFileLock = inputFileChannel.lock(0L, Long.MAX_VALUE, true);
            outputFileLock = outputFileChannel.lock(0L, Long.MAX_VALUE, false);

            while (inputFileChannel.position() < inputFileChannel.size()) {
                buffer.clear();
                inputFileChannel.read(buffer);
                buffer.flip();
                outputFileChannel.write(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new FileException("File control operation was failed.", e);
        } catch (IOException e) {
            throw new FileException("File control operation was failed.", e);
        } finally {
            try {
                if (inputFileLock != null) {
                    inputFileLock.release();
                }
                if (outputFileLock != null) {
                    outputFileLock.release();
                }

                if (ios != null) {
                    ios.close();
                }

                if (fos != null) {
                    fos.close();
                }

                if (outputFileChannel != null) {
                    outputFileChannel.close();
                }

                if (inputFileChannel != null) {
                    inputFileChannel.close();
                }
            } catch (IOException e) {
                // 何もしない。(例外を無視する)
            }
        }
    }

    /**
     * ファイル削除。
     * <p>
     * 削除するファイルのパスを受け取り、ファイルを削除する。<br>
     * 削除するファイルが存在しない場合、非検査例外をスローする。<br>
     * 削除に失敗した場合、非検査例外をスローする。
     * </p>
     * @param srcFile 削除するファイルのパス
     * @throws ファイル機能例外
     */
    public static void deleteFile(String srcFile) {

        checkAbsolutePath(srcFile);

        File srcFileObject = new File(srcFile);

        // 削除対象のファイルが存在しない場合、エラーを投げて処理を終了する。
        if (!srcFileObject.exists()) {
            throw new FileException(srcFile + " is not exist.", srcFile);
        }

        boolean result = srcFileObject.delete();

        if (!result) {
            throw new FileException("File control operation was failed.",
                    srcFile);
        }
    }

    /**
     * ファイル結合。
     * <p>
     * 結合するファイルのリストを受け取り、ファイルを結合する。<br>
     * 結合して新しく作成するファイルのパスに、 処理開始までにファイルが存在した場合、 そのファイルを削除したのち、ファイルを結合する。<br>
     * 結合するファイルリストに含まれるファイルが存在しない場合、 非検査例外をスローする。<br>
     * ファイルの結合に失敗した場合、非検査例外をスローする。
     * </p>
     * @param fileList 結合するファイルのリスト
     * @param newFile 結合してできるファイルのパス
     * @throws ファイル機能例外
     */
    public static void mergeFile(List<String> fileList, String newFile) {

        checkAbsolutePath(newFile);

        File newFileObject = new File(newFile);

        // 移動先のファイルが存在する場合、そのファイルを削除する。
        if (newFileObject.exists() && checkFileExist) {
            boolean result = newFileObject.delete();
            if (!result) {
                throw new FileException("File control operation was failed.",
                        newFile);
            }
        } else if (newFileObject.exists() && !checkFileExist) {
            throw new FileException(newFile + " is exist.", newFile);
        }

        FileOutputStream fos = null;
        FileChannel outputFileChannel = null;
        FileLock outputFileLock = null;

        try {
            fos = new FileOutputStream(newFileObject, true);
            outputFileChannel = fos.getChannel();
            outputFileLock = outputFileChannel.lock(0L, Long.MAX_VALUE, false);

            File srcFileObject = null;

            for (String srcFile : fileList) {

                checkAbsolutePath(srcFile);

                srcFileObject = new File(srcFile);

                // マージ元のファイルが存在しない場合、エラーを投げて処理を終了する。
                if (!srcFileObject.exists()) {
                    throw new FileException(srcFile + " is not exist.", srcFile);
                }

                FileInputStream fis = null;
                FileChannel inputFileChannel = null;
                FileLock inputFileLock = null;

                try {
                    fis = new FileInputStream(srcFileObject);
                    inputFileChannel = fis.getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    inputFileLock = inputFileChannel.lock(0L, Long.MAX_VALUE,
                            true);

                    while (inputFileChannel.position() < inputFileChannel
                            .size()) {
                        buffer.clear();
                        inputFileChannel.read(buffer);
                        buffer.flip();
                        outputFileChannel.write(buffer);
                    }
                } finally {
                    if (inputFileLock != null) {
                        inputFileLock.release();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    if (inputFileChannel != null) {
                        inputFileChannel.close();
                    }
                }

            }
        } catch (FileNotFoundException e) {
            throw new FileException("File control operation was failed.", e);
        } catch (IOException e) {
            throw new FileException("File control operation was failed.", e);
        } finally {
            try {
                if (outputFileLock != null) {
                    outputFileLock.release();
                }

                if (fos != null) {
                    fos.close();
                }

                if (outputFileChannel != null) {
                    outputFileChannel.close();
                }

            } catch (IOException e) {
                // 何もしない。(例外を無視する)
            }

        }
    }

    /**
     * ファイル名の変更･ファイルの移動。
     * <p>
     * 移動元のファイルのパスを受け取り、移動先のパスにデータを移動させる。<br>
     * 移動先のパスにファイルが存在する場合、そのファイルを削除した後、 ファイルの移動を実行する。<br>
     * 移動元のファイルが存在しない場合、非検査例外をスローする。<br>
     * ファイルの移動に失敗した場合、非検査例外をスローする。
     * </p>
     * @param srcFile 移動前のパス
     * @param newFile 移動後のパス
     * @throws ファイル機能例外
     */
    public static void renameFile(String srcFile, String newFile) {

        checkAbsolutePath(srcFile);
        checkAbsolutePath(newFile);

        File srcFileObject = new File(srcFile);
        File newFileObject = new File(newFile);

        // 移動もとのファイルが存在しない場合、エラーを投げて処理を終了する。
        if (!srcFileObject.exists()) {
            throw new FileException(srcFile + " is not exist.", srcFile);
        }

        // 移動先のファイルが存在する場合、そのファイルを削除する。
        if (newFileObject.exists() && checkFileExist) {
            boolean result = newFileObject.delete();
            if (!result) {
                throw new FileException("File control operation was failed.",
                        newFile);
            }
        } else if (newFileObject.exists() && !checkFileExist) {
            throw new FileException(newFile + " is exist.", newFile);
        }

        boolean result = true;
        result = srcFileObject.renameTo(newFileObject);

        if (!result) {
            throw new FileException("File control operation was failed.");
        }
    }

    /**
     * ファイルが存在するかどうかのフラグを取得する。
     * @return ファイルが存在するかどうかのフラグ
     */
    public static boolean isCheckFileExist() {
        return checkFileExist;
    }

    /**
     * ファイルが存在するかどうかのフラグを設定する。
     * @param checkFileExist ファイルが存在するかどうかのフラグ
     */
    public static void setCheckFileExist(boolean checkFileExist) {
        FileUtility.checkFileExist = checkFileExist;
    }

    /**
     * 引数のパスが絶対パスであることを確認する。 絶対パスでない場合、例外をスローする。
     * @param filePath ファイルのパス
     * @throws ファイル機能例外
     */
    private static void checkAbsolutePath(String filePath) {
        if (filePath == null) {
            throw new FileException("File path is not set.", filePath);
        }
        File file = new File(filePath);
        if (!file.isAbsolute()) {
            throw new FileException("File path is not absolute.", filePath);
        }
    }
}
