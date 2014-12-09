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

import java.util.List;

/**
 * ファイルアクセス(データ出力)用のインタフェース。
 * <p>
 * ファイル行オブジェクトから値を取り出し、テキストファイルに書き込む。 FileLineWriterの生成は、FileUpdateDAOもしくは、ジョブBean定義ファイルにオブジェクト生成の設定を記述する。<br>
 * FileUpdateDAOの詳細は、{@link jp.terasoluna.fw.file.dao.FileUpdateDAO}を参照のこと。
 * </p>
 * <p>
 * <strong>設定例</strong>
 * <li>ジョブBean定義ファイルの設定例
 * 
 * <pre>
 * &lt;code&gt;
 * &lt;!-- ジョブBean定義ファイルここから --&gt;
 * &lt;bean id=&quot;blogic&quot; class=&quot;testBlogic&quot;&gt;
 *   &lt;property name=&quot;writer&quot;&gt;
 *     &lt;bean class=&quot;jp.terasoluna.fw.file.dao.standard.CSVFileLineWriter&quot;
 *       destroy-method=&quot;closeFile&quot;&gt;
 *       &lt;constructor-arg index=&quot;0&quot;&gt;&lt;value&gt;【ファイル名】&lt;/value&gt;&lt;/constructor-arg&gt;
 *       &lt;constructor-arg index=&quot;1&quot;&gt;&lt;value&gt;【ファイル行オブジェクトのクラス(フルパス)】&lt;/value&gt;&lt;/constructor-arg&gt;
 *       &lt;constructor-arg index=&quot;2&quot; ref=&quot;columnFormatterMap&quot; /&gt;
 *     &lt;/bean&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * &lt;!-- ジョブBean定義ファイルここまで --&gt;
 * ※コンストラクタの引数にファイル名、ファイル行オブジェクトのクラスを渡す。
 * コンストラクタの引数の1番目は【ファイル名】
 * コンストラクタの引数の2番目は【ファイル行オブジェクトのクラス(フルパス)】
 * コンストラクタの引数の3番目は「columnFormatterMap」(固定)。
 *  　
 * ビジネスロジックにはFileLineWriter型のオブジェクトとそのsetterを用意する。
 * // ビジネスロジックの記述例　ここから
 * private FileLineWriter&lt;FileColumnSample&gt; fileLineWriter = null;
 * 
 * public void setFileLineWriter(FileLineWriter&lt;FileColumnSample&gt; 
 * 　fileLineWriter){
 *     this.fileLineWriter = fileLineWriter;
 * }
 * // ビジネスロジックの記述例　ここまで
 * &lt;/code&gt;
 * </pre>
 * 
 * <strong>使用例</strong><br>
 * <li>ファイル行オブジェクトの情報を出力する。
 * 
 * <pre>
 * &lt;code&gt;
 * // ビジネスロジックの記述例　ここから
 * private FileLineWriter&lt;FileColumnSample&gt; fileLineWriter = null;
 * ……
 *     // FileColumnSample型のファイル行オブジェクトから値を取り出し、テキストファイルに出力する。
 *     fileLineWriter.&lt;strong&gt;printDataLine&lt;/strong&gt;(fileColumnSample);
 * ……
 * // ビジネスロジックの記述例　ここまで
 * &lt;/code&gt;
 * </pre>
 * 
 * <strong>太字</strong>はFileLineWriterが提供するメソッド。 詳細は<code>printDataLine</code>を参照のこと。
 * </P>
 * @param <T> ファイル行オブジェクト
 */
public interface FileLineWriter<T> {

    /**
     * ヘッダ部に文字列を書き込む。
     * @param headerLine ヘッダ部に書き込む文字列型のリストオブジェクト
     */
    void printHeaderLine(List<String> headerLine);

    /**
     * ファイル行オブジェクトのデータを書き込む。
     * @param t ファイル行オブジェクト
     */
    void printDataLine(T t);

    /**
     * トレイラ部に文字列を書き込む。
     * @param trailerLine トレイラ部に書き込む文字列型のリストオブジェクト
     */
    void printTrailerLine(List<String> trailerLine);

    /**
     * ファイル閉塞処理。
     * <p>
     * 処理終了後に必ず実行すること。
     * </p>
     */
    void closeFile();
}
