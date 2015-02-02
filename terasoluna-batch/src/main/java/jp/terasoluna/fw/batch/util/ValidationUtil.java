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

package jp.terasoluna.fw.batch.util;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * 入力チェックユーティリティ。<br>
 * <br>
 * ビジネスロジック中に入力チェック処理を行いたい場合に利用する。
 */
public class ValidationUtil {

    /**
     * コンストラクタ
     */
    protected ValidationUtil() {
    }

    /**
     * 入力チェックを行う
     * @param validator Validator
     * @param value チェック対象オブジェクト
     * @return 入力チェック結果
     */
    public static Errors validate(Validator validator, Object value) {
        String objectName = null;
        Errors errors = null;

        if (value != null) {
            objectName = value.getClass().getSimpleName();
            if (objectName != null) {
                objectName = Introspector.decapitalize(objectName);
            }
        }

        errors = new BindException(value, objectName);

        if (validator != null) {
            validator.validate(value, errors);
        }

        return errors;
    }

    /**
     * ErrorsからFieldErrorのリストを取得する
     * @param errors Errors
     * @return List<FieldError>
     */
    public static List<FieldError> getFieldErrorList(Errors errors) {
        List<FieldError> resultList = new ArrayList<FieldError>();

        List<?> errs = errors.getAllErrors();
        for (Object errObj : errs) {
            if (errObj instanceof FieldError) {
                FieldError fe = (FieldError) errObj;
                resultList.add(fe);
            }
        }

        return resultList;
    }

    /**
     * ErrorsからObjectErrorのリストを取得する
     * @param errors Errors
     * @return List<ObjectError>
     */
    public static List<ObjectError> getObjectErrorList(Errors errors) {
        List<ObjectError> resultList = new ArrayList<ObjectError>();

        List<?> errs = errors.getAllErrors();
        for (Object errObj : errs) {
            if (errObj instanceof ObjectError) {
                ObjectError oe = (ObjectError) errObj;
                resultList.add(oe);
            }
        }

        return resultList;
    }

    /**
     * ErrorsからDefaultMessageSourceResolvableのリストを取得する
     * @param errors Errors
     * @return List<DefaultMessageSourceResolvable>
     */
    public static List<DefaultMessageSourceResolvable> getDefaultMessageSourceResolvableList(
            Errors errors) {
        List<DefaultMessageSourceResolvable> resultList = new ArrayList<DefaultMessageSourceResolvable>();

        List<?> errs = errors.getAllErrors();
        for (Object errObj : errs) {
            if (errObj instanceof DefaultMessageSourceResolvable) {
                DefaultMessageSourceResolvable dmsr = (DefaultMessageSourceResolvable) errObj;
                resultList.add(dmsr);
            }
        }

        return resultList;
    }
}
