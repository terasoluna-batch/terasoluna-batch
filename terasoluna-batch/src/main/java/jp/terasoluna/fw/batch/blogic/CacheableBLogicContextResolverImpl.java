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

import jp.terasoluna.fw.batch.constants.LogId;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.logger.TLogger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.Map;

/**
 * 業務コンテキストのキャッシュと、業務コンテキストの親として共通コンテキストを
 * 指定可能にする{@code BLogicApplicationContextResolver}実装。
 * 非同期バッチ起動を行い同じジョブを繰り返し実行する場合、業務コンテキストのキャッシュによる性能向上が見込まれる。
 * <p>
 * 本機能ではSpring Cache Abstractionを用い、コンテナ内部でジョブ業務コードをキーとした
 * 業務コンテキストのキャッシュを行う。
 *
 * このため、業務コンテキストのキャッシュを使用するためには、Bean定義ファイル内に
 * {@code <cache:annotation-driven/>}の指定と、{@code CacheManager}の
 * 定義・インジェクションが必要となる。
 * </p>
 * <p>
 * Bean定義ファイルの記述例：
 * <code><pre>
 * &lt;!-- cache名前空間のXMLスキーマ定義を追加 -->
 * &lt;beans xmlns=&quot;http://www.springframework.org/schema/beans&quot;
 *           xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *           xmlns:cache=&quot;http://www.springframework.org/schema/cache&quot;
 *           xsi:schemaLocation=&quot;http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
 *           http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd&quot;&gt;
 *   (略)
 *   &lt;!-- Spring Cache Abstraction機能の使用宣言 --&gt;
 *   &lt;cache:annotation-driven /&gt;
 *
 *   &lt;bean id=&quot;cacheManager&quot; class=&quot;org.springframework.cache.support.SimpleCacheManager&quot;&gt;
 *     &lt;property name=&quot;caches&quot;&gt;
 *       &lt;set&gt;
 *         &lt;bean class=&quot;org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean&quot;&gt;
 *           &lt;!-- 業務コンテキストのキャッシュ名はbusinessContext固定 --&gt;
 *           &lt;property name=&quot;name&quot; value=&quot;businessContext&quot;/&gt;
 *         &lt;/bean&gt;
 *       &lt;/set&gt;
 *     &lt;/property&gt;
 *   &lt;/bean&gt;
 *
 *   &lt;bean id=&quot;blogicContextResolver&quot; class=&quot;jp.terasoluna.fw.batch.blogic.CacheableBLogicContextResolverImpl&quot;&gt;
 *     &lt;!-- 共通コンテキストを業務コンテキストの親とする場合、commonContextClassPathでBean定義ファイルのクラスパスを記述する。(複数指定時はカンマ区切り) --&gt;
 *     &lt;property name=&quot;commonContextClassPath&quot; value=&quot;beansDef/commonContext.xml,beansDef/dataSource.xml&quot;/&gt;
 *     &lt;!-- cacheManagerのsetter-injection --&gt;
 *     &lt;property name=&quot;cacheManager&quot; ref=&quot;cacheManager&quot;/&gt;
 *   &lt;/bean&gt;
 *   (略)
 * &lt;/beans&gt;
 * </pre></code>
 * </p>
 * <p>
 * 使用上の注意点として、上記記述例で使用しているキャッシュ名のbusinessContextは固定名であり、
 * 変更することはできない。
 * 既にSpring Cache Abstractionの{@code ConcurrentMapCacheFactoryBean}による
 * ローカルキャッシュを使用している場合、{@code cacheManager}のBean定義にbusinessContextのキャッシュ領域を追加し、
 * {@code cacheManager}をインジェクションすることで本機能による業務コンテキストのキャッシュと併用可能となる。
 *
 * また、{@code BLogicApplicationContextResolver}ではフレームワークにより
 * {@code closeApplicationContext()}メソッドによるコンテキストのクローズが行われるが、
 * {@code cacheManager}をインジェクションしている場合、
 * キャッシュ対象の業務コンテキストとしてクローズがスキップされる。
 * </p>
 * @see org.springframework.cache.CacheManager
 * @since 3.6
 */
public class CacheableBLogicContextResolverImpl
        extends BLogicApplicationContextResolverImpl
        implements InitializingBean, DisposableBean {

    /**
     * ロガー
     */
    private static final TLogger LOGGER = TLogger.getLogger(
            CacheableBLogicContextResolverImpl.class);

    /**
     * 共通コンテキストとなるXMLBean定義ファイルのクラスパス
     */
    protected String[] commonContextClassPath;

    /**
     * 業務コンテキストキャッシュを管理するキャッシュマネージャー
     */
    protected CacheManager cacheManager;

    /**
     * キャッシュ対象となる業務コンテキストのキャッシュキー
     */
    public static final String BLOGIC_CONTEXT_CACHE_KEY = "businessContext";

    /**
     * 共通コンテキストとなるXMLBean定義ファイルのクラスパスを指定する。
     *
     * @param commonContextClassPath 共通コンテキストとなるXMLBean定義ファイルのクラスパス
     */
    public void setCommonContextClassPath(String[] commonContextClassPath) {
        this.commonContextClassPath = commonContextClassPath;
    }

    /**
     * 業務コンテキストのキャッシュを保持するキャッシュマネージャを設定する。
     *
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * {@inheritDoc}
     *
     * ジョブ業務コードをキーとして、キャッシュ済みの業務コンテキストを返却する。
     *
     * キャッシュが行われていない場合、親クラスによる業務コンテキスト取得メソッドが呼び出される。<br>
     * 本クラスのプロパティに{@code CacheManager}が指定されていない場合キャッシュは行われず、
     * メソッド呼び出しの都度業務コンテキストが生成される。
     */
    @Override
    @Cacheable(value = BLOGIC_CONTEXT_CACHE_KEY, key = "#batchJobData.jobAppCd")
    public ApplicationContext resolveApplicationContext(
            BatchJobData batchJobData) {
        if (isCacheEnabled()) {
            LOGGER.info(LogId.IAL025019, batchJobData.getJobAppCd());
        }
        return super.resolveApplicationContext(batchJobData);
    }

    /**
     * {@inheritDoc}
     *
     * キャッシュ機能を利用していない業務コンテキストの破棄を行う。
     * キャッシュ機能利用時は本メソッドはスキップする。
     *
     * @param applicationContext 業務用Bean定義のアプリケーションコンテキスト
     */
    @Override
    public void closeApplicationContext(ApplicationContext applicationContext) {
        if (isCacheEnabled()) {
            return;
        }
        LOGGER.debug(LogId.DAL025062);
        super.closeApplicationContext(applicationContext);
    }

    /**
     * 共有コンテキストのクラスパスがプロパティとして設定されている時、
     * Bean初期化処理として業務コンテキストの親コンテキストを指定する。
     */
    @Override
    public void afterPropertiesSet() {
        if (this.commonContextClassPath != null) {
            this.parent = new ClassPathXmlApplicationContext(
                    this.commonContextClassPath);
        }
    }

    /**
     * 本インスタンス破棄時、共有コンテキスト及びキャッシュとして保持されている
     * 業務コンテキストの破棄を行う。
     */
    @Override
    public void destroy() {
        destroyCachedContext();
        // 子コンテキストを破棄しても親コンテキストは破棄されないため、
        // 業務コンテキスト破棄の後で親である共通コンテキストの破棄を行う。
        if (this.parent != null) {
            super.closeApplicationContext(this.parent);
        }
    }

    /**
     * キャッシュされた業務コンテキストの破棄とキャッシュ自身の破棄を行う。
     */
    protected void destroyCachedContext() {
        if (!isCacheEnabled()) {
            return;
        }
        Cache cache = this.cacheManager.getCache(BLOGIC_CONTEXT_CACHE_KEY);
        Collection<?> cacheValues = Map.class.cast(cache.getNativeCache())
                .values();
        for (Object obj : cacheValues) {
            if (obj instanceof ApplicationContext) {
                super.closeApplicationContext(ApplicationContext.class.cast(obj));
            }
        }
        cache.clear();
    }

    /**
     * 業務コンテキストがキャッシュ可能であるかを判定する。
     *
     * @return キャッシュ可能ならばtrue、キャッシュ機能を使用していないためキャッシュ不可能ならばfalse
     */
    protected boolean isCacheEnabled() {
        if (this.cacheManager == null || !this.cacheManager.getCacheNames()
                .contains(BLOGIC_CONTEXT_CACHE_KEY)) {
            return false;
        }
        Cache cache = this.cacheManager.getCache(BLOGIC_CONTEXT_CACHE_KEY);
        if (cache == null) {
            return false;
        }
        // NoOpCache使用時以外はConcurrentMapCacheとなる。
        return (cache.getNativeCache() instanceof Map);
    }
}
