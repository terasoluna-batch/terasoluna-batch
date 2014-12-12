■ TLogger...汎用ロガー

このロガー経由でログ出力を行うと以下の利点があります。

・ログメッセージをプロパティファイルで管理できる
・ログ出力メソッドをログIDと置換パラメータで呼び出せる
・置換パラメータは可変長配列で渡せる
・出力されるログにログIDが自動で埋め込まれる
・ログレベル確認のif文が(多くの場合)不要
・モジュール毎にメッセージプロパティファイルを管理できる
・メッセージプロパティファイルなしでも利用できる

■■ ロガー取得

ロガーの取得は他のライブラリとほぼ同じです。
private static final TLogger LOGGER = TLogger.getLogger(XXX.class);
または
private static final TLogger LOGGER = TLogger.getLogger("Hoge");

■■ ログ出力

次のようなログメッセージプロパティファイルがある場合

DEB001=debug message
ERR001=error message
このメッセージを出力するには

LOGGER.debug("DEB001"); 
LOGGER.error("ERR001");
のようにログレベルに応じたメソッドにログIDを渡して実行します。 出力メッセージは

[DEB001] debug message
[ERR001] error message
となります。メッセージの前に[ログID]が自動で付きます。

ログレベルは

FATAL
ERROR
WARN
INFO
DEBUG
TRACE
があります。log(String logId)メソッドを使用すると、ログIDの一文字目を見てログレベルを判断します。

LOGGER.log("ERR001"); // ログレベルはERROR

■■ パラメータ置換

出力するログメッセージを作成する際に、java.text.MessageFormatを使用しています。置換パラメータを可変長配列で渡すことができます。

DEB002={0} is {1}.
という定義がある場合、

LOGGER.debug("DEB002", "hoge", "foo");
を実行すると出力メッセージは

[DEB002] hoge is foo.
となります。

内部でメッセージ文字列を作成する際にログレベルのチェックを行っているので、

if (LOGGER.isDebugEnabled()) {
    LOGGER.debug("DEB002", "hoge", "foo");
}
というif文を書く必要がありません。(ただし、パラメータを作成する際にコストのかかるメソッドを呼び出している場合はif文を書いて明示的にログレベルチェックを行ってください。)

■■ メッセージプロパティファイルにないメッセージの出力

メッセージプロパティファイル中のログIDを渡す他に、直接メッセージを渡す方法があります。第1引数にfalse を設定し、第2引数にメッセージ本文を直接記述できます。第3引数以降は置換パラメータです。この場合は当然ログIDは出力されません。

LOGGER.warn(false, "warn!!");
LOGGER.info(false, "Hello {0}!", "World");
出力メッセージは

warn!!
Hello World!
となります。

■■ 設定ファイル

クラスパス直下のMETA-INFディレクトリにterasoluna-logger.propertiesを作成してください。

■■■ メッセージプロパティファイルのベースネーム設定

terasoluna-logger.propertiesのmessage.basenameキーにメッセージプロパティファイルのベースネームをクラスパス相対(FQCN)で設定してください。
java.util.ResourceBundleで読み込むので、国際化に対応しています。

message.basename=hoge
と書くとクラスパス直下のhoge.propertiesが読み込まれます。

message.basename=hoge,foo,bar
のように半角カンマ区切りで設定すると全てを読み込みます。

META-INF/terasoluna-logger.properiesのmessage.basenameはモジュール毎に設定できます。
ロガーは全てのモジュール(jar)が持つ、message.basenameの値をマージしてメッセージを取得します。

これにより、モジュール毎にログメッセージを管理することができます。

■■■ 出力ログIDフォーマット設定

ログ出力時に自動で付加されるログIDのフォーマットを設定できます。
message.id.formatキーにjava.lang.String.format()のフォーマット形式で設定してください。ログIDが文字列として渡されます。

設定しない場合は「[%s]」がデフォルト値として使用されます。

message.id.format=[%-8s]
のように設定すると、モジュール間で異なる長さのログIDを左寄せで揃えて出力できます。

この設定値はモジュール毎に管理することはできません。
クラスローダの読み込み優先度が一番高いterasoluna-logger.propertiesの値が反映されます。
 (通常、アプリ側の設定となります。)


■ TException,TRuntimeException...汎用例外

この例外クラスを用いると以下の利点があります。

・例外メッセージをプロパティファイルで管理できる
・例外メッセージを例外メッセージIDと置換パラメータで作成できる

ロガーとほぼ同様です。
■■ 例外クラス作成

メッセージファイルに
ERR01= {0} was occured!
と定義されている場合、

TException e = new TException("ERR01", "something");

を作成すると、例外メッセージが
[ERR01] somethins was occured!
になります。

■■ 設定ファイル

クラスパス直下のMETA-INFディレクトリにterasoluna-exception.propertiesを作成してください。
その他は汎用ロガーの場合と同じです。

