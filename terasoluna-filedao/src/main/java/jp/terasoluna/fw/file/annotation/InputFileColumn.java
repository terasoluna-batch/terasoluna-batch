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
 * 入力設定用のアノテーション。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface InputFileColumn {
    /**
     * カラムIndex。<br>
     * <br>
     * カラムのIndex（順番）を示す。<b>入力必須項目</b>。<br>
     * カラムIndexは同じクラスの中で重複しないように実装すること。
     */
    int columnIndex();

    /**
     * カラムのフォーマット。<br>
     * <br>
     * DATE型、BigDecimal型の入力値のフォーマットを示す。<br>
     * (例)"yyyy/MM/dd","###,###,###"<br>
     */
    String columnFormat() default "";

    /**
     * カラムのバイト数。
     * <p>
     * 固定長入出力の各カラムのバイト数を示す。また、その他のファイルでパディング処理を行う場合に入力を行う。
     * </p>
     * <b>固定長ファイルの場合、入力必須項目。</b><br>
     * <b>パディング処理を行う場合、入力必須項目。</b><br>
     */
    int bytes() default -1;

    /**
     * パディング種別。<br>
     * <br>
     * パディングの種別(右詰/左詰/パディングなし[LEFT/RIGHT/NONE])を示す。
     */
    PaddingType paddingType() default PaddingType.NONE;

    /**
     * パディング文字。<br>
     * <br>
     * パディングする文字を示す。<b>(半角文字1文字のみ設定可能。)</b><br>
     * パディング文字は、ジョブBean定義ファイルに設定された文字列の1文字目の半角文字のみ有効となる。<br>
     * 全角文字が入力された場合はエラーとなり処理を終了する。<br>
     * 2文字以上入力しても1文字目のみ処理に利用される。<br>
     * 区切り文字、囲み文字、行区切り文字と同じ文字を利用することはできない。
     */
    char paddingChar() default ' ';

    /**
     * 文字変換種別。<br>
     * <br>
     * String型の項目について、大文字変換・小文字変換・無変換を示す。<br>
     * 大文字変換:StringConverterToUpperCase.class<br>
     * 小文字変換:StringConverterToLowerCase.class<br>
     * 無変換:NullStringConverter.class<br>
     */
    Class<? extends StringConverter> stringConverter() default NullStringConverter.class;

    /**
     * トリム種別。<br>
     * <br>
     * トリムの種別(右詰/左詰/トリムなし[LEFT/RIGHT/NONE])を示す。
     */
    TrimType trimType() default TrimType.NONE;

    /**
     * トリム文字。<br>
     * <br>
     * トリムする文字を示す。<b>(半角文字のみ設定可能)</b><br>
     * トリム文字は、ジョブBean定義ファイルに設定された文字列の1文字目の半角文字のみ有効となる。<br>
     * 全角文字が入力された場合はエラーとなり処理を終了する。<br>
     * 2文字以上入力しても1文字目のみ処理に利用される。<br>
     * 区切り文字、囲み文字、行区切り文字と同じ文字を利用することはできない。
     */
    char trimChar() default ' ';

    /**
     * 囲み文字。
     * <p>
     * CSV,可変長ファイルの各カラムの囲み文字を設定する。囲み文字は半角文字に限る。<br>
     * 「'\u0000'（char型の最小値）」を設定すると、フレームワークは囲み文字無しと判断する。 デフォルト値は「'\u0000'（char型の最小値）」。
     * </p>
     */
    char columnEncloseChar() default Character.MIN_VALUE;
}
