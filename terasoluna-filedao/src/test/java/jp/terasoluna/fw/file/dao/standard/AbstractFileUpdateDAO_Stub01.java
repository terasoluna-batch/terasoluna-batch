/*
 * $Id: AbstractFileUpdateDAO_Stub01.java 5230 2007-09-28 10:04:13Z anh $
 *
 * Copyright (c) 2006 NTT DATA Corporation
 *
 */
package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.dao.FileLineWriter;
import jp.terasoluna.fw.file.dao.standard.AbstractFileUpdateDAO;

/**
 * <code>AbstractFileUpdateDAO</code>をテストするためのスタブクラス。
 * <p>
 * 空実装
 */
public class AbstractFileUpdateDAO_Stub01 extends AbstractFileUpdateDAO {

    @Override
    public <T> FileLineWriter<T> execute(String fileName, Class<T> clazz) {
        return null;
    }

}
