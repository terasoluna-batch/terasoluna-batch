/*
 * Copyright (c) 2015 NTT DATA Corporation
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

import jp.terasoluna.fw.util.PropertyTestCase;
import jp.terasoluna.utlib.UTUtil;

import java.util.Date;
import java.util.Map;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DateUtil ブラックボックステスト。<br>
 * staticイニシャライザの動作をテスト対象としているため、 他のメソッドが実行されるDateUtilTest01とはテストケースを分割している。
 */
public class DateUtilTest02 extends PropertyTestCase {

    private TestLogger logger = TestLoggerFactory.getTestLogger(DateUtil.class);

    @Before
    public void setUpData() throws Exception {
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

    @After
    public void cleanUpData() throws Exception {
        clearProperty();
    }

    /**
     * testStatic01() <br>
     * <br>
     * (正常系) <br>
     * 観点：A <br>
     * <br>
     * 入力値：(状態) プロパティ:wareki.gengo.0.name = 平成<br>
     * wareki.gengo.0.roman = H<br>
     * wareki.gengo.0.startDate = 1989/01/08<br>
     * wareki.gengo.1.name = 昭和<br>
     * wareki.gengo.1.roman = S<br>
     * wareki.gengo.1.startDate = 1926/12/25<br>
     * wareki.gengo.2.name = 大正<br>
     * wareki.gengo.2.roman = T<br>
     * wareki.gengo.2.startDate = 1912/07/30<br>
     * wareki.gengo.3.name = 明治<br>
     * wareki.gengo.3.roman = M<br>
     * wareki.gengo.3.startDate = 1868/09/04<br>
     * wareki.gengo.4.name = 平成<br>
     * wareki.gengo.4.roman = H<br>
     * wareki.gengo.5.name = 平成<br>
     * wareki.gengo.5.roman = H<br>
     * wareki.gengo.5.startDate = asdf<br>
     * <br>
     * 期待値：(状態変化) プライベートフィールド:プライベートフィールドである「GENGO_NAME」「GENGO_ROMAN」「GENGO_BEGIN_DATES」「GENGO_BEGIN_YEARS」のサイズが４であること。<br>
     * (状態変化) ログ:<errorレベル><br>
     * メッセージ：wareki.gengo.4.startDate not found<br>
     * <errorレベル><br>
     * メッセージ：Unparseable date: "asdf"<br>
     * <br>
     * すべてのパターンを網羅するテスト <br>
     * @throws Exception このメソッドで発生した例外
     */
    @Test
    public void testStatic01() throws Exception {

        // 結果確認
        // プライベートフィールドの件数が4件であることを確認する。
        Map GENGO_NAME = (Map) UTUtil.getPrivateField(DateUtil.class,
                "GENGO_NAME");
        Map GENGO_ROMAN = (Map) UTUtil.getPrivateField(DateUtil.class,
                "GENGO_ROMAN");
        Date[] GENGO_BEGIN_DATES = (Date[]) UTUtil.getPrivateField(
                DateUtil.class, "GENGO_BEGIN_DATES");
        int[] GENGO_BEGIN_YEARS = (int[]) UTUtil.getPrivateField(DateUtil.class,
                "GENGO_BEGIN_YEARS");
        assertEquals(4, GENGO_NAME.size());
        assertEquals(4, GENGO_ROMAN.size());
        assertEquals(4, GENGO_BEGIN_DATES.length);
        assertEquals(4, GENGO_BEGIN_YEARS.length);
        assertTrue(logger.getLoggingEvents().get(4).equals(error(
                "wareki.gengo.4.startDate not found")));
        assertTrue(logger.getLoggingEvents().get(5).equals(error(
                "wareki.gengo.4.startDate not found")));
        assertTrue(logger.getLoggingEvents().get(6).equals(error(
                "Unparseable date: \"asdf\"")));
    }
}
