/*
 * Copyright (c) 2008 NTT DATA Corporation
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

package jp.terasoluna.fw.dao.event;

/**
 * <p>
 * 1行のデータを処理するためのインタフェース。
 * </p>
 *
 * <p>
 * QueryRowHandleDAOの実装クラスから、SQLの実行結果の1行毎に
 * handleRowメソッドが呼ばれる。<br>
 * その際、引数には1行分のデータが格納されたオブジェクトが渡される。
 * <br>
 * QueryRowHandleDAOの実装クラスを使用の際には、
 * 本インタフェースを実装したクラスを作成する必要がある。
 * </p>
 *
 * @see jp.terasoluna.fw.dao.QueryRowHandleDAO
 */
public interface DataRowHandler {

    /**
     * 1行毎に呼ばれるメソッド
     *
     * @param valueObject 1行のデータ
     */
    void handleRow(Object valueObject);
}
