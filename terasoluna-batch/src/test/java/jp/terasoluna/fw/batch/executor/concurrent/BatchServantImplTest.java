/*
 * Copyright (c) 2011 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.terasoluna.fw.batch.executor.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;

import jp.terasoluna.fw.batch.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.batch.unit.testcase.junit4.loader.DaoTestCaseContextLoader;

/**
 * 事前条件<br>
 * <br>
 * 
 * ・ジョブ管理テーブルにジョブが登録されていること。<br>
 * ・beanDefフォルダにジョブBean定義ファイルが存在すること。<br>
 * ・Bean定義ファイルに設定されたビジネスロジックが存在すること<br>
 * 
 */
@ContextConfiguration(loader = DaoTestCaseContextLoader.class)
public class BatchServantImplTest extends DaoTestCaseJunit4 {

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		deleteFromTable("job_control");

		update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000001', 'B000001', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL)");
		update("INSERT INTO job_control (job_seq_id, job_app_cd, job_arg_nm1, job_arg_nm2, job_arg_nm3, job_arg_nm4, job_arg_nm5, job_arg_nm6, job_arg_nm7, job_arg_nm8, job_arg_nm9, job_arg_nm10, job_arg_nm11, job_arg_nm12, job_arg_nm13, job_arg_nm14, job_arg_nm15, job_arg_nm16, job_arg_nm17, job_arg_nm18, job_arg_nm19, job_arg_nm20, blogic_app_status, cur_app_status, add_date_time, upd_date_time) VALUES ('0000000002', 'B000002', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL)");
	}

	/**
	 * testRun01()<br>
	 * <br>
	 * 事前状態：ジョブ管理テーブルのジョブシーケンスコードに0000000001が登録されていること<br>
	 * 事前状態：ジョブ管理テーブルのジョブ業務コードにB000001が登録されていること<br>
	 * <br>
	 * テスト概要：正常にジョブが実行されることを確認する<br>
	 * <br>
	 * 確認項目：ジョブシーケンスコードが正しいこと<br>
	 * 確認項目：ビジネスロジック結果が正しいこと<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRun01() throws Exception {

		BatchServantImpl exe = new BatchServantImpl();


		exe.setJobSequenceId("0000000001");
		exe.run();

		assertEquals(0, exe.getResult().getBlogicStatus());
		assertEquals("0000000001", exe.getJobSequenceId());
		
	}

	/**
	 * testRun02()<br>
	 * <br>
	 * 事前状態：ジョブ管理テーブルのジョブシーケンスコードに0000000000が登録されていないこと<br>
	 * <br>
	 * テスト概要：正常にジョブが実行されることを確認する<br>
	 * <br>
	 * 確認項目：ビジネスロジック結果が正しいこと<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRun02() throws Exception {

		BatchServantImpl exe = new BatchServantImpl();
		exe.setJobSequenceId("0000000000");
		exe.run();

		assertEquals(255, exe.getResult().getBlogicStatus());
	}



	/**
	 * testRun03()<br>
	 * <br>
	 * 事前状態：ジョブ管理テーブルのジョブシーケンスコードに0000000002が登録されていること<br>
	 * 事前状態：ジョブ管理テーブルのジョブ業務コードにB000002が登録されていること<br>
	 * <br>
	 * テスト概要：正常にジョブが実行されることを確認する<br>
	 * <br>
	 * 確認項目：ビジネスロジック結果が正しいこと<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
    @Test
	public void testRun03() throws Exception {

		BatchServantImpl exe = new BatchServantImpl();
		exe.setJobSequenceId("0000000002");
		exe.run();

		assertEquals(1, exe.getResult().getBlogicStatus());
	}


}
