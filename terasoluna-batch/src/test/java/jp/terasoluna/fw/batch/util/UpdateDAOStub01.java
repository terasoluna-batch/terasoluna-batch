package jp.terasoluna.fw.batch.util;

import java.util.List;

import org.springframework.dao.DataAccessException;

import jp.terasoluna.fw.dao.SqlHolder;
import jp.terasoluna.fw.dao.UpdateDAO;

public class UpdateDAOStub01 implements UpdateDAO {

	public int execute(String sqlID, Object bindParams) {
		throw new DataAccessException("DBステータス更新時例外確認用") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2702384362766047436L;
		};
	}

	public void addBatch(String sqlID, Object bindParams) {
		throw new IllegalStateException("addBatch.");
	}

	public int executeBatch() {
		throw new IllegalStateException("executeBatch.");
	}

	public int executeBatch(List<SqlHolder> sqlHolders) {
		throw new IllegalStateException("executeBatch(List).");
	}

}
