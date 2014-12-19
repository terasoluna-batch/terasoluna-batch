package jp.terasoluna.fw.batch.util;

import jp.terasoluna.fw.batch.executor.dao.SystemUpdateDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementUpdateParam;
import org.springframework.dao.DataAccessException;

public class SystemUpdateDaoStub01 implements SystemUpdateDao {
    public int updateJobTable(BatchJobManagementUpdateParam batchJobManagementUpdateParam) {
        throw new DataAccessException("DBステータス更新時例外確認用") {
            private static final long serialVersionUID = 2702384362766047436L;
        };
    }
}
