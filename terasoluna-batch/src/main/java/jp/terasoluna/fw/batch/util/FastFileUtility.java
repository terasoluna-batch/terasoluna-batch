/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.util.FileUtility;

/**
 * ファイルコピーを高速に行うユーティリティ。<br>
 * <br>
 * TERASOLUNAバッチフレームワークのFileUtilityによるコピーを行うと、ファイルロック、チャンク1024バイトによるバイト移送時により、特にNFSへのファイルコピーにて性能劣化が発生する。<br>
 * このため、NFSを使用したファイルコピーで性能が出ない場合は 当クラスによるファイルコピーを行うこと。
 */
public class FastFileUtility extends FileUtility {

    /**
     * ファイルをコピーする。
     * <p>
     * コピー元のファイルのパスを受け取り、 コピー先のパスにファイルをコピーする。<br>
     * コピー先にファイルが存在する場合、上書きでコピーされる。<br>
     * </p>
     * @param srcFile コピー元のファイルのパス
     * @param newFile コピー先のファイルのパス
     */
    public static void copyFile(String srcFile, String newFile) {

        if (srcFile == null) {
            throw new FileException("srcFile is null.");
        }
        if (newFile == null) {
            throw new FileException("newFile is null.");
        }

        File srcFileObject = new File(srcFile);
        // コピー元のパスにファイルが存在しない場合、エラーを投げて処理を終了する。
        if (!srcFileObject.exists()) {
            throw new FileException(srcFile + " is not exist.");
        }
        File outputFileObject = new File(newFile);
        copyFile(srcFileObject, outputFileObject);
    }

    /**
     * ファイルをコピーする。
     * <p>
     * コピー元のファイルのパスを受け取り、 コピー先のパスにファイルをコピーする。<br>
     * コピー先にファイルが存在する場合、上書きでコピーされる。<br>
     * </p>
     * @param srcFile コピー元のファイルオブジェクト
     * @param newFile コピー先のファイルオブジェクト
     */
    static void copyFile(File srcFile, File newFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel ic = null;
        FileChannel oc = null;

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(newFile);
            ic = fis.getChannel();
            oc = fos.getChannel();
            transferFileEntirely(ic, oc);
        } catch (IOException e) {
            throw new FileException("File control operation was failed.", e);
        } finally {
            closeQuietly(oc);
            closeQuietly(ic);
            closeQuietly(fos);
            closeQuietly(fis);
        }
    }

    /**
     * ファイルを完全にコピーする。
     * @param srcChannel コピー元FileChannel
     * @param destChannel コピー先FileChannel
     */
    private static void transferFileEntirely(FileChannel srcChannel, FileChannel destChannel) throws IOException {
        long srcFileSize = srcChannel.size();
        long transferedSize = 0L;
        while(transferedSize < srcFileSize){
            long transferSize = srcChannel.transferTo(transferedSize, srcFileSize - transferedSize, destChannel);
            transferedSize += transferSize;
        }
    }

    /**
     * Channelをクローズする。<br>
     * <p>
     * 引数に渡されたchannelがnullでなければクローズする。<br>
     * また、クローズする際にIOException例外が発生した場合は無視する。<br>
     * </p>
     * @param channel Channel
     */
    public static void closeQuietly(Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            // なにもしない
        }
    }

    /**
     * FileInputStreamをクローズする。<br>
     * <p>
     * 引数に渡されたstreamがnullでなければクローズする。<br>
     * また、クローズする際にIOException例外が発生した場合は無視する。<br>
     * </p>
     * @param stream FileInputStream
     */
    public static void closeQuietly(FileInputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // なにもしない
        }
    }

    /**
     * FileOutputStreamをクローズする。<br>
     * <p>
     * 引数に渡されたstreamがnullでなければクローズする。<br>
     * また、クローズする際にIOException例外が発生した場合は無視する。<br>
     * </p>
     * @param fos FileOutputStream
     */
    public static void closeQuietly(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // なにもしない
        }
    }
}
