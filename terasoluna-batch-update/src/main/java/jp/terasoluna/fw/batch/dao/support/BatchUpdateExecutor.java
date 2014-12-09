/*
 * Copyright (c) 2011 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.dao.support;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import jp.terasoluna.fw.dao.UpdateDAO;
import jp.terasoluna.fw.logger.TLogger;

/**
 * バッチ更新一括実行クラス<br>
 */
public class BatchUpdateExecutor {
    /**
     * ロガー.
     */
    private static final TLogger LOGGER = TLogger
            .getLogger(BatchUpdateExecutor.class);

    /**
     * バッチ更新一括実行.<br>
     * <p>
     * オブジェクト内のBatchUpdateSupportフィールドを探索して、すべてのバッチ更新を実行する。<br>
     * </p>
     * @param value Object 探索するオブジェクト
     * @param updateDAO UpdateDAO 実行に使用するUpdateDAO
     * @return executeBatchの実行結果リスト
     */
    public static List<BatchUpdateResult> executeBatch(Object value,
            UpdateDAO updateDAO) {
        List<BatchUpdateResult> resultList = new ArrayList<BatchUpdateResult>();

        if (value != null) {
            if (value instanceof BatchUpdateSupport) {
                BatchUpdateSupport bus = (BatchUpdateSupport) value;
                // 1件実行
                int res = 0;
                try {
                    res = bus.executeBatch(updateDAO);
                    resultList.add(new BatchUpdateResult(bus, Integer
                            .valueOf(res)));
                } catch (Throwable e) {
                    // 発生した例外を格納
                    resultList.add(new BatchUpdateResult(bus, e));
                }
            } else if (value instanceof List) {
                // リストを探索
                List<?> valueList = (List<?>) value;

                for (Object obj : valueList) {
                    List<BatchUpdateResult> res = executeBatch(obj, updateDAO);
                    resultList.addAll(res);
                }
            } else if (value.getClass().isArray()) {
                // 配列を探索
                Object[] valueArray = (Object[]) value;

                for (Object obj : valueArray) {
                    List<BatchUpdateResult> res = executeBatch(obj, updateDAO);
                    resultList.addAll(res);
                }
            } else {
                // オブジェクトを探索
                List<BatchUpdateResult> res = executeBatchInnerObject(value,
                        updateDAO);
                resultList.addAll(res);
            }
        }

        return resultList;
    }

    /**
     * オブジェクトに対するバッチ更新一括実行（内部呼び出し用）.<br>
     * @param value Object 探索するオブジェクト
     * @param updateDAO UpdateDAO 実行に使用するUpdateDAO
     * @return executeBatchの実行結果リスト
     */
    protected static List<BatchUpdateResult> executeBatchInnerObject(
            Object value, UpdateDAO updateDAO) {

        List<BatchUpdateResult> resultList = new ArrayList<BatchUpdateResult>();

        if (value != null) {
            BeanInfo bi = null;

            try {
                bi = Introspector.getBeanInfo(value.getClass());

                if (bi != null) {
                    PropertyDescriptor[] pds = bi.getPropertyDescriptors();

                    for (PropertyDescriptor pd : pds) {
                        if (pd != null) {
                            Class<?> pt = pd.getPropertyType();
                            Method rm = pd.getReadMethod();

                            if (rm != null && isTargetClass(pt)) {
                                try {
                                    Object readValue = rm.invoke(value);
                                    List<BatchUpdateResult> res = executeBatch(
                                            readValue, updateDAO);
                                    resultList.addAll(res);
                                } catch (IllegalArgumentException e) {
                                    outputExceptionLog(e);
                                } catch (IllegalAccessException e) {
                                    outputExceptionLog(e);
                                } catch (InvocationTargetException e) {
                                    outputExceptionLog(e);
                                }
                            }
                        }
                    }
                }
            } catch (IntrospectionException e) {
                outputExceptionLog(e);
            }

        }

        return resultList;
    }

    /**
     * バッチ更新リストクリア.<br>
     * <p>
     * オブジェクト内のBatchUpdateSupportフィールドを探索して、すべてのバッチ更新をクリアする。<br>
     * </p>
     * @param value Object 探索するオブジェクト
     */
    public static void clearAll(Object value) {
        if (value != null) {
            if (value instanceof BatchUpdateSupport) {
                // クリア
                ((BatchUpdateSupport) value).clear();
            } else if (value instanceof List) {
                // リストを探索
                List<?> valueList = (List<?>) value;

                for (Object obj : valueList) {
                    clearAll(obj);
                }
            } else if (value.getClass().isArray()) {
                // 配列を探索
                Object[] valueArray = (Object[]) value;

                for (Object obj : valueArray) {
                    clearAll(obj);
                }
            } else {
                // オブジェクトを探索
                clearAllInnerObject(value);
            }
        }

        return;
    }

    /**
     * オブジェクトに対するバッチ更新リストクリア（内部呼び出し用）.<br>
     * @param value Object 探索するオブジェクト
     */
    protected static void clearAllInnerObject(Object value) {

        if (value != null) {
            BeanInfo bi = null;

            try {
                bi = Introspector.getBeanInfo(value.getClass());

                if (bi != null) {
                    PropertyDescriptor[] pds = bi.getPropertyDescriptors();

                    for (PropertyDescriptor pd : pds) {
                        if (pd != null) {
                            Class<?> pt = pd.getPropertyType();
                            Method rm = pd.getReadMethod();

                            if (rm != null && isTargetClass(pt)) {
                                try {
                                    Object readValue = rm.invoke(value);
                                    clearAll(readValue);
                                } catch (IllegalArgumentException e) {
                                    outputExceptionLog(e);
                                } catch (IllegalAccessException e) {
                                    outputExceptionLog(e);
                                } catch (InvocationTargetException e) {
                                    outputExceptionLog(e);
                                }
                            }
                        }
                    }
                }
            } catch (IntrospectionException e) {
                outputExceptionLog(e);
            }

        }

        return;
    }

    /**
     * 処理対象クラスであるか判定する。<br>
     * @param clazz クラス型
     * @return true:処理対象 / false:処理対象外クラス
     */
    protected static boolean isTargetClass(Class<?> clazz) {
        if (clazz != null && clazz != Object.class && clazz != Class.class
                && !(Date.class.isAssignableFrom(clazz))
                && !(clazz.isPrimitive()) && !(isPrimitiveWrapper(clazz))) {
            return true;
        }
        return false;
    }

    /**
     * プリミティブのラッパークラスを判定する.<br>
     * @param pt Class&lt;?&gt;
     * @return true:プリミティブのラッパークラスである / false:プリミティブのラッパークラスではない
     */
    protected static boolean isPrimitiveWrapper(Class<?> clazz) {
        if (clazz != null) {
            if (Number.class.isAssignableFrom(clazz)
                    && !AtomicInteger.class.isAssignableFrom(clazz)
                    && !AtomicLong.class.isAssignableFrom(clazz)
                    && !AtomicBoolean.class.isAssignableFrom(clazz)) {
                return true;
            }
            if (Boolean.class == clazz || Character.class == clazz
                    || String.class == clazz || Date.class == clazz
                    || Void.class == clazz) {
                return true;
            }
        }
        return false;
    }

    /**
     * 例外ログを出力する。
     * @param e Throwable
     */
    protected static void outputExceptionLog(Throwable e) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(LogId.WAL036001, e, (e.getMessage() == null ? "" : e
                    .getMessage()));
        }
    }

}
