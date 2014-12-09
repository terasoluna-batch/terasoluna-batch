package jp.terasoluna.fw.dao.ibatis;

import jp.terasoluna.fw.dao.event.DataRowHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link QueryRowHandleDAOiBatisImpl}の試験のために使用される。
 * 
 * {@link QueryRowHandleDAOiBatisImpl}から使用される。
 * 
 */
public class QueryRowHandleDAOiBatisImpl_DataRowHandlerImpl implements DataRowHandler {

    /**
     * ログインスタンス 
     */
    private static Log log = LogFactory.
            getLog(QueryRowHandleDAOiBatisImpl_DataRowHandlerImpl.class);
    
    public void handleRow(Object param) {
        // 引数確認用
        if (param != null) {
            log.info("param=" + param);
        } else {
            log.info("param is null");
        }
    }

}
