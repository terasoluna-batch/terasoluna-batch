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

package jp.terasoluna.fw.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jp.terasoluna.utlib.PropertyTestCase;

/**
 * DateUtil ブラックボックステスト。<br>
 * 前提条件<br>
 * ・test.propertiesがクラスパスに設定されている必要がある<br>
 * ・プロパティファイルに以下のような設定をしておく<br>
 * 　wareki.gengo.0.name = 平成<br>
 * 　wareki.gengo.0.roman = H<br>
 * 　wareki.gengo.0.startDate = 1989/01/08<br>
 * 　wareki.gengo.1.name = 昭和<br>
 * 　wareki.gengo.1.roman = S<br>
 * 　wareki.gengo.1.startDate = 1926/12/25<br>
 * 　wareki.gengo.2.name = 大正<br>
 * 　wareki.gengo.2.roman = T<br>
 * 　wareki.gengo.2.startDate = 1912/07/30<br>
 * 　wareki.gengo.3.name = 明治<br>
 * 　wareki.gengo.3.roman = M<br>
 * 　wareki.gengo.3.startDate = 1868/09/04<br>
 * 　wareki.gengo.4.name = 平成<br>
 * 　wareki.gengo.4.roman = H<br>
 * 　wareki.gengo.5.name = 平成<br>
 * 　wareki.gengo.5.roman = H<br>
 * 　wareki.gengo.5.startDate = asdf<br>
 *
 */
@SuppressWarnings("unused")
public class DateUtilTest01 extends PropertyTestCase {

    /**
     * 日時を設定するためのフィールド
     */
    private SimpleDateFormat df = null;

    /**
     * 日時を設定するためのフィールド
     */
    private Date date = null;

    /**
     * Constructor for DateUtilTest01.
     * @param arg0
     */
    public DateUtilTest01(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUpData() throws Exception {
        addProperty("wareki.gengo.0.name", "平成");
        addProperty("wareki.gengo.0.roman", "H");
        addProperty("wareki.gengo.0.startDate", "1989/01/08");
        addProperty("wareki.gengo.1.name", "昭和");
        addProperty("wareki.gengo.1.roman", "S");
        addProperty("wareki.gengo.1.startDate", "1926/12/25");
        addProperty("wareki.gengo.2.name", "大正");
        addProperty("wareki.gengo.2.roman", "T");
        addProperty("wareki.gengo.2.startDate", "1912/07/30");
        addProperty("wareki.gengo.3.name", "明治");
        addProperty("wareki.gengo.3.roman", "M");
        addProperty("wareki.gengo.3.startDate", "1868/09/04");
        addProperty("wareki.gengo.4.name", "平成");
        addProperty("wareki.gengo.4.roman", "H");
        addProperty("wareki.gengo.5.name", "平成");
        addProperty("wareki.gengo.5.roman", "H");
        addProperty("wareki.gengo.5.startDate", "asdf");
    }

    @Override
    protected void cleanUpData() throws Exception {
        clearProperty();
    }

    /**
     * testGetSystemTime01。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：なし<br>
     * 期待値：テスト実行時のシステム時刻<br>
     *
     * ・システム時刻が取得できることを確認する。<br>
     * 　結果確認ではテスト対象の内容と同じことをしている。<br>
     * @throws Exception 例外
     */
    public void testGetSystemTime01() throws Exception {
        // 入力値の設定
        // システム時刻の取得のため、入力なし

        // テスト対象の実行
        Date result = DateUtil.getSystemTime();

        // 結果確認
        Date hope = Calendar.getInstance().getTime();
        assertEquals(hope, result);
    }

    // ************************************************************************
    //  和暦変換用メソッドに関する確認
    // ************************************************************************

    // ************************************************************************
    //  元号のフォーマット「G」に関する確認
    // ************************************************************************

    /**
     * testDateToWarekiString01。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="G"<br>
     *         date="2001.01.01 00:00:00"<br>
     *
     * 期待値：date="H"<br>
     *
     * ・元号のフォーマットを「G」と1文字にし、元号の出力形式を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString01() throws Exception {
        // 入力値の設定
        String format = "G";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("H", str);
    }

    /**
     * testDateToWarekiString02。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="G"<br>
     * 　　　　currentTime="1980年1月1日 0時0分0秒"<br>
     * 期待値："S"<br>
     *
     * ・元号のフォーマットを「G」と1文字にし、元号の出力形式を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString02() throws Exception {
        // 入力値の設定
        String format = "G";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1980.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("S", str);
    }

    /**
     * testDateToWarekiString03。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："H"<br>
     *
     * ・元号のフォーマットを「GGG」と3文字にし、元号の出力形式を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString03() throws Exception {
        // 入力値の設定
        String format = "GGG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("H", str);
    }

    /**
     * testDateToWarekiString04。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："平成"<br>
     *
     * ・元号のフォーマットを「GGGG」と4文字にし、元号の出力形式を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString04() throws Exception {
        // 入力値の設定
        String format = "GGGG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成", str);
    }

    /**
     * testDateToWarekiString05。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGGGGGGGG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："平成"<br>
     *
     * ・元号のフォーマットを「GGGGGGGGGG」と10文字にし、
     * 元号の出力形式を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString05() throws Exception {
        // 入力値の設定
        String format = "GGGGGGGGGG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成", str);
    }

    // ************************************************************************
    //  年のフォーマット「y」に関する確認
    // ************************************************************************

    /**
     * testDateToWarekiString06。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="y"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："13"<br>
     *
     * ・元号のフォーマットを「y」と1文字にし、和暦年が出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString06() throws Exception {
        // 入力値の設定
        String format = "y";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("13", str);
    }

    /**
     * testDateToWarekiString07。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="y"<br>
     * 　　　　currentTime="1869年1月1日 0時0分0秒"<br>
     * 期待値："2"<br>
     *
     * ・元号のフォーマットを「y」と1文字にし、和暦年が出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString07() throws Exception {
        // 入力値の設定
        String format = "y";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1869.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("2", str);
    }

    /**
     * testDateToWarekiString08。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="yy"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："13"<br>
     *
     * ・元号のフォーマットを「yy」と2文字にし、和暦年が出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString08() throws Exception {
        // 入力値の設定
        String format = "yy";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("13", str);
    }

    /**
     * testDateToWarekiString09。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="yyyyyyyyyy"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："13"<br>
     *
     * ・元号のフォーマットを「yyyyyyyyyy」と10文字にし、和暦年が出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString09() throws Exception {
        // 入力値の設定
        String format = "yyyyyyyyyy";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("13", str);
    }

    // ************************************************************************
    //  曜日のフォーマット「E」に関する確認
    // ************************************************************************

    /**
     * testDateToWarekiString10。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="E"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："月"<br>
     *
     * ・曜日のフォーマットを「E」と1文字にした場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString10() throws Exception {
        // 入力値の設定
        String format = "E";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("月", str);
    }

    /**
     * testDateToWarekiString11。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="E"<br>
     * 　　　　currentTime="1868年9月3日 0時0分0秒"<br>
     * 期待値："木"<br>
     *
     * ・曜日のフォーマットを「E」と1文字にし、入力する日付が<br>
     * 明治最初の日の前日の場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString11() throws Exception {
        // 入力値の設定
        String format = "E";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("木", str);
    }

    /**
     * testDateToWarekiString12。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="EEE"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："月"<br>
     *
     * ・曜日のフォーマットを「EEE」と3文字にした場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString12() throws Exception {
        // 入力値の設定
        String format = "EEE";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("月", str);
    }

    /**
     * testDateToWarekiString13。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="EEEE"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："月曜日"<br>
     *
     * ・曜日のフォーマットを「EEEE」と4文字にした場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString13() throws Exception {
        // 入力値の設定
        String format = "EEEE";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("月曜日", str);
    }

    /**
     * testDateToWarekiString14。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="EEEEEEEEEE"<br>
     * 　　　　currentTime="2001年1月2日 0時0分0秒"<br>
     * 期待値："火曜日"<br>
     *
     * ・曜日のフォーマットを「EEEEEEEEEE」と10文字にした場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString14() throws Exception {
        // 入力値の設定
        String format = "EEEEEEEEEE";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.02 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("火曜日", str);
    }

    // ************************************************************************
    //  元号と年の出力順に関する確認
    // ************************************************************************

    /**
     * testDateToWarekiString15。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="Gy"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："H13"<br>
     *
     * ・フォーマットを「Gy」とし、元号(ローマ字)＋年の順に出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString15() throws Exception {
        // 入力値の設定
        String format = "Gy";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("H13", str);
    }

    /**
     * testDateToWarekiString16。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="yG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："13H"<br>
     *
     * ・フォーマットを「yG」とし、年＋元号(ローマ字)の順に出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString16() throws Exception {
        // 入力値の設定
        String format = "yG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("13H", str);
    }

    /**
     * testDateToWarekiString17。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGGy"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："平成13"<br>
     *
     * ・フォーマットを「GGGGy」とし、元号(漢字)＋年の順に出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString17() throws Exception {
        // 入力値の設定
        String format = "GGGGy";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成13", str);
    }

    /**
     * testDateToWarekiString18。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="yGGGG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："13平成"<br>
     *
     * ・フォーマットを「yGGGG」とし、年＋元号(漢字)の順に出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString18() throws Exception {
        // 入力値の設定
        String format = "yGGGG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("13平成", str);
    }

    /**
     * testDateToWarekiString19。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="G GGGG"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："H 平成"<br>
     *
     * ・フォーマットを「G GGGG」とし、元号(ローマ字)＋元号(漢字)の順に<br>
     * 出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString19() throws Exception {
        // 入力値の設定
        String format = "G GGGG";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("H 平成", str);
    }

    /**
     * testDateToWarekiString20。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGG G"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："平成 H"<br>
     *
     * ・フォーマットを「GGGG G」とし、元号(漢字)＋元号(ローマ字)の順に<br>
     * 出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString20() throws Exception {
        // 入力値の設定
        String format = "GGGG G";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成 H", str);
    }

    /**
     * testDateToWarekiString21。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGGG E"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："平成 月"<br>
     *
     * ・フォーマットを「GGGGG E」とし、元号(漢字)＋曜日の順に<br>
     * 出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString21() throws Exception {
        // 入力値の設定
        String format = "GGGGG E";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成 月", str);
    }

    /**
     * testDateToWarekiString22。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="MM.dd HH:mm:ss z"<br>
     * 　　　　currentTime="2001年2月1日 3時4分5秒"<br>
     * 期待値："02.01 03:04:05 JST"<br>
     *
     * ・y,G,Eを含まないフォーマットの場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString22() throws Exception {
        // 入力値の設定
        String format = "MM.dd HH:mm:ss z";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.02.01 03:04:05").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("02.01 03:04:05 " + this.getTimeZoneName(date), str);
    }

    /**
     * タイムゾーンの略名を取得。 SimpleDateFormatクラスと同様の方法でタイムゾーンの略名を取得している。
     *
     * @param dt 時刻データ
     * @return タイムゾーンの略名
     * @throws Exception 例外
     */
    private String getTimeZoneName(Date dt) throws Exception {
        Calendar calender = Calendar.getInstance();
        calender.setTime(dt);

        // DateFormatSymbols は、月、曜日、タイムゾーンデータなど、
        // 地域対応が可能な日付/時刻フォーマットデータをカプセル化するための public クラス。
        DateFormatSymbols formatData = new DateFormatSymbols(Locale
                .getDefault());
        // タイムゾーン文字列の取得
        String zoneStrings[][] = formatData.getZoneStrings();

        // zoneIndexの取得
        int zoneIndex = -1;
        String zoneID = calender.getTimeZone().getID();
        for (int index = 0; index < zoneStrings.length; index++) {
            if (zoneID.equalsIgnoreCase(zoneStrings[index][0])) {
                zoneIndex = index;
                break;
            }
        }

        // 特別の略名は存在しない時
        if (zoneIndex == -1) {
            return zoneID;
        }

        // 略名のインデックスの取得
        int index = -1;
        if(calender.get(Calendar.DST_OFFSET) == 0 ){
            index = 2;
        } else {
            index = 4;
        }

        return zoneStrings[zoneIndex][index];
    }

    // ************************************************************************
    //  入力が不正な場合の確認
    // ************************************************************************

    /**
     * testDateToWarekiString23。<br>
     *
     * (正常系)<br>
     * 観点：A,C<br>
     *
     * 入力値：format=""<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値：""<br>
     *
     * ・フォーマットを空文字にし、空文字が出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString23() throws Exception {
        // 入力値の設定
        String format = "";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("", str);
    }

    /**
     * testDateToWarekiString24。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="#!--0"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："#!--0"<br>
     *
     * ・フォーマットをパターン文字以外にし、<br>
     * そのまま出力されることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString24() throws Exception {
        // 入力値の設定
        String format = "#!--0";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("#!--0", str);
    }

    /**
     * testDateToWarekiString25。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="'GGGG' G 'dd' dd"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値："GGGG H dd 01"<br>
     *
     * ・フォーマットキャラクタをシングルクォーテーションで囲んだ場合、<br>
     * 文字列としてエスケープされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString25() throws Exception {
        // 入力値の設定
        String format = "'GGGG' G 'dd' dd";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("GGGG H dd 01", str);
    }

    /**
     * testDateToWarekiString26。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="'y' y 'E' E"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値：y 13 E 月<br>
     *
     * ・フォーマットキャラクタをシングルクォーテーションで囲んだ場合、<br>
     * "y"と"E"は文字列としてエスケープされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString26() throws Exception {
        // 入力値の設定
        String format = "'y' y 'E' E";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("y 13 E 月", str);
    }

    /**
     * testDateToWarekiString27。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：format="A"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値：IllegalArgumentExceptionがスローされる<br>
     *
     * ・フォーマット文字にない「A」を指定した場合、<br>
     * IllegalArgumentExceptionがスローされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString27() throws Exception {
        // 入力値の設定
        String format = "A";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.dateToWarekiString(format, date);
            fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * testDateToWarekiString28。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：format="G"<br>
     * 　　　　currentTime="1868年9月3日 0時0分0秒"<br>
     * 期待値：IllegalArgumentExceptionがスローされる<br>
     *
     * ・プロパティファイルで指定された最古日付以前の日付をdateで渡した場合、<br>
     * フォーマットに"G"を指定するとIllegalArgumentExceptionがスローされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString28() throws Exception {
        // 入力値の設定
        String format = "G";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.dateToWarekiString(format, date);
            fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * testDateToWarekiString29。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：format="y"<br>
     * 　　　　currentTime="1868年9月3日 0時0分0秒"<br>
     * 期待値：IllegalArgumentExceptionがスローされる<br>
     *
     * ・プロパティファイルで指定された最古日付以前の日付をdateで渡した場合、<br>
     * フォーマットに"y"を指定するとIllegalArgumentExceptionがスローされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString29() throws Exception {
        // 入力値の設定
        String format = "y";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.dateToWarekiString(format, date);
            fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * testDateToWarekiString30。<br>
     *
     * (異常系)<br>
     * 観点：C,G<br>
     *
     * 入力値：format=null<br>
     * 　　　　currentTime=*<br>
     * 期待値：NullPointerExceptionがスローされる<br>
     *
     * ・フォーマットにnullを指定した場合、<br>
     * NullPointerExceptionがスローされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString30() throws Exception {
        // 入力値の設定
        String format = null;
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.dateToWarekiString(format, date);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testDateToWarekiString31。<br>
     *
     * (異常系)<br>
     * 観点：C,G<br>
     *
     * 入力値：format=not null<br>
     * 　　　　currentTime=null<br>
     * 期待値：NullPointerExceptionがスローされる<br>
     *
     * ・日付にnullを指定した場合、<br>
     * NullPointerExceptionがスローされることを確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString31() throws Exception {
        // 入力値の設定
        String format = "G";
        date = null;

        // テスト対象の実行
        try {
            String str = DateUtil.dateToWarekiString(format, date);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * testDateToWarekiString32。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="G 'G' ''G'' '''G''' ''''G'''' y 'y' ''y'' '''y''' ''''y'''' E 'E' ''E'' '''E''' ''''E''''"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値：H G 'H' 'G' ''H'' 13 y '13' 'y' ''13'' 月 E '月' 'E' ''月''<br>
     *
     * ・フォーマットキャラクタをシングルクォーテーションで囲んだ場合、<br>
     * エスケープされることを確認する。<br>
     * ・フォーマットキャラクタをシングルクォーテーション2つで囲んだ場合、<br>
     * シングルクオーテーション自体が表示されることを確認する。<br>
     * ・和暦元号・曜日が省略表記の場合<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString32() throws Exception {
        // 入力値の設定
        String format = "G 'G' ''G'' '''G''' ''''G'''' y 'y' ''y'' '''y''' ''''y'''' E 'E' ''E'' '''E''' ''''E''''";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("H G 'H' 'G' ''H'' 13 y '13' 'y' ''13'' 月 E '月' 'E' ''月''", str);
    }

    /**
     * testDateToWarekiString33。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="GGGG 'GGGG' ''GGGG'' '''GGGG''' ''''GGGG'''' yyyy 'yyyy' ''yyyy'' '''yyyy''' ''''y'''' EEEE 'EEEE' ''EEEE'' '''EEEE''' ''''EEEE''''"<br>
     * 　　　　currentTime="2001年1月1日 0時0分0秒"<br>
     * 期待値：平成 GGGG '平成' 'GGGG' ''平成'' 13 yyyy '13' 'yyyy' ''13'' 月曜日 EEEE '月曜日' 'EEEE' ''月曜日''<br>
     *
     * ・フォーマットキャラクタをシングルクォーテーションで囲んだ場合、<br>
     * エスケープされることを確認する。<br>
     * ・フォーマットキャラクタをシングルクォーテーション2つで囲んだ場合、<br>
     * シングルクオーテーション自体が表示されることを確認する。<br>
     * ・和暦元号・曜日が完全表記の場合<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString33() throws Exception {
        // 入力値の設定
        String format = "GGGG 'GGGG' ''GGGG'' '''GGGG''' ''''GGGG'''' yyyy 'yyyy' ''yyyy'' '''yyyy''' ''''y'''' EEEE 'EEEE' ''EEEE'' '''EEEE''' ''''EEEE''''";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.01.01 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("平成 GGGG '平成' 'GGGG' ''平成'' 13 yyyy '13' 'yyyy' ''13'' 月曜日 EEEE '月曜日' 'EEEE' ''月曜日''", str);
    }

    /**
     * testDateToWarekiString34。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：format="'MM.dd HH:mm:ss z'"<br>
     * 　　　　currentTime="2001年2月1日 3時4分5秒"<br>
     * 期待値："MM.dd HH:mm:ss z"<br>
     *
     * ・y,G,Eを含まないフォーマットをシングルクォーテーションで囲んだ場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testDateToWarekiString34() throws Exception {
        // 入力値の設定
        String format = "'MM.dd HH:mm:ss z'";
        // 時刻の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("2001.02.01 03:04:05").getTime());

        // テスト対象の実行
        String str = DateUtil.dateToWarekiString(format, date);

        // 結果確認
        assertEquals("MM.dd HH:mm:ss z", str);
    }

    // ************************************************************************
    //  getWarekiGengoName
    // ************************************************************************

    /**
     * testGetWarekiGengoName01。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分0秒<br>
     * 期待値："平成"<br>
     *
     * ・平成最初の日の午前0時ちょうどを確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoName01() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoName(date);

        // 結果確認
        assertEquals("平成", str);
    }

    /**
     * testGetWarekiGengoName02。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月7日23時59分59秒<br>
     * 期待値："昭和"<br>
     *
     * ・平成最初の日の午前0時の1秒前を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoName02() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.07 23:59:59").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoName(date);

        // 結果確認
        assertEquals("昭和", str);
    }

    /**
     * testGetWarekiGengoName03。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分1秒<br>
     * 期待値："平成"<br>
     *
     * ・平成最初の日の午前0時の1秒後を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoName03() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:01").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoName(date);

        // 結果確認
        assertEquals("平成", str);
    }

    /**
     * testGetWarekiGengoName04。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=1868年9月3日<br>
     * 期待値：IllegalArgumentException<br>
     *
     * ・入力する日付が明治最初の日の前日の場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoName04() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.getWarekiGengoName(date);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Wareki Gengo Name not found for "
                    + date, e.getMessage());
            return;
        }
    }

    /**
     * testGetWarekiGengoName05。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=null<br>
     * 期待値：NullPointerExceptionがスローされる<br>
     *
     * ・入力する日付がnullの時、NullPointerExceptionが発生する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoName05() throws Exception {
        // 入力値の設定
        date = null;

        // テスト対象の実行
        try {
            String str = DateUtil.getWarekiGengoName(date);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    // ************************************************************************
    //  getWarekiGengoRoman
    // ************************************************************************

    /**
     * testGetWarekiGengoRoman01。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分0秒<br>
     * 期待値："平成"<br>
     *
     * ・平成最初の日の午前0時ちょうどを確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoRoman01() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:00").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoRoman(date);

        // 結果確認
        assertEquals("H", str);
    }

    /**
     * testGetWarekiGengoRoman02。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月7日23時59分59秒<br>
     * 期待値："昭和"<br>
     *
     * ・平成最初の日の午前0時の1秒前を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoRoman02() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.07 23:59:59").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoRoman(date);

        // 結果確認
        assertEquals("S", str);
    }

    /**
     * testGetWarekiGengoRoman03。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分1秒<br>
     * 期待値："平成"<br>
     *
     * ・平成最初の日の午前0時の1秒後を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoRoman03() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:01").getTime());

        // テスト対象の実行
        String str = DateUtil.getWarekiGengoRoman(date);

        // 結果確認
        assertEquals("H", str);
    }

    /**
     * testGetWarekiGengoRoman04。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=1868年9月3日<br>
     * 期待値：IllegalArgumentExceptionがスローされる<br>
     *
     * ・入力する日付が明治最初の日の前日の場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoRoman04() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        try {
            String str = DateUtil.getWarekiGengoRoman(date);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Wareki Gengo Roman not found for "
                    + date, e.getMessage());
            return;
        }
    }

    /**
     * testGetWarekiGengoRoman05。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=null<br>
     * 期待値：NullPointerExceptionがスローされる<br>
     *
     * ・入力する日付がNullの時、NullPointerExceptionが発生する<br>
     * @throws Exception 例外
     */
    public void testGetWarekiGengoRoman05() throws Exception {
        // 入力値の設定
        date = null;

        // テスト対象の実行
        try {
            String str = DateUtil.getWarekiGengoRoman(date);
            fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    // ************************************************************************
    //  getWarekiGengoYear
    // ************************************************************************

    /**
     * testGetWarekiYear01。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分0秒<br>
     * 期待値：1<br>
     *
     * ・平成最初の日の午前0時ちょうどを確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiYear01() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:00").getTime());

        // テスト対象の実行
        int year = DateUtil.getWarekiYear(date);

        // 結果確認
        assertEquals(1, year);
    }

    /**
     * testGetWarekiYear02。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月7日23時59分59秒<br>
     * 期待値：64<br>
     *
     * ・平成最初の日の午前0時の1秒前を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiYear02() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.07 23:59:59").getTime());

        // テスト対象の実行
        int year = DateUtil.getWarekiYear(date);

        // 結果確認
        assertEquals(64, year);
    }

    /**
     * testGetWarekiYear03。<br>
     *
     * (正常系)<br>
     * 観点：A<br>
     *
     * 入力値：date=1989年1月8日0時0分1秒<br>
     * 期待値：1<br>
     *
     * ・平成最初の日の午前0時の1秒後を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiYear03() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1989.01.08 00:00:01").getTime());

        // テスト対象の実行
        int year = DateUtil.getWarekiYear(date);

        // 結果確認
        assertEquals(1, year);
    }

    /**
     * testGetWarekiYear04。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=1868年9月3日<br>
     * 期待値：IllegalArgumentExceptionがスローされる<br>
     *
     * ・入力する日付が明治最初の日の前日の場合を確認する。<br>
     * @throws Exception 例外
     */
    public void testGetWarekiYear04() throws Exception {
        // 入力値の設定
        df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = new Date(df.parse("1868.09.03 00:00:00").getTime());

        // テスト対象の実行
        try {
            int year = DateUtil.getWarekiYear(date);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Wareki Gengo not found for "
                    + date, e.getMessage());
            return;
        }
    }

    /**
     * testGetWarekiYear05。<br>
     *
     * (異常系)<br>
     * 観点：G<br>
     *
     * 入力値：date=null<br>
     * 期待値：NullPointerExceptionがスローされる<br>
     *
     * ・NullPointerExceptionが発生する<br>
     * @throws Exception 例外
     */
    public void testGetWarekiYear05() throws Exception {
        // 入力値の設定

        try {
            // テスト対象の実行
            int year = DateUtil.getWarekiYear(null);
            fail();
        } catch (NullPointerException e) {
            return;
        }

    }

}
