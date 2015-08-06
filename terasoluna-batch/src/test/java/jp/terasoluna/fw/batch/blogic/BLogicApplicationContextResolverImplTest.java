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

package jp.terasoluna.fw.batch.blogic;

import jp.terasoluna.fw.batch.executor.B000001BLogic;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.spel.SpelEvaluationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * {@code BLogicApplicationContextResolverImpl}のテストケースクラス。
 *
 * @since 3.6
 */
public class BLogicApplicationContextResolverImplTest {

    private ApplicationContext parent = new ClassPathXmlApplicationContext(
            "beansDef/AdminContext.xml", "beansDef/AdminDataSource.xml");

    private BLogicApplicationContextResolverImpl target;

    @Before
    public void setUp() {
        target = new BLogicApplicationContextResolverImpl();
    }

    /**
     * setApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・親の{@code ApplicationContext}が設定されていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSetApplicationContext() throws Exception {

        // テスト実行
        target.setApplicationContext(parent);

        assertSame(parent, target.parent);
    }

    /**
     * resolveApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・業務DIコンテナである{@code ApplicationContext}が返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext01() throws Exception {
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("B000001");

        target.setApplicationContext(parent);
        target.classpath = "beansDef/";

        // テスト実行
        ApplicationContext ctx = target.resolveApplicationContext(batchJobData);

        // 業務DIコンテナのBeanが取得できること。
        B000001BLogic blogic = ctx
                .getBean("B000001BLogic", B000001BLogic.class);
        assertNotNull(blogic);

        // 親のDIコンテナのBeanが取得できること。
        MessageAccessor msg = ctx.getBean("msgAcc", MessageAccessor.class);
        assertNotNull(msg);
    }

    /**
     * resolveApplicationContext()のテスト 【異常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・Bean定義ファイルが生成できないジョブ業務コードを指定したとき、
     * 　{@code BeansException}をスローすること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext02() throws Exception {
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("not-defined");

        target.setApplicationContext(parent);
        target.classpath = "beansDef/";

        // テスト実行
        try {
            target.resolveApplicationContext(batchJobData);
            fail();
        } catch (BeansException e) {
            assertTrue(e.getMessage().contains(
                    "IOException parsing XML document from class path resource [beansDef/not-defined.xml]"));
        }
    }

    /**
     * resolveApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・フレームワークのDIコンテナ自身に親コンテナが無い場合、
     * 　業務DIコンテナである{@code ApplicationContext}の親コンテナは
     * 　フレームワークのDIコンテナと同一になること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext03() throws Exception {
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("B000001");

        target.setApplicationContext(parent);
        target.classpath = "beansDef/";

        // テスト実行
        ApplicationContext ctx = target.resolveApplicationContext(batchJobData);

        // 業務用DIコンテナの親コンテナが、フレームワークのDIコンテナと同一であること
        assertSame(parent, ctx.getParent());
    }

    /**
     * resolveApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・フレームワークのDIコンテナ自身に親コンテナを持つ場合、
     * 　業務DIコンテナである{@code ApplicationContext}の親コンテナは
     * 　フレームワークの親コンテナと同一になること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext04() throws Exception {
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("B000001");
        // フレームワークのDIコンテナ（parentを親にもつ）
        ApplicationContext fwChildCtx = new ClassPathXmlApplicationContext(
                parent);
        target.setApplicationContext(fwChildCtx);
        target.classpath = "beansDef/";

        // テスト実行
        ApplicationContext ctx = target.resolveApplicationContext(batchJobData);

        // 業務用DIコンテナの親コンテナが、フレームワークのDIコンテナ(fwChildCtx)
        // の親コンテナ(parent)と同一であること
        assertSame(parent, ctx.getParent());
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・classpathプロパティがnullのときディレクトリ指定がなく、
     * 　ファイル名のみ{@code jobAppCd + ".xml"}というファイルパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName01() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("jobAppCd");
        }};
        target.classpath = null;

        // テスト実行
        assertEquals("jobAppCd.xml", target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・classpathプロパティが空文字のときディレクトリ指定がなく、
     * 　ファイル名のみ{@code jobAppCd + ".xml"}というファイルパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName02() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("jobAppCd");
        }};
        target.classpath = "";

        // テスト実行
        assertEquals("jobAppCd.xml", target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・classpathプロパティが空文字のときディレクトリ指定がなく、
     * 　ジョブ業務コードがnullのとき、".xml"というファイルパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName03() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd(null);
        }};
        target.classpath = "";

        // テスト実行
        assertEquals(".xml", target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・classpathプロパティが"classpath/"のとき、
     * 　"{@code classpath/jobAppCd.xml}というファイルパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName04() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("jobAppCd");
        }};
        target.classpath = "classpath/";

        // テスト実行
        assertEquals("classpath/jobAppCd.xml",
                target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・classpathプロパティが"classpath/${jobAppCd}/"、ジョブ業務コードが"B000001"のとき、
     * 　"{@code classpath/B0000001/B0000001.xml}というファイルパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName05() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("B0000001");
        }};
        target.classpath = "classpath/${jobAppCd}/";

        // テスト実行
        assertEquals("classpath/B0000001/B0000001.xml",
                target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【異常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・EL式で存在しないプロパティ名を明示した場合に{@code SpelEvaluationException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName06() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("B0000001");
        }};
        target.classpath = "classpath/${noDefProperty}/";

        // テスト実行
        try {
            target.getBeanFileName(batchJobData);
            fail();
        } catch (SpelEvaluationException e) {
            assertTrue(e.getMessage().contains(
                    "EL1008E:(pos 0): Property or field 'noDefProperty' cannot be found on object of type 'jp.terasoluna.fw.batch.blogic.BLogicApplicationContextResolverImplTest"));
        }
    }

    /**
     * getBeanFileName()のテスト 【異常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・EL式として不正なプロパティ名を明示した場合に{@code ParseException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName07() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("B0000001");
        }};
        target.classpath = "classpath/${jobAppCd/";

        // テスト実行
        try {
            target.getBeanFileName(batchJobData);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[EAL025090] invalid format in batch.properties, key: beanDefinition.business.classpath",
                    e.getMessage());
        }
    }

    /**
     * getBeanFileName()のテスト 【異常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・引数の{@code BatchJobData}が{@code null}であるとき、
     * 　{@code IllegalArgumentException}がスローされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName08() throws Exception {
        target.classpath = "classpath/";

        // テスト実行
        try {
            target.getBeanFileName(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "[Assertion failed] - this argument is required; it must not be null",
                    e.getMessage());
        }
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・引数に${jobAppCdUpper}が含まれているとき、プロパティが大文字化された
     * 　ディレクトリパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName09() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("to_upper");
        }};
        target.classpath = "classpath/${jobAppCdUpper}/";

        // テスト実行
        assertEquals("classpath/TO_UPPER/to_upper.xml",
                target.getBeanFileName(batchJobData));
    }

    /**
     * getBeanFileName()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・引数に${jobAppCdLower}が含まれているとき、プロパティが小文字化された
     * 　ディレクトリパスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testGetBeanFileName10() throws Exception {
        BatchJobData batchJobData = new BatchJobData() {{
            setJobAppCd("TO_LOWER");
        }};
        target.classpath = "classpath/${jobAppCdLower}/";

        // テスト実行
        assertEquals("classpath/to_lower/TO_LOWER.xml",
                target.getBeanFileName(batchJobData));
    }

    /**
     * closeApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・子のDIコンテナがクローズされ、親DIコンテナからはBeanが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testCloseApplicationContext01() throws Exception {
        ApplicationContext child = new ClassPathXmlApplicationContext(
                new String[] { "beansDef/B000001.xml" }, parent);

        // テスト実行
        target.closeApplicationContext(child);

        // 子のDIコンテナにアクセスした場合、IllegalStateExceptionが発生すること。
        try {
            child.containsBean("B000001BLogic");
            fail();
        } catch (IllegalStateException e) {
        }

        // 親DIコンテナのBeanは取得可能であること。
        assertTrue(parent.containsBean("msgAcc"));
    }

    /**
     * closeApplicationContext()のテスト 【正常系】
     * <pre>
     * 事前条件
     * ・なし
     * 確認項目
     * ・{@code AbstractApplicationContext}ではないコンテキストを指定した場合
     * 　例外が発生しないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testCloseApplicationContext02() throws Exception {
        ApplicationContext child = mock(ApplicationContext.class);

        // テスト実行
        target.closeApplicationContext(child);
    }
}
