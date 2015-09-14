/*
 * $Id: FileControlImplTest.java 5576 2007-11-15 13:13:32Z pakucn $
 *
 * Copyright (c) 2006-2015 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jp.terasoluna.fw.file.dao.FileException;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * {@link jp.terasoluna.fw.file.util.FileControlImpl} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> FileControlインタフェースを実装するクラス.
 * <p>
 * @author 吉信郁美
 * @see jp.terasoluna.fw.file.util.FileControlImpl
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileUtility.class, FileControlImpl.class })
public class FileControlImplTest {

    /**
     * testCopyFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testCopyFile01_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testCopyFile01_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出されることを確認する。<br>
     * 引数srcFile、newFileの順にFileUtilityの引数に渡されることを確認する。<br>
     * <br>
     * コピー元とコピー先のファイルパスに絶対パスを指定するケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testCopyFile01_src.txt";
        String newFile = directoryPath + "testCopyFile01_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> fileControlStringArguments = fileControlStringArgumentCaptor
                    .getAllValues();

            assertEquals(2, fileControlStringArguments.size());
            assertEquals(srcFile, fileControlStringArguments.get(0));
            assertEquals(newFile, fileControlStringArguments.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> fileUtilityStringArguments = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, fileUtilityStringArguments.get(0));
            assertEquals(newFile, fileUtilityStringArguments.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testCopyFile02_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testCopyFile02_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出されることを確認する。<br>
     * 引数srcFile、newFileの順に<br>
     * FileControl.basePathをそれぞれの先頭につけて<br>
     * FileUtilityの引数に渡されることを確認する。<br>
     * <br>
     * コピー元とコピー先のファイルパスに相対パスを指定するケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile02() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testCopyFile02_src.txt";
        String newFile = "testCopyFile02_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> fileControlStringArguments = fileControlStringArgumentCaptor
                    .getAllValues();

            assertEquals(2, fileControlStringArguments.size());
            assertEquals(srcFile, fileControlStringArguments.get(0));
            assertEquals(newFile, fileControlStringArguments.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> fileUtilityStringArguments = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(basePath + srcFile, fileUtilityStringArguments.get(0));
            assertEquals(basePath + newFile, fileUtilityStringArguments.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testCopyFile03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testCopyFile03_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出されることを確認する。<br>
     * 引数srcFile、newFileの順に<br>
     * FileControl.basePathをそれぞれの先頭につくことなく<br>
     * FileUtilityの引数に渡されることを確認する。<br>
     * <br>
     * 引数のファイルパスが絶対パスであり、基準パスが設定されている場合は基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile03() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testCopyFile03_src.txt";
        String newFile = directoryPath + "testCopyFile03_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> fileControlStringArguments = fileControlStringArgumentCaptor
                    .getAllValues();

            assertEquals(2, fileControlStringArguments.size());
            assertEquals(srcFile, fileControlStringArguments.get(0));
            assertEquals(newFile, fileControlStringArguments.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> fileUtilityStringArguments = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, fileUtilityStringArguments.get(0));
            assertEquals(newFile, fileUtilityStringArguments.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile04() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testCopyFile04_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testCopyFile04_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出されることを確認する。<br>
     * 引数srcFile、newFileの順に渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであり、基準パスが設定されていないケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile04() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testCopyFile04_src.txt";
        String newFile = "testCopyFile04_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> fileControlStringArguments = fileControlStringArgumentCaptor
                    .getAllValues();

            assertEquals(2, fileControlStringArguments.size());
            assertEquals(srcFile, fileControlStringArguments.get(0));
            assertEquals(newFile, fileControlStringArguments.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> fileUtilityStringArguments = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, fileUtilityStringArguments.get(0));
            assertEquals(newFile, fileUtilityStringArguments.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testCopyFile05_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数srcFileにnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile05() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = null;
        String newFile = directoryPath + "testCopyFile05_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし
            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(1)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String fileControlStringArgument = fileControlStringArgumentCaptor
                    .getValue();
            assertNull(fileControlStringArgument);
            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
            PowerMockito.verifyStatic(Mockito.never());
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testCopyFile06_src.txt"<br>
     * (引数) newFile:null<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数newFileにnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile06() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testCopyFile06_src.txt";
        String newFile = null;

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String fileControlStringArgument = fileControlStringArgumentCaptor
                    .getValue();
            assertNull(fileControlStringArgument);
            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
            PowerMockito.verifyStatic(Mockito.never());
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testCopyFile07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testCopyFile07_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testCopyFile07_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数ｓｒｃFileのファイルパスが相対パスで、引数newFileのファイルパスが絶対パスの場合、srcFileのファイルパスに基準パスが付与されnewFileのファイルパスには基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile07() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testCopyFile07_src.txt";
        String newFile = directoryPath + "testCopyFile07_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argListGetAbsolutePath = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListGetAbsolutePath.get(0));
            assertEquals(newFile, argListGetAbsolutePath.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListCopyFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(basePath + srcFile, argListCopyFile.get(0));
            assertEquals(newFile, argListCopyFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testCopyFile08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testCopyFile08_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testCopyFile08_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility#copyFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * 引数ｓｒｃFileのファイルパスが絶対パスで、引数newFileのファイルパスが相対パスの場合、srcFileのファイルパスに基準パスが付与されずnewFileのファイルパスには基準パスが付与されること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testCopyFile08() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testCopyFile08_src.txt";
        String newFile = "testCopyFile08_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "copyFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.copyFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argListGetAbsolutePath = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListGetAbsolutePath.get(0));
            assertEquals(newFile, argListGetAbsolutePath.get(1));

            PowerMockito.verifyStatic();
            FileUtility.copyFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListCopyFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListCopyFile.get(0));
            assertEquals(basePath + newFile, argListCopyFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testDeleteFile01_src.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.deleteFile():呼び出される。<br>
     * 引数srcFileが渡されることを確認する。<br>
     * <br>
     * ファイルのパスが絶対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDeleteFile01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testDeleteFile01_src.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "deleteFile",
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.deleteFile(srcFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String argGetAbsolutePath = fileControlStringArgumentCaptor
                    .getValue();
            assertEquals(srcFile, argGetAbsolutePath);

            PowerMockito.verifyStatic();
            FileUtility.deleteFile(Mockito.anyString());

            String argDeleteFile = fileUtilityStringArgumentCaptor.getValue();
            assertEquals(srcFile, argDeleteFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testDeleteFile02_src.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.deleteFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDeleteFile02() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testDeleteFile02_src.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "deleteFile",
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.deleteFile(srcFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String argGetAbsolutePath = fileControlStringArgumentCaptor
                    .getValue();
            assertEquals(srcFile, argGetAbsolutePath);

            PowerMockito.verifyStatic();
            FileUtility.deleteFile(Mockito.anyString());

            String argDeleteFile = fileUtilityStringArgumentCaptor.getValue();
            assertEquals(basePath + srcFile, argDeleteFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testDeleteFile03_src.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.deleteFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数のファイルパスが絶対パスであり、基準パスが設定されている場合は基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDeleteFile03() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testDeleteFile03_src.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "deleteFile",
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.deleteFile(srcFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String argListGetAbsolutePath = fileControlStringArgumentCaptor
                    .getValue();
            assertEquals(srcFile, argListGetAbsolutePath);

            PowerMockito.verifyStatic();
            FileUtility.deleteFile(Mockito.anyString());

            String argDeleteFile = fileUtilityStringArgumentCaptor.getValue();
            assertEquals(srcFile, argDeleteFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile04() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testDeleteFile04_src.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.deleteFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであり、基準パスが設定されていないケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDeleteFile04() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testDeleteFile04_src.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "deleteFile",
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.deleteFile(srcFile);

            // 状態変化の確認
            PowerMockito.verifyStatic();
            FileUtility.deleteFile(Mockito.anyString());

            String argDeleteFile = fileUtilityStringArgumentCaptor.getValue();
            assertEquals(basePath + srcFile, argDeleteFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
        }
    }

    /**
     * testDeleteFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * <br>
     * 引数にnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testDeleteFile05() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String srcFile = null;

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doCallRealMethod().when(FileUtility.class, "deleteFile",
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.deleteFile(srcFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            PowerMockito.verifyStatic(Mockito.never());
            FileUtility.deleteFile(Mockito.anyString());

            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
        }
    }

    /**
     * testMergeFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:要素をもたないArrayListインスタンス<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile01_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数newFile、fileListが渡されることを確認する。<br>
     * <br>
     * fileListの要素が空であるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String newFile = directoryPath + "testMergeFile01_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String argGetAbsolutePath = fileControlStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argGetAbsolutePath);

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(fileList, argListMergeFile);
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile02-01_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile02_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数newFile、fileListが渡されることを確認する。<br>
     * <br>
     * fileListの要素が1つで、統合ファイルのパスが絶対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile02() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile02-01_src.txt";
        fileList.add(srcFile1);

        String newFile = directoryPath + "testMergeFile02_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile1, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(fileList, argListMergeFile);
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile03() <br>
     * <br>
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"testMergeFile03-01_src.txt"<br>
     * 要素2:"testMergeFile03-02_src.txt"<br>
     * 要素3:"testMergeFile03-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testMergeFile03_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数newFile、fileListがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * fileListの要素が3つで、統合ファイルのパスが相対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile03() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = "testMergeFile03-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = "testMergeFile03-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = "testMergeFile03-03_src.txt";
        fileList.add(srcFile3);

        String newFile = "testMergeFile03_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile1, argGetAbsolutePathList.get(0));
            assertEquals(srcFile2, argGetAbsolutePathList.get(1));
            assertEquals(srcFile3, argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(basePath + srcFile1, argListMergeFile.get(0));
            assertEquals(basePath + srcFile2, argListMergeFile.get(1));
            assertEquals(basePath + srcFile3, argListMergeFile.get(2));
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(basePath + newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile1);
            file.delete();
            file = new File(directoryPath + srcFile2);
            file.delete();
            file = new File(directoryPath + srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile04() <br>
     * <br>
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile04-01_src.txt"<br>
     * 要素2:"(パス)testMergeFile04-02_src.txt"<br>
     * 要素3:"(パス)testMergeFile04-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile04_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数newFile、fileListがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数のファイルパスが絶対パスであり、基準パスが設定されている場合は基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile04() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile04-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = directoryPath + "testMergeFile04-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = directoryPath + "testMergeFile04-03_src.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile04_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);
        ArgumentCaptor<String> fileUtlityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtlityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(
                            fileControlStringArgumentCaptor.capture());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(fileList.get(0), argGetAbsolutePathList.get(0));
            assertEquals(fileList.get(1), argGetAbsolutePathList.get(1));
            assertEquals(fileList.get(2), argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(fileList.get(0), argListMergeFile.get(0));
            assertEquals(fileList.get(1), argListMergeFile.get(1));
            assertEquals(fileList.get(2), argListMergeFile.get(2));
            String argStringMergeFile = fileUtlityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
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
     * testMergeFile05() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"testMergeFile05-01_src.txt"<br>
     * 要素2:"testMergeFile05-02_src.txt"<br>
     * 要素3:"testMergeFile05-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testMergeFile05_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数newFile、fileListがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであり、基準パスが設定されていないケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile05() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = "testMergeFile05-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = "testMergeFile05-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = "testMergeFile05-03_src.txt";
        fileList.add(srcFile3);

        String newFile = "testMergeFile05_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(fileList.get(0), argGetAbsolutePathList.get(0));
            assertEquals(fileList.get(1), argGetAbsolutePathList.get(1));
            assertEquals(fileList.get(2), argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(srcFile1, argListMergeFile.get(0));
            assertEquals(srcFile2, argListMergeFile.get(1));
            assertEquals(srcFile3, argListMergeFile.get(2));
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile1);
            file.delete();
            file = new File(directoryPath + srcFile2);
            file.delete();
            file = new File(directoryPath + srcFile3);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileList:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile06_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外NullPointerExceptionが発生する。<br>
     * <br>
     * 引数fileListにnullが設定された場合は、NullPointerExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMergeFile06() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = null;
        String newFile = directoryPath + "testMergeFile06_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);
            fail("NullPointerExceptionが発生しませんでした。失敗です。");
        } catch (Exception e) {
            // 返却値なし

            // 状態変化の確認
            assertEquals(NullPointerException.class, e.getClass());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile07() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile07-01_src.txt"<br>
     * (引数) newFile:null<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数newFileにnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMergeFile07() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile07-01_src.txt";
        fileList.add(srcFile1);

        String newFile = null;

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile1, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
            file.delete();
        }
    }

    /**
     * testMergeFile08() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile08-01_src.txt"<br>
     * 要素2:null<br>
     * 要素3:"(パス)testMergeFile08-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile08_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数fileListの要素にnullが含まれている場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testMergeFile08() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile08-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = null;
        fileList.add(srcFile2);

        String srcFile3 = directoryPath + "testMergeFile08-03_src.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile08_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile1, argGetAbsolutePathList.get(0));
            assertEquals(srcFile2, argGetAbsolutePathList.get(1));

            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
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
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"testMergeFile09-01_src.txt"<br>
     * 要素2:"testMergeFile09-02_src.txt"<br>
     * 要素3:"testMergeFile09-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile09_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数fileListがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数fileListのファイルパスが相対パスで、引数newFileのファイルパスが絶対パスの場合。<br>
     * fileListのファイルパスに基準パスが付与されnewFileのファイルパスには基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile09() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = "testMergeFile09-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = "testMergeFile09-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = "testMergeFile09-03_src.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile09_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(fileList.get(0), argGetAbsolutePathList.get(0));
            assertEquals(fileList.get(1), argGetAbsolutePathList.get(1));
            assertEquals(fileList.get(2), argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(basePath + srcFile1, argListMergeFile.get(0));
            assertEquals(basePath + srcFile2, argListMergeFile.get(1));
            assertEquals(basePath + srcFile3, argListMergeFile.get(2));
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile1);
            file.delete();
            file = new File(directoryPath + srcFile2);
            file.delete();
            file = new File(directoryPath + srcFile3);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testMergeFile10() <br>
     * <br>
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile10-01_src.txt"<br>
     * 要素2:"testMergeFile10-02_src.txt"<br>
     * 要素3:"(パス)testMergeFile10-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testMergeFile10_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数fileListの要素1、3がFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * 引数fileListの要素2がFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数fileListの要素にファイルパスが相対パスと絶対パスが含まれていて、引数newFileのファイルパスが絶対パスの場合。<br>
     * fileListの要素2のファイルパスに基準パスが付与されその他の要素には付与されず、newFileのファイルパスには基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile10() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile10-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = "testMergeFile10-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = directoryPath + "testMergeFile10-03_src.txt";
        fileList.add(srcFile3);

        String newFile = directoryPath + "testMergeFile10_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(fileList.get(0), argGetAbsolutePathList.get(0));
            assertEquals(fileList.get(1), argGetAbsolutePathList.get(1));
            assertEquals(fileList.get(2), argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(srcFile1, argListMergeFile.get(0));
            assertEquals(basePath + srcFile2, argListMergeFile.get(1));
            assertEquals(srcFile3, argListMergeFile.get(2));
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
            file.delete();
            file = new File(directoryPath + srcFile2);
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
     * (正常系) <br>
     * 観点：D, E, F <br>
     * <br>
     * 入力値：(引数) fileList:以下の要素をもつArrayListインスタンス<br>
     * 要素1:"(パス)testMergeFile11-01_src.txt"<br>
     * 要素2:"(パス)testMergeFile11-02_src.txt"<br>
     * 要素3:"(パス)testMergeFile11-03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testMergeFile11_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.mergeFile():1回呼び出される。<br>
     * 引数fileListがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * 引数fileListのファイルパスが絶対パスで、引数newFileのファイルパスが相対パスの場合。<br>
     * fileListのファイルパスに基準パスが付与されずnewFileのファイルパスには基準パスが付与されること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMergeFile11() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        List<String> fileList = new ArrayList<String>();

        String srcFile1 = directoryPath + "testMergeFile11-01_src.txt";
        fileList.add(srcFile1);

        String srcFile2 = directoryPath + "testMergeFile11-02_src.txt";
        fileList.add(srcFile2);

        String srcFile3 = directoryPath + "testMergeFile11-03_src.txt";
        fileList.add(srcFile3);

        String newFile = "testMergeFile11_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile1);
        file.delete();
        file.createNewFile();

        file = new File(srcFile2);
        file.delete();
        file.createNewFile();

        file = new File(srcFile3);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> fileUtilityListArgumentCaptor = ArgumentCaptor
                .forClass(List.class);

        PowerMockito.doNothing().when(FileUtility.class, "mergeFile",
                fileUtilityListArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.mergeFile(fileList, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(4)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(fileList.get(0), argGetAbsolutePathList.get(0));
            assertEquals(fileList.get(1), argGetAbsolutePathList.get(1));
            assertEquals(fileList.get(2), argGetAbsolutePathList.get(2));
            assertEquals(newFile, argGetAbsolutePathList.get(3));

            PowerMockito.verifyStatic();
            FileUtility.mergeFile(Mockito.anyList(), Mockito.anyString());

            @SuppressWarnings("rawtypes")
            List argListMergeFile = fileUtilityListArgumentCaptor.getValue();
            assertEquals(srcFile1, argListMergeFile.get(0));
            assertEquals(srcFile2, argListMergeFile.get(1));
            assertEquals(srcFile3, argListMergeFile.get(2));
            String argStringMergeFile = fileUtilityStringArgumentCaptor
                    .getValue();
            assertEquals(basePath + newFile, argStringMergeFile);
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile1);
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
     * testRenameFile01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testRenameFile01_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testRenameFile01_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFile、newFileが渡されることを確認する。<br>
     * <br>
     * ファイルのパスが絶対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testRenameFile01_src.txt";
        String newFile = directoryPath + "testRenameFile01_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListRenameFile.get(0));
            assertEquals(newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile02() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testRenameFile02_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testRenameFile02_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFile、newFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであるケース。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile02() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testRenameFile02_src.txt";
        String newFile = "testRenameFile02_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(basePath + srcFile, argListRenameFile.get(0));
            assertEquals(basePath + newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testRenameFile03_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testRenameFile03_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFile、newFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数のファイルパスが絶対パスであり、基準パスが設定されている場合は基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile03() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testRenameFile03_src.txt";
        String newFile = directoryPath + "testRenameFile03_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListRenameFile.get(0));
            assertEquals(newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile04() <br>
     * <br>
     * (正常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testRenameFile04_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testRenameFile04_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFile、newFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * ファイルのパスが相対パスであり、基準パスが設定されていないケース。<br>
     * 例外FileExceptionがスローされることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile04() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testRenameFile04_src.txt";
        String newFile = "testRenameFile04_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListRenameFile.get(0));
            assertEquals(newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile05() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:null<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testRenameFile05_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数srcFileにnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile05() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = null;
        String newFile = directoryPath + "testRenameFile05_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            String argGetAbsolutePath = fileControlStringArgumentCaptor
                    .getValue();
            assertEquals(srcFile, argGetAbsolutePath);
            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile06() <br>
     * <br>
     * (異常系) <br>
     * 観点：G <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testRenameFile06_src.txt"<br>
     * (引数) newFile:null<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(状態変化) 例外:例外FileExceptionが発生する。<br>
     * メッセージ："File name is not set."<br>
     * <br>
     * 引数newFileにnullが設定された場合は、FileExceptionがスローされること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile06() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testRenameFile06_src.txt";
        String newFile = null;

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = "";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        PowerMockito.doNothing().when(FileUtility.class, "renameFile", Mockito
                .anyString(), Mockito.anyString());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);
            fail("FileExceptionが発生しませんでした。失敗です。");
        } catch (FileException e) {
            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic(Mockito.never());
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            assertEquals(FileException.class, e.getClass());
            assertEquals("File name is not set.", e.getMessage());
            assertNull(e.getFileName());
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
        }
    }

    /**
     * testRenameFile07() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "testRenameFile07_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "(パス)testRenameFile07_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * <br>
     * 引数ｓｒｃFileのファイルパスが相対パスで、引数newFileのファイルパスが絶対パスの場合、srcFileのファイルパスに基準パスが付与されnewFileのファイルパスには基準パスが付与されないこと。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile07() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = "testRenameFile07_src.txt";
        String newFile = directoryPath + "testRenameFile07_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(directoryPath + srcFile);
        file.delete();
        file.createNewFile();

        file = new File(newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());

        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(basePath + srcFile, argListRenameFile.get(0));
            assertEquals(newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(directoryPath + srcFile);
            file.delete();
            file = new File(newFile);
            file.delete();
        }
    }

    /**
     * testRenameFile08() <br>
     * <br>
     * (正常系) <br>
     * 観点：E, F <br>
     * <br>
     * 入力値：(引数) srcFile:Stringインスタンス<br>
     * "(パス)testRenameFile08_src.txt"<br>
     * (引数) newFile:Stringインスタンス<br>
     * "testRenameFile08_new.txt"<br>
     * (状態) FileControl.basePath:Stringインスタンス<br>
     * "(パス)"<br>
     * <br>
     * 期待値：(状態変化) FileUtility.renameFile():呼び出される。<br>
     * 引数srcFileがFileControl.basePathを先頭につけずに渡されることを確認する。<br>
     * 引数newFileがFileControl.basePathを先頭につけて渡されることを確認する。<br>
     * <br>
     * 引数ｓｒｃFileのファイルパスが絶対パスで、引数newFileのファイルパスが相対パスの場合、srcFileのファイルパスに基準パスが付与されずnewFileのファイルパスには基準パスが付与されること。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testRenameFile08() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = PowerMockito.spy(new FileControlImpl());

        // 引数の設定
        String classFileName = this.getClass().getSimpleName() + ".class";
        URL url = this.getClass().getResource(classFileName);
        String directoryPath = url.getPath().substring(0, url.getPath().length()
                - classFileName.length());

        String srcFile = directoryPath + "testRenameFile08_src.txt";
        String newFile = "testRenameFile08_new.txt";

        // テスト前、対象ファイルを初期化
        File file = new File(srcFile);
        file.delete();
        file.createNewFile();

        file = new File(directoryPath + newFile);
        file.delete();
        file.createNewFile();

        // 前提条件の設定
        String basePath = directoryPath;
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<String> fileUtilityStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);
        PowerMockito.doNothing().when(FileUtility.class, "renameFile",
                fileUtilityStringArgumentCaptor.capture(),
                fileUtilityStringArgumentCaptor.capture());

        Method getAbsolutePathMethod = PowerMockito.method(
                FileControlImpl.class, "getAbsolutePath", String.class);
        ArgumentCaptor<String> fileControlStringArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        PowerMockito.doCallRealMethod().when(fileControl, getAbsolutePathMethod)
                .withArguments(fileControlStringArgumentCaptor.capture());
        try {
            // テスト実施
            fileControl.renameFile(srcFile, newFile);

            // 返却値なし

            // 状態変化の確認
            PowerMockito.verifyPrivate(fileControl, Mockito.times(2)).invoke(
                    getAbsolutePathMethod).withArguments(Mockito.anyString());
            List<String> argGetAbsolutePathList = fileControlStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argGetAbsolutePathList.get(0));
            assertEquals(newFile, argGetAbsolutePathList.get(1));

            PowerMockito.verifyStatic();
            FileUtility.renameFile(Mockito.anyString(), Mockito.anyString());

            List<String> argListRenameFile = fileUtilityStringArgumentCaptor
                    .getAllValues();
            assertEquals(srcFile, argListRenameFile.get(0));
            assertEquals(basePath + newFile, argListRenameFile.get(1));
        } finally {
            // テスト後、対象ファイルを初期化
            file = new File(srcFile);
            file.delete();
            file = new File(directoryPath + newFile);
            file.delete();
        }
    }

    /**
     * testGetBasePath01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) basePath:Stringインスタンス<br>
     * "aaa"<br>
     * <br>
     * 期待値：(戻り値) String:前提条件のbasePathと同じ<br>
     * Stringインスタンス<br>
     * <br>
     * 属性が取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testGetBasePath01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = new FileControlImpl();

        // 引数なし

        // 前提条件の設定
        String basePath = "aaa";
        ReflectionTestUtils.setField(fileControl, "basePath", basePath);

        // テスト実施
        String getBasePath = fileControl.getBasePath();

        // 返却値の確認
        assertEquals(basePath, getBasePath);

        // 状態変化なし
    }

    /**
     * testSetBasePath01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) basePath:Stringインスタンス<br>
     * "aaa"<br>
     * (状態) basePath:""<br>
     * <br>
     * 期待値：(状態変化) basePath:引数のbasePathと同じ<br>
     * Stringインスタンス<br>
     * <br>
     * 引数が正しく属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetBasePath01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = new FileControlImpl();

        // 引数の設定
        String basePath = "aaa";

        // 前提条件の設定
        ReflectionTestUtils.setField(fileControl, "basePath", "");

        // テスト実施
        fileControl.setBasePath(basePath);

        // 返却値なし

        // 状態変化の確認
        assertEquals(basePath, ReflectionTestUtils.getField(fileControl,
                "basePath"));
    }

    /**
     * testSetCheckFileExist01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) checkFileExist:true<br>
     * <br>
     * 期待値：(状態変化) setCheckFileExist:呼び出される。<br>
     * 引数checkFileExistが渡されることを確認する。<br>
     * <br>
     * 引数がtrue <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetCheckFileExist01() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = new FileControlImpl();

        // 引数の設定
        boolean checkFileExist = true;

        // 前提条件なし
        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<Boolean> argSetCheckFileExist = ArgumentCaptor.forClass(
                Boolean.class);
        PowerMockito.doNothing().when(FileUtility.class, "setCheckFileExist",
                argSetCheckFileExist.capture());

        // テスト実施
        fileControl.setCheckFileExist(checkFileExist);

        // 返却値なし

        // 状態変化の確認
        PowerMockito.verifyStatic();
        FileUtility.setCheckFileExist(Mockito.anyBoolean());

        assertEquals(checkFileExist, argSetCheckFileExist.getValue());
    }

    /**
     * testSetCheckFileExist02() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(引数) checkFileExist:false<br>
     * <br>
     * 期待値：(状態変化) setCheckFileExist:呼び出される。<br>
     * 引数checkFileExistが渡されることを確認する。<br>
     * <br>
     * 引数がfalse <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testSetCheckFileExist02() throws Exception {
        // テスト対象のインスタンス化
        FileControlImpl fileControl = new FileControlImpl();

        // 引数の設定
        boolean checkFileExist = false;

        // 前提条件なし
        PowerMockito.mockStatic(FileUtility.class);
        ArgumentCaptor<Boolean> argSetCheckFileExist = ArgumentCaptor.forClass(
                Boolean.class);
        PowerMockito.doNothing().when(FileUtility.class, "setCheckFileExist",
                argSetCheckFileExist.capture());

        // テスト実施
        fileControl.setCheckFileExist(checkFileExist);

        // 返却値なし

        // 状態変化の確認
        PowerMockito.verifyStatic();
        FileUtility.setCheckFileExist(Mockito.anyBoolean());

        assertEquals(checkFileExist, argSetCheckFileExist.getValue());
    }
}
