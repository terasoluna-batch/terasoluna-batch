/*
 * $Id: NullStringConverterTest.java 5819 2007-12-20 05:55:47Z fukuyot $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */

package jp.terasoluna.fw.file.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * {@link jp.terasoluna.fw.file.annotation.NullStringConverter} クラスのテスト。
 * <p>
 * <h4>【クラスの概要】</h4> アノテーションStringConverterの記述に従い、文字列の変換処理を行う。<br>
 * NullStringConverterは文字列変換処理を行わないので、 入力された文字列がそのまま返却される。
 * <p>
 * @author 奥田哲司
 * @see jp.terasoluna.fw.file.annotation.NullStringConverter
 */
public class NullStringConverterTest {

    /**
     * testConvert01() <br>
     * <br>
     * (正常系) <br>
     * 観点：C <br>
     * <br>
     * 入力値：(引数) string:null<br>
     * <br>
     * 期待値：(戻り値) resultString:null<br>
     * <br>
     * 引数にNullが入った場合の処理。<br>
     * nullを返却する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvert01() throws Exception {
        // 前処理
        StringConverter stringTransformer = new NullStringConverter();

        // テスト実施
        String resultString = stringTransformer.convert(null);

        // 判定
        assertNull(resultString);
    }

    /**
     * testConvert02() <br>
     * <br>
     * (正常系) <br>
     * 観点：C, E <br>
     * <br>
     * 入力値：(引数) string:not null<br>
     * 以下のStringインスタンス<br>
     * "abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ"<br>
     *<br>
     * 期待値：(戻り値) resultString:以下のStringインスタンス<br>
     * "abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ"<br>
     * <br>
     * 入力した文字列がそのまま返却されることのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvert02() throws Exception {
        // 前処理
        StringConverter stringTransformer = new NullStringConverter();

        // テスト実施
        String resultString = stringTransformer
                .convert("abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        // 判定
        assertEquals("abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                resultString);
    }

    /**
     * testConvert03() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) string:not null<br>
     * 以下のStringインスタンス<br>
     * ""<br>
     * <br>
     * 期待値：(戻り値) resultString:not null<br>
     * 以下のStringインスタンス<br>
     * ""<br>
     * <br>
     * 入力した""がそのまま返却されることのテスト。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvert03() throws Exception {
        // 前処理(引数)
        StringConverter stringTransformer = new NullStringConverter();

        // テスト実施
        String resultString = stringTransformer.convert("");

        // 判定
        assertEquals("", resultString);
    }

    /**
     * testConvert04() <br>
     * <br>
     * (正常系) <br>
     * 観点：E <br>
     * <br>
     * 入力値：(引数) string:以下のStringインスタンス<br>
     * "あいうカキク漢字"<br>
     * <br>
     * 期待値：(戻り値) resultString:以下のStringインスタンス<br>
     * "あいうカキク漢字"<br>
     * <br>
     * 文字変換処理を実施。<br>
     * ひらがな、カタカナ、漢字は、そのまま変換されずに出力されることを確認する。 <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testConvert04() throws Exception {
        // 前処理(引数)
        StringConverter stringTransformer = new NullStringConverter();

        // テスト実施
        String resultString = stringTransformer.convert("あいうカキク漢字");

        // 判定
        assertEquals("あいうカキク漢字", resultString);
    }
}
