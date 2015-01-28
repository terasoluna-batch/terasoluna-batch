package jp.terasoluna.fw.batch.executor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import jp.terasoluna.fw.batch.executor.dao.SystemDao;
import jp.terasoluna.fw.batch.executor.vo.*;

public class MockSystemDao implements SystemDao{

    public List<BatchJobListResult> selectJobList(BatchJobListParam batchJobListParam) {
        return new ArrayList<BatchJobListResult>(){{
            add(new BatchJobListResult());
        }};
    }

    public List<BatchJobListResult> selectJobList(RowBounds rowBounds, BatchJobListParam batchJobListParam) {
        return new ArrayList<BatchJobListResult>(){{
            add(new BatchJobListResult());
        }};
    }

    public BatchJobData selectJob(BatchJobManagementParam batchJobManagementParam) {
        return new BatchJobData();
    }

    public Timestamp readCurrentTime() {
        return null;
    }

    public Date readCurrentDate() {
        return null;
    }

    public int updateJobTable(BatchJobManagementUpdateParam batchJobManagementUpdateParam) {
        return 0;
    }
}
