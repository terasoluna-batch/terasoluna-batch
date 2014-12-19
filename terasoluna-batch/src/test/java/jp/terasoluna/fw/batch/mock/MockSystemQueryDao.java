package jp.terasoluna.fw.batch.mock;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jp.terasoluna.fw.batch.executor.dao.SystemQueryDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import org.apache.ibatis.session.RowBounds;

public class MockSystemQueryDao extends AbstractMockDao implements
        SystemQueryDao {

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
}
