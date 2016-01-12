package jp.terasoluna.fw.collector.dao;

import org.apache.ibatis.session.ResultHandler;

public interface UserListQueryResultHandleDao {

    void collect(Object object, ResultHandler<?> handler);
    void collectOrder(Object object, ResultHandler<?> handler);
    void collectOrder2(Object object, ResultHandler<?> handler);
    void collectDummy(Object object, ResultHandler<?> handler);
}
