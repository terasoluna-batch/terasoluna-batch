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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.InputFileColumn;
import jp.terasoluna.fw.file.annotation.PaddingType;
import jp.terasoluna.fw.file.annotation.StringConverter;
import jp.terasoluna.fw.file.annotation.TrimType;
import jp.terasoluna.fw.file.dao.FileException;
import jp.terasoluna.fw.file.dao.FileLineException;
import jp.terasoluna.fw.file.dao.FileLineIterator;

import org.apache.commons.lang3.StringUtils;

/**
 * ファイルアクセス(データ取得)用の共通クラス。
 * <p>
 * ファイルアクセス(データ取得)を行う3つのクラス(CSV、固定長、可変長) に共通する処理をまとめた抽象クラス。 ファイルの種類に対応するサブクラスが処理を行う。<br>
 * 使用例は{@link jp.terasoluna.fw.file.dao.FileLineIterator}を参照のこと。
 * </p>
 * ファイル取得処理はデータを先頭から順番に読み込むため、下記の手順で呼び出されるように実装する必要があります。<br>
 * <ul>
 * <li>ヘッダ部取得(getHeader())</li>
 * <li>スキップ処理(skip())</li>
 * <li>データ部取得処理(hasNext()、readLine())</li>
 * <li>トレイラ部取得(getTrailer())</li>
 * </ul>
 * もし、トレイラ部の取得を行うと内部で残っているデータ部を全部スキップするため、 処理途中にトレイラ部を取得するとデータ部の取得が出来なくなります。<br>
 * トレイラ部の取得後データ部取得処理を実行すると<code>IllegalStateException<code>が発生します。<br>
 * @see jp.terasoluna.fw.file.dao.FileLineIterator
 * @see jp.terasoluna.fw.file.dao.standard.CSVFileLineIterator
 * @see jp.terasoluna.fw.file.dao.standard.FixedFileLineIterator
 * @see jp.terasoluna.fw.file.dao.standard.VariableFileLineIterator
 * @see jp.terasoluna.fw.file.dao.standard.PlainFileLineIterator
 * @param <T> ファイル行オブジェクト。
 */
public abstract class AbstractFileLineIterator<T> implements
                                              FileLineIterator<T> {

    /**
     * 初期化処理をあらわす行番号。
     */
    private static final int INITIAL_LINE_NO = -1;

    /**
     * ファイル名。
     */
    private String fileName = null;

    /**
     * 結果クラス。
     */
    private Class<T> clazz = null;

    /**
     * 行区切り文字。
     */
    private String lineFeedChar = System.getProperty("line.separator");

    /**
     * ファイルエンコーディング。
     */
    private String fileEncoding = System.getProperty("file.encoding");

    /**
     * ヘッダ行数。
     */
    private int headerLineCount = 0;

    /**
     * トレイラ行数。
     */
    private int trailerLineCount = 0;

    /**
     * ファイル入力処理済みのデータ部の行数。
     */
    private int currentLineCount = 0;

    /**
     * ファイルアクセス用の文字ストリーム。
     */
    private BufferedReader reader = null;

    /**
     * ファイル行オブジェクトのField情報（Annotation）を格納する変数。
     */
    private Field[] fields = null;

    /**
     * ファイル行オブジェクトの入力設定アノテーションを格納する変数。
     */
    private InputFileColumn[] inputFileColumns = null;

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
     * 1行分のバイト数を格納する変数。
     */
    private int totalBytes = 0;

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
    @SuppressWarnings("rawtypes")
    private static Map<Class, StringConverter> stringConverterCacheMap = new ConcurrentHashMap<Class, StringConverter>();

    /**
     * ファイル行オブジェクトのFieldに対応するsetterメソッドを格納する。
     */
    private Method[] methods = null;

    /**
     * カラムパーサーを格納するマップ。
     */
    private Map<String, ColumnParser> columnParserMap = null;

    /**
     * ヘッダ部の文字列リスト。
     */
    private List<String> header = new ArrayList<String>();

    /**
     * トレイラ部の文字列リスト。
     */
    private List<String> trailer = new ArrayList<String>();

    /**
     * トレイラ部処理確認用フラグ。
     */
    private boolean readTrailer = false;

    /**
     * トレイラ部の一時格納用のキュー。
     */
    private Queue<String> trailerQueue = null;

    /**
     * 1行分の文字列を読み込むオブジェクト
     */
    private LineReader lineReader = null;

    /**
     * 初期化処理確認用フラグ。
     */
    private boolean calledInit = false;

    /**
     * 囲み文字確認用フラグ。
     */
    private boolean enclosed = false;

    /**
     * コンストラクタ。<br>
     * 引数のチェック及び、ファイル行オブジェクトのFileFormatアノテーション 設定のチェックを行う。<br>
     * チェック結果問題がある場合は例外を発生する。<br>
     * @param fileName ファイル名最後に移動
     * @param clazz ファイル行オブジェクトクラス
     * @param columnParserMap フォーマット処理リスト
     * @throws FileException 初期化処理で失敗した場合。
     */
    public AbstractFileLineIterator(String fileName, Class<T> clazz,
            Map<String, ColumnParser> columnParserMap) {
        // ファイル名の必須チェックを行う。
        if (fileName == null || "".equals(fileName)) {
            throw new FileException("fileName is required.", new IllegalArgumentException(), fileName);
        }

        // ファイル行オブジェクトクラスの必須チェックを行う。
        if (clazz == null) {
            throw new FileException("clazz is required.", new IllegalArgumentException(), fileName);
        }

        // フォーマット処理リストの必須チェックを行う。
        if (columnParserMap == null || columnParserMap.isEmpty()) {
            throw new FileException("columnFormaterMap is required.", new IllegalArgumentException(), fileName);
        }

        // ファイル行オブジェクトクラスがインスタンス化できるかをチェックする。
        try {
            clazz.newInstance();
        } catch (InstantiationException e) {
            throw new FileException("Failed in instantiation of clazz.", e, fileName);
        } catch (IllegalAccessException e) {
            throw new FileException("clazz's nullary  constructor is not accessible", e, fileName);
        }

        this.fileName = fileName;
        this.clazz = clazz;
        this.columnParserMap = columnParserMap;

        // FileFormatアノテーションの設定をチェックする。
        FileFormat fileFormat = clazz.getAnnotation(FileFormat.class);

        // ファイル行オブジェクトのClassにFileFormatアノテーションがあるかチェックする。
        if (fileFormat == null) {
            throw new FileException("FileFormat annotation is not found.", new IllegalStateException(), fileName);
        }

        // 区切り文字と囲み文字が同じ場合、例外をスローする。
        if (fileFormat.delimiter() == fileFormat.encloseChar()) {
            throw new FileException("Delimiter is the same as EncloseChar and is no use.", new IllegalStateException(), fileName);
        }

        // 行区切り文字をチェックする。設定がない場合はシステムデフォルト値を利用する。
        if (fileFormat.lineFeedChar() != null && !"".equals(fileFormat
                .lineFeedChar())) {
            this.lineFeedChar = fileFormat.lineFeedChar();
        }

        // ファイルエンコーディングをチェックする。設定がない場合はシステムデフォルト値を利用する。
        if (fileFormat.fileEncoding() != null && !"".equals(fileFormat
                .fileEncoding())) {
            this.fileEncoding = fileFormat.fileEncoding();
        }

        // ヘッダ行数を設定する。
        this.headerLineCount = fileFormat.headerLineCount();

        // トレイラ行数を設定する。
        this.trailerLineCount = fileFormat.trailerLineCount();
    }

    /**
     * 次の行のレコードがあるかどうか確認する。<br>
     * 繰り返し処理でさらに要素がある場合に true を返します。
     * @return 繰り返し処理でさらに要素がある場合に <code>true</code>
     * @throws FileException リーダからIOExceptionが発生した場合。
     */
    @Override
    public boolean hasNext() {
        try {
            reader.mark(1);
            if (reader.read() != -1) {
                return true;
            }
        } catch (IOException e) {
            throw new FileException("Processing of reader was failed.", e, fileName);
        } finally {
            try {
                reader.reset();
            } catch (IOException e) {
                throw new FileException("Processing of reader#reset was failed.", e, fileName);
            }
        }

        return false;
    }

    /**
     * 繰り返し処理でファイル行オブジェクトを返却する。<br>
     * <p>
     * 次の行のレコードの情報をファイル行オブジェクトに格納して返却します。<br>
     * 繰り返し処理で次の要素を返します。<br>
     * </p>
     * 次の行のレコードの情報はファイル行オブジェクトのInputFileColumnの定義に 基づいて格納される。<br>
     * もし、ファイル行オブジェクトのマッピングフィールドの数と合わない レコード情報が来た場合は例外を発生する。<br>
     * また、InputFileColumnに設定されたバイト数と違う情報が来た場合も例外を発生する。<br>
     * それではない場合は以下の順番でデータを処理し格納する。<br>
     * <ul>
     * <li>トリム処理</li>
     * <li>パディング処理</li>
     * <li>文字列変換処理</li>
     * <li>型変換(マッピング)処理</li>
     * </ul>
     * @return ファイル行オブジェクト
     * @throws FileException ファイル行オブジェクトの生成に失敗した場合。
     * @throws FileLineException ファイル行オブジェクトの取得に失敗した場合。
     */
    @Override
    public T next() {
        if (readTrailer) {
            throw new FileLineException("Data part should be called before trailer part.", new IllegalStateException(), fileName, currentLineCount);
        }

        if (!hasNext()) {
            throw new FileLineException("The data which can be acquired doesn't exist.", new NoSuchElementException(), fileName, currentLineCount
                    + 1);
        }

        T fileLineObject = null;

        // 次の行データを読む。hasNext()チェックを行ったため、nullの場合ない。
        String currentString = readLine();
        currentLineCount++;

        // ファイル行オブジェクトを新たに生成する処理。
        try {
            fileLineObject = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new FileException("Failed in an instantiate of a FileLineObject.", e, fileName);
        } catch (IllegalAccessException e) {
            throw new FileException("Failed in an instantiate of a FileLineObject.", e, fileName);
        }

        // CSVの区切り文字にしたがって入力データを分解する。
        // 区切り文字はアノテーションから取得する。
        String[] columns = separateColumns(currentString);

        // ファイルから読み取ったカラム数とファイル行オブジェクトのカラム数を比較する。
        if (fields.length != columns.length) {
            throw new FileLineException("Column Count is different from "
                    + "FileLineObject's column counts", new IllegalStateException(), fileName, currentLineCount);
        }

        int columnIndex = -1;
        String columnString = null;

        for (int i = 0; i < fields.length; i++) {

            // JavaBeanの入力用のアノテーションを設定する。
            columnIndex = columnIndexs[i];

            // 1カラムの文字列をセットする。
            columnString = columns[columnIndex];

            // カラムのバイト数チェック。
            if (isCheckByte(columnBytes[i])) {
                try {
                    if (columnString.getBytes(
                            fileEncoding).length != columnBytes[i]) {
                        throw new FileLineException("Data size is different from a set point "
                                + "of a column.", new IllegalStateException(), fileName, currentLineCount, fields[i]
                                        .getName(), columnIndex);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new FileException("fileEncoding which isn't supported was set.", e, fileName);
                }
            }

            // トリム処理
            columnString = FileDAOUtility.trim(columnString, fileEncoding,
                    trimChars[i], trimTypes[i]);

            // パディング処理
            columnString = FileDAOUtility.padding(columnString, fileEncoding,
                    columnBytes[i], paddingChars[i], paddingTypes[i]);

            // 文字列変換の処理。
            columnString = stringConverters[i].convert(columnString);

            // 値を格納する処理。
            // JavaBeanの属性の型の名前によって処理を振り分ける。
            ColumnParser columnParser = columnParserMap.get(fields[i].getType()
                    .getName());
            try {
                columnParser.parse(columnString, fileLineObject, methods[i],
                        columnFormats[i]);
            } catch (IllegalArgumentException e) {
                throw new FileLineException("Failed in coluomn data parsing.", e, fileName, currentLineCount, fields[i]
                        .getName(), columnIndex);
            } catch (IllegalAccessException e) {
                throw new FileLineException("Failed in coluomn data parsing.", e, fileName, currentLineCount, fields[i]
                        .getName(), columnIndex);
            } catch (InvocationTargetException e) {
                throw new FileLineException("Failed in coluomn data parsing.", e, fileName, currentLineCount, fields[i]
                        .getName(), columnIndex);
            } catch (ParseException e) {
                throw new FileLineException("Failed in coluomn data parsing.", e, fileName, currentLineCount, fields[i]
                        .getName(), columnIndex);
            }

        }

        return fileLineObject;
    }

    /**
     * サポートしない。<br>
     * Iteratorで定義されているメソッド。<br>
     * FileQueryDAOでは実装しないので、他のクラスから呼び出した場合、 UnsupportedOperationExceptionをスローする。
     * @throws UnsupportedOperationException このメソッドはサポートしない。
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() isn't supported.");
    }

    /**
     * 初期化処理を行う。<br>
     * 初期化処理で行う処理は以下です。。
     * <ul>
     * <li>ファイル行オブジェクトの属性(Field)の取得</li>
     * <li>文字変換種別オブジェクト(stringConverters)の生成</li>
     * <li>ファイル行オブジェクトの属性に対するセッタメソッド(methods)の取得</li>
     * <li>ファイルからデータを読込むためのLineReaderの生成</li>
     * <li>ヘッダ部の取得</li>
     * <li>トレイラキューの初期化</li>
     * </ul>
     * init()はAbstracFileLineIteratorを継承するクラスのコンストラクタで 呼ぶメソッドである。<br>
     * 下位互換性のため、2回以上実行できないようにしている。
     * @throws FileException 初期化処理で失敗した場合。
     * @throws FileLineException カラムに関連する初期化処理で失敗した場合。
     */
    protected void init() {
        if (!calledInit) {
            // リフレクション処理の基本情報を生成する。
            buildFields();

            if (isCheckEncloseChar()) {
                // カラムごとの囲み文字が設定されている場合、例外をスローする。
                if (isEnclosed()) {
                    throw new FileException("columnEncloseChar can not change.", new IllegalStateException(), fileName);
                }
            }

            if (isCheckColumnAnnotationCount()) {
                // ファイル行オブジェクトにアノテーションが設定されていない場合、例外をスローする。
                if (fields.length == 0) {
                    throw new FileException("InputFileColumn is not found.", new IllegalStateException(), fileName);
                }
            }

            buildStringConverters();
            buildMethods();

            try {
                // ファイルからデータを読込むためのLineReaderを生成する。
                buildLineReader();

                // ヘッダ部とトレイラ部の取得するための基本情報を生成する。
                buildHeader();
                buildTrailerQueue();
            } catch (FileException e) {
                if (this.reader != null) {
                    try {
                        closeFile();
                    } catch (FileException fe) {
                        // リスローする例外を上書きしてしまうため、
                        // クローズ時に発生するFileExceptionはハンドリングしない
                    }
                }
                throw e;
            }

            calledInit = true;
        }
    }

    /**
     * ファイルからデータを読込むためのLineReaderを生成する。<br>
     * ファイルのReaderの生成および、利用すべきLineReaderの生成を行う。<br>
     * 行区切り文字が0,1,2桁ではない場合は例外を発生する。
     * @throws FileException LineReaderの生成に失敗した場合。
     */
    private void buildLineReader() {
        // 対象ファイルに対するReaderを取得する。
        try {
            this.reader = new BufferedReader(new InputStreamReader((new FileInputStream(fileName)), fileEncoding));
            if (!reader.markSupported()) {
                throw new FileException("BufferedReader of this JVM dose not support mark method");
            }
        } catch (UnsupportedEncodingException e) {
            throw new FileException("Failed in generation of reader.", e, fileName);
        } catch (FileNotFoundException e) {
            throw new FileException("Failed in generation of reader.", e, fileName);
        }

        // 行区切り文字と囲み文字の情報に基づいてLineReaderを生成する。
        if (lineFeedChar.length() == 2) {
            // 行区切り文字が2文字
            if (!enclosed) {
                // 囲み文字無し
                lineReader = new LineFeed2LineReader(reader, lineFeedChar);
            } else {
                // 囲み文字あり
                lineReader = new EncloseCharLineFeed2LineReader(getDelimiter(), getEncloseChar(), columnEncloseChar, reader, lineFeedChar);
            }
        } else if (lineFeedChar.length() == 1) {
            // 行区切り文字が1文字
            if (!enclosed) {
                // 囲み文字無し
                lineReader = new LineFeed1LineReader(reader, lineFeedChar);
            } else {
                // 囲み文字あり
                lineReader = new EncloseCharLineFeed1LineReader(getDelimiter(), getEncloseChar(), columnEncloseChar, reader, lineFeedChar);
            }
        } else if (lineFeedChar.length() == 0) {
            // 行区切り文字が0文字
            lineReader = new LineFeed0LineReader(reader, fileEncoding, totalBytes);
        } else {
            throw new FileException("lineFeedChar length must be 0 or 1 or 2. but: "
                    + lineFeedChar
                            .length(), new IllegalStateException(), fileName);
        }
    }

    /**
     * InputFileColumnアノテーションが設定されているファイル行オブジェクトの 属性の配列を生成する。<br>
     * 取得対象属性はファイル行オブジェクトと継承元の全クラスの属性です。<br>
     * 取得した属性の{@link InputFileColumn#columnIndex()}が他属性と重複して いる場合は例外が発生する。<br>
     * また、{@link InputFileColumn#columnIndex()}の最大値がカラムの数と合わない 場合も例外が発生する。<br>
     * ファイル行オブジェクトの属性の設定に問題が無い場合は InputFileColumnアノテーション設定がある属性のみ整理し配列にする。<br>
     * @throws FileException カラムインデックスが重複した場合。
     */
    private void buildFields() {
        // フィールドオブジェクトを生成
        List<Field[]> allFields = new ArrayList<Field[]>();

        // フィールドオブジェクトを生成
        Class<?> tempClass = clazz;
        Field[] declaredFieldArray = null;
        int allFieldCount = 0;
        while (tempClass != null) {
            declaredFieldArray = tempClass.getDeclaredFields();
            allFields.add(declaredFieldArray);
            allFieldCount += declaredFieldArray.length;
            tempClass = tempClass.getSuperclass();
        }

        // カラムインデックスの定義の順番に並び替え
        Field[] dataColumnFields = new Field[allFieldCount];

        InputFileColumn inputFileColumn = null;
        int maxColumnIndex = -1;
        int columnIndex = -1;
        int columnCount = 0;

        for (Field[] fields : allFields) {
            for (Field field : fields) {
                inputFileColumn = field.getAnnotation(InputFileColumn.class);
                if (inputFileColumn != null) {
                    // マッピング可能な型のフィールドなのか確認する。
                    if (columnParserMap.get(field.getType()
                            .getName()) == null) {
                        throw new FileException("There is a type which isn't supported in a "
                                + "mapping target field in FileLineObject.", new IllegalStateException(), fileName);
                    }

                    columnIndex = inputFileColumn.columnIndex();
                    // カラムIndexがマイナス値なのか確認する。
                    if (columnIndex < 0) {
                        throw new FileException("Column Index in FileLineObject is the minus "
                                + "number.", new IllegalStateException(), fileName);
                    }
                    // カラムIndexがフィールド数を超えているかいるか確認する。
                    if (dataColumnFields.length <= columnIndex) {
                        throw new FileException("Column Index in FileLineObject is bigger than "
                                + "the total number of the field.", new IllegalStateException(), fileName);
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
            throw new FileException("columnIndex in FileLineObject is not sequential order.", new IllegalStateException(), fileName);
        }

        // フィールドをコピー(nullの部分削除)
        if (dataColumnFields.length == columnCount) {
            this.fields = dataColumnFields;
        } else {
            this.fields = new Field[columnCount];
            System.arraycopy(dataColumnFields, 0, this.fields, 0, columnCount);
        }

        // InputFileColumn（アノテーション）オブジェクトを変数に格納する。（StringConverter以外）
        inputFileColumns = new InputFileColumn[fields.length];
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
            inputFileColumns[i] = fields[i].getAnnotation(
                    InputFileColumn.class);
            columnIndexs[i] = inputFileColumns[i].columnIndex();
            columnFormats[i] = inputFileColumns[i].columnFormat();
            columnBytes[i] = inputFileColumns[i].bytes();
            totalBytes += columnBytes[i];
            paddingTypes[i] = inputFileColumns[i].paddingType();
            paddingChars[i] = inputFileColumns[i].paddingChar();
            trimTypes[i] = inputFileColumns[i].trimType();
            trimChars[i] = inputFileColumns[i].trimChar();

            // 囲み文字設定。inputFileColumnsの設定で上書きをする。
            if (inputFileColumns[i]
                    .columnEncloseChar() != Character.MIN_VALUE) {
                columnEncloseChar[i] = inputFileColumns[i].columnEncloseChar();
                enclosed = true;
            }
        }
    }

    /**
     * ファイル行オブジェクトの属性の文字変換種別オブジェクトの配列を生成する。<br>
     * 生成された文字変換種別オブジェクトインスタンスはキャッシュし、 同様の設定がある属性で利用する。<br>
     * 設定された文字変換種別オブジェクトがインスタンス化出来ないものの場合に 例外が発生する。<br>
     * @throws FileLineException 文字変換種別オブジェクトの生成に失敗した場合。
     */
    private void buildStringConverters() {

        // 文字変換種別の配列を生成
        StringConverter[] dataColumnStringConverters = new StringConverter[fields.length];

        InputFileColumn inputFileColumn = null;
        Class<? extends StringConverter> converterKind = null;

        for (int i = 0; i < fields.length; i++) {

            // JavaBeanの入力用のアノテーションを取得する。
            inputFileColumn = inputFileColumns[i];

            // inputFileColumn.stringConverter()の内容により処理を振り分ける。
            try {
                // 文字変換種別のアノテーションを取得する。
                converterKind = inputFileColumn.stringConverter();

                // マップ内に取得した文字変換種別と一致するキーが存在するか判定する。
                if (stringConverterCacheMap.containsKey(converterKind)) {
                    // マップからオブジェクトを取得し、文字変換種別の配列にセットする。
                    dataColumnStringConverters[i] = stringConverterCacheMap.get(
                            converterKind);

                } else {
                    // インスタンスを生成し、文字変換種別の配列にセットする。
                    dataColumnStringConverters[i] = converterKind.newInstance();
                    stringConverterCacheMap.put(converterKind,
                            dataColumnStringConverters[i]);
                }

            } catch (InstantiationException e) {
                throw new FileLineException("Failed in an instantiate of a stringConverter.", e, fileName, INITIAL_LINE_NO, fields[i]
                        .getName(), inputFileColumn.columnIndex());

            } catch (IllegalAccessException e) {
                throw new FileLineException("Failed in an instantiate of a stringConverter.", e, fileName, INITIAL_LINE_NO, fields[i]
                        .getName(), inputFileColumn.columnIndex());
            }
        }
        this.stringConverters = dataColumnStringConverters;
    }

    /**
     * ファイル行オブジェクトの属性のsetterメソッドのメソッドオブジェクトの配列を生成する。<br>
     * 属性に対するsetterメソッドは以下のルールで検索する。<br>
     * <ul>
     * <li>属性名の最初の文字を大文字にした文字列の先頭に「set」をつけたもの。</li>
     * </ul>
     * setterメソッドが検索できない場合は例外が発生する。
     * @throws FileException setterメソッドが見つからなかった場合。
     */
    private void buildMethods() {
        Method[] dataColumnSetMethods = new Method[fields.length];
        StringBuilder setterName = new StringBuilder();
        String fieldName = null;

        for (int i = 0; i < fields.length; i++) {
            // JavaBeanから処理の対象となる属性の属性名を取得する。
            fieldName = fields[i].getName();

            // 属性名を元に、setterメソッドの名前を生成する。
            setterName.setLength(0);
            setterName.append("set");
            setterName.append(StringUtils.upperCase(fieldName.substring(0, 1)));
            setterName.append(fieldName.substring(1, fieldName.length()));

            // setterのリフレクションオブジェクトを取得する。
            // fields[i].getType()で引数の型を指定している。
            try {
                dataColumnSetMethods[i] = clazz.getMethod(setterName.toString(),
                        new Class[] { fields[i].getType() });
            } catch (NoSuchMethodException e) {
                throw new FileException("The setter method of column doesn't exist.", e, fileName);
            }
        }
        this.methods = dataColumnSetMethods;
    }

    /**
     * ヘッダ部の取得を行う。<br>
     * 指定された行数分のデータが存在しない場合に例外を返す。<br>
     * @throws FileException ヘッダ部の取得に失敗した場合。
     */
    private void buildHeader() {
        if (0 < headerLineCount) {
            for (int i = 0; i < headerLineCount; i++) {
                if (!hasNext()) {
                    throw new FileException("The data which can be acquired doesn't exist.", new NoSuchElementException(), fileName);
                }
                try {
                    header.add(lineReader.readLine());
                } catch (FileException e) {
                    throw new FileException("Error occurred by reading processing of a File.", e, fileName);
                }
            }
        }
    }

    /**
     * トレイラキューの初期化を行う。<br>
     * トレイラ部はデータ部を全部読んだ後の部分で構成されますが、 ファイルは前から後に順調に読まれるため、<br>
     * 今取得したデータがデータ部の情報なのかトレイラ部の情報かが判断できない。<br>
     * そのため、キューにデータを入れて取得する。<br>
     * 指定された行数分のデータが存在しない場合に例外を返す。<br>
     * @throws FileException トレイラキューの初期化処理が失敗した場合。
     */
    private void buildTrailerQueue() {
        if (0 < trailerLineCount) {
            // トレイラキューインスタンスを生成する。
            trailerQueue = new ArrayBlockingQueue<String>(trailerLineCount);

            // トレイラキューのトレイラ行数分のデータを追加する。
            for (int i = 0; i < trailerLineCount; i++) {
                if (!hasNext()) {
                    throw new FileException("The data which can be acquired doesn't exist.", new NoSuchElementException(), fileName);
                }
                try {
                    trailerQueue.add(lineReader.readLine());
                } catch (FileException e) {
                    throw new FileException("Error occurred by reading processing of a File.", e, fileName);
                }
            }
        }
    }

    /**
     * ファイル閉塞処理を行う。<br>
     * @throws FileException ファイル閉塞処理で失敗した場合。
     */
    @Override
    public void closeFile() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new FileException("Processing of reader was failed.", e, fileName);
        }
    }

    /**
     * ヘッダ部のデータを取得する。<br>
     * データ部とトレイラ部の取得処理の実行可否と関係なくヘッダ部を取得することが 出来る。
     * @return header ヘッダ部の文字列リスト
     */
    @Override
    public List<String> getHeader() {
        return header;
    }

    /**
     * トレイラ部のデータを取得する。<br>
     * トレイラ部のデータを取得するとデータ部のデータを取得することは出来ない。<br>
     * 理由はトレイラ部のデータ取得時に、データ部の情報を全部スキップするためです。<br>
     * <b>※注意事項</b><br>
     * データ部のデータを全部取得する以前にトレイラ部を取得しないこと。<br>
     * @return トレイラ部の文字列リスト
     * @throws FileException データ行取得処理で失敗した場合。
     */
    @Override
    public List<String> getTrailer() {
        // トレイラ部のキャッシュがない場合に実行する。
        if (!readTrailer) {
            String currentData = null;
            // 残っているデータ部を飛ばす処理を行う。
            while (hasNext()) {
                try {
                    currentData = lineReader.readLine();
                } catch (FileException e) {
                    throw new FileException("Processing of lineReader was failed.", e, fileName);
                }
                if (0 < trailerLineCount) {
                    trailerQueue.poll();
                    trailerQueue.add(currentData);
                }
            }

            // ファイルのトレイラ部情報をキャッシュに格納する。
            if (0 < trailerLineCount) {
                int trailerQueueLength = trailerQueue.size();

                for (int i = 0; i < trailerQueueLength; i++) {
                    trailer.add(trailerQueue.poll());
                }
            }
            readTrailer = true;
        }
        return trailer;
    }

    /**
     * ファイルからデータ部のデータを1行分読み取り、文字列として呼出元に返却する。<br>
     * トレイラ部が存在する場合はトレイラキューからデータを取得して結果文字列に する。<br>
     * その後LineReaderから1行分の文字列を取得しトレイラキューに格納する。<br>
     * トレイラ部が存在しない場合はLineReaderから取得した1行分の文字列を結果文字列に する。<br>
     * もし、次の1行分のデータがない場合はnullを返す。
     * @return データ部の１行分の文字列
     * @throws FileException データ行取得処理で失敗した場合。
     */
    protected String readLine() {

        // 次の行データがない場合はnullを返す。
        if (!hasNext()) {
            return null;
        }

        // 次の1行分の文字列を取得する。
        String currentReadLineString = null;
        try {
            currentReadLineString = lineReader.readLine();
        } catch (FileException e) {
            throw new FileException("Processing of lineReader was failed.", e, fileName);
        }

        // トレイラキューが存在する場合は、結果としてキューの先頭データを返す。
        // 今取得した1行分の文字列はトレイラキューに入れる。
        if (0 < trailerLineCount) {
            String pollingLineString = trailerQueue.poll();
            trailerQueue.add(currentReadLineString);
            return pollingLineString;
        }

        return currentReadLineString;
    }

    /**
     * データ部のデータを読み飛ばす処理を行う。<br>
     * @param skipLines 読み飛ばす行数。
     */
    @Override
    public void skip(int skipLines) {
        for (int i = 0; i < skipLines; i++) {
            if (!hasNext()) {
                throw new FileLineException("The data which can be acquired doesn't exist.", new NoSuchElementException(), fileName, currentLineCount
                        + 1);
            }
            readLine();
            currentLineCount++;
        }
    }

    /**
     * 区切り文字を取得する。
     * @return 行区切り文字。
     */
    protected abstract char getDelimiter();

    /**
     * 囲み文字を取得する。
     * @return 囲み文字。
     */
    protected abstract char getEncloseChar();

    /**
     * データ部のデータ１行分をファイル行オブジェクトのアノテーションの記述に 従いカラムに分割する。<br>
     * 引数<code>fileLineString</code>が<code>null</code>もしくは 空文字の場合は、要素を持たない<code>String</code>配列を返します。<br>
     * サブクラスはこのメソッドをオーバーライドします。
     * @param fileLineString データ部のデータ１行分
     * @return データ部１行の文字列を分解した文字配列
     */
    protected abstract String[] separateColumns(String fileLineString);

    /**
     * 対象カラムに対するバイト数チェックを行うかを返す。
     * @param inputFileColumn 対象カラムのInputFileColumn情報
     * @return バイト数が設定されている(1バイト以上)場合はtrue。
     */
    protected boolean isCheckByte(InputFileColumn inputFileColumn) {
        if (0 < inputFileColumn.bytes()) {
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
     * 行区切り文字を取得する。
     * @return 行区切り文字
     */
    protected String getLineFeedChar() {
        return lineFeedChar;
    }

    /**
     * 行区切り文字を設定する。
     * @param 行区切り文字
     */
    protected void setLineFeedChar(String lineFeedChar) {
        this.lineFeedChar = lineFeedChar;
    }

    /**
     * ファイルエンコーディング取得する。
     * @return ファイルエンコーディング
     */
    protected String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * ヘッダ行数を取得する。
     * @return ヘッダ行数
     */
    protected int getHeaderLineCount() {
        return headerLineCount;
    }

    /**
     * トレイラ行数を取得する。
     * @return トレイラ行数
     */
    protected int getTrailerLineCount() {
        return trailerLineCount;
    }

    /**
     * 現在ファイル入力処理済みのデータ部内の行数を取得する。
     * @return ファイル入力処理済みのデータ部の行数。
     */
    public int getCurrentLineCount() {
        return currentLineCount;
    }

    /**
     * ファイル行オブジェクトのField情報（Annotation）を格納する変数を取得する。
     * @return ファイル行オブジェクトのField情報（Annotation）を格納する変数
     */
    protected Field[] getFields() {
        return fields;
    }

    /**
     * ファイル名を取得する。
     * @return fileName ファイル名
     */
    protected String getFileName() {
        return fileName;
    }

    /**
     * カラムの囲み文字を取得する。
     * @return columnEncloseChar 囲み文字
     */
    protected char[] getColumnEncloseChar() {
        return columnEncloseChar;
    }

    /**
     * 囲み文字が設定されているかを返す。
     * @return enclosed 囲み文字
     */
    protected boolean isEnclosed() {
        return enclosed;
    }

    /**
     * 各カラムのバイト数を取得する。
     * @return columnBytes 各カラムのバイト数
     */
    protected int[] getColumnBytes() {
        return columnBytes;
    }

    /**
     * 1行分のバイト数を取得する。
     * @return totalBytes 1行分のバイト数
     */
    protected int getTotalBytes() {
        return totalBytes;
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
}
