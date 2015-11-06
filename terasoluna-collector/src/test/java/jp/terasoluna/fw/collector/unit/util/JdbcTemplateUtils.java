package jp.terasoluna.fw.collector.unit.util;

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
        @SuppressWarnings("rawtypes")
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
        @SuppressWarnings("rawtypes")
        RowMapper rowMapper = new BeanPropertyRowMapper(clazz, false);
        return (T) jdbcTemplate.queryForObject(sql, args, rowMapper);
    }    
}
