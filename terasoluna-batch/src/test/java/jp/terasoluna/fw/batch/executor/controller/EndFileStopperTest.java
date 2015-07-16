package jp.terasoluna.fw.batch.executor.controller;

import javax.annotation.Resource;

import junit.framework.TestCase;

/**
 * EndFileStopperのテストケースクラス
 */
public class EndFileStopperTest extends TestCase {

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
	 */
	public void testCanStop001() {
        // テストデータ設定

        // テスト実施
		// 結果検証
		assertTrue(asyncBatchStopper.canStop());
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
	public void testCanStop002() {
        // テストデータ設定

		// テスト実施
		// 結果検証
		assertFalse(asyncBatchStopper.canStop());
	}
}
