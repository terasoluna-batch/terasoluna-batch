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

package jp.terasoluna.fw.exception;


/**
 * フレームワークから発生する汎用例外クラス。
 *
 * <p>
 * 処理の続行が困難な場合にフレームワークから発生する例外クラス。<br>
 * 本クラスにはメッセージキーとメッセージ置換文字列を設定することができる。
 * 但し、メッセージ取得、及びプレースホルダの置換は別のクラスで実行し、
 * 再度 {@link #setMessage(String)} を実行してメッセージを設定する必要がある。
 * メッセージが設定されていない状態で、{@link #getMessage()}
 * メソッドを用いてメッセージを取得するとメッセージキーが返却される。
 * 従って、メッセージ置換が行えないような状況で本クラスを使用する場合は、
 * コンストラクタのerrorCodeに、メッセージキーではなくエラーコードや
 * エラーメッセージを直接設定すること。
 * </p>
 *
 */
public class SystemException extends RuntimeException {

    /**
     * シリアルバージョンID。
     */
    private static final long serialVersionUID = -3348737638719112576L;

    /**
     * エラーコード。
     */
    private String errorCode = null;

    /**
     * エラーメッセージの置換文字列。
     */
    private String[] options = null;

    /**
     * エラーメッセージ。
     */
    private String message = null;

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     */
    public SystemException(Throwable cause) {
        super(cause);
        this.errorCode = "";
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     */
    public SystemException(Throwable cause,
                            String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     * @param optionStrings メッセージ中の{n}を置換する文字列の配列
     */
    public SystemException(Throwable cause,
                            String errorCode,
                            String[] optionStrings) {
        super(cause);
        this.errorCode = errorCode;
        this.options = optionStrings;
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     * @param s0 エラーメッセージ中の{0}を置換する文字列
     */
    public SystemException(Throwable cause,
                            String errorCode,
                            String s0) {
        super(cause);
        this.errorCode = errorCode;
        this.options = new String[]{s0};
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     * @param s0 エラーメッセージ中の{0}を置換する文字列
     * @param s1 エラーメッセージ中の{1}を置換する文字列
     */
    public SystemException(Throwable cause,
                            String errorCode,
                            String s0,
                            String s1) {
        super(cause);
        this.errorCode = errorCode;
        this.options = new String[]{s0, s1};
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     * @param s0 エラーメッセージ中の{0}を置換する文字列
     * @param s1 エラーメッセージ中の{1}を置換する文字列
     * @param s2 エラーメッセージ中の{2}を置換する文字列
     */
    public SystemException(Throwable cause,
                            String errorCode,
                            String s0,
                            String s1,
                            String s2) {
        super(cause);
        this.errorCode = errorCode;
        this.options = new String[]{s0, s1, s2};
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因となった例外
     * @param errorCode エラーコード
     * @param s0 エラーメッセージ中の{0}を置換する文字列
     * @param s1 エラーメッセージ中の{1}を置換する文字列
     * @param s2 エラーメッセージ中の{2}を置換する文字列
     * @param s3 エラーメッセージ中の{3}を置換する文字列
     */
    public SystemException(Throwable cause,
                            String errorCode,
                            String s0,
                            String s1,
                            String s2,
                            String s3) {
        super(cause);
        this.errorCode = errorCode;
        this.options = new String[]{s0, s1, s2, s3};
    }

    /**
     * エラーコードを取得する。
     *
     * @return エラーコード
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * エラーメッセージの置換文字列を取得する。
     *
     * @return エラーメッセージ置換文字列
     */
    public String[] getOptions() {
        return this.options;
    }

    /**
     * エラーメッセージを格納する。
     *
     * @param message エラーメッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * エラーメッセージを返却する。
     * 外部から {@link #setMessage(String)} を用いてメッセージが設定されていない
     * 場合は、コンストラクタのerrorCodeに指定した文字列を返却する。
     *
     * @return
     *     エラーメッセージ、またはコンストラクタのerrorCodeに指定された文字列
     */
    @Override
    public String getMessage() {
        if (this.message == null) {
            return this.errorCode;
        }
        return this.message;
    }
}
