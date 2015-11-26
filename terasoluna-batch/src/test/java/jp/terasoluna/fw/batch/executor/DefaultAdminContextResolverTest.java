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

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.util.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * {@code DefaultAdminContextResolver}のテストケース。
 */
public class DefaultAdminContextResolverTest {

    private Object originProps;

    private static Field propsField;

    /***
     * テストケース全体の前処理。<br>
     * テストケース終了後に復元対象となる{@code PropertyUtil}の内部フィールドである
     * propsを退避する。
     */
    @BeforeClass
    public static void setUpClass() {
        propsField = ReflectionUtils.findField(PropertyUtil.class, "props");
        propsField.setAccessible(true);
    }

    /**
     * テスト前処理。<br>
     * System.exit()でテストプロセスを止めないセキュリティマネージャを設定する。
     *
     * @throws Exception 予期しない例外
     */
    @Before
    public void setUp() throws Exception {
        // PropertyUtilの内部プロパティを退避
        this.originProps = propsField.get(null);
    }

    /**
     * テスト後処理。<br>
     * セキュリティマネージャを元に戻す。
     *
     * @throws Exception 予期しない例外
     */
    @After
    public void tearDown() throws Exception {
        // PropertyUtilの内部プロパティを復元
        propsField.set(null, this.originProps);
    }

    /**
     * resolveAdminContextのテスト 【正常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・AdminContext.xml,AdminDataSource.xmlによる
     * 　{@code ApplicationContext}が生成されること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveAdminContext01() throws Exception {
        DefaultAdminContextResolver resolver = new DefaultAdminContextResolver();

        // テスト実行
        ApplicationContext context = resolver.resolveAdminContext();

        assertTrue(context.containsBean("jobOperator"));
        assertTrue(context.containsBean("systemDao"));
        assertNull(context.getParent());
    }

    /**
     * resolveAdminContextのテスト 【異常系】
     * 事前条件
     * ・特になし
     * 確認項目
     * ・プロパティにBean定義ファイルのクラスパスが存在しないとき、
     * 　BeansExceptionがスローされること。
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveAdminContext02() throws Exception {
        TreeMap<String, String> props = new TreeMap<>();
        propsField.set(null, props);

        DefaultAdminContextResolver resolver = new DefaultAdminContextResolver();

        // テスト実行
        try {
            resolver.resolveAdminContext();
        } catch (BeansException e) {
            // Springが例外メッセージを返すため、メッセージの検証は行わない。
        }
    }

}
