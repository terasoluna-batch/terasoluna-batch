/*
 * $Id: FileContent.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.ut;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ファイルの中身をバイナリ比較するためのラッパークラス。 <BR>
 * <BR>
 * 通常はUTUtil.assertEqualsFile()メソッドがあるので、このクラスを直接 使う必要性は少ないと思われる。
 * @author 木村真幸
 * @version 2003.08.20
 */
public class FileContent {

    /**
     * 保持するファイルオブジェクト。
     */
    private File file = null;

    /**
     * コンストラクタ。
     * @param file 中身を比較したいファイル
     */
    public FileContent(File file) {
        this.file = file;
    }

    /**
     * 保持しているFileオブジェクトを取得する。
     * @return Fileオブジェクト
     */
    public File getFile() {
        return file;
    }

    /**
     * ハッシュコードを返す。
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        long hash = 0L;
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(getFile());
            for (long i = 0; i < getFile().length(); i++) {
                int input = stream.read();
                if (input == -1) {
                    printReadBytes(i);
                    break;
                }
                hash = (hash << 1) + hash + (long) input;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (int) ((hash >> 32) + hash);
    }

    /**
     * ファイルをバイナリ比較する。
     * @param other 比較対照のファイル
     * @return 比較結果。同一ならtrue、異なればfalseを返す。
     */
    public boolean equals(FileContent other) {
        if (hashCode() != other.hashCode()) {
            printHashCode();
            other.printHashCode();
            return false;
        }
        if (getFile().length() != other.getFile().length()) {
            printLength();
            other.printLength();
            return false;
        }

        FileInputStream stream1 = null;
        FileInputStream stream2 = null;
        try {
            stream1 = new FileInputStream(getFile());
            stream2 = new FileInputStream(other.getFile());
            for (long i = 0; i < getFile().length(); i++) {
                int input1 = stream1.read();
                int input2 = stream2.read();
                if (input1 != input2) {
                    System.out.println("first " + i + " bytes are same.");
                    return false;
                }
                if (input1 == -1) {
                    printReadBytes(i);
                    break;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stream1 != null) {
                    stream1.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (stream2 != null) {
                    stream2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * toStringのオーバーライド。ファイルのパスを返す実装になっている。
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getFile().toString();
    }

    /**
     * 読み込んだバイト数を標準出力に出力する。
     * @param bytes 読み込んだバイト数
     */
    private void printReadBytes(long bytes) {
        printLength();
        System.out.println("but only " + bytes + " bytes can be read.");
    }

    /**
     * ハッシュコードを標準出力に出力する。
     */
    private void printHashCode() {
        System.out.println("hashCode of " + toString() + " is " + hashCode());
    }

    /**
     * ファイルの長さを標準出力に出力する。
     */
    private void printLength() {
        System.out
                .println(toString() + " is " + getFile().length() + " bytes.");
    }
}
