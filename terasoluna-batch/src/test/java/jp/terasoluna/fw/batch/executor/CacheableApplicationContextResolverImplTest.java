/*
 * Copyright (c) 2016 NTT DATA Corporation
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

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * {@code CacheableApplicationContextResolverImpl}のテストケース
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/AdminContext_CacheableApplicationContextResolverImplTest.xml")
public class CacheableApplicationContextResolverImplTest {

    @Autowired
    private CacheableApplicationContextResolverImpl applicationContextResolver;

    @Autowired
    private CacheManager cacheManager;

    /**
     * ロガー
     */
    private static final TestLogger logger = TestLoggerFactory.getTestLogger(
            CacheableApplicationContextResolverImpl.class);

    /**
     * テスト前処理
     * @throws Exception 予期しない例外
     */
    @Before
    public void setUp() throws Exception {
        cacheManager.getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY)
                .clear();
        logger.clear();
    }

    /**
     * testSetCacheManager01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・内部フィールドにcacheManagerが設定されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testSetCacheManager01() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        CacheManager cacheManager = mock(CacheManager.class);

        // テスト実行
        target.setCacheManager(cacheManager);

        assertThat(target.cacheManager, is(cacheManager));
    }

    /**
     * testResolveApplicationContext01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・Spring cache abstractionによるキャッシュ機構が使用可能なインスタンスを使用し、
     *   事前に内部キャッシュがクリアされていること。
     * 確認項目
     * ・1度目に業務コンテキストが生成の後返却され、
     *   2度目にキャッシュされたコンテキストが返却されること。
     *   この後ジョブ業務コードの異なる別の業務コンテキストをキャッシュさせた場合、
     *   ジョブ業務コードをキーとしてそれぞれ異なるキャッシュインスタンスが取得できること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext01() throws Exception {
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("B000001");

        // テスト実行（１回目）
        ApplicationContext blogicContextFirst = applicationContextResolver
                .resolveApplicationContext(batchJobData);
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025019] BLogic context will be cached. jobAppCd:B000001"))));
        logger.clear();

        // テスト実行（２回目）
        ApplicationContext blogicContextSecond = applicationContextResolver
                .resolveApplicationContext(batchJobData);

        // 業務コンテキスト生成ログが出力されないこと。
        assertFalse(logger.getLoggingEvents().contains(info(
                "[IAL025019] BLogic context will be cached. jobAppCd:B000001")));
        logger.clear();

        // キャッシュされたコンテキストとインスタンスが同一であること。
        assertThat(blogicContextSecond, is(blogicContextFirst));

        batchJobData.setJobAppCd("B000002");

        // テスト実行（別の業務コンテキストを生成 １回目）
        ApplicationContext anotherContextFirst = applicationContextResolver
                .resolveApplicationContext(batchJobData);
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025019] BLogic context will be cached. jobAppCd:B000002"))));
        logger.clear();

        // テスト実行（別の業務コンテキストを生成 ２回目）
        ApplicationContext anotherContextSecond = applicationContextResolver
                .resolveApplicationContext(batchJobData);
        // 業務コンテキスト生成ログが出力されないこと。
        assertFalse(logger.getLoggingEvents().contains(info(
                "[IAL025019] BLogic context will be cached. jobAppCd:B000002")));

        // キャッシュされたコンテキストとインスタンスが同一であること。
        assertThat(anotherContextSecond, is(anotherContextFirst));

        // キャッシュされたB000001とB000002のコンテキストのインスタンスが異なっていること。
        assertThat(blogicContextSecond, not(anotherContextSecond));
    }

    /**
     * testResolveApplicationContext02 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・Spring cache abstractionによるキャッシュ機構を使用していない。(インスタンスをnewで生成)
     * 確認項目
     * ・1度目に業務コンテキストが生成の後返却される際、INFOログが出力されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testResolveApplicationContext02() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        BatchJobData batchJobData = new BatchJobData();
        batchJobData.setJobAppCd("B000001");
        target.classpath = "beansDef/";

        // テスト実行
        target.resolveApplicationContext(batchJobData);
        // 業務コンテキスト生成ログが出力されること。
        assertThat(logger.getLoggingEvents(), is(asList(info(
                "[IAL025019] BLogic context will be cached. jobAppCd:B000001"))));
    }

    /**
     * testCloseApplicationContext01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・Spring cache abstractionによるキャッシュ機構を使用している。
     * 　（CacheManagerフィールドを持ち、businessContextのキャッシュが可能な状態）
     * 確認項目
     * ・ApplicationContextがクローズされないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testCloseApplicationContext01() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beansDef/B000001.xml");

        // テスト実行
        applicationContextResolver.closeApplicationContext(ctx);

        // コンテキストがクローズされていない⇒コンテキスト内部のBeanが取り出し可能であること。
        assertThat(ctx.getBean("B000001BLogic"), is(notNullValue()));

    }

    /**
     * testCloseApplicationContext02 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・Spring cache abstractionによるキャッシュ機構を使用していない。
     * 確認項目
     * ・ApplicationContextがクローズされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testCloseApplicationContext02() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beansDef/B000001.xml");
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();

        // テスト実行
        target.closeApplicationContext(ctx);

        // コンテキストがクローズされている⇒コンテキスト内部のBeanを取り出した場合、ビジネスロジックが取り出せること。
        assertNotNull(ctx.getBean("B000001BLogic"));
    }

    /**
     * testAfterPropertiesSet01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・特になし。
     * 確認項目
     * ・共通コンテキストのパスが未設定の場合、親DIコンテナが生成されないこと。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testAfterPropertiesSet01() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();

        // テスト実行
        try {
            target.afterPropertiesSet();
            fail();
        } catch (BeanCreationException e) {
            assertThat(e.getMessage(), is(
                    "[EAL025061] Cannot create CacheableApplicationContextResolverImpl, because either cacheManager is not injected or Cache instance is not found by key:businessContext"));
        }
    }

    /**
     * testDestroy01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ対象の業務コンテキストがフィールドに存在している。
     * 確認項目
     * ・業務コンテキストがクローズされていること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDestroy01() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        final ApplicationContext b000001 = new ClassPathXmlApplicationContext("beansDef/B000001.xml");
        final ApplicationContext b000002 = new ClassPathXmlApplicationContext("beansDef/B000002.xml");

        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = -6836804456272606019L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();

        Cache cache = mock(Cache.class);
        doReturn(new HashMap<String, ApplicationContext>() {
            private static final long serialVersionUID = -8224529649381655075L;

            {
                put("B000001", b000001);
                put("B000002", b000002);
            }
        }).when(cache).getNativeCache();

        doReturn(cache).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
        target.cacheManager = cacheManager;

        // テスト実行
        target.destroy();

        verify(cache).clear();

        // 業務コンテキストの全てがクローズ済みであること。
        try {
            b000001.getBean("B000001BLogic");
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is(
                    "BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext"));
        }

        try {
            b000002.getBean("B000002BLogic");
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is(
                    "BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext"));
        }
    }

    /**
     * testDestroyCachedContext01 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されているが、キャッシュは空。
     * 確認項目
     * ・何も行われないこと。(キャッシュのクリアはよばれること。)
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDestroyCachedContext01() throws Exception {
        CacheableApplicationContextResolverImpl target = spy(
                new CacheableApplicationContextResolverImpl());
        Cache cache = mock(Cache.class);
        doReturn(new HashMap<String, ApplicationContext>()).when(cache)
                .getNativeCache();

        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(cache).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = 2574172591143649628L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();
        target.setCacheManager(cacheManager);

        // テスト実行
        target.destroyCachedContext();

        verify(target, never()).closeApplicationContext(any(
                ApplicationContext.class));
        verify(cache).clear();
    }

    /**
     * testDestroyCachedContext02 【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されており、
     * 　ApplicationContextとそれ以外のオブジェクトが含まれているとき、
     * 　ApplicationContextのみクローズされること。
     * 確認項目
     * ・キャッシュされているApplicationContextのみがクローズされること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testDestroyCachedContext02() throws Exception {
        final ApplicationContext b000001 = new ClassPathXmlApplicationContext("beansDef/B000001.xml");
        final ApplicationContext b000002 = new ClassPathXmlApplicationContext("beansDef/B000002.xml");

        CacheableApplicationContextResolverImpl target = spy(
                new CacheableApplicationContextResolverImpl());
        Cache cache = mock(Cache.class);
        doReturn(new HashMap<String, Object>() {
            private static final long serialVersionUID = 2574172591143649628L;

            {
                put("B000001", b000001);
                put("B000002", b000002);
                put("another", "anotherObject");
            }
        }).when(cache).getNativeCache();
        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(cache).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = 4460457695974083597L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();
        target.setCacheManager(cacheManager);

        // テスト実行
        target.destroyCachedContext();

        // 業務コンテキストの全てがクローズ済みであること。
        try {
            b000001.getBean("B000001BLogic");
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is(
                    "BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext"));
        }

        try {
            b000002.getBean("B000002BLogic");
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is(
                    "BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext"));
        }

        verify(cache).clear();
    }

    /**
     * testIsCacheEnabled01【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されていない。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testIsCacheEnabled01() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();

        // テスト実行
        assertThat(target.isCacheEnabled(), is(false));
    }

    /**
     * testIsCacheEnabled02【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されているが、blogicContextというキャッシュ名を含んでいない。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testIsCacheEnabled02() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(new ArrayList<String>()).when(cacheManager).getCacheNames();
        target.setCacheManager(cacheManager);

        // テスト実行
        assertThat(target.isCacheEnabled(), is(false));
    }

    /**
     * testIsCacheEnabled03【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されているが、キャッシュオブジェクトがnull。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testIsCacheEnabled03() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = -5780661534958374434L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();
        doReturn(null).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);

        target.setCacheManager(cacheManager);

        // テスト実行
        assertThat(target.isCacheEnabled(), is(false));
    }

    /**
     * testIsCacheEnabled04【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されているが、キャッシュオブジェクトがMapではない。
     * 確認項目
     * ・falseが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testIsCacheEnabled04() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = -5780661534958374434L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();
        Cache cache = mock(Cache.class);
        doReturn("not Map").when(cache).getNativeCache();
        doReturn(cache).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);

        target.setCacheManager(cacheManager);

        // テスト実行
        assertThat(target.isCacheEnabled(), is(false));
    }

    /**
     * testIsCacheEnabled05【正常系】
     * 
     * <pre>
     * 事前条件
     * ・事前にキャッシュ機能(CacheManager)が設定されており、キャッシュオブジェクトとしてMapを設定している。
     * 確認項目
     * ・trueが返却されること。
     * </pre>
     *
     * @throws Exception 予期しない例外
     */
    @Test
    public void testIsCacheEnabled05() throws Exception {
        CacheableApplicationContextResolverImpl target = new CacheableApplicationContextResolverImpl();
        CacheManager cacheManager = mock(CacheManager.class);
        doReturn(new ArrayList<String>() {
            private static final long serialVersionUID = -5780661534958374434L;

            {
                add(CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);
            }
        }).when(cacheManager).getCacheNames();
        Cache cache = mock(Cache.class);
        doReturn(new HashMap<String, Object>()).when(cache).getNativeCache();
        doReturn(cache).when(cacheManager).getCache(
                CacheableApplicationContextResolverImpl.BLOGIC_CONTEXT_CACHE_KEY);

        target.setCacheManager(cacheManager);

        // テスト実行
        assertThat(target.isCacheEnabled(), is(true));
    }
}
