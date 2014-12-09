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

package jp.terasoluna.fw.message;

import java.util.List;

/**
 * DBに格納されているメッセージリソースにアクセスするためのDAOインタフェース。
 * <br>
 * TERASOLUNAでは本クラスの実装クラスDBMessageDAOImplを提供する。
 * <br><br>
 * DAOはDBからメッセージリソースを検索する機能を提供する。
 * 
 * @see jp.terasoluna.fw.message.DataSourceMessageSource
 * @see jp.terasoluna.fw.message.DBMessage
 * @see jp.terasoluna.fw.message.DBMessageResourceDAOImpl
 * @see jp.terasoluna.fw.message.DBMessageQuery
 *  
 */
public interface DBMessageResourceDAO {

    /**
     * DB内の全てのメッセージリソースをリストで返却する。リストのパラメータ型は
     * DBMessageである。したがってリストから取り出せばそのままDBMessage
     * オブジェクトとして利用できる。詳しくはDBMessageクラスを参照のこと。
     * 
     * @return メッセージリソースのリスト。
     */
    List<DBMessage> findDBMessages();

}