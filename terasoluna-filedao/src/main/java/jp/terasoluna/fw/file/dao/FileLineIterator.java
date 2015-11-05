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

import java.util.Iterator;
import java.util.List;

/**
 * ファイルアクセス(データ取得)用のイテレータインタフェース。
 * <P>
 * テキストファイルを読み、ファイルの文字列をファイル行オブジェクトに格納する。 FileLineIteratorのインスタンス生成はFileQueryDAOが行う。 詳細は
 * {@link jp.terasoluna.fw.file.dao.FileQueryDAO}を参照のこと。<br>
 * <strong>使用例</strong><br>
 * <li>ファイル行オブジェクトを取得する例。
 * 
 * <pre>
 * &lt;code&gt;
 * // ファイルから1レコードのデータを入力しFileColumnSample型のオブジェクトに格納する
 * ……
 *     while(fileLineIterator.&lt;strong&gt;hasNext()&lt;/strong&gt;){
 *        FileColumnSample fileColumnSample = fileLineIterator.&lt;strong&gt;next()&lt;/strong&gt;;
 * ……
 * &lt;/code&gt;
 * </pre>
 * 
 * <strong>太字</strong>はFileLineIteratorが提供するメソッド。 詳細は{@link #hasNext()}、{@link #next()}を参照のこと。
 * </P>
 * @param <T> ファイル行オブジェクト。
 */
public interface FileLineIterator<T> extends Iterator<T> {

    /**
     * ファイルからデータが取得できるか確認する。
     * <p>
     * 繰り返し処理でさらに要素がある場合に<code>true</code> を返す。
     * </p>
     * @return 反復子がさらに要素を持つ場合は<code>true</code>。
     */
    boolean hasNext();

    /**
     * ファイル行オブジェクトを返却する。
     * <p>
     * <code>hasNext()</code>メソッドが<code>false</code>を返すまで このメソッド呼び出す毎に、ファイル行オブジェクトを1つ返却する。
     * </p>
     * @return 次のファイル行オブジェクト。
     */
    T next();

    /**
     * ヘッダ部の文字列を返却する。
     * <p>
     * ヘッダ部のデータを文字列のリストとして呼び出し元に返却する。
     * </p>
     * @return 文字型のリスト。
     */
    List<String> getHeader();

    /**
     * トレイラ部の文字列を返却する。
     * <p>
     * トレイラ部のデータを文字列のリストとして呼び出し元に返却する。
     * </p>
     * @return 文字型のリスト。
     */
    List<String> getTrailer();

    /**
     * スキップ処理。
     * <p>
     * ファイル入力機能では、入力を開始する行を指定できる。<br>
     * 主に、リスタートポイントからファイルの読込を再開するときに利用する。
     * </p>
     * @param skipLines 読み飛ばす行数
     */
    void skip(int skipLines);

    /**
     * ファイルクローズ.
     * <p>
     * ファイルの入力ストリームを閉じる。 ファイル入力が完了した段階で必ず実行すること。
     * </p>
     */
    void closeFile();

}
