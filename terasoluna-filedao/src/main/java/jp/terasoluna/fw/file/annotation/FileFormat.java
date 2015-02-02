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

package jp.terasoluna.fw.file.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 入出力設定用のアノテーション。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface FileFormat {
    /**
     * 行区切り文字。
     * <p>
     * 行区切り文字を設定する。行区切り文字は半角文字に限る。なお、エスケープシーケンスを使う行区切り文字は以下に挙げるものに限る。<br>
     * <ul>
     * <li>\r</li>
     * <li>\n</li>
     * <li>\r\n</li>
     * </ul>
     * デフォルト値は実行環境に依存する。
     * </p>
     */
    String lineFeedChar() default "";

    /**
     * 区切り文字。
     * <p>
     * CSV,可変長ファイルの区切り文字を設定する。区切り文字は半角文字に限る。<br>
     * デフォルト値は「','（カンマ）」。
     * </p>
     */
    char delimiter() default ',';

    /**
     * 囲み文字。
     * <p>
     * CSV,可変長ファイルの囲み文字を設定する。囲み文字は半角文字に限る。<br>
     * 「'\u0000'（char型の最小値）」を設定すると、フレームワークは囲み文字無しと判断する。 デフォルト値は「'\u0000'（char型の最小値）」。
     * </p>
     */
    char encloseChar() default Character.MIN_VALUE;

    /**
     * ファイルエンコーディング。
     * <p>
     * 入出力を行うファイルのエンコーディングを設定する。<br>
     * デフォルト値は実行環境に依存する。
     */
    String fileEncoding() default "";

    /**
     * ヘッダ行数。
     * <p>
     * 入力ファイルのヘッダ部に相当する行数を設定する。<br>
     * デフォルト値は「0（ゼロ）」。
     */
    int headerLineCount() default 0;

    /**
     * トレイラ行数。
     * <p>
     * 入力ファイルのトレイラ部に相当する行数を設定する。<br>
     * デフォルト値は「0（ゼロ）」。
     * </p>
     */
    int trailerLineCount() default 0;

    /**
     * 上書きフラグ。
     * <p>
     * 出力ファイルと同じファイルが存在する場合に上書きするかどうかを設定する。<br>
     * デフォルト値は「false（上書きしない）」。
     * </p>
     */
    boolean overWriteFlg() default false;
}
