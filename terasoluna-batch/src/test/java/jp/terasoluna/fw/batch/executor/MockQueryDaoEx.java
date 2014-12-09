package jp.terasoluna.fw.batch.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jp.terasoluna.fw.ex.unit.mock.MockQueryDao;

public class MockQueryDaoEx extends MockQueryDao {
	static Queue<Object> results = new LinkedList<Object>();

	@Override
	protected List<?> pollList() throws ClassCastException {
		return (List<?>) results.poll();
	}

	public static void addResults(Object o) {
		results.add(o);
	}

	@Override
	public void clear() {
		results.clear();
		super.clear();
	}
}
