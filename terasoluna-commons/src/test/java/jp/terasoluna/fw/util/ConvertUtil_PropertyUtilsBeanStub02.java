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

package jp.terasoluna.fw.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * {@link ConvertUtil}をテストするための{@link PropertyUtilsBean}実装クラス。
 * 
 * @see org.apache.commons.beanutils.PropertyUtilsBean
 * @see jp.terasoluna.fw.util.ConvertUtilTest
 */
public class ConvertUtil_PropertyUtilsBeanStub02 extends PropertyUtilsBean {

    /**
     * 実行時にNoSuchMethodExceptionをスローする
     * @param bean Bean whose properties are to be extracted
     */
    @Override
    public Map describe(Object bean) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        throw new NoSuchMethodException();
    }


}
