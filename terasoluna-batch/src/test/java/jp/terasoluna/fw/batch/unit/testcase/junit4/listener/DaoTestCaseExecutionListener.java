package jp.terasoluna.fw.batch.unit.testcase.junit4.listener;

/*
 * Copyright (c) 2012 NTT DATA Corporation
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

import jp.terasoluna.fw.batch.unit.testcase.junit4.DaoTestCaseJunit4;
import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * DaoTestCase用のDI時前処理を行うテスト実行時リスナー拡張です。
 * 
 * <p>
 * DI処理の前にコンテキストルートをクラスパスに追加します。 <br>
 * {@link #beforeGetConfig()}をオーバーライドすることで、テスト時に読み込むBean定義ファイルを解決する前の処理を実装できます。
 * </p>
 */
public class DaoTestCaseExecutionListener extends
                                         DependencyInjectionTestExecutionListener {

    /*
     * (non-Javadoc)
     * @see org.springframework.test.context.support.DependencyInjectionTestExecutionListener#prepareTestInstance(org.springframework.test.context.TestContext)
     */
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        ClassLoaderUtils.addContextRootToClassPath();
        beforeGetConfig();
        super.prepareTestInstance(testContext);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.test.context.support.AbstractTestExecutionListener#afterTestMethod(org.springframework.test.context.TestContext)
     */
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        super.afterTestMethod(testContext);
    }

    /**
     * テスト時に読み込むBean定義ファイルを解決する前の処理。
     * <p>
     * デフォルトでは何もしません。
     * </p>
     */
    protected void beforeGetConfig() {
        // デフォルトでは何もしません
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.test.context.support.DependencyInjectionTestExecutionListener#injectDependencies(org.springframework.test.context.TestContext)
     */
    @Override
    protected void injectDependencies(TestContext testContext) throws Exception {
        Object bean = testContext.getTestInstance();
        AutowireCapableBeanFactory beanFactory = testContext
                .getApplicationContext().getAutowireCapableBeanFactory();

        int autowireMode = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
        boolean dependencyCheck = false;

        if (bean instanceof DaoTestCaseJunit4) {
            DaoTestCaseJunit4 obj = (DaoTestCaseJunit4) bean;
            autowireMode = obj.getAutowireMode();
            dependencyCheck = obj.isDependencyCheck();
        }
        beanFactory.autowireBeanProperties(bean, autowireMode, dependencyCheck);

        beanFactory.initializeBean(bean, testContext.getTestClass().getName());
        testContext.removeAttribute(REINJECT_DEPENDENCIES_ATTRIBUTE);
    }
}
