/*
 * $Id: FileUtilityTest.java 5576 2007-11-15 13:13:32Z pakucn $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.ut.VMOUTUtil;
import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.util.FileUtility} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイル操作機能を実装するクラス.
 * <p>
 * @author 吉信郁美
 * @author 趙俸徹
 * @see jp.terasoluna.fw.file.util.FileUtility
 */
public class FileUtilityTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(FileUtilityTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VMOUTUtil.initialize();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // FileUtilityのstaticフィールドをデフォルト値に初期化する。
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public FileUtilityTest(String name) {
        super(name);
    }

    /*
     * testCopyFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile01_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile01_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:false<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile01_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:コピー元のsrcFileで指定したファイルと同一の内容であることを確認する。<br>
     * <br>
     * 正常ケース<br>
     * コピー先のファイルが存在しない場合、正しくファイルコピーされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    // This testcase is ignored, because of Windows environment dependency.
    public void _ignore_testCopyFile01() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile01_src.txt";
        String newFile = directoryPath + "testCopyFile01_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();

        FileWriter testSrcFileFileWriter = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile01_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File getFile = new File(newFile);
            UTUtil.assertEqualsFile(testSrcFile, getFile);
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }

            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile02_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile02_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile02_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile02_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:コピー元のsrcFileで指定したファイルと同一の内容であることを確認する。<br>
     * <br>
     * 正常ケース<br>
     * （checkFileExistがTRUE）<br>
     * コピー先のファイルが存在する場合、正しくファイルコピーされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile02() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile02_src.txt";
        String newFile = directoryPath + "testCopyFile02_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile02_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile02_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File getFile = new File(newFile);
            UTUtil.assertEqualsFile(testSrcFile, getFile);
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile03() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile03_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile03_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:false<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile03_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testCopyFile03_src.txt is not exist."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * コピー元のファイルがない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile03() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile03_src.txt";
        String newFile = directoryPath + "testCopyFile03_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile03_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(srcFile + " is not exist.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile03_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile04() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile04_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile04_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:false<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile04_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile04_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testCopyFile04_new.txt is exist."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * （checkFileExistがFALSE）<br>
     * コピー先のファイルが存在する場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile04() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile04_src.txt";
        String newFile = directoryPath + "testCopyFile04_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile04_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile04_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(newFile + " is exist.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile04_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile05_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile05_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile05_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile05_new.txtデータ<br>
     * (状態) FileOutputStream#<init>():FileNotFoundException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:ファイルが存在しない。<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・原因例外：FileNotFoundException<br>
     * <br>
     * 異常ケース<br>
     * ファイルの存在チェック後のタイミングでコピー先のファイルが削除された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile05() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile05_src.txt";
        String newFile = directoryPath + "testCopyFile05_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileNotFoundException fileNotFoundException = new FileNotFoundException(
                "testCopyFile05例外");
        VMOUTUtil.setExceptionAtAllTimes(FileOutputStream.class, "<init>",
                fileNotFoundException);

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile05_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile05_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertSame(fileNotFoundException, e.getCause());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File file = new File(newFile);
            assertFalse(file.exists());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile06_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile06_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile06_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile06_new.txtデータ<br>
     * (状態) FileChannel#position():IOException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:ファイルが存在するが、内容がない。<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・原因例外：IOException<br>
     * <br>
     * 異常ケース<br>
     * ファイルのコピー処理途中にIOExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile06() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile06_src.txt";
        String newFile = directoryPath + "testCopyFile06_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        IOException exception = new IOException("testCopyFile06例外");
        VMOUTUtil.setExceptionAtAllTimes(FileChannel.class, "position",
                exception);

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile06_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile06_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertSame(exception, e.getCause());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile07_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile07_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile07_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile07_new.txtデータ<br>
     * (状態) FileOutputStream#close():IOException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:コピー元のsrcFileで指定したファイルと同一の内容であることを確認する。<br>
     * <br>
     * 異常ケース<br>
     * ファイルのコピー処理完了後リソースの開放処理でIOExceptionが発生した場合、そのまま処理を続き正常終了されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile07() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile07_src.txt";
        String newFile = directoryPath + "testCopyFile07_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        IOException exception = new IOException("testCopyFile07例外");
        VMOUTUtil.setExceptionAtAllTimes(FileOutputStream.class, "close",
                exception);

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile07_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile07_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File getFile = new File(newFile);
            UTUtil.assertEqualsFile(testSrcFile, getFile);
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile08() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile08_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile08_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼び出される。<br>
     * 引数として、引数srcFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * コピー元のファイル名がnullで入力された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile08() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = null;
        String newFile = directoryPath + "testCopyFile08_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        IOException exception = new IOException("TEST");
        VMOUTUtil.setExceptionAtAllTimes(FileLock.class, "release", exception);

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile08_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile08_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile09() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile09_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testCopyFile09_new.txt"<br>
     * <br>
     * ※相対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile09_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile09_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * コピー先のファイル名が相対パスで入力された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile09() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile09_src.txt";
        String newFile = "testCopyFile09_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(directoryPath + newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile09_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile09_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(directoryPath + newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile09_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile10() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile10_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:null<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile10_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * コピー先のファイル名がnullで入力された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile10() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile10_src.txt";
        String newFile = null;

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile10_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testCopyFile11() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testCopyFile11_src.txt"<br>
     * <br>
     * ※相対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile11_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile11_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼び出される。<br>
     * 引数として、引数srcFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * コピー元のファイル名が相対パスで入力された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testCopyFile11() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = "testCopyFile10_src.txt";
        String newFile = directoryPath + "testCopyFile10_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist",
                Boolean.TRUE);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(directoryPath + srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile11_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile11_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // コピー先のファイル内容確認
            File resultFile = new File(newFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile11_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /*
     * testCopyFile12() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testCopyFile12_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testCopyFile02_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile02_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * ・testCopyFile02_new.txtデータ<br>
     * <br>
     * ※ロックが掛かっている。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数として、引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数として、引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変更なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * （checkFileExistがTRUE）<br>
     * コピー先のファイルが存在するがロックされている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    // This testcase is ignored, because of Windows environment dependency.
    public void _ignore_testCopyFile12() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testCopyFile12_src.txt";
        String newFile = directoryPath + "testCopyFile12_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        FileInputStream fis = null;
        FileLock lock = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile02_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testCopyFile02_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            fis = new FileInputStream(testNewFile);
            lock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);

            // テスト実施
            FileUtility.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // コピー先のファイル内容確認
            lock.release();
            // ファイル名の変更を確認
            lock.release();
            File getFile = new File(newFile);
            assertTrue(getFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(getFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile02_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

        } finally {
            if (lock != null) {
                lock.release();
            }
            if (fis != null) {
                fis.close();
            }
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testDeleteFile01_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile01_src.txtデータ<br>
     * <br>
     * 期待値：(状態変化) srcFileで指定したファイル:存在しない。<br>
     * <br>
     * 正常ケース<br>
     * 対象ファイルが正しく削除されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDeleteFile01() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testDeleteFile01_src.txt";

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile01_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.deleteFile(srcFile);

            // 判定
            // コピー先のファイル内容確認
            File resultFile = new File(srcFile);
            assertFalse(resultFile.exists());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            File file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile02() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testDeleteFile02_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) srcFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testDeleteFile02_src.txt  is not exist."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 対象ファイルが存在しない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDeleteFile02() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testDeleteFile01_src.txt";

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();

        try {
            FileUtility.deleteFile(srcFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(srcFile + " is not exist.", e.getMessage());
            assertEquals(srcFile, e.getFileName());
        } finally {
            File file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile03() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testDeleteFile03_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile03_src.txtデータ<br>
     * (状態) File#delete():false<br>
     * <br>
     * 期待値：(状態変化) srcFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 対象ファイルの削除がファイルロックなどによって失敗した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDeleteFile03() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testDeleteFile03_src.txt";

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        VMOUTUtil.setReturnValueAtAllTimes(File.class, "delete", false);

        FileWriter testSrcFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile03_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.deleteFile(srcFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // コピー先のファイル内容確認
            File resultFile = new File(srcFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile03_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            File file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile04() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) srcFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 対象ファイルのパスがnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDeleteFile04() throws Exception {
        // 引数の設定
        String srcFile = null;

        try {
            FileUtility.deleteFile(srcFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());
        }
    }

    /**
     * testDeleteFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testDeleteFile05_src.txt"<br>
     * <br>
     * ※相対パス<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testCopyFile05_src.txtデータ<br>
     * <br>
     * 期待値：(状態変化) srcFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 対象ファイルのパスが相対パスの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testDeleteFile05() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = "testDeleteFile05_src.txt";

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(directoryPath + srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testCopyFile05_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.deleteFile(srcFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // コピー先のファイル内容確認
            File resultFile = new File(directoryPath + srcFile);
            assertTrue(resultFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testCopyFile05_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            File file = new File(directoryPath + srcFile);
            file.delete();
        }
    }

    /**
     * testMergeFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,D,F <br>
     * <br>
     * 入力値：(引数) fileList:要素を持たないListインスタンス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile01_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:false<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在しない<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:空のファイル<br>
     * <br>
     * 正常ケース<br>
     * 結合対象ファイルリストが空の場合、空ファイルが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile01() throws Exception {
        // 引数の設定
        List<String> fileList = new ArrayList<String>();

        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String newFile = directoryPath + "testMergeFile01_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();

        BufferedReader postReader = null;
        try {
            // テスト実施
            FileUtility.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());

            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(newFile)));
            assertFalse(postReader.ready());
        } finally {
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }

    }

    /**
     * testMergeFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,D,F <br>
     * <br>
     * 入力値：(引数) fileList:要素を持たないListインスタンス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile02_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile02_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:空のファイル<br>
     * <br>
     * 正常ケース<br>
     * (checkFileExist設定：TRUE)<br>
     * 指定された結果ファイルが既に存在する場合、問題なく結果ファイルが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile02() throws Exception {
        // 引数の設定
        List<String> fileList = new ArrayList<String>();

        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String newFile = directoryPath + "testMergeFile02_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile02_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());

            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile03() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:要素を持たないListインスタンス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile03_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:false<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile03_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testMergeFile03_new.txt is exist."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * (checkFileExist設定：FALSE)<br>
     * 指定された結果ファイルが既に存在する場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile03() throws Exception {
        // 引数の設定
        List<String> fileList = new ArrayList<String>();

        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String newFile = directoryPath + "testMergeFile03_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile03_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(newFile + " is exist.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile03_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }

    }

    /**
     * testMergeFile04() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,D,F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile04_src1.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile04_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile04_src1.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile04_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile04_src1.txtデータ<br>
     * <br>
     * 正常ケース<br>
     * 結合対象ファイルリストに存在するファイルのパスが一つ設定されている場合、その一つのファイルと同じ内容のファイルが生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile04() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile04_src1.txt";
        fileList.add(srcFile1);

        String newFile = directoryPath + "testMergeFile04_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile04_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile04_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            UTUtil.assertEqualsFile(testSrcFile1, mergeFile);
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile05() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,D,F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile05_src1.txt"<br>
     * 2．"(パス)/testMergeFile05_src2.txt"<br>
     * 3．"(パス)/testMergeFile05_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile05_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile05_src1.txtデータ<br>
     * ・testMergeFile05_src2.txtデータ<br>
     * ・testMergeFile05_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile05_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():4回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * 3回目の呼び出し：引数fileListの要素2のファイルパスが渡されること。<br>
     * 4回目の呼び出し：引数fileListの要素3のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータ入っている。<br>
     * ・testMergeFile05_src1.txtデータtestMergeFile05_src2.txtデータtestMergeFile05_src3.txtデータ<br>
     * <br>
     * 正常ケース<br>
     * 結合対象ファイルリストに存在するファイルのパスが複数設定されている場合、その全ファイルの内容が順番に結合されて結果ファイルとして生成されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile05() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile05_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile05_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile05_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile05_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile05_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile05_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile05_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile05_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(4, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertEquals(srcFile2, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 2, 0));
            assertEquals(srcFile3, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 3, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile05_src1.txtデータ"
                    + "testMergeFile05_src2.txtデータtestMergeFile05_src3.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile06_src1.txt"<br>
     * 2．"(パス)/testMergeFile06_src2.txt"<br>
     * 3．"(パス)/testMergeFile06_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile06_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile06_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:空のファイル<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testMergeFile06_src1.txt is exist."<br>
     * ・ファイル名："(パス)/testMergeFile06_src1.txt"<br>
     * <br>
     * 異常ケース<br>
     * 結合対象ファイルリストに存在しないファイルのパスが複数設定されている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile06() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile06_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile06_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile06_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile06_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile06_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の判定
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(srcFile1 + " is not exist.", e.getMessage());
            assertEquals(srcFile1, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile07_src1.txt"<br>
     * 2．"(パス)/testMergeFile07_src2.txt"<br>
     * 3．"(パス)/testMergeFile07_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile07_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:"(パス)/testMergeFile07_src3.txt"のみ存在しない<br>
     * <br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile07_src1.txtデータ<br>
     * ・testMergeFile07_src2.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile07_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():4回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * 3回目の呼び出し：引数fileListの要素2のファイルパスが渡されること。<br>
     * 4回目の呼び出し：引数fileListの要素3のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile07_src1.txtデータtestMergeFile07_src2.txtデータ<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testMergeFile07_src3.txt is not exist."<br>
     * ・ファイル名："(パス)/testMergeFile07_src3.txt"<br>
     * <br>
     * 異常ケース<br>
     * 結合対象ファイルリストに一部存在しないファイルのパスが設定されている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile07() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile07_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile07_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile07_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile07_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile07_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile07_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile07_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(srcFile3 + " is not exist.", e.getMessage());
            assertEquals(srcFile3, e.getFileName());

            // 状態変化の確認
            assertEquals(4, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertEquals(srcFile2, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 2, 0));
            assertEquals(srcFile3, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 3, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile07_src1.txtデータ"
                    + "testMergeFile07_src2.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile08() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile08_src1.txt"<br>
     * 2．"(パス)/testMergeFile08_src2.txt"<br>
     * 3．"(パス)/testMergeFile08_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile08_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile08_src1.txtデータ<br>
     * ・testMergeFile08_src2.txtデータ<br>
     * ・testMergeFile08_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile08_new.txtデータ<br>
     * (状態) inputFileChannel#position():IOException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:空のファイル<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・原因例外：FileOutputStream#close()で発生したIOException<br>
     * <br>
     * 異常ケース<br>
     * ファイル処理でIOExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile08() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile08_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile08_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile08_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile08_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        IOException ioException = new IOException("testMergeFile08例外");
        VMOUTUtil.setExceptionAtAllTimes(FileChannel.class, "position",
                ioException);

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile08_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile08_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile08_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile08_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertSame(ioException, e.getCause());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile09() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile09_src1.txt"<br>
     * 2．"(パス)/testMergeFile09_src2.txt"<br>
     * 3．"(パス)/testMergeFile09_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile09_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile09_src1.txtデータ<br>
     * ・testMergeFile09_src2.txtデータ<br>
     * ・testMergeFile09_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile09_new.txtデータ<br>
     * (状態) FileOutputStream#close():IOException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():4回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * 3回目の呼び出し：引数fileListの要素2のファイルパスが渡されること。<br>
     * 4回目の呼び出し：引数fileListの要素3のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile09_src1.txtデータtestMergeFile09_src2.txtデータtestMergeFile09_src3.txtデータ<br>
     * <br>
     * 異常ケース<br>
     * ファイル生成処理時利用してリソースの開放に失敗した場合、例外が発生しないことを確認する。<br>
     * また、結果ファイルが正しく生成されていることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile09() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile09_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile09_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile09_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile09_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        IOException ioException = new IOException("testMergeFile09例外");
        VMOUTUtil.setExceptionAtAllTimes(FileOutputStream.class, "close",
                ioException);

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile09_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile09_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile09_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile09_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);

            // 状態変化の確認
            assertEquals(4, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertEquals(srcFile2, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 2, 0));
            assertEquals(srcFile3, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 3, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile09_src1.txtデータ"
                    + "testMergeFile09_src2.txtデータtestMergeFile09_src3.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());

        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile10() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile10_src1.txt"<br>
     * 2．"(パス)/testMergeFile10_src2.txt"<br>
     * 3．"(パス)/testMergeFile10_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile10_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile10_src1.txtデータ<br>
     * ・testMergeFile10_src2.txtデータ<br>
     * ・testMergeFile10_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile10_new.txtデータ<br>
     * (状態) FileOutputStream.<init>:FileNotFountException発生<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:ファイルが存在しない<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・原因例外：FileOutputStream.<init>で発生したFileNotFoundException<br>
     * <br>
     * 異常ケース<br>
     * 処理途中にファイルが削除されFileNotFoundExceptionが発生した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile10() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile10_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile10_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile10_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile10_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileNotFoundException fileNotFoundException = new FileNotFoundException(
                "testMergeFile10例外");
        VMOUTUtil.setExceptionAtAllTimes(FileOutputStream.class, "<init>",
                fileNotFoundException);

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile10_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile10_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile10_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile10_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertSame(fileNotFoundException, e.getCause());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertFalse(mergeFile.exists());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile11() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile11_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile11_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:空のファイル<br>
     * (状態変化) 例外:NullPointerExceptionが発生することを確認する。<br>
     * <br>
     * 異常ケース<br>
     * 結合対象ファイルリストがnullの場合、NullPointerExceptionが発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile11() throws Exception {
        // 引数の設定
        List<String> fileList = null;

        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String newFile = directoryPath + "testMergeFile11_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile11_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("NullPointerExceptionが発生しませんでした。失敗です。");
        } catch (NullPointerException e) {
            // 例外の確認
            assertTrue(NullPointerException.class
                    .isAssignableFrom(e.getClass()));

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile12() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile12_src1.txt"<br>
     * 2．"(パス)/testMergeFile12_src2.txt"<br>
     * 3．"(パス)/testMergeFile12_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:null<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile12_src1.txtデータ<br>
     * ・testMergeFile12_src2.txtデータ<br>
     * ・testMergeFile12_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在しない<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * 結果ファイルのパスがnullの場合、例外が出ることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile12() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile12_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile12_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile12_src3.txt";
        fileList.add(srcFile3);

        String newFile = null;

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile12_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile12_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile12_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
        }
    }

    /**
     * testMergeFile13() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile13_src1.txt"<br>
     * 2．"(パス)/testMergeFile13_src2.txt"<br>
     * 3．"(パス)/testMergeFile13_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testMergeFile13_new.txt"<br>
     * <br>
     * ※相対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile13_src1.txtデータ<br>
     * ・testMergeFile13_src2.txtデータ<br>
     * ・testMergeFile13_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile13_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 結果ファイルのパスが相対パスの場合、例外が出ることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile13() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile13_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile13_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile13_src3.txt";
        fileList.add(srcFile3);

        String newFile = "testMergeFile13_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(directoryPath + newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile13_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile13_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile13_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile13_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(directoryPath + newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile13_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile14() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile14_src1.txt"<br>
     * 2．null<br>
     * 3．"(パス)/testMergeFile14_src3.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile14_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:2番目のみ存在しない<br>
     * <br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile14_src1.txtデータ<br>
     * ・testMergeFile14_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile14_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():3回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * 3回目の呼び出し：引数fileListの要素2のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile14_src1.txtデータ<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * 結合対象ファイルリストの項目の中にnullがある場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile14() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile14_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = null;
        fileList.add(srcFile2);
        String srcFile3 = directoryPath + "testMergeFile14_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile14_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile3 = new File(srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile14_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile14_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile14_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(3, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertNull(VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 2, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile14_src1.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile15() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素を持つListインスタンス<br>
     * 1．"(パス)/testMergeFile15_src1.txt"<br>
     * 2．"(パス)/testMergeFile15_src1.txt"<br>
     * 3．"testMergeFile15_src3.txt"(相対パス)<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile15_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在する。<br>
     * 以下のデータが各ファイルに入っている。<br>
     * ・testMergeFile15_src1.txtデータ<br>
     * ・testMergeFile15_src2.txtデータ<br>
     * ・testMergeFile15_src3.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile15_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():4回呼ばれる。<br>
     * 1回目の呼び出し：引数newFileが渡されること。<br>
     * 2回目の呼び出し：引数fileListの要素1のファイルパスが渡されること。<br>
     * 3回目の呼び出し：引数fileListの要素2のファイルパスが渡されること。<br>
     * 4回目の呼び出し：引数fileListの要素3のファイルパスが渡されること。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile15_src1.txtデータtestMergeFile15_src2.txtデータ<br>
     * (状態変化) 例外:以下の設定を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：srcFile3と同じ値<br>
     * <br>
     * 異常ケース<br>
     * 結合対象ファイルリストの項目の中に相対パスがある場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testMergeFile15() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());
        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile14_src1.txt";
        fileList.add(srcFile1);
        String srcFile2 = directoryPath + "testMergeFile14_src2.txt";
        fileList.add(srcFile2);
        String srcFile3 = "testMergeFile14_src3.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile14_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile1 = new File(srcFile1);
        testSrcFile1.delete();
        testSrcFile1.createNewFile();

        File testSrcFile2 = new File(srcFile2);
        testSrcFile2.delete();
        testSrcFile2.createNewFile();

        File testSrcFile3 = new File(directoryPath + srcFile3);
        testSrcFile3.delete();
        testSrcFile3.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFile1FileWriter = null;
        FileWriter testSrcFile2FileWriter = null;
        FileWriter testSrcFile3FileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFile1FileWriter = new FileWriter(testSrcFile1);
            testSrcFile1FileWriter.write("testMergeFile15_src1.txtデータ");
            testSrcFile1FileWriter.flush();
            testSrcFile1FileWriter.close();

            testSrcFile2FileWriter = new FileWriter(testSrcFile2);
            testSrcFile2FileWriter.write("testMergeFile15_src2.txtデータ");
            testSrcFile2FileWriter.flush();
            testSrcFile2FileWriter.close();

            testSrcFile3FileWriter = new FileWriter(testSrcFile3);
            testSrcFile3FileWriter.write("testMergeFile15_src3.txtデータ");
            testSrcFile3FileWriter.flush();
            testSrcFile3FileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile15_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(srcFile3, e.getFileName());

            // 状態変化の確認
            assertEquals(4, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(srcFile1, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertEquals(srcFile2, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 2, 0));
            assertEquals(srcFile3, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 3, 0));

            // マージ先のファイル内容確認
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile15_src1.txtデータ"
                    + "testMergeFile15_src2.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFile1FileWriter != null) {
                testSrcFile1FileWriter.close();
            }
            if (testSrcFile2FileWriter != null) {
                testSrcFile2FileWriter.close();
            }
            if (testSrcFile3FileWriter != null) {
                testSrcFile3FileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile1);
            file.delete();
            file = new File(srcFile2);
            file.delete();
            file = new File(directoryPath + srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /*
     * testMergeFile16() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,D,F,G <br>
     * <br>
     * 入力値：(引数) fileList:要素を持たないListインスタンス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testMergeFile16_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) this.checkFileExist:true<br>
     * (状態) fileListで指定したファイル:存在しない<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testMergeFile16_new.txtデータ<br>
     * <br>
     * ※ロックが掛かっている。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼ばれる。<br>
     * 引数newFileが渡されること。<br>
     * (状態変化) newFileで指定したファイル:変化なし<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * (checkFileExist設定：TRUE)<br>
     * 指定された結果ファイルが既に存在するがロックされている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    // This testcase is ignored, because of Windows environment dependency.
    public void _ignore_testMergeFile16() throws Exception {
        // 引数の設定
        List<String> fileList = new ArrayList<String>();

        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String newFile = directoryPath + "testMergeFile16_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        FileInputStream fis = null;
        FileLock lock = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testMergeFile16_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            fis = new FileInputStream(testNewFile);
            lock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);

            // テスト実施
            FileUtility.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // マージ先のファイル内容確認
            lock.release();
            File mergeFile = new File(newFile);
            assertTrue(mergeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mergeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testMergeFile16_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

        } finally {
            if (lock != null) {
                lock.release();
            }
            if (fis != null) {
                fis.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile01_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile01_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():false<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile01_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在しない。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile01_src.txtデータ<br>
     * <br>
     * 正常ケース<br>
     * 変更先のファイルが存在しない場合、正しくファイル名が変更されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile01() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile01_src.txt";

        String newFile = directoryPath + "testRenameFile01_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();

        FileWriter testSrcFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile01_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertFalse(removeFile.exists());

            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile01_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C,F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile02_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile02_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile02_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile02_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在しない。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile02_src.txtデータ<br>
     * <br>
     * 正常ケース<br>
     * (checkFileExist設定：TRUE)<br>
     * 変更先のファイルが存在する場合、元のファイルを削除した後、正しくファイル名が変更されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile02() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile02_src.txt";

        String newFile = directoryPath + "testRenameFile02_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile02_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile02_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertFalse(removeFile.exists());

            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile02_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile03() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile03_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile03_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():false<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile03_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在しない。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile03_new.txtデータ<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testRenameFile03_src.txt is not exist."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 変更前ファイルが存在しない場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile03() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile03_src.txt";

        String newFile = directoryPath + "testRenameFile03_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile03_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(srcFile + " is not exist.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile03_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile04() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile04_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile04_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():false<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile04_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile04_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile04_src.txtデータ<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile04_new.txtデータ<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："(パス)/testRenameFile04_new.txt is exist."<br>
     * ・newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * (checkFileExist設定：FALSE)<br>
     * 変更先のファイルが存在する場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile04() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile04_src.txt";

        String newFile = directoryPath + "testRenameFile04_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile04_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile04_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals(newFile + " is exist.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));
            assertTrue(testSrcFile.exists());

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile04_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            expectationResultData = "testRenameFile04_new.txtデータ";
            data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /*
     * testRenameFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile05_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile05_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile05_src.txtデータ<br>
     * <br>
     * ※ロックが掛かっている。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile05_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile05_src.txtデータ<br>
     * (状態変化) newFileで指定したファイル:存在しない。<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File control operation was failed."<br>
     * <br>
     * 異常ケース<br>
     * ファイルロックなどでファイルのRename処理に失敗した場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    // This testcase is ignored, because of Windows environment dependency.
    public void _ignore_testRenameFile05() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile05_src.txt";

        String newFile = directoryPath + "testRenameFile05_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        FileInputStream fis = null;
        FileLock lock = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile05_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            fis = new FileInputStream(testSrcFile);
            lock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile05_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            lock.release();
            File removeFile = new File(srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile05_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

            File renameFile = new File(newFile);
            assertFalse(renameFile.exists());
        } finally {
            if (lock != null) {
                lock.release();
            }
            if (fis != null) {
                fis.close();
            }
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile06_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在しない。<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile06_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼び出される。<br>
     * 引数srcFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在しない。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile06_new.txtデータ<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * 変更前ファイルがnullの場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile06() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = null;

        String newFile = directoryPath + "testRenameFile06_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile06_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // ファイル名の変更を確認
            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile06_new.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile07() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile07_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testRenameFile07_new.txt"<br>
     * <br>
     * ※相対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile07_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile07_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile07_src.txtデータ<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile07_new.txtデータ<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：newFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 変更先ファイルが相対パスで設定された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile07() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile07_src.txt";

        String newFile = "testRenameFile07_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(directoryPath + newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile07_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile07_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile07_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

            File renameFile = new File(directoryPath + newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            expectationResultData = "testRenameFile07_new.txtデータ";
            data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile08() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile08_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:null<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile08_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在しない。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile08_src.txtデータ<br>
     * (状態変化) newFileで指定したファイル:存在しない。<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not set."<br>
     * ・ファイル名：null<br>
     * <br>
     * 異常ケース<br>
     * 変更先ファイルがnullで設定された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile08() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile08_src.txt";

        String newFile = null;

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile08_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not set.", e.getMessage());
            assertNull(e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile08_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testRenameFile09() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testRenameFile09_src.txt"<br>
     * <br>
     * ※相対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile09_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile09_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile09_new.txtデータ<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():1回呼び出される。<br>
     * 引数srcFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile09_src.txtデータ<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile09_new.txtデータ<br>
     * (状態変化) 例外:以下の情報を持つFileExceptionが発生する。<br>
     * ・メッセージ："File path is not absolute."<br>
     * ・ファイル名：srcFileと同じ値<br>
     * <br>
     * 異常ケース<br>
     * 変更前ファイルが相対パスで設定された場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testRenameFile09() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = "testRenameFile09_src.txt";

        String newFile = directoryPath + "testRenameFile09_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(directoryPath + srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile09_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile09_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File path is not absolute.", e.getMessage());
            assertEquals(srcFile, e.getFileName());

            // 状態変化の確認
            assertEquals(1, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));

            // ファイル名の変更を確認
            File removeFile = new File(directoryPath + srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile09_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
            postReader.close();

            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            expectationResultData = "testRenameFile09_new.txtデータ";
            data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /*
     * testRenameFile10() <br>
     * <br>
     * (異常系) <br>
     * 観点：C,F,G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)/testRenameFile10_src.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)/testRenameFile10_new.txt"<br>
     * <br>
     * ※絶対パス<br>
     * (状態) FileUtility#checkFileExist():true<br>
     * (状態) srcFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile10_src.txtデータ<br>
     * (状態) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile10_new.txtデータ<br>
     * <br>
     * ※ロックが掛かっている。<br>
     * <br>
     * 期待値：(状態変化) checkAbsolutePath():2回呼び出される。<br>
     * 1回目の呼び出し：引数srcFileが渡されること。<br>
     * 2回目の呼び出し：引数newFileが渡されること。<br>
     * (状態変化) srcFileで指定したファイル:存在しない。<br>
     * (状態変化) newFileで指定したファイル:存在する。<br>
     * 以下のデータが入っている。<br>
     * ・testRenameFile02_src.txtデータ<br>
     * <br>
     * 異常ケース<br>
     * (checkFileExist設定：TRUE)<br>
     * 変更先のファイルが存在するがロックされている場合、例外が発生することを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    // This testcase is ignored, because of Windows environment dependency.
    public void _ignore_testRenameFile10() throws Exception {
        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0,
                url.getPath().length() - classFileName.length());

        String srcFile = directoryPath + "testRenameFile10_src.txt";

        String newFile = directoryPath + "testRenameFile10_new.txt";

        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", true);

        // テスト対象ファイルを初期化する。
        File testSrcFile = new File(srcFile);
        testSrcFile.delete();
        testSrcFile.createNewFile();

        File testNewFile = new File(newFile);
        testNewFile.delete();
        testNewFile.createNewFile();

        FileWriter testSrcFileFileWriter = null;
        FileWriter testNewFileFileWriter = null;
        FileInputStream fis = null;
        FileLock lock = null;
        BufferedReader postReader = null;
        try {
            testSrcFileFileWriter = new FileWriter(testSrcFile);
            testSrcFileFileWriter.write("testRenameFile10_src.txtデータ");
            testSrcFileFileWriter.flush();
            testSrcFileFileWriter.close();

            testNewFileFileWriter = new FileWriter(testNewFile);
            testNewFileFileWriter.write("testRenameFile10_new.txtデータ");
            testNewFileFileWriter.flush();
            testNewFileFileWriter.close();

            fis = new FileInputStream(testNewFile);
            lock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);

            // テスト実施
            FileUtility.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。");
        } catch (FileException e) {
            // 例外の確認
            assertTrue(FileException.class.isAssignableFrom(e.getClass()));
            assertEquals("File control operation was failed.", e.getMessage());
            assertEquals(newFile, e.getFileName());

            // 状態変化の確認
            assertEquals(2, VMOUTUtil.getCallCount(FileUtility.class,
                    "checkAbsolutePath"));
            assertEquals(srcFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 0, 0));
            assertEquals(newFile, VMOUTUtil.getArgument(FileUtility.class,
                    "checkAbsolutePath", 1, 0));

            // ファイル名の変更を確認
            File removeFile = new File(srcFile);
            assertTrue(removeFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile)));
            assertTrue(postReader.ready());
            String expectationResultData = "testRenameFile10_src.txtデータ";
            String data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());

            File renameFile = new File(newFile);
            assertTrue(renameFile.exists());
            postReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(renameFile)));
            assertTrue(postReader.ready());
            expectationResultData = "testRenameFile10_new.txtデータ";
            data = "";
            for (int i = 0; i < expectationResultData.length(); i++) {
                assertTrue(i + "回目の判定で失敗しました。", postReader.ready());
                data += (char) postReader.read();
            }
            assertEquals(expectationResultData, data);
            assertFalse(postReader.ready());
        } finally {
            if (lock != null) {
                lock.release();
            }
            if (fis != null) {
                fis.close();
            }
            if (testSrcFileFileWriter != null) {
                testSrcFileFileWriter.close();
            }
            if (testNewFileFileWriter != null) {
                testNewFileFileWriter.close();
            }
            if (postReader != null) {
                postReader.close();
            }
            // テスト後ファイルを削除
            File file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testIsCheckFileExist01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) FileUtility#checkFileExist():false<br>
     * <br>
     * 期待値：(戻り値) boolean:false<br>
     * <br>
     * 属性の値が取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testIsCheckFileExist01() throws Exception {
        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト実施
        boolean result = FileUtility.isCheckFileExist();
        // 判定
        assertFalse(result);
    }

    /**
     * testSetCheckFileExist01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) checkFileExist:true<br>
     * (状態) FileUtility#checkFileExist():false<br>
     * <br>
     * 期待値：(状態変化) checkFileExist:true<br>
     * <br>
     * 引数の値が属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    public void testSetCheckFileExist01() throws Exception {
        // 前提条件の設定
        UTUtil.setPrivateField(FileUtility.class, "checkFileExist", false);

        // テスト実施
        FileUtility.setCheckFileExist(true);

        // 状態変化の確認
        assertEquals(true, UTUtil.getPrivateField(FileUtility.class,
                "checkFileExist"));
    }
}
