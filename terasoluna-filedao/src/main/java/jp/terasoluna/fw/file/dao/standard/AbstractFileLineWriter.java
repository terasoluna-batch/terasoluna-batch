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

package jp.terasoluna.fw.file.dao.standard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;
import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.annotation.StringConverter;
import jp.terasoluna.fw.file.annotation.TrimType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineException;
import jp.terasoluna.fw.file.dao.FileLineWriter;
import org.apache.commons.lang3.StringUtils;


/**
 * ファイルアクセス(データ書込)用の共通クラス。
 * <p>
 * ファイルアクセス(データ書込)を行う3つのクラス(CSV、固定長、可変長) に共通する処理をまとめた抽象クラス。 ファイルの種類に対応するサブクラスが処理を行う。<br>
 * 使用例は{@link jp.terasoluna.fw.file.dao.FileLineWriter}を参照のこと。
 * </p>
 * <p>
 * ファイル取得処理は下記の手順で呼び出されるように実装すること。
 * <ul>
 * <li>初期化処理(init()、インスタンス生成時必ず１回行なう)</li>
 * <li>ヘッダ部取得(printHeaderLine())</li>
 * <li>データ部取得処理(printDataLine())</li>
 * <li>トレイラ部取得(printTrailerLine())</li>
 * </ul>
 * 上記の順番でのみ正確に出力できる。<br>
 * </p>
 * @see jp.terasoluna.fw.file.dao.FileLineWriter
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileLineWriter
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileLineWriter
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileLineWriter
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileLineWriter
 * @param <T> ファイル行オブジェクト。
 */
public abstract class AbstractFileLineWriter<T> implements FileLineWriter<T> {

    /**
     * 初期化処理時の行番号。
     */
    private static final int INITIAL_LINE_NO = -1;

    /**
     * ファイルアクセス（出力）用の文字ストリーム。
     */
    private Writer writer = null;

    /**
     * ファイルアクセスを行うファイル名。
     */
    private String fileName = null;

    /**
     * ファイルエンコーディング。
     */
    private String fileEncoding = System.getProperty("file.encoding");

    /**
     * パラメータクラスのクラス。
     */
    private Class<T> clazz = null;

    /**
     * 行区切り文字。
     */
    private String lineFeedChar = System.getProperty("line.separator");

    /**
     * ファイル行オブジェクトのField情報（Annotation）を格納する変数。
     */
    private Field[] fields = null;

    /**
     * ファイル行オブジェクトの出力設定アノテーションを格納する変数。
     */
    private OutputFileColumn[] outputFileColumns = null;

    /**
     * 各カラムごとのカラムIndexを格納する変数。
     */
    private int[] columnIndexs = null;

    /**
     * 各カラムごとのカラムのフォーマットを格納する変数。
     */
    private String[] columnFormats = null;

    /**
     * 各カラムごとのバイト数を格納する変数。
     */
    private int[] columnBytes = null;

    /**
     * 各カラムごとのパディング種別を格納する変数。
     */
    private PaddingType[] paddingTypes = null;

    /**
     * 各カラムごとのパディング文字を格納する変数。
     */
    private char[] paddingChars = null;

    /**
     * 各カラムごとのトリム種別を格納する変数。
     */
    private TrimType[] trimTypes;

    /**
     * 各カラムのトリム文字を格納する変数。
     */
    private char[] trimChars;

    /**
     * 各カラムごとの囲み文字を格納する変数。
     */
    private char[] columnEncloseChar;

    /**
     * ファイル行オブジェクトのストリングコンバータを格納する変数。
     */
    private StringConverter[] stringConverters = null;

    /**
     * ファイル行オブジェクトのストリングコンバータを格納するマップ。
     */
    @SuppressWarnings("unchecked")
    private static Map<Class, StringConverter> stringConverterCacheMap = new HashMap<Class, StringConverter>();

    /**
     * メソッドオブジェクト
     */
    private Method[] methods = null;

    /**
     * カラムフォーマット(ファイル書込）を格納するマップ。
     */
    private Map<String, ColumnFormatter> columnFormatterMap = null;

    /**
     * データ部出力確認用フラグ。
     */
    private boolean writeData = false;

    /**
     * トレイラ部出力確認用フラグ。
     */
    private boolean writeTrailer = false;

    /**
     * 書き込み処理済みデータ部の行数。
     */
    private int currentLineCount = 0;

    /**
     * 初期化処理実行フラグ。
     */
    private boolean calledInit = false;

    /**
     * 囲み文字確認用フラグ。
     */
    private boolean enclosed = false;

    /**
     * コンストラクタ。<br>
     * 引数と<code>@FileFormat</code>アノテーションの設定チェックする。 <code>@FileFormat</code>アノテーションが設定されていない場合は非検査例外をスローする。<br>
     * 区切り文字と囲み文字に同一文字が設定されている場合は、非検査例外をスローする。<br>
     * 行区切り文字が３文字以上の場合は、非検査例外をスローする。<br>
     * @param fileName ファイル名
     * @param clazz パラメータクラス
     * @param columnFormatterMap テキスト取得ルール
     */
    public AbstractFileLineWriter(String fileName, Class<T> clazz,
            Map<String, ColumnFormatter> columnFormatterMap) {

        if (fileName == null || "".equals(fileName)) {
            throw new FileException("fileName is required.",
                    new IllegalArgumentException(), fileName);
        }

        if (clazz == null) {
            throw new FileException("clazz is required.",
                    new IllegalArgumentException(), fileName);
        }

        if (columnFormatterMap == null || columnFormatterMap.isEmpty()) {
            throw new FileException("columnFormatterMap is required.",
                    new IllegalArgumentException(), fileName);
        }

        this.fileName = fileName;
        this.clazz = clazz;
        this.columnFormatterMap = columnFormatterMap;

        // FileFormatに関するチェック処理。

        // ファイルフォーマットを取得する。
        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

        // @FileFormatが無い場合、例外をスローする。
        if (fileFormat == null) {
            throw new FileException("FileFormat annotation is not found.",
                    new IllegalStateException(), fileName);
        }

        // 区切り文字と囲み文字が同じ場合、例外をスローする。
        if (fileFormat.delimiter() == fileFormat.encloseChar()) {
            throw new FileException(
                    "Delimiter is the same as EncloseChar and is no use.",
                    new IllegalStateException(), fileName);
        }

        // 文字コードを初期化する。
        if (fileFormat.fileEncoding() != null
                && !"".equals(fileFormat.fileEncoding())) {
            this.fileEncoding = fileFormat.fileEncoding();
        }

        // 行区切り文字をチェックする。設定がない場合はシステムデフォルト値を利用する。
        if (fileFormat.lineFeedChar() != null
                && !"".equals(fileFormat.lineFeedChar())) {
            this.lineFeedChar = fileFormat.lineFeedChar();
        }

        // 行区切り文字が3文字以上の場合、例外をスローする。
        if (lineFeedChar.length() > 2) {
            throw new FileException("lineFeedChar length must be 1 or 2. but: "
                    + lineFeedChar.length(), new IllegalStateException(),
                    fileName);
        }
    }

    /**
     * 初期化処理を行う。<br>
     * 初期化処理で行う処理は以下です。。
     * <ul>
     * <li>ファイル行オブジェクトの属性(Field)の取得</li>
     * <li>文字変換種別オブジェクト(stringConverters)の生成</li>
     * <li>ファイル行オブジェクトの属性に対するセッタメソッド(methods)の取得</li>
     * <li>対象ファイルの上書き設定の確認</li>
     * <li>ファイルへのストリームを開く</li>
     * </ul>
     * init()はAbstracFileLineWriterを継承するクラスのコンストラクタで 呼ぶメソッドである。<br>
     * 下位互換性のため、2回以上実行できないようにしている。
     */
    protected void init() {
        if (!calledInit) {
            // ファイルフォーマットを取得する。
            FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

            buildFields();

            if (isCheckEncloseChar()) {
                // カラムごとの囲み文字が設定されている場合、例外をスローする。
                if (enclosed) {
                    throw new FileException(
                            "columnEncloseChar can not change.",
                            new IllegalStateException(), fileName);
                }
            }

            if (isCheckColumnAnnotationCount()) {
                // ファイル行オブジェクトにアノテーションが設定されていない場合、例外をスローする。
                if (fields.length == 0) {
                    throw new FileException("OutputFileColumn is not found.",
                            new IllegalStateException(), fileName);
                }
            }

            buildStringConverters();
            buildMethods();

            // 上書きフラグを確認
            if (fileFormat.overWriteFlg()) {
                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                }
            }

            // ファイルオープン
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        (new FileOutputStream(fileName, true)), fileEncoding));
            } catch (UnsupportedEncodingException e) {
                throw new FileException("Failed in generation of writer.", e,
                        fileName);
            } catch (FileNotFoundException e) {
                throw new FileException("Failed in generation of writer.", e,
                        fileName);
            }
            calledInit = true;
        }
    }

    /**
     * ファイル行オブジェクトの属性のフィールドオブジェクトの配列を生成する。
     */
    @SuppressWarnings("unchecked")
    private void buildFields() {
        List<Field[]> fieldList = new ArrayList<Field[]>();

        // フィールドオブジェクトを生成
        Class tempClass = clazz;
        Field[] declaredFieldArray = null;
        int allFieldCount = 0;
        while (tempClass != null) {
            declaredFieldArray = tempClass.getDeclaredFields();
            fieldList.add(declaredFieldArray);
            allFieldCount += declaredFieldArray.length;
            tempClass = tempClass.getSuperclass();
        }

        // カラムインデックスの定義の順番に並び替え
        Field[] dataColumnFields = new Field[allFieldCount];

        OutputFileColumn outputFileColumn = null;
        int maxColumnIndex = -1;
        int columnIndex = -1;
        int columnCount = 0;

        for (Field[] fields : fieldList) {
            for (Field field : fields) {
                outputFileColumn = field.getAnnotation(OutputFileColumn.class);
                if (outputFileColumn != null) {
                    // マッピング可能な型のフィールドなのか確認する。
                    if (columnFormatterMap.get(field.getType().getName()) == null) {
                        throw new FileException(
                                "There is a type which isn't supported in a "
                                        + "mapping target field in FileLineObject.",
                                new IllegalStateException(), fileName);
                    }

                    columnIndex = outputFileColumn.columnIndex();
                    // カラムIndexがマイナス値なのか確認する。
                    if (columnIndex < 0) {
                        throw new FileException(
                                "Column Index in FileLineObject is the minus "
                                        + "number.",
                                new IllegalStateException(), fileName);
                    }
                    // カラムIndexがフィールド数を超えているかいるか確認する。
                    if (dataColumnFields.length <= columnIndex) {
                        throw new FileException(
                                "Column Index in FileLineObject is bigger than "
                                        + "the total number of the field.",
                                new IllegalStateException(), fileName);
                    }
                    // カラムIndexが重複してないのか確認する。
                    if (dataColumnFields[columnIndex] == null) {
                        dataColumnFields[columnIndex] = field;
                        if (maxColumnIndex < columnIndex) {
                            maxColumnIndex = columnIndex;
                        }
                        columnCount++;
                    } else {
                        throw new FileException("Column Index is duplicate : "
                                + columnIndex, fileName);
                    }
                }
            }
        }
        // columnIndexが連番で定義されているかをチェックする
        if (columnCount != (maxColumnIndex + 1)) {
            throw new FileException(
                    "columnIndex in FileLineObject is not sequential order.",
                    new IllegalStateException(), fileName);
        }

        // フィールドをコピー(nullの部分削除)
        if (dataColumnFields.length == columnCount) {
            this.fields = dataColumnFields;
        } else {
            this.fields = new Field[columnCount];
            System.arraycopy(dataColumnFields, 0, this.fields, 0, columnCount);
        }

        // OutputFileColumn（アノテーション）をListオブジェクトに格納
        outputFileColumns = new OutputFileColumn[fields.length];
        columnIndexs = new int[fields.length];
        columnFormats = new String[fields.length];
        columnBytes = new int[fields.length];
        paddingTypes = new PaddingType[fields.length];
        paddingChars = new char[fields.length];
        trimTypes = new TrimType[fields.length];
        trimChars = new char[fields.length];

        // 囲み文字設定。まずFileFormatの設定を適用する。
        columnEncloseChar = new char[fields.length];
        if (getEncloseChar() != Character.MIN_VALUE) {
            enclosed = true;
            for (int index = 0; index < fields.length; index++) {
                columnEncloseChar[index] = getEncloseChar();
            }
        }

        for (int i = 0; i < fields.length; i++) {
            outputFileColumns[i] = fields[i]
                    .getAnnotation(OutputFileColumn.class);
            columnIndexs[i] = outputFileColumns[i].columnIndex();
            columnFormats[i] = outputFileColumns[i].columnFormat();
            columnBytes[i] = outputFileColumns[i].bytes();
            paddingTypes[i] = outputFileColumns[i].paddingType();
            paddingChars[i] = outputFileColumns[i].paddingChar();
            trimTypes[i] = outputFileColumns[i].trimType();
            trimChars[i] = outputFileColumns[i].trimChar();
            // 囲み文字設定。inputFileColumnsの設定で上書きをする。
            if (outputFileColumns[i].columnEncloseChar() != Character.MIN_VALUE) {
                columnEncloseChar[i] = outputFileColumns[i].columnEncloseChar();
                enclosed = true;
            }
        }
    }

    /**
     * ファイル行オブジェクトの属性の文字変換種別オブジェクトの配列を生成する。<br>
     */
    private void buildStringConverters() {

        // 文字変換種別の配列を生成
        StringConverter[] dataColumnStringConverters = new StringConverter[fields.length];

        OutputFileColumn outputFileColumn = null;
        Class<? extends StringConverter> converterKind = null;

        for (int i = 0; i < fields.length; i++) {
            // JavaBeanの入力用のアノテーションを取得する。
            // outputFileColumn = fields[i].getAnnotation(OutputFileColumn.class);
            outputFileColumn = outputFileColumns[i];

            // OutputFileColumn.stringConverter()の内容により処理を振り分ける。
            try {
                // 文字変換種別のアノテーションを取得する。
                converterKind = outputFileColumn.stringConverter();

                // マップ内に取得した文字変換種別と一致するキーが存在するか判定する。
                if (stringConverterCacheMap.containsKey(converterKind)) {
                    // マップからオブジェクトを取得し、文字変換種別の配列にセットする。
                    dataColumnStringConverters[i] = stringConverterCacheMap
                            .get(converterKind);
                } else {
                    // インスタンスを生成し、文字変換種別の配列にセットする。
                    dataColumnStringConverters[i] = converterKind.newInstance();
                    stringConverterCacheMap.put(converterKind,
                            dataColumnStringConverters[i]);
                }

            } catch (InstantiationException e) {
                throw new FileLineException(
                        "Failed in an instantiate of a stringConverter.", e,
                        fileName, INITIAL_LINE_NO, fields[i].getName(),
                        outputFileColumn.columnIndex());

            } catch (IllegalAccessException e) {
                throw new FileLineException(
                        "Failed in an instantiate of a stringConverter.", e,
                        fileName, INITIAL_LINE_NO, fields[i].getName(),
                        outputFileColumn.columnIndex());
            }
        }
        this.stringConverters = dataColumnStringConverters;
    }

    /**
     * ファイル行オブジェクトの属性のgetterメソッドのメソッドオブジェクトの配列を生成する。<br>
     * 属性に対するgetterメソッドは以下のルールで検索する。<br>
     * <ul>
     * <li>属性名の最初の文字を大文字にした文字列の先頭に「get」をつけたもの。</li>
     * <li>is～()、has～()などのgetterメソッドは検索対象外です。</li>
     * </ul>
     * getterメソッドが検索できない場合は例外が発生する。
     * @throws FileException getterメソッドが見つからなかった場合。
     */
    private void buildMethods() {
        Method[] dataColumnGetMethods = new Method[fields.length];
        StringBuilder getterName = new StringBuilder();
        String fieldName = null;

        for (int i = 0; i < fields.length; i++) {
            // JavaBeanから処理の対象となる属性の属性名を取得する。
            fieldName = fields[i].getName();

            // 属性名を元に、getterメソッドの名前を生成する。
            getterName.setLength(0);
            getterName.append("get");
            getterName.append(StringUtils.upperCase(fieldName.substring(0, 1)));
            getterName.append(fieldName.substring(1, fieldName.length()));

            // getterのリフレクションオブジェクトを取得する。
            try {
                dataColumnGetMethods[i] = clazz
                        .getMethod(getterName.toString());
            } catch (NoSuchMethodException e) {
                throw new FileException(
                        "The getter method of column doesn't exist.", e,
                        fileName);
            }
        }
        this.methods = dataColumnGetMethods;
    }

    /**
     * ヘッダ部への書込み処理。
     * @param headerLine ヘッダ部へ書き込む文字列のリスト
     */
    public void printHeaderLine(List<String> headerLine) {
        if (writeData || writeTrailer) {
            throw new FileException("Header part should be called before "
                    + "data part or trailer part.",
                    new IllegalStateException(), fileName);
        }
        printList(headerLine);
    }

    /**
     * データ部への書き込み処理。
     * @param t データ部へ書き込むファイル行オブジェクト
     */
    public void printDataLine(T t) {
        checkWriteTrailer();
        // ファイル書き込みの初期化
        StringBuilder fileLineBuilder = new StringBuilder();

        // 固定長ファイルの場合
        // (区切り文字、囲み文字がない場合は固定長ファイルと判断する。)
        if (getDelimiter() == Character.MIN_VALUE
                && getEncloseChar() == Character.MIN_VALUE) {
            for (int i = 0; i < fields.length; i++) {
                fileLineBuilder.append(getColumn(t, i));
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                // 囲み文字、区切り文字の追加処理。
                if (columnEncloseChar[i] != Character.MIN_VALUE) {
                    fileLineBuilder.append(columnEncloseChar[i]);
                    fileLineBuilder.append(getColumn(t, i));
                    fileLineBuilder.append(columnEncloseChar[i]);
                } else {
                    fileLineBuilder.append(getColumn(t, i));
                }
                fileLineBuilder.append(getDelimiter());
            }
            // 一番最後の区切り文字を削除する。
            if (fileLineBuilder.length() > 0) {
                fileLineBuilder.deleteCharAt(fileLineBuilder.length() - 1);
            }
        }

        // 行区切り文字を追加する。
        fileLineBuilder.append(getLineFeedChar());

        // ファイルへの書き込み処理。
        try {
            getWriter().write(fileLineBuilder.toString());
        } catch (IOException e) {
            throw new FileException("Processing of writer was failed.", e,
                    fileName);
        }
        currentLineCount++;
        setWriteData(true);
    }

    /**
     * トレイラ部への書込み処理。
     * @param trailerLine トレイラ部へ書き込む文字列のリスト
     */
    public void printTrailerLine(List<String> trailerLine) {
        printList(trailerLine);
        writeTrailer = true;
    }

    /**
     * ヘッダ部、トレイラ部の書き込み用の共通メソッド。
     * @param stringList 文字列のリスト
     */
    private void printList(List<String> stringList) {
        for (String stringData : stringList) {
            try {
                getWriter().write(stringData);
                getWriter().write(lineFeedChar);
            } catch (IOException e) {
                throw new FileException("Processing of writer was failed.", e,
                        fileName);
            }
        }
    }

    /**
     * ファイルクローズ処理。
     */
    public void closeFile() {
        try {
            getWriter().flush();
            getWriter().close();
        } catch (IOException e) {
            throw new FileException("Closing of writer was failed.", e,
                    fileName);
        }
    }

    /**
     * <p>
     * ファイル行オブジェクトからカラムインデックスと一致する属性の値を取得する。
     * </p>
     * <p>
     * 属性を取得する際、ファイル行オブジェクトのアノテーションの記述により 以下の処理を行う。<br>
     * <li>トリム処理<br>
     * <li>パディング<br>
     * <li>文字変換処理<br>
     * <br>
     * ファイル行オブジェクトのアノテーションでカラムのバイト長が指定されている場合、<br>
     * 返却する文字列がバイト長と一致しているか確認する。
     * </p>
     * @param t ファイル行オブジェクト
     * @param index カラムのインデックス
     * @return カラムの文字列
     */
    protected String getColumn(T t, int index) {
        // ファイルに書き込むカラムの文字列。
        String columnString = null;

        // ファイル行オブジェクト(t)からカラムインデックスと一致する属性の値を取得する。
        ColumnFormatter columnFormatter = columnFormatterMap.get(methods[index]
                .getReturnType().getName());
        try {
            columnString = columnFormatter.format(t, methods[index],
                    columnFormats[index]);
        } catch (IllegalArgumentException e) {
            throw new FileLineException("Failed in column data formatting.", e,
                    fileName, currentLineCount + 1, fields[index].getName(),
                    columnIndexs[index]);
        } catch (IllegalAccessException e) {
            throw new FileLineException("Failed in column data formatting.", e,
                    fileName, currentLineCount + 1, fields[index].getName(),
                    columnIndexs[index]);
        } catch (InvocationTargetException e) {
            throw new FileLineException("Failed in column data formatting.", e,
                    fileName, currentLineCount + 1, fields[index].getName(),
                    columnIndexs[index]);
        }

        if (columnString == null) {
            columnString = "";
        }

        // トリム処理
        columnString = FileDAOUtility.trim(columnString, fileEncoding,
                trimChars[index], trimTypes[index]);

        // パディング処理
        columnString = FileDAOUtility.padding(columnString, fileEncoding,
                columnBytes[index], paddingChars[index], paddingTypes[index]);

        // 文字列変換処理
        // OutputFileColumn.stringConverter()の内容により処理を振り分ける。
        columnString = stringConverters[index].convert(columnString);

        // カラムのバイト数チェック。
        if (isCheckByte(columnBytes[index])) {
            try {
                // 固定長出力時、Bytes値の設定が時の例外
                if (columnBytes[index] <= 0) {
                    throw new FileLineException("bytes is not set "
                            + "or a number equal to or less than 0 is set.",
                            new IllegalStateException(), getFileName(),
                            currentLineCount + 1, fields[index].getName(),
                            columnIndexs[index]);
                }
                // 設定されたBytes値とデータのサイズが違う場合は例外発生
                if (columnString.getBytes(fileEncoding).length != columnBytes[index]) {
                    throw new FileLineException(
                            "The data size is different from bytes value of "
                                    + "the set value of the column .",
                            new IllegalStateException(), fileName,
                            currentLineCount + 1, fields[index].getName(),
                            columnIndexs[index]);
                }
            } catch (UnsupportedEncodingException e) {
                throw new FileException(
                        "fileEncoding which isn't supported was set.", e,
                        fileName);
            }
        }
        return columnString;
    }

    /**
     * ファイル名を取得する。
     * @return fileName ファイル名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 行区切り文字を設定する。
     * @return lineFeedChar 行区切り文字
     */
    protected String getLineFeedChar() {
        return lineFeedChar;
    }

    /**
     * 行区切り文字を設定する。
     * @param lineFeedChar 行区切り文字
     */
    protected void setLineFeedChar(String lineFeedChar) {
        this.lineFeedChar = lineFeedChar;
    }

    /**
     * カラムフォーマット(ファイル書込）処理を格納するマップを取得する。
     * @param columnFormatterMap カラムフォーマット(ファイル書込）を格納するマップ
     */
    public void setColumnFormatterMap(
            Map<String, ColumnFormatter> columnFormatterMap) {
        this.columnFormatterMap = columnFormatterMap;
    }

    /**
     * ファイルアクセス（出力）用の文字ストリームを取得する。
     * @return bufferedWriter ファイルアクセス（出力）用の文字ストリーム
     */
    protected Writer getWriter() {
        return writer;
    }

    /**
     * ファイル行オブジェクトのField情報（Annotation）を格納する変数を取得する。
     * @return fields ファイル行オブジェクトのField情報（Annotation）を格納する変数
     */
    protected Field[] getFields() {
        return fields;
    }

    /**
     * ファイル行オブジェクトのField情報に対応するgetterメソッドを格納する変数を取得する。
     * @return methods ファイル行オブジェクトのField情報に対応するgetterメソッドを格納する変数
     */
    protected Method[] getMethods() {
        return methods;
    }

    /**
     * データ部の出力が開始されているかどうかを判定するフラグ。
     * @param writeData フラグ
     */
    protected void setWriteData(boolean writeData) {
        this.writeData = writeData;
    }

    /**
     * トレイラ部の処理が終わっているかどうかを判定する。<br>
     * 処理が完了している場合、例外をスローする。
     */
    protected void checkWriteTrailer() {
        if (writeTrailer) {
            throw new FileException("Header part or data part should be "
                    + "called before TrailerPart", new IllegalStateException(),
                    fileName);
        }
    }

    /**
     * 区切り文字を取得する。
     * @return 区切り文字
     */
    public abstract char getDelimiter();

    /**
     * 囲み文字を取得する。
     * @return 囲み文字
     */
    public abstract char getEncloseChar();

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。
     * <p>
     * 固定長ファイル形式の場合は常に<code>true</code>を返却してバイト数チェックを行う。<br>
     * 可変長、ＣＳＶ形式で<code>bytes</code>が指定されているときには trueを返却しバイト数チェックを行う。
     * </p>
     * @param outputFileColumn 対象カラムのOutputFileColumn情報
     * @return バイト数が設定されている(1バイト以上)場合はtrue。
     */
    protected boolean isCheckByte(OutputFileColumn outputFileColumn) {

        if (0 < outputFileColumn.bytes()) {
            return true;
        }

        return false;
    }

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。
     * @param columnByte 対象カラムのバイト数
     * @return バイト数が設定されている(1バイト以上)場合はtrue。
     */
    protected boolean isCheckByte(int columnByte) {
        if (0 < columnByte) {
            return true;
        }
        return false;
    }

    /**
     * 囲み文字が設定されていない事をチェックするかどうかを返す。
     * @return チェックを行う場合はtrue。
     */
    protected boolean isCheckEncloseChar() {
        return false;
    }

    /**
     * ファイル行オブジェクトにアノテーションが設定されている事をチェックするかどうかを返す。
     * @return チェックを行う場合はtrue。
     */
    protected boolean isCheckColumnAnnotationCount() {
        return true;
    }

    /**
     * カラムの囲み文字を取得する。
     * @return columnEncloseChar 囲み文字
     */
    protected char[] getColumnEncloseChar() {
        return columnEncloseChar;
    }
}
