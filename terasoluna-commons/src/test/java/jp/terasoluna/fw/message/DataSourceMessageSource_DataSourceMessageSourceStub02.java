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

import java.util.ArrayList;
import java.util.List;

/**
 * DataSourceMessageSourceクラスで利用するスタブ
 *
 */
public class DataSourceMessageSource_DataSourceMessageSourceStub02 extends
        DataSourceMessageSource {
    
    /**
     * 引数messageを格納する
     */
    protected List<DBMessage> list = new ArrayList<DBMessage>();


    @Override
    protected void mapMessage(DBMessage message) {
        list.add(message);
    }
}
