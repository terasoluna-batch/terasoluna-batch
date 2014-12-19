package jp.terasoluna.fw.batch.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jp.terasoluna.fw.batch.executor.dao.SystemQueryDao;
import jp.terasoluna.fw.batch.executor.vo.BatchJobData;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListParam;
import jp.terasoluna.fw.batch.executor.vo.BatchJobListResult;
import jp.terasoluna.fw.batch.executor.vo.BatchJobManagementParam;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;

public class SystemQueryDaoStub01 implements SystemQueryDao {

    public List<BatchJobListResult> selectJobList(BatchJobListParam batchJobListParam) {
        throw new IllegalArgumentException("テスト使用対象外例外");
    }

    public List<BatchJobListResult> selectJobList(RowBounds rowBaounds, BatchJobListParam batchJobListParam) {
        throw new IllegalArgumentException("テスト使用対象外例外");
    }

    public BatchJobData selectJob(BatchJobManagementParam batchJobManagementParam) {
        throw new DataAccessException("DBステータス取得時例外確認用") {};
    }

    public Timestamp currentTimeReader() {
        throw new DataAccessException("DBステータス取得時例外確認用") {};
    }

    public Date currentDateReader() {
        throw new IllegalArgumentException("テスト使用対象外例外");
    }
}
