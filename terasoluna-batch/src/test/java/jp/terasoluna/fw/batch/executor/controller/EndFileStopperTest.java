package jp.terasoluna.fw.batch.executor.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * EndFileStopperのテストケースクラス
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beansDef/AsyncBatchStopper.xml")
public class EndFileStopperTest {

	@Resource
	protected AsyncBatchStopper asyncBatchStopper;

	/**
	 * canStopテスト 【正常系】
	 * 
	 * <pre>
	 * 事前条件
	 * ・終了ファイルが存在する
	 * 確認項目
	 * </pre>
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCanStop001() throws IOException {
		// テストデータ準備 (batch.properties:executor.endMonitoringFileで指定しているファイル)
		Files.createFile(Paths.get("/tmp/batch_terminate_file"));

		// テスト実施
		// 結果検証
		try {
			assertTrue(asyncBatchStopper.canStop());
		} finally {
			// テストデータ削除
			Files.deleteIfExists(Paths.get("/tmp/batch_terminate_file"));
		}
	}

	/**
	 * canStopテスト 【正常系】
	 * 
	 * <pre>
	 * 事前条件
	 * ・終了ファイルが存在しない
	 * 確認項目
	 * </pre>
	 */
	@Test
	public void testCanStop002() {
		// テスト実施
		// 結果検証
		assertFalse(asyncBatchStopper.canStop());
	}
}
