package jp.terasoluna.fw.batch.executor.controller;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 非同期バッチ起動プロセスの終了判定。<br>
 * <p>
 * 終了ファイルの有無を利用して非同期バッチ起動プロセスの終了判定を行う
 * </p>
 * 
 * @see jp.terasoluna.fw.batch.executor.controller.AsyncBatchStopper
 * @since 3.6
 */
@Component("asyncBatchStopper")
public class EndFileStopper implements AsyncBatchStopper {

	@Value("${executor.endMonitoringFile}")
	protected String endMonitoringFileName;

	/**
	 * ファイルの有無によってプロセスの終了判定を行う。<br>
	 *
	 * @return 非同期バッチ起動プロセスの終了条件（<code>true</code>返却時に終了する）
	 */
	@Override
	public boolean canStop() {
		File f = new File(endMonitoringFileName);
		return f.exists();
	}

}
