package jp.terasoluna.fw.batch.mock;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.*;
import org.apache.ibatis.session.RowBounds;

public class MockSystemDao extends AbstractMockDao implements
        SystemDao {

    @SuppressWarnings("unchecked")
    public List<BatchJobListResult> selectJobList(BatchJobListParam param) {
        addParam(new DaoParam(param));
        return (List<BatchJobListResult>) pollList();
    }

    @SuppressWarnings("unchecked")
    public List<BatchJobListResult> selectJobList(RowBounds rowBounds,
                                                  BatchJobListParam param) {
        addParam(new DaoParam(rowBounds, param));
        return (List<BatchJobListResult>) pollList();
    }

    public BatchJobData selectJob(
            BatchJobManagementParam batchJobManagementParam) {
        addParam(new DaoParam(batchJobManagementParam));
        return (BatchJobData) poll();
    }

    public Timestamp currentTimeReader() {
        return null;
    }

    public Date currentDateReader() {
        return null;
    }

    public int updateJobTable(BatchJobManagementUpdateParam batchJobManagementUpdateParam) {
        return 0;
    }
}
