/*
 * $Id:$
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.dao;

import jp.terasoluna.utlib.UTUtil;
import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.file.dao.FileException} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> ファイルアクセス機能で発生する例外をラップするRuntimeException
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.dao.FileException
 */
public class FileExceptionTest extends TestCase {

    /**
     * このテストケースを実行する為の GUI アプリケーションを起動する。
     * @param args java コマンドに設定されたパラメータ
     */
    public static void main(String[] args) {
        // junit.swingui.TestRunner.run(FileExceptionTest.class);
    }

    /**
     * 初期化処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * コンストラクタ。
     * @param name このテストケースの名前。
     */
    public FileExceptionTest(String name) {
        super(name);
    }

    /**
     * testFileExceptionException01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) e:Exceptionクラスのインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.cause:引数eと同じオブジェクト。<br>
     * (状態変化) FileException.fileName:nullが設定<br>
     * <br>
     * コンストラクタの引数で受け取ったExceptionをラップすることを確認する。<br>
     * 正常ケース <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionException01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        Exception e = new Exception();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(e);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(e, fe.getCause());
        assertNull(UTUtil.getPrivateField(fe, "fileName"));
    }

    /**
     * testFileExceptionExceptionString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) e:not null<br>
     * Exceptionクラスのインスタンス<br>
     * (引数) fileName:not null<br>
     * 空Stringクラスのインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.fileName:引数filenameと同じオブジェクト<br>
     * (状態変化) FileException.cause:引数eと同じオブジェクト。<br>
     * <br>
     * コンストラクタの引数で受け取ったExceptionをラップすることを確認する。<br>
     * fileNameが属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionExceptionString01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        Exception e = new Exception();
        String fileName = new String();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(e, fileName);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(fileName, UTUtil.getPrivateField(fe, "fileName"));
        assertSame(e, fe.getCause());
    }

    /**
     * testFileExceptionString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) message:not null<br>
     * 空のStringインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.detailMessage: 引数StringObjectと同じStringオブジェクト<br>
     * (状態変化) FileException.fileName:null<br>
     * <br>
     * コンストラクタ引数に例外メッセージを設定する。<br>
     * 例外オブジェクト生成後にメッセージが格納されていることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionString01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        String message = new String();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(message);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(message, UTUtil.getPrivateField(fe, "detailMessage"));
        assertNull(UTUtil.getPrivateField(fe, "fileName"));
    }

    /**
     * testFileExceptionStringString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) message:not null<br>
     * 空のStringインスタンス<br>
     * (引数) fileName:not null<br>
     * 空のStringインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.detailMessage: 引数StringObjectと同じオブジェクト。<br>
     * (状態変化) FileException.fileName:引数filenameと同じオブジェクト<br>
     * <br>
     * コンストラクタ引数に例外メッセージを設定する。<br>
     * 例外オブジェクト生成後にメッセージが格納されていることを確認する。<br>
     * fileNameが属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionStringString01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        String message = new String();
        String fileName = new String();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(message, fileName);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(message, UTUtil.getPrivateField(fe, "detailMessage"));
        assertSame(fileName, UTUtil.getPrivateField(fe, "fileName"));
    }

    /**
     * testFileExceptionStringException01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) message:not null<br>
     * 空のStringインスタンス<br>
     * (引数) e:not null<br>
     * Exceptionクラスのインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.detailMessage:引数stringObjectと同じオブジェクト。<br>
     * (状態変化) FileException.cause:引数eと同じオブジェクト<br>
     * (状態変化) FileException.fileName:null<br>
     * <br>
     * コンストラクタ引数に例外メッセージを設定する。<br>
     * 例外オブジェクト生成後にメッセージが格納されていることを確認する。<br>
     * messageが属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionStringException01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        Exception e = new Exception();
        String message = new String();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(message, e);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(message, UTUtil.getPrivateField(fe, "detailMessage"));
        assertNull(UTUtil.getPrivateField(fe, "fileName"));
        assertSame(e, UTUtil.getPrivateField(fe, "cause"));
    }

    /**
     * testFileExceptionStringExceptionString01() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) message:not null<br>
     * 空のStringインスタンス<br>
     * (引数) e:not null<br>
     * Exceptionクラスのインスタンス<br>
     * (引数) fileName:not null<br>
     * 空のStringインスタンス<br>
     * <br>
     * 期待値：(状態変化) FileException.detailMessage:引数stringObjectと 同じオブジェクト。<br>
     * (状態変化) FileException.cause:引数eと同じオブジェクト<br>
     * (状態変化) FileException.fileName:引数filenameと同じオブジェクト<br>
     * <br>
     * コンストラクタの引数で受け取ったExceptionをラップすることを確認する。<br>
     * コンストラクタ引数に例外メッセージを設定する。<br>
     * 例外オブジェクト生成後にメッセージが格納されていることを確認する。<br>
     * fileNameが属性に設定されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testFileExceptionStringExceptionString01() throws Exception {
        // テスト対象のインスタンス化
        // コンストラクタの試験なので、不要

        // 引数の設定
        Exception e = new Exception();
        String message = new String();
        String fileName = new String();

        // 前提条件の設定
        // なし

        // テスト実施
        FileException fe = new FileException(message, e, fileName);

        // 返却値の確認
        // なし

        // 状態変化の確認
        assertSame(message, UTUtil.getPrivateField(fe, "detailMessage"));
        assertSame(fileName, UTUtil.getPrivateField(fe, "fileName"));
        assertSame(e, UTUtil.getPrivateField(fe, "cause"));
    }

    /**
     * testGetFileName01() <br>
     * <br>
     * (正常系) <br>
     * 観点：F <br>
     * <br>
     * 入力値：(状態) fileName:not null<br>
     * 空のStringインスタンス<br>
     * <br>
     * 期待値：(戻り値) String:前提条件のfilenameと同じオブジェクト<br>
     * <br>
     * 属性が正しく取得できることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */

    public void testGetFileName01() throws Exception {
        // テスト対象のインスタンス化
        FileException fe = new FileException(new String());

        // 引数の設定
        // なし

        // 前提条件の設定
        String fileName = new String();
        UTUtil.setPrivateField(fe, "fileName", fileName);

        // テスト実施
        String result = fe.getFileName();

        // 返却値の確認
        assertSame(fileName, result);

        // 状態変化の確認
        // なし
    }

}
