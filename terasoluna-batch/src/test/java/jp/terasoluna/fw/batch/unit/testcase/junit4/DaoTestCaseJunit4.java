package jp.terasoluna.fw.batch.unit.testcase.junit4;

/*
 * Copyright (c) 2012 NTT DATA Corporation
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

import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.batch.unit.testcase.TestCaseUtils;
import jp.terasoluna.fw.batch.unit.testcase.junit4.listener.DaoTestCaseExecutionListener;
import jp.terasoluna.fw.batch.unit.testcase.junit4.loader.DaoTestCaseContextLoader;
import jp.terasoluna.fw.batch.unit.util.JdbcTemplateUtils;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.Assert;

/**
 * JUnit4用DAO実行試験支援テストケース。
 * 
 * <pre>
 * 本試験では前処理中にSpringの{@link org.springframework.context.ApplicationContext}を作成します。
 * 作成時には次の設定ファイルを読み込みます。
 * </pre>
 * <table border=1>
 * <tr>
 * <th>設定ファイル</th>
 * <th>デフォルト値</th>
 * <th>変更ポイント</th>
 * <th>備考</th>
 * </tr>
 * <td>共通Bean定義ファイル</td>
 * <td>WebContext/WEB-INF/applicationContext.xml</td>
 * <td>テストクラスに <code>@{@link ContextConfiguration}</code>アノテーションを付与し、その<code>loader</code>プロパティに{@link DaoTestCaseContextLoader}<code>.class</code>を設定してください。<br>
 * (<strong style="color:red">{@link DaoTestCase}では明示的な設定は不要でしたが、JUnit4用では明示的に設定する必要があります</strong>) <br>
 * ※例<br>
 * 
 * <pre>
 * {@literal @}ContextConfiguration(locations = { ... }, loader = <strong style="color:red">DaoTestCaseContextLoader.class</strong>)
 * public class SampleBLogic1Test extends DaoTestCaseJunit4 {
 * </pre>
 * 
 * <hr>
 * <br>
 * terasoluna-unit-override.propertiesの以下にキーに対する値を設定することで、変更可能です。<br>
 * WebContext → webapp.pathキー<br>
 * WEB-INF → webinf.dirキー<br>
 * applicationContext.xml → applicationcontext.fileキー<br></td>
 * <td>ファイルが存在しない場合、無視します。</td>
 * </tr>
 * <tr>
 * <td>モジュールBean定義ファイル</td>
 * <td>WebContext/WEB-INF/moduleContext.xml</td>
 * <td>テストクラスに <code>@{@link ContextConfiguration}</code>アノテーションを付与し、その<code>loader</code>プロパティに{@link DaoTestCaseContextLoader}<code>.class</code>を設定してください。<br>
 * (<strong style="color:red">{@link DaoTestCase}では明示的な設定は不要でしたが、JUnit4用では明示的に設定する必要があります</strong>) <br>
 * ※例<br>
 * 
 * <pre>
 * {@literal @}ContextConfiguration(locations = { ... }, loader = <strong style="color:red">DaoTestCaseContextLoader.class</strong>)
 * public class SampleBLogic1Test extends DaoTestCaseJunit4 {
 * </pre>
 * 
 * <hr>
 * <br>
 * terasoluna-unit-override.propertiesの以下にキーに対する値を設定することで、変更可能です。<br>
 * WebContext → webapp.pathキー<br>
 * WEB-INF → webinf.dirキー<br>
 * moduleContext.xml → modulecontext.fileキー<br></td>
 * <td>ファイルが存在しない場合、無視します。コンストラクタにて{@link #setLoadDefaultConfig(boolean)} でfalseを設定した場合または{@link #isLoadDefaultConfig()}がfalseを返すようにオーバライドした場合、読み込みません。</td>
 * </tr>
 * <tr>
 * <td>試験毎Bean定義ファイル</td>
 * <td>なし</td>
 * <td>
 * テストクラスに <code>@{@link ContextConfiguration}</code>アノテーションを付与し、その<code>locations</code>プロパティに読み込ませるパスを指定してください（複数可）。<br>
 * ※例<br>
 * 
 * <pre>
 * {@literal @}ContextConfiguration(<strong style="color:red">locations = { "classpath:sampleBLogic1.xml", ... }</strong>)
 * public class SampleBLogic1Test extends DaoTestCaseJunit4 {
 * </pre>
 * 
 * </td>
 * <td>&nbsp;</td>
 * </tr>
 * </table>
 * 
 * <pre>
 * Bean定義ファイル中には{@link java.sql.DataSource}の定義が必要であり、DBを接続した試験を行うことができます。
 * (ただし、DB環境を用意する必要があります。)
 * 
 * 
 * 読み込んだBean定義ファイルに定義したBeanを試験コード中で使用する場合、以下の二種類のBean取得方法があります。
 * 
 * 1. {@link org.springframework.context.ApplicationContext#getBean(String)}によって取得する方法
 * 2. setterを定義してautowire(by type)する方法
 * 
 * 以下のような定義がある場合を例にとって説明します。
 * 
 * WEB-INF/foo/fooContext.xml
 * 
 * &lt;!-- 前後略 --&gt;
 * &lt;bean id=&quot;foo&quot; class=&quot;com.example.Foo&quot; /&gt;
 * &lt;bean name=&quot;/foo&quot; class=&quot;com.example.FooBLogic&quot; /&gt;
 * 
 * 1. {@link org.springframework.context.ApplicationContext#getBean(String)}によって取得する方法
 * 
 * {@literal @}ContextConfiguration(locations = { "/WEB-INF/foo/fooContext.xml" }, loader = DaoTestCaseContextLoader.class)
 * public FooTest extends {@link DaoTestCaseJunit4} {
 *   public void testDoSomething() throws Exception {
 *     Foo foo = getBean(&quot;foo&quot;);
 *     // あるいは Foo foo = (Foo) applicationContext.getBean(&quot;foo&quot;);
 *     int result = foo.doSomething();
 *     assertEquals(1, result);
 *   }
 * }
 * 
 * {@literal @}ContextConfiguration(locations = { "/WEB-INF/foo/fooContext.xml" }, loader = DaoTestCaseContextLoader.class)
 * public FooBLogicTest extends {@link DaoTestCaseJunit4} {
 *   public void testFoo()  throws Exception {
 *     FooBLogic foo = getBean(&quot;/foo&quot;);
 *     // あるいは FooBLogic foo = (FooBLogic) applicationContext.getBean(&quot;/foo&quot;);
 *     BLogicResult result = foo.execute(null);
 *     {@link AssertUtils}.assertBLogicSuccess(result);
 *   }
 * }
 * 
 * 取得するBeanのインターフェイスにAOP処理を設定している場合は、変数の型をAOP対象のインターフェイスにする必要があります。
 * 
 * 2. setterを定義してautowire(by type)する方法
 * 取得したいBeanのセッターを定義してください。
 * 
 * {@literal @}ContextConfiguration(locations = { "/WEB-INF/foo/fooContext.xml" }, loader = DaoTestCaseContextLoader.class)
 * public FooTest extends {@link DaoTestCaseJunit4} {
 *   private Foo foo;
 *   
 *   public void setFoo(Foo foo) {
 *     this.foo = foo;
 *   }
 *  
 *   public void testDoSomething()  throws Exception {
 *     int result = foo.doSomething();
 *     assertEquals(1, result);
 *   }
 * }
 * 
 * {@literal @}ContextConfiguration(locations = { "/WEB-INF/foo/fooContext.xml" }, loader = DaoTestCaseContextLoader.class)
 * public FooBLogicTest extends {@link DaoTestCaseJunit4} {
 *   private FooBLogic foo;
 *   
 *   public void setFoo(FooBLogic foo) {
 *     this.foo = foo;
 *   }
 *   
 *   public void testFoo() throws Exception {
 *     BLogicResult result = foo.execute(null);
 *     {@link AssertUtils}.assertBLogicSuccess(result);
 *   }
 * }
 * 
 * &lt;strong&gt;この方法は以下の場合には利用できません。&lt;/strong&gt;
 * ・Bean定義ファイル中に同じ型のBeanが複数定義されている場合
 * ・AOP処理により{@link ApplicationContext}から{@link java.lang.reflect.Proxy}実装クラスとして取得される場合
 * 
 * 本クラスを継承したテストは以下の様に処理が行われる
 * - テスト実行初回時
 * -- WEBAPディレクトリ(コンテキストルート)のクラスパスへの追加({@link TestCaseUtils#getConfigLocationsForDaoTestCase(String[])})
 * - 前処理
 * -- {@link ApplicationContext}作成
 * -- トランザクション開始前前処理({@link #onSetUpBeforeTransaction()}で実装可)
 * -- トランザクション開始後前処理({@link #onSetUpInTransaction()}で実装可)
 * 
 * - 試験実行
 * 
 * - 後処理
 * -- トランザクション終了前後処理({@link #onTearDownInTransaction()})で実装可
 * -- トランザクション終了後後処理({@link #onTearDownAfterTransaction()}で実装可)
 * 
 * </pre>
 */
@TestExecutionListeners(value = { TransactionalTestExecutionListener.class,
        DaoTestCaseExecutionListener.class,
        DirtiesContextTestExecutionListener.class }, inheritListeners = false)
abstract public class DaoTestCaseJunit4 extends
                                       AbstractTransactionalJUnit4SpringContextTests {

    /**
     * AUTOWIRE_NO
     */
    protected static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;

    /**
     * AUTOWIRE_BY_NAMe
     */
    protected static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    /**
     * AUTOWIRE_BY_TYPE
     */
    protected static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

    /**
     * AUTOWIRE_CONSTRUCTOR
     */
    protected static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    /**
     * AUTOWIRE_AUTODETECT
     */
    protected static final int AUTOWIRE_AUTODETECT = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;

    /**
     * autowireモード
     */
    private int autowireMode = AUTOWIRE_BY_TYPE;

    /**
     * 依存性チェックを行うか
     */
    private boolean dependencyCheck = false;

    /**
     * autowireモードを返却します。
     * 
     * @return autowireモード
     */
    public int getAutowireMode() {
        return autowireMode;
    }

    /**
     * autowireモードを設定します。
     * 
     * @param autowireMode autowireモード
     */
    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    /**
     * 依存性チェックを行うかを返却します。
     * 
     * @return　依存性チェックを行うか
     */
    public boolean isDependencyCheck() {
        return dependencyCheck;
    }

    /**
     * 依存性チェックを行うかを設定します。
     * 
     * @param dependencyCheck 依存性チェックを行うか
     */
    public void setDependencyCheck(boolean dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    /**
     * トランザクション開始前前処理
     * 
     * @throws Exception
     */
    @BeforeTransaction
    public final void setUpBeforeTransaction() throws Exception {
        onSetUpBeforeTransaction();
    }

    /**
     * トランザクション開始前前処理
     * 
     * @throws Exception
     */
    protected void onSetUpBeforeTransaction() throws Exception {
    }

    /**
     * トランザクション開始後前処理
     * 
     * @throws Exception
     */
    @Before
    public final void setUpInTransaction() throws Exception {
        onSetUpInTransaction();
    }

    /**
     * トランザクション開始後前処理
     * 
     * @throws Exception
     */
    protected void onSetUpInTransaction() throws Exception {
    }

    /**
     * トランザクション終了前後処理
     * 
     * @throws Exception
     */
    @After
    public final void tearDownInTranscation() throws Exception {
        onTearDownInTransaction();
    }

    /**
     * トランザクション終了前後処理
     * 
     * @throws Exception
     */
    protected void onTearDownInTransaction() throws Exception {
    }

    /**
     * トランザクション終了後後処理
     * 
     * @throws Exception
     */
    @AfterTransaction
    public final void tearDownAfterTransaction() throws Exception {
    }

    /**
     * トランザクション終了後後処理
     * 
     * @throws Exception
     */
    protected void onTearDownAfterTransaction() throws Exception {
    }

    /**
     * Beanを取得します。
     * 
     * @param <T> 取得するBeanの型
     * @param name beanのidまたはname
     * @return 取得したBean
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        Assert.notNull(applicationContext, "applicationContext is null!");
        return (T) applicationContext.getBean(name);
    }

    public JdbcOperations getJdbcTemplate() {
        return jdbcTemplate;
//        return simpleJdbcTemplate.getJdbcOperations();
    }

    public int deleteFromTable(String name) {
        return super.deleteFromTables(new String[] { name });
    }

    // 以下JdbcTemplateUtilsのラッパー

    /**
     * @param sql
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#execute(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public void execute(String sql) throws DataAccessException {
        JdbcTemplateUtils.execute(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#update(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public int update(String sql) throws DataAccessException {
        return JdbcTemplateUtils.update(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#update(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public int update(String sql, Object[] args) throws DataAccessException {
        return JdbcTemplateUtils.update(getJdbcTemplate(), sql, args);
    }

    /**
     * @param sqls
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#batchUpdate(org.springframework.jdbc.core.JdbcTemplate, java.lang.String[])
     */
    public int[] batchUpdate(String... sqls) throws DataAccessException {
        return JdbcTemplateUtils.batchUpdate(getJdbcTemplate(), sqls);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForLong(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public long queryForLong(String sql) throws DataAccessException {
        return JdbcTemplateUtils.queryForLong(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForLong(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public long queryForLong(String sql, Object[] args)
                                                       throws DataAccessException {
        return JdbcTemplateUtils.queryForLong(getJdbcTemplate(), sql, args);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForInt(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public int queryForInt(String sql) throws DataAccessException {
        return JdbcTemplateUtils.queryForInt(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForInt(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public int queryForInt(String sql, Object[] args)
                                                     throws DataAccessException {
        return JdbcTemplateUtils.queryForInt(getJdbcTemplate(), sql, args);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForString(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public String queryForString(String sql) throws DataAccessException {
        return JdbcTemplateUtils.queryForString(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForString(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public String queryForString(String sql, Object[] args)
                                                           throws DataAccessException {
        return JdbcTemplateUtils.queryForString(getJdbcTemplate(), sql, args);
    }

    /**
     * @param <T>
     * @param sql
     * @param requiredType
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForObject(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Class)
     */
    public <T> T queryForObject(String sql, Class<T> requiredType)
                                                                  throws DataAccessException {
        return JdbcTemplateUtils.queryForObject(getJdbcTemplate(), sql,
                requiredType);
    }

    /**
     * @param <T>
     * @param sql
     * @param args
     * @param requiredType
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForObject(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[], java.lang.Class)
     */
    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType)
                                                                                 throws DataAccessException {
        return JdbcTemplateUtils.queryForObject(getJdbcTemplate(), sql, args,
                requiredType);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowMap(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public Map<String, ?> queryForRowMap(String sql) throws DataAccessException {
        return JdbcTemplateUtils.queryForRowMap(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowMap(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public Map<String, ?> queryForRowMap(String sql, Object[] args)
                                                                   throws DataAccessException {
        return JdbcTemplateUtils.queryForRowMap(getJdbcTemplate(), sql, args);
    }

    /**
     * @param <T>
     * @param sql
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowObject(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Class)
     */
    public <T> T queryForRowObject(String sql, Class<T> clazz)
                                                              throws DataAccessException {
        return JdbcTemplateUtils.queryForRowObject(getJdbcTemplate(), sql,
                clazz);
    }

    /**
     * @param <T>
     * @param sql
     * @param args
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowObject(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[], java.lang.Class)
     */
    public <T> T queryForRowObject(String sql, Object[] args, Class<T> clazz)
                                                                             throws DataAccessException {
        return JdbcTemplateUtils.queryForRowObject(getJdbcTemplate(), sql,
                args, clazz);
    }

    /**
     * @param <T>
     * @param sql
     * @param elementType
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForSingleColumnList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Class)
     */
    public <T> List<T> queryForSingleColumnList(String sql, Class<T> elementType)
                                                                                 throws DataAccessException {
        return JdbcTemplateUtils.queryForSingleColumnList(getJdbcTemplate(),
                sql, elementType);
    }

    /**
     * @param <T>
     * @param sql
     * @param args
     * @param elementType
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForSingleColumnList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[], java.lang.Class)
     */
    public <T> List<T> queryForSingleColumnList(String sql, Object[] args,
            Class<T> elementType) throws DataAccessException {
        return JdbcTemplateUtils.queryForSingleColumnList(getJdbcTemplate(),
                sql, args, elementType);
    }

    /**
     * @param sql
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowMapList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String)
     */
    public List<Map<String, ?>> queryForRowMapList(String sql)
                                                              throws DataAccessException {
        return JdbcTemplateUtils.queryForRowMapList(getJdbcTemplate(), sql);
    }

    /**
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowMapList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[])
     */
    public List<Map<String, ?>> queryForRowMapList(String sql, Object[] args)
                                                                             throws DataAccessException {
        return JdbcTemplateUtils.queryForRowMapList(getJdbcTemplate(), sql,
                args);
    }

    /**
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowMapList(org.springframework.jdbc.core.JdbcTemplate, java.lang.Class)
     */
    public List<Map<String, ?>> queryForRowMapList(Class<?> clazz)
                                                                  throws DataAccessException {
        return JdbcTemplateUtils.queryForRowMapList(getJdbcTemplate(), clazz);
    }

    /**
     * @param <T>
     * @param sql
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowObjectList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Class)
     */
    public <T> List<T> queryForRowObjectList(String sql, Class<T> clazz)
                                                                        throws DataAccessException {
        return JdbcTemplateUtils.queryForRowObjectList(getJdbcTemplate(), sql,
                clazz);
    }

    /**
     * @param <T>
     * @param sql
     * @param args
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowObjectList(org.springframework.jdbc.core.JdbcTemplate, java.lang.String, java.lang.Object[], java.lang.Class)
     */
    public <T> List<T> queryForRowObjectList(String sql, Object[] args,
            Class<T> clazz) throws DataAccessException {
        return JdbcTemplateUtils.queryForRowObjectList(getJdbcTemplate(), sql,
                args, clazz);
    }

    /**
     * @param <T>
     * @param clazz
     * @return
     * @throws DataAccessException
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#queryForRowObjectList(org.springframework.jdbc.core.JdbcTemplate, java.lang.Class)
     */
    public <T> List<T> queryForRowObjectList(Class<T> clazz)
                                                            throws DataAccessException {
        return JdbcTemplateUtils
                .queryForRowObjectList(getJdbcTemplate(), clazz);
    }

    /**
     * @param tableName
     * @param fieldNames
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createSelectSql(java.lang.String, java.lang.String[])
     */
    public String createSelectSql(String tableName, String[] fieldNames) {
        return JdbcTemplateUtils.createSelectSql(tableName, fieldNames);
    }

    /**
     * @param tableName
     * @param clazz
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createSelectSql(java.lang.String, java.lang.Class)
     */
    public String createSelectSql(String tableName, Class<?> clazz) {
        return JdbcTemplateUtils.createSelectSql(tableName, clazz);
    }

    /**
     * @param clazz
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createSelectSql(java.lang.Class)
     */
    public String createSelectSql(Class<?> clazz) {
        return JdbcTemplateUtils.createSelectSql(clazz);
    }

    /**
     * @param tableName
     * @param fieldNames
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createInsertSql(java.lang.String, java.lang.String[])
     */
    public String createInsertSql(String tableName, String[] fieldNames) {
        return JdbcTemplateUtils.createInsertSql(tableName, fieldNames);
    }

    /**
     * @param tableName
     * @param clazz
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createInsertSql(java.lang.String, java.lang.Class)
     */
    public String createInsertSql(String tableName, Class<?> clazz) {
        return JdbcTemplateUtils.createInsertSql(tableName, clazz);
    }

    /**
     * @param clazz
     * @return
     * @see jp.terasoluna.fw.ex.unit.util.JdbcTemplateUtils#createInsertSql(java.lang.Class)
     */
    public String createInsertSql(Class<?> clazz) {
        return JdbcTemplateUtils.createInsertSql(clazz);
    }
}
