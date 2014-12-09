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

package jp.terasoluna.fw.file.dao;

/**
 * ファイル行オブジェクト生成用のイテレータを生成するためのインタフェース。
 * <p>
 * 文字ストリームからファイル行オブジェクトを生成する<code>FileLineIterator</code> を生成するためのインタフェースである。 サブクラスで実装するメソッドは<code>execute</code>のみ。
 * 引数にはデータを読み取るファイルのパス（相対パス/絶対パス）と、 ファイル行オブジェクトのクラスを設定する。<br>
 * FileLineIteratorを生成する方法を以下にあげる。
 * </p>
 * <p>
 * <strong>設定例</strong><br>
 * ビジネスロジック(SampleLogic)の中でFileLineIteratorを生成する例
 * 
 * <pre>
 * &lt;li&gt;1.FileQueryDAOのインスタンスの情報をジョブBean定義ファイルに設定する。&lt;/li&gt;
 * &lt;code&gt;
 * &lt;bean id=&quot;blogic&quot; 
 * 　class=&quot;jp.terasoluna.batch.sample.SampleLogic&quot;&gt;
 *  &lt;property name=&quot;fileQueryDAO&quot; ref=&quot;csvFileQueryDao&quot; /&gt;
 * &lt;/bean&gt;
 * &lt;/code&gt;
 * 参照するFileQueryDAOのサブクラスは「FileAccessBean.xml」を参照のこと。
 * 
 * &lt;li&gt;2.FileLineIteratorをビジネスロジックの中で生成する。&lt;/li&gt;
 * &lt;code&gt;
 * FileQueryDAO fileDao = null;　　　//FileQueryDAOのインスタンスの情報はジョブBean定義ファイルに設定する。setterは省略。
 * ……
 * // FileLineIteratorを生成。
 * FileLineIterator fileLineIterator 
 *     = fileDao.execute(&quot;【アクセスするファイル名】&quot;, 【ファイル行オブジェクトのクラス】);
 * ……
 * &lt;/code&gt;
 * </pre>
 * 
 * FileLineIteratorについては｛@link jp.terasoluna.fw.file.dao.FileLineIterator}を参照のこと。
 */
public interface FileQueryDAO {

    /**
     * ファイル名を指定して、<code>FileLineIterator</code>を取得する。
     * @param fileName ファイル名（絶対パスまたは相対パスのどちらか）
     * @param clazz 1行分の文字列を格納するファイル行オブジェクトクラス
     * @param <T> ファイル行オブジェクト
     * @return ファイル行オブジェクト生成用のイテレータ
     */
    <T> FileLineIterator<T> execute(String fileName, Class<T> clazz);
}
