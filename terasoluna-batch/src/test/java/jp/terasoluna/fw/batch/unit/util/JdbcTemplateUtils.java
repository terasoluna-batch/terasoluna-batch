package jp.terasoluna.fw.batch.unit.util;

import java.beans.PropertyDescriptor;

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

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/**
 * {@link JdbcTemplate}をラップしたユーティリティクラスです。
 * 
 */
public class JdbcTemplateUtils {

    /**
     * 任意のSQLを実行します。<br />
     * DDL等を実行したい場合に使用します。
     * 
     * <pre>
     * 例
     * execute(jdbcTemplate, &quot;create table foo(val integer, name varchar2(10))&quot;);
     * </pre>
     * 
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @throws DataAccessException
     */
    public static void execute(JdbcOperations jdbcTemplate, String sql)
                                                                       throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        jdbcTemplate.execute(sql);
    }

    /**
     * 更新系SQLを実行します。
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 更新件数
     * @throws DataAccessException
     */
    public static int update(JdbcOperations jdbcTemplate, String sql)
                                                                     throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.update(sql);
    }

    /**
     * 更新系SQLを実行します。({@link PreparedStatement}使用)
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 更新件数
     * @throws DataAccessException
     */
    public static int update(JdbcOperations jdbcTemplate, String sql,
            Object[] args) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.update(sql, args);
    }

    /**
     * 更新系SQLをバッチ実行します。
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sqls バッチ実行するSQLの配列
     * @return 更新件数配列
     * @throws DataAccessException
     */
    public static int[] batchUpdate(JdbcOperations jdbcTemplate, String... sqls)
                                                                                throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        Assert.notEmpty(sqls);
        return jdbcTemplate.batchUpdate(sqls);
    }

    /**
     * 参照系SQLを実行しlong型の結果を返却します。<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val) values(100)&quot;);
     * queryForLong(jdbcTemplate, &quot;select val from foo&quot;); // -&gt; 100
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws DataAccessException
     */
    public static long queryForLong(JdbcOperations jdbcTemplate, String sql)
                                                                            throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForLong(sql);
    }

    /**
     * 参照系SQLを実行しlong型の結果を返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val) values(100)&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val) values(10)&quot;);
     * queryForLong(jdbcTemplate, &quot;select val from foo where val < ?&quot;, new Object[]{50}); // -&gt; 10
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 実行結果
     * @throws DataAccessException
     */
    public static long queryForLong(JdbcOperations jdbcTemplate, String sql,
            Object[] args) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForLong(sql, args);
    }

    /**
     * 
     * 参照系SQLを実行しint型の結果を返却します。<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val) values(100)&quot;);
     * queryForInt(jdbcTemplate, &quot;select val from foo&quot;); // -&gt; 100
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws DataAccessException
     */
    public static int queryForInt(JdbcOperations jdbcTemplate, String sql)
                                                                          throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForInt(sql);
    }

    /**
     * 参照系SQLを実行しint型の結果を返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val) values(100)&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val) values(10)&quot;);
     * queryForInt(jdbcTemplate, &quot;select val from foo where val < ?&quot;, new Object[]{50}); // -&gt; 10
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 実行結果
     * @throws DataAccessException
     */
    public static int queryForInt(JdbcOperations jdbcTemplate, String sql,
            Object[] args) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForInt(sql, args);
    }

    /**
     * 
     * 参照系SQLを実行しString型の結果を返却します。<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample')&quot;);
     * queryForString(jdbcTemplate, &quot;select name from foo&quot;); // -&gt; Sample
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws DataAccessException
     */
    public static String queryForString(JdbcOperations jdbcTemplate, String sql)
                                                                                throws DataAccessException {
        return JdbcTemplateUtils.<String> queryForObject(jdbcTemplate, sql,
                String.class);
    }

    /**
     * 参照系SQLを実行しint型の結果を返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForInt(jdbcTemplate, &quot;select name from foo where val < ?&quot;, new Object[]{50}); // -&gt; Sample2
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 実行結果
     * @throws DataAccessException
     */
    public static String queryForString(JdbcOperations jdbcTemplate,
            String sql, Object[] args) throws DataAccessException {
        return JdbcTemplateUtils.<String> queryForObject(jdbcTemplate, sql,
                args, String.class);
    }

    /**
     * 参照系SQLを実行し指定した型の結果を返却します。<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * @param <T> 返却値の型 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param requiredType 返却値の型 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> T queryForObject(JdbcOperations jdbcTemplate, String sql,
            Class<T> requiredType) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return (T) jdbcTemplate.queryForObject(sql, requiredType);
    }

    /**
     * 参照系SQLを実行し指定した型の結果を返却します。<br>
     * 問い合わせ結果が一件である必要があります。
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @param requiredType 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> T queryForObject(JdbcOperations jdbcTemplate, String sql,
            Object[] args, Class<T> requiredType) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return (T) jdbcTemplate.queryForObject(sql, args, requiredType);
    }

    /**
     * 参照系SQLを実行し1行分の結果を格納した{@link Map}を返却します。<br>
     * 問い合わせ結果が一件である必要があります。<br>
     * 返り値の{@link Map}のキー文字列は大文字・小文字を無視します。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample')&quot;);
     * queryForRowMap(jdbcTemplate, &quot;select val, name from foo&quot;); // -&gt; {VAL=100, NAME=Sample}
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ?> queryForRowMap(JdbcOperations jdbcTemplate,
            String sql) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        Map<String, ?> result = jdbcTemplate.queryForMap(sql);
        return new CaseInsensitiveMap(result);
    }

    /**
     * 参照系SQLを実行し1行分の結果を格納した{@link Map}を返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一件である必要があります。<br>
     * 返り値の{@link Map}のキー文字列は大文字・小文字を無視します。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowMap(jdbcTemplate, &quot;select val from foo where val < ?&quot;, new Object[]{50}); // -&gt; {VAL=10, NAME=Sample2}
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ?> queryForRowMap(JdbcOperations jdbcTemplate,
            String sql, Object[] args) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        Map<String, ?> result = jdbcTemplate.queryForMap(sql, args);
        return new CaseInsensitiveMap(result);
    }

    /**
     * 参照系SQLを実行し1行分の結果をT型のオブジェクトにマッピングして返却します。<br>
     * 問い合わせ結果が一件である必要があります。<br>
     * <strong>Springのバージョンが2.5以上である必要があります。</strong>
     * 
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample')&quot;);
     * queryForRowObject(jdbcTemplate, &quot;select val, name from foo&quot;, Foo.class); // -&gt; Foo{val=100, name=Sample}
     * </pre>
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param clazz
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> T queryForRowObject(JdbcOperations jdbcTemplate,
            String sql, Class<T> clazz) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return (T) jdbcTemplate.queryForObject(sql, rowMapper);
    }

    /**
     * 参照系SQLを実行し1行分の結果をT型のオブジェクトにマッピングして返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一件である必要があります。<br>
     * <strong>Springのバージョンが2.5以上である必要があります。</strong>
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowObject(jdbcTemplate, &quot;select val from foo where val < ?&quot;, new Object[]{50}, Foo.class); // -&gt; Foo{val=10, name=Sample2}
     * </pre>
     * 
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @param clazz 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> T queryForRowObject(JdbcOperations jdbcTemplate,
            String sql, Object[] args, Class<T> clazz)
                                                      throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return (T) jdbcTemplate.queryForObject(sql, args, rowMapper);
    }

    /**
     * 参照系のSQLを実行し1列分の結果をT型で返却します。<br>
     * 問い合わせ結果が一列である必要があります。<br>
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForSingleColumnList(jdbcTemplate, &quot;select val from foo&quot;, Integer.class);
     * // -&gt; [100, 10]
     * </pre>
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param elementType
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForSingleColumnList(
            JdbcOperations jdbcTemplate, String sql, Class<T> elementType)
                                                                          throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForList(sql, elementType);
    }

    /**
     * 参照系のSQLを実行し1列分の結果をT型で返却します。({@link PreparedStatement}使用)<br>
     * 問い合わせ結果が一列である必要があります。<br>
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForSingleColumnList(jdbcTemplate, &quot;select name from foo where val &gt; ?&quot;,
     *         new Object[] { 0 }, String.class);
     * // -&gt; [Sample1, Sample2]
     * </pre>
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @param elementType
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForSingleColumnList(
            JdbcOperations jdbcTemplate, String sql, Object[] args,
            Class<T> elementType) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return jdbcTemplate.queryForList(sql, args, elementType);
    }

    /**
     * Mapのリストをキー大文字小文字無視Mapに変換して返却します。
     * 
     * @param list Mapのリスト
     * @return 実行結果
     */
    @SuppressWarnings("unchecked")
    private static List<Map<String, ?>> convertToCaseInsensitiveMapList(
            List<?> list) {
        List<Map<String, ?>> result = new ArrayList<Map<String, ?>>();
        if (list != null) {
            for (Object o : list) {
                result.add(new CaseInsensitiveMap((Map<String, ?>) o));
            }
        }
        return result;
    }

    /**
     * 参照系SQLを実行し各行の結果を格納した{@link Map}のリストを返却します。
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowMapList(jdbcTemplate, &quot;select val, name from foo&quot;);
     * // -&gt; [{VAL=100, NAME=Sample1}, {VAL=10, NAME=Sample2}]
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @return 実行結果
     * @throws DataAccessException
     */
    public static List<Map<String, ?>> queryForRowMapList(
            JdbcOperations jdbcTemplate, String sql) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return convertToCaseInsensitiveMapList(jdbcTemplate.queryForList(sql));
    }

    /**
     * 参照系SQLを実行し各行の結果を格納した{@link Map}のリストを返却します。({@link PreparedStatement}使用)
     * 
     * <pre>
     * 例
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowMapList(jdbcTemplate, &quot;select val, name from foo where val &gt; ?&quot;,
     *         new Object[] { 0 });
     * // -&gt; [{VAL=100, NAME=Sample1}, {VAL=10, NAME=Sample2}]
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @return 実行結果
     * @throws DataAccessException
     */
    public static List<Map<String, ?>> queryForRowMapList(
            JdbcOperations jdbcTemplate, String sql, Object[] args)
                                                                   throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return convertToCaseInsensitiveMapList(jdbcTemplate.queryForList(sql,
                args));
    }

    /**
     * 指定した型のフィールド情報から参照SQL(全フィールド参照)を生成して実行し、各行の結果を格納した{@link Map}のリストを返却します。 <br>
     * SQLは{@link #createSelectSql(Class)}より生成します。
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowMapList(jdbcTemplate, Foo.class);
     * // -&gt; [{VAL=100, NAME=Sample1}, {VAL=10, NAME=Sample2}]
     * </pre>
     * 
     * @param jdbcTemplate JdbcTemplate
     * @param clazz 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    public static List<Map<String, ?>> queryForRowMapList(
            JdbcOperations jdbcTemplate, Class<?> clazz)
                                                        throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        return convertToCaseInsensitiveMapList(jdbcTemplate
                .queryForList(createSelectSql(clazz)));
    }

    /**
     * 参照系SQLを実行し各行の結果をT型にマッピングして格納したリストを返却します。<br>
     * <strong>Springのバージョンが2.5以上である必要があります。</strong>
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowOnjectList(jdbcTemplate, &quot;select val, name from foo&quot;, Foo.class);
     * // -&gt; [Foo{val=100, name=Sample1}, Foo{val=10, name=Sample2}]
     * </pre>
     * 
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param clazz 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForRowObjectList(
            JdbcOperations jdbcTemplate, String sql, Class<T> clazz)
                                                                    throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 参照系SQLを実行し各行の結果をT型にマッピングして格納したリストを返却します。({@link PreparedStatement}使用)<br>
     * <strong>Springのバージョンが2.5以上である必要があります。</strong>
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowOnjectList(jdbcTemplate, &quot;select val, name from foo where val > ?&quot;, new Object[] {0}, Foo.class);
     * // -&gt; [Foo{val=100, name=Sample1}, Foo{val=10, name=Sample2}]
     * </pre>
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param sql 実行するSQL
     * @param args プリペアドステートメントのパラメータ
     * @param clazz 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForRowObjectList(
            JdbcOperations jdbcTemplate, String sql, Object[] args,
            Class<T> clazz) throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return jdbcTemplate.query(sql, args, rowMapper);
    }

    /**
     * 指定した型のフィールド情報から参照SQL(全フィールド参照)を生成して実行し、各行の結果をT型のオブジェクトにマッピングしてリストを返却します。 <br>
     * SQLは{@link #createSelectSql(Class)}より生成します。 <strong>Springのバージョンが2.5以上である必要があります。</strong>
     * 
     * <pre>
     * 例
     * public class Foo {
     *   private int val;
     *   private String name;
     *   // setter/getter略
     * }
     * 
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(100, 'Sample1')&quot;);
     * upate(jdbcTemplate, &quot;insert into foo (val, name) values(10, 'Sample2')&quot;);
     * queryForRowOnjectList(jdbcTemplate, Foo.class);
     * // -&gt; [Foo{val=100, name=Sample1}, Foo{val=10, name=Sample2}]
     * </pre>
     * 
     * @param <T> 返却値の型
     * @param jdbcTemplate JdbcTemplate
     * @param clazz 返却値の型
     * @return 実行結果
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForRowObjectList(
            JdbcOperations jdbcTemplate, Class<T> clazz)
                                                        throws DataAccessException {
        Assert.notNull(jdbcTemplate);
        Assert.notNull(clazz);
        String[] header = createFiledNames(clazz);
        String sql = createSelectSql(clazz.getSimpleName(), header);
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 指定したフィールド名を全て参照するSELECT文を生成します。
     * 
     * <pre>
     * 例
     * createSelectSql("foo", new String[] {"val", "name"}); // SELECT val,name FROM foo
     * </pre>
     * 
     * @param tableName テーブル名
     * @param fieldNames フィールド名
     * @return SQL文
     */
    public static String createSelectSql(String tableName, String[] fieldNames) {
        Assert.notNull(tableName);
        Assert.hasText(tableName);
        Assert.notNull(fieldNames);
        String fields = null;
        if (fieldNames.length == 0) {
            fields = "*";
        } else {
            fields = StringUtils.join(fieldNames, ",");
        }
        String sql = "SELECT " + fields + " FROM " + tableName;
        return sql;
    }

    /**
     * 指定したクラスの持つフィールド名を全て参照するSELECT文を生成します。
     * 
     * <pre>
     * 例
     * public class Foo {
     *     private int val;
     *     private String name;
     *     // setter/getter略
     * }
     * 
     * createSelectSql("foo", Foo.clas); // -> SELECT val,name FROM foo
     * </pre>
     * 
     * @param tableName テーブル名
     * @param clazz クラス名
     * @return SQL文
     */
    public static String createSelectSql(String tableName, Class<?> clazz) {
        Assert.notNull(clazz);
        return createSelectSql(tableName,
                createFiledNames(clazz));
    }

    /**
     * 指定したクラスの持つフィールド名を全て参照するSELECT文を生成します。テーブル名はクラス名になります。
     * 
     * <pre>
     * 例
     * public class Foo {
     *     private int val;
     *     private String name;
     *     // setter/getter略
     * }
     * 
     * createSelectSql(Foo.clas); // -> SELECT val,name FROM Foo
     * </pre>
     * 
     * @param clazz クラス名
     * @return SQL文
     */
    public static String createSelectSql(Class<?> clazz) {
        Assert.notNull(clazz);
        return createSelectSql(clazz.getSimpleName(), clazz);
    }

    /**
     * 指定したフィールド名を全て設定するINSERT文を生成します。
     * 
     * <pre>
     * 例
     * createInsertSql("foo", new String[] {"val", "name"}); // INSERT INTO foo (val,name) VALUES(?,?)
     * </pre>
     * 
     * @param tableName テーブル名
     * @param fieldNames フィールド名
     * @return SQL文
     */
    public static String createInsertSql(String tableName, String[] fieldNames) {
        Assert.notNull(tableName);
        Assert.hasText(tableName);
        Assert.notNull(fieldNames);

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append("(");
        sb.append(StringUtils.join(fieldNames, ","));
        sb.append(") VALUES (");
        for (int i = 0; i < fieldNames.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 指定したクラスの持つフィールド名を全て設定するINSERT文を生成します。
     * 
     * <pre>
     * 例
     * createInsertSql("foo", Foo.class); // INSERT INTO foo (val,name) VALUES(?,?)
     * </pre>
     * 
     * @param tableName テーブル名
     * @param clazz クラス名
     * @return SQL文
     */
    public static String createInsertSql(String tableName, Class<?> clazz) {
        return createInsertSql(tableName,
                createFiledNames(clazz));
    }

    /**
     * 指定したクラスの持つフィールド名を全て設定するINSERT文を生成します。テーブル名はクラス名になります。
     * 
     * <pre>
     * 例
     * createInsertSql(Foo.class); // INSERT INTO Foo (val,name) VALUES(?,?)
     * </pre>
     * 
     * @param clazz クラス名
     * @return SQL文
     */
    public static String createInsertSql(Class<?> clazz) {
        Assert.notNull(clazz);
        return createInsertSql(clazz.getSimpleName(),
                createFiledNames(clazz));
    }
    
    /**
     * クラス内のフィールド名を取得する。<br>
     * 
     * <pre>
     * 親クラスのフィールド名も取得できる。staticなフィールドの名前は取得しない。
     * terasoluna-unitのReflectionUtilsから部分的に移植
     * </pre>
     * @param clazz 対象クラス
     * @return フィールド名一覧配列
     */
    private static String[] createFiledNames(Class<?> clazz) {
        Assert.notNull(clazz);

        BeanWrapper beanWrapper = new BeanWrapperImpl(clazz);
        PropertyDescriptor[] descs = beanWrapper.getPropertyDescriptors();

        String[] fieldNames = new String[descs.length - 1]; // classは除く

        for (int i = 0, j = 0; i < descs.length; i++) {
            String name = descs[i].getName();
            if (!"class".equals(name)) {
                fieldNames[j++] = name;
            }
        }

        return fieldNames;
    }    
}
