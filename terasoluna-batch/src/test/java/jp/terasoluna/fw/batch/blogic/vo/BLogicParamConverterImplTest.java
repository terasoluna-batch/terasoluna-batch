/*
 * Copyright (c) 2015 NTT DATA Corporation
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

package jp.terasoluna.fw.batch.blogic.vo;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.Timestamp;

import jp.terasoluna.fw.batch.executor.vo.BatchJobData;

import org.dozer.Mapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BLogicParamConverterImplのテストケース<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beansDef/BLogicParamConverterImplTest.xml" })
public class BLogicParamConverterImplTest {

    @Autowired
    protected Mapper beanMapper;

    /**
     * テスト前処理
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * テスト後処理
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * コンストラクタのテスト01 【異常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・特になし
     * 確認項目
     * ・{@code beanMpper}に{@code null}を渡した場合、{@code IllegalArgumentException}をスローすること。
     * 
     * @return Exception 予期しない例外
     */
    @Test
    public void testBLogicParamConverterImpl01() throws Exception {
        try {
            new BLogicParamConverterImpl(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    is("[EAL025086] [Assertion failed] - BLogicParamConverterImpl constructor needs org.dozer.Mapper"));
        }
    }

    /**
     * コンストラクタのテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・beanMapperがAutowiredされていること
     * 確認項目
     * ・{@code beanMpper}を{@code BLogicParamConverterImpl}のコンストラクタの引数に渡した場合、{@code beanMpper}がフィールドに設定こと。
     * 
     * @return Exception 予期しない例外
     */
    @Test
    public void testBLogicParamConverterImpl02() throws Exception {
        BLogicParamConverterImpl bLogicParamConverter = new BLogicParamConverterImpl(beanMapper);
        assertThat(this.beanMapper, is(bLogicParamConverter.beanMpper));
    }

    /**
     * convertBLogicParamのテスト01 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・beanMapperがAutowiredされていること
     * 確認項目
     * ・{@code BatchJobData}に設定した各値が{@code BLogicParam}の各プロパティにセットされること。
     * 
     * @return Exception 予期しない例外
     */
    @Test
    public void testConvertBLogicParam01() throws Exception {
        BatchJobData source = new BatchJobData() {
            {
                setCurAppStatus("3");
                setErrAppStatus("3");
                setAddDateTime(new Timestamp(System.currentTimeMillis()));
                setUpdDateTime(new Timestamp(System.currentTimeMillis()));
                setJobAppCd("B000001");
                setJobSequenceId("000000001");
                setJobArgNm1("jobArgNm1");
                setJobArgNm2("jobArgNm2");
                setJobArgNm3("jobArgNm3");
                setJobArgNm4("jobArgNm4");
                setJobArgNm5("jobArgNm5");
                setJobArgNm6("jobArgNm6");
                setJobArgNm7("jobArgNm7");
                setJobArgNm8("jobArgNm8");
                setJobArgNm9("jobArgNm9");
                setJobArgNm10("jobArgNm10");
                setJobArgNm11("jobArgNm11");
                setJobArgNm12("jobArgNm12");
                setJobArgNm13("jobArgNm13");
                setJobArgNm14("jobArgNm14");
                setJobArgNm15("jobArgNm15");
                setJobArgNm16("jobArgNm16");
                setJobArgNm17("jobArgNm17");
                setJobArgNm18("jobArgNm18");
                setJobArgNm19("jobArgNm19");
                setJobArgNm20("jobArgNm20");
            }
        };

        BLogicParamConverterImpl bLogicParamConverter = new BLogicParamConverterImpl(beanMapper);
        BLogicParam dist = bLogicParamConverter.convertBLogicParam(source);

        assertThat(dist.getJobAppCd(), is("B000001"));
        assertThat(dist.getJobSequenceId(), is("000000001"));
        assertThat(dist.getJobArgNm1(), is("jobArgNm1"));
        assertThat(dist.getJobArgNm2(), is("jobArgNm2"));
        assertThat(dist.getJobArgNm3(), is("jobArgNm3"));
        assertThat(dist.getJobArgNm4(), is("jobArgNm4"));
        assertThat(dist.getJobArgNm5(), is("jobArgNm5"));
        assertThat(dist.getJobArgNm6(), is("jobArgNm6"));
        assertThat(dist.getJobArgNm7(), is("jobArgNm7"));
        assertThat(dist.getJobArgNm8(), is("jobArgNm8"));
        assertThat(dist.getJobArgNm9(), is("jobArgNm9"));
        assertThat(dist.getJobArgNm10(), is("jobArgNm10"));
        assertThat(dist.getJobArgNm11(), is("jobArgNm11"));
        assertThat(dist.getJobArgNm12(), is("jobArgNm12"));
        assertThat(dist.getJobArgNm13(), is("jobArgNm13"));
        assertThat(dist.getJobArgNm14(), is("jobArgNm14"));
        assertThat(dist.getJobArgNm15(), is("jobArgNm15"));
        assertThat(dist.getJobArgNm16(), is("jobArgNm16"));
        assertThat(dist.getJobArgNm17(), is("jobArgNm17"));
        assertThat(dist.getJobArgNm18(), is("jobArgNm18"));
        assertThat(dist.getJobArgNm19(), is("jobArgNm19"));
        assertThat(dist.getJobArgNm20(), is("jobArgNm20"));
    }

    /**
     * convertBLogicParamのテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・beanMapperがAutowiredされていること
     * 確認項目
     * ・{@code BatchJobData}のプロパティに{@code null}を渡した場合でも、例外が起きることなく{@code BLogicParam}の各プロパティにセットされること。
     *  尚、全てnullの場合、マッピングができているのか不明なため、jobArgNmの番号が奇数のもののみ{@code null}とする。
     * 
     * @return Exception 予期しない例外
     */
    @Test
    public void testConvertBLogicParam02() throws Exception {
        BatchJobData source = new BatchJobData() {
            {
                setJobAppCd("B000001");
                setJobSequenceId("000000001");
                setJobArgNm2("jobArgNm2");
                setJobArgNm4("jobArgNm4");
                setJobArgNm6("jobArgNm6");
                setJobArgNm8("jobArgNm8");
                setJobArgNm10("jobArgNm10");
                setJobArgNm12("jobArgNm12");
                setJobArgNm14("jobArgNm14");
                setJobArgNm16("jobArgNm16");
                setJobArgNm18("jobArgNm18");
                setJobArgNm20("jobArgNm20");
            }
        };

        BLogicParamConverterImpl bLogicParamConverter = new BLogicParamConverterImpl(beanMapper);
        BLogicParam dist = bLogicParamConverter.convertBLogicParam(source);

        assertThat(dist.getJobAppCd(), is("B000001"));
        assertThat(dist.getJobSequenceId(), is("000000001"));
        assertThat(dist.getJobArgNm1(), nullValue());
        assertThat(dist.getJobArgNm2(), is("jobArgNm2"));
        assertThat(dist.getJobArgNm3(), nullValue());
        assertThat(dist.getJobArgNm4(), is("jobArgNm4"));
        assertThat(dist.getJobArgNm5(), nullValue());
        assertThat(dist.getJobArgNm6(), is("jobArgNm6"));
        assertThat(dist.getJobArgNm7(), nullValue());
        assertThat(dist.getJobArgNm8(), is("jobArgNm8"));
        assertThat(dist.getJobArgNm9(), nullValue());
        assertThat(dist.getJobArgNm10(), is("jobArgNm10"));
        assertThat(dist.getJobArgNm11(), nullValue());
        assertThat(dist.getJobArgNm12(), is("jobArgNm12"));
        assertThat(dist.getJobArgNm13(), nullValue());
        assertThat(dist.getJobArgNm14(), is("jobArgNm14"));
        assertThat(dist.getJobArgNm15(), nullValue());
        assertThat(dist.getJobArgNm16(), is("jobArgNm16"));
        assertThat(dist.getJobArgNm17(), nullValue());
        assertThat(dist.getJobArgNm18(), is("jobArgNm18"));
        assertThat(dist.getJobArgNm19(), nullValue());
        assertThat(dist.getJobArgNm20(), is("jobArgNm20"));
    }
    
    /**
     * convertBLogicParamのテスト02 【正常系】<br>
     * 
     * <pre>
     * 事前条件
     * ・beanMapperがAutowiredされていること
     * 確認項目
     * ・{@code BatchJobData}のプロパティに{@code null}を渡した場合でも、例外が起きることなく{@code BLogicParam}の各プロパティにセットされること。
     *  尚、全てnullの場合、マッピングができているのか不明なため、jobArgNmの番号が偶数のもの、及びjobAppCd、jobSequenceIdのみ{@code null}とする。
     * 
     * @return Exception 予期しない例外
     */
    @Test
    public void testConvertBLogicParam03() throws Exception {
        BatchJobData source = new BatchJobData() {
            {
                setJobArgNm1("jobArgNm1");
                setJobArgNm3("jobArgNm3");
                setJobArgNm5("jobArgNm5");
                setJobArgNm7("jobArgNm7");
                setJobArgNm9("jobArgNm9");
                setJobArgNm11("jobArgNm11");
                setJobArgNm13("jobArgNm13");
                setJobArgNm15("jobArgNm15");
                setJobArgNm17("jobArgNm17");
                setJobArgNm19("jobArgNm19");
            }
        };

        BLogicParamConverterImpl bLogicParamConverter = new BLogicParamConverterImpl(beanMapper);
        BLogicParam dist = bLogicParamConverter.convertBLogicParam(source);

        assertThat(dist.getJobAppCd(), nullValue());
        assertThat(dist.getJobSequenceId(), nullValue());
        assertThat(dist.getJobArgNm1(), is("jobArgNm1"));
        assertThat(dist.getJobArgNm2(), nullValue());
        assertThat(dist.getJobArgNm3(), is("jobArgNm3"));
        assertThat(dist.getJobArgNm4(), nullValue());
        assertThat(dist.getJobArgNm5(), is("jobArgNm5"));
        assertThat(dist.getJobArgNm6(), nullValue());
        assertThat(dist.getJobArgNm7(), is("jobArgNm7"));
        assertThat(dist.getJobArgNm8(), nullValue());
        assertThat(dist.getJobArgNm9(), is("jobArgNm9"));
        assertThat(dist.getJobArgNm10(), nullValue());
        assertThat(dist.getJobArgNm11(), is("jobArgNm11"));
        assertThat(dist.getJobArgNm12(), nullValue());
        assertThat(dist.getJobArgNm13(), is("jobArgNm13"));
        assertThat(dist.getJobArgNm14(), nullValue());
        assertThat(dist.getJobArgNm15(), is("jobArgNm15"));
        assertThat(dist.getJobArgNm16(), nullValue());
        assertThat(dist.getJobArgNm17(), is("jobArgNm17"));
        assertThat(dist.getJobArgNm18(), nullValue());
        assertThat(dist.getJobArgNm19(), is("jobArgNm19"));
        assertThat(dist.getJobArgNm20(), nullValue());
    }
}
