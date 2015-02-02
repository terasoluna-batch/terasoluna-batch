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

package jp.terasoluna.fw.batch.exception;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.terasoluna.fw.batch.exception.BatchException;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import jp.terasoluna.fw.util.PropertyUtil;
import junit.framework.TestCase;

/**
 * 事前条件<br>
 * <br>
 * 
 * ・src/test/resourcesフォルダ配下にAppricationResources.propertiesが存在すること。<br>
 * <br>
 * 
 * ・プロパティMessageAccessor.defaultの値が設定されていること。<br>
 * <fieldset><legend>batch.properties設定例</legend>
 * #メッセージソースアクセサのBean名<br>
 * MessageAccessor.default=msgAcc
 * 
 * </fieldset> <br>
 * ・Bean定義ファイルにプロパティで設定されたの値のBean名が設定されていること。<br>
 * <fieldset><legend>AdminContext.xml設定例</legend>
 * 
 * &lt;!-- メッセージアクセサ --&gt;<br>
 * &lt;bean id=&quot;msgAcc&quot;
 * class=&quot;jp.terasoluna.fw.batch.message.MessageAccessorImpl&quot;
 * /&gt;
 * 
 * </fieldset> <br>
 * ・messages.propertiesファイルが存在すること<br>
 * 
 */
public class BatchExceptionTest extends TestCase {

	/**
	 * プロパティ値取得値
	 */
	private String value = null;

	/**
	 * コンテナ用のフィールド
	 */
	private ApplicationContext context;

	/**
	 * MessageAccessorクラスのフィールド
	 */
	private MessageAccessor messageAccessor;

	@Override
	protected void setUp() throws Exception {

		// メッセージソースアクセサのBean名取得
		context = new ClassPathXmlApplicationContext(
				"beansDef/AdminContext.xml");
		value = PropertyUtil.getProperty("messageAccessor.default");
		messageAccessor = (MessageAccessor) context.getBean(value,
				MessageAccessor.class);
		MessageUtil.setMessageAccessor(messageAccessor);
	}
	/**
	 * testBatchException01()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testBatchException01() throws Exception {

		BatchException result = new BatchException();
		assertNotNull(result);
	}

	/**
	 * testBatchException02()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：messageに"test"が設定されていること<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testBatchException02() throws Exception {

		BatchException result = new BatchException("test");
		assertNotNull(result);
		assertEquals("test", result.getMessage());
	}

	/**
	 * testBatchException03()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：messageに"test"が設定されていること<br>
	 * 確認項目：causeがRuntimeExceptionであることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testBatchException03() throws Exception {

		BatchException result = new BatchException("test",
				new RuntimeException());

		assertNotNull(result);
		assertEquals("test", result.getMessage());
		assertEquals("java.lang.RuntimeException", result.getCause().toString());
	}

	/**
	 * testBatchException04()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：causeがRuntimeExceptionであることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testBatchException04() throws Exception {

		BatchException result = new BatchException(new RuntimeException());

		assertNotNull(result);
		assertEquals("java.lang.RuntimeException", result.getCause().toString());
	}

	/**
	 * testCreateException01()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：errors.requiredキーのメッセージが設定されていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException01() throws Exception {

		BatchException result = BatchException
				.createException("errors.required");

		assertNotNull(result);
		assertEquals("{0}は入力必須項目です.", result.getMessage());
	}

	/**
	 * testCreateException02()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：メッセージが設定されていないことを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException02() throws Exception {

		BatchException result = BatchException.createException(null);

		assertNotNull(result);
		assertEquals("Message not found. CODE:[null]", result.getMessage());
	}

	/**
	 * testCreateException03()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：メッセージが設定されていないことを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException03() throws Exception {

		BatchException result = BatchException.createException("");

		assertNotNull(result);
		assertEquals("Message not found. CODE:[]", result.getMessage());
	}

	/**
	 * testCreateException04()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：aerrors.requiredキーのメッセージにargが設定されていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException04() throws Exception {

		BatchException result = BatchException.createException(
				"errors.required", "test1");

		assertNotNull(result);
		assertEquals("test1は入力必須項目です.", result.getMessage());
	}

	/**
	 * testCreateException05()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：aerrors.requiredキーのメッセージにargが3つ設定されていることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException05() throws Exception {

		BatchException result = BatchException.createException("errors.range",
				"test1", "10", "20");

		assertNotNull(result);
		assertEquals("test1には10から20までの範囲で入力してください.", result.getMessage());
	}

	/**
	 * testCreateException06()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：errors.requiredキーのメッセージが設定されていることを確認する<br>
	 * 確認項目：causeがRuntimeExceptionであることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException06() throws Exception {

		BatchException result = BatchException.createException(
				"errors.required", new RuntimeException());

		assertNotNull(result);
		assertEquals("{0}は入力必須項目です.", result.getMessage());
		assertEquals("java.lang.RuntimeException", result.getCause().toString());
		
	}

	/**
	 * testCreateException07()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：aerrors.requiredキーのメッセージにargが設定されていることを確認する<br>
	 * 確認項目：causeがRuntimeExceptionであることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException07() throws Exception {

		BatchException result = BatchException.createException(
				"errors.required", new RuntimeException(), "test1");

		assertNotNull(result);
		assertEquals("test1は入力必須項目です.", result.getMessage());
		assertEquals("java.lang.RuntimeException", result.getCause().toString());
	}

	/**
	 * testCreateException08()<br>
	 * <br>
	 * テスト概要：BatchExceptionがインスタンスされたことを確認する<br>
	 * <br>
	 * 確認項目：BatchExceptionがnullでないこと<br>
	 * 確認項目：errors.rangeキーのメッセージにargが3つ設定されていることを確認する<br>
	 * 確認項目：causeがRuntimeExceptionであることを確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testCreateException08() throws Exception {

		BatchException result = BatchException.createException("errors.range",
				new RuntimeException(), "test1", "10", "20");

		assertNotNull(result);
		assertEquals("test1には10から20までの範囲で入力してください.", result.getMessage());
		assertEquals("java.lang.RuntimeException", result.getCause().toString());
	}

	/**
	 * testGetLogMessage01()<br>
	 * <br>
	 * テスト概要：返却されたログメッセージが正常であることを確認する<br>
	 * <br>
	 * 確認項目：errors.requiredキーのメッセージが正しくログメッセージとなっているか確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetLogMessage01() throws Exception {

		BatchException exception = BatchException.createException(
				"errors.required", "test1");

		String result = exception.getLogMessage();

		assertEquals("[errors.required] test1は入力必須項目です. (\n\ttest1\n)", result);

	}

	/**
	 * testGetMessageId01()<br>
	 * <br>
	 * テスト概要：設定されたメッセージキーが正しいメッセージキーか確認する<br>
	 * <br>
	 * 確認項目：errors.requiredキーのメッセージが正しく取得できるか確認する<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessageId01() throws Exception {

		BatchException exception = BatchException.createException(
				"errors.required", "test1");

		String result = exception.getMessageId();

		assertEquals("errors.required", result);

	}

	/**
	 * testGetMessageId02()<br>
	 * <br>
	 * テスト概要：設定されたメッセージキーがnullの場合、結果がnullであることを確認する<br>
	 * <br>
	 * 確認項目：nullであること<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessageId02() throws Exception {

		BatchException exception = BatchException.createException(
				null);

		String result = exception.getMessageId();

		assertNull(result);

	}
	

	/**
	 * testGetMessageId03()<br>
	 * <br>
	 * テスト概要：設定されたメッセージキーが空文字の場合、結果が空文字であることを確認する<br>
	 * <br>
	 * 確認項目：空文字であること<br>
	 * <br>
	 * 
	 * @throws Exception
	 */
	public void testGetMessageId03() throws Exception {

		BatchException exception = BatchException.createException(
				"");

		String result = exception.getMessageId();

		assertEquals("", result);

	}
	
	
}
