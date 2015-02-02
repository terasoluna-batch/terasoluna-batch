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

package jp.terasoluna.fw.validation;

/**
 * 複数のフィールドの相関入力チェックを行うインタフェース。
 *
 * 複数フィールド間の依存関係による入力チェックを実行する場合は、
 * このインタフェースを実装したクラスを作成する。
 * {@link #validate(Object, Object[])} メソッドの第一引数には検証対象の値、
 * 第二引数には依存するフィールドの値が配列で渡される。検証エラーの場合は
 * <code>false</code> を返却すること。<br>
 * 検証対象のフィールドは <code>null</code> または空文字で渡される場合がある
 * ので、注意が必要である。また、この検証ルールにはデフォルトの
 * エラーメッセージが存在しないため、<code>validation.xml</code>
 * には必ずメッセージの設定を行うこと。<br>
 * <strong>※この検証ルールはJavaScriptでのチェックをサポートしていない。
 * </strong>
 * <br>
 * <br>
 * valueフィールドの値が、value1フィールドの値以上、value2フィールドの値以下
 * であることを検証する場合、以下のように実装、設定を行う。
 * <h5>{@link MultiFieldValidator} の実装例</h5>
 * <code><pre>
 * public boolean validate(Object value, Object[] fields) {
 *     if (!(value instanceof Integer)) {
 *         return false;
 *     }
 *     if (!(fields[0] instanceof Integer)) {
 *         return false;
 *     }
 *     if (!(fields[1] instanceof Integer)) {
 *         return false;
 *     }
 *     int value0 = Integer.parseInt(value);
 *     int value1 = Integer.parseInt(fields[0]);
 *     int value2 = Integer.parseInt(fields[1]);
 *     return (value1 <= value0 && value2 >= value0);
 * }
 * </pre></code>
 * <h5>validation.xmlの設定例</h5>
 * <code><pre>
 * &lt;form name=&quot;/validateMultiField&quot;&gt;
 *   &lt;field property=&quot;value&quot; depends=&quot;multiField&quot;&gt;
 *     &lt;msg key=&quot;errors.multiField&quot;
 *             name=&quot;multiField&quot;/&gt;
 *     &lt;arg key=&quot;label.value&quot; position=&quot;0&quot; /&gt;
 *     &lt;arg key=&quot;label.value1&quot; position=&quot;1&quot; /&gt;
 *     &lt;arg key=&quot;label.value2&quot; position=&quot;2&quot; /&gt;
 *     &lt;var&gt;
 *       &lt;var-name&gt;fields&lt;/var-name&gt;
 *       &lt;var-value&gt;value1,value2&lt;/var-value&gt;
 *     &lt;/var&gt;
 *     &lt;var&gt;
 *       &lt;var-name&gt;multiFieldValidator&lt;/var-name&gt;
 *       &lt;var-value&gt;sample.SampleMultiFieldValidator&lt;/var-value&gt;
 *     &lt;/var&gt;
 *   &lt;/field&gt;
 * &lt;/form&gt;
 * </pre></code>
 * <h5>メッセージリソースファイルの設定例</h5>
 * <code>
 * errors.multiField={0}は{1}から{2}の間の値を入力してください。
 * </code>
 *
 */
public interface MultiFieldValidator {

    /**
     * 複数フィールドの相関入力チェックを実行する。
     * <br>
     * 検証対象の値は第一引数で渡される。検証に必要な他のフィールドの
     * 値は第二引数に配列として渡される。検証エラーの場合は <code>false</code>
     * を返却すること。
     *
     * @param value 検証対象の値
     * @param fields 検証に必要な他のフィールドの値配列
     * @return エラーがなければ <code>true</code>
     */
    boolean validate(Object value, Object[] fields);

}
