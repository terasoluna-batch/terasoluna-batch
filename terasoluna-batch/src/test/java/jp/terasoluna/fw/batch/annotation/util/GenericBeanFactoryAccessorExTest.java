package jp.terasoluna.fw.batch.annotation.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import jp.terasoluna.fw.batch.annotation.JobComponent;
import junit.framework.TestCase;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.ClassUtils;

public class GenericBeanFactoryAccessorExTest extends TestCase {

    public void setUp() throws Exception {
    }

    public void tearDown() throws Exception {
    }

    /**
     * getBeansWithAnnotationメソッド<br>
     * <p>
     * (正常系)
     * </p>
     * <p>
     * 観点：
     * </p>
     * <p>
     * 入力値：<br>
     * <li>beanName : "hoge"</li>
     * <li>bean : new AnnotationTestBLogic()</li>
     * <li>beanNames : { beanName }</li>
     * <li>applicationContext : MockWebApplicationContext( getBeanNamesForType(Object.class) : beanNames, <br>
     * containsBean(beanName) : Boolean.TRUE, <br>
     * getBean(beanName) : bean, <br>
     * )</li>
     * </p>
     * <br>
     * <p>
     * 期待値：<br>
     * <li>return : not null</li>
     * </p>
     */
    public void testGetBeansWithAnnotationClassOfQextendsAnnotation001() {
        // パラメータ
        MockApplicationContext ac = new MockApplicationContext();
        String beanName = "hoge";
        Object bean = new AnnotationTestBLogic();
        String[] beanNames = { beanName };
        ac.addBeanNamesForType(Object.class, beanNames);
        ac.addContainsBeanMap(beanName, Boolean.TRUE);
        ac.addBeanMap(beanName, bean);

        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ac
                .getBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(bean.getClass().getName());
        bf.registerBeanDefinition(beanName, beanDefinition);

        // 生成
        GenericBeanFactoryAccessorEx ax = new GenericBeanFactoryAccessorEx(ac);

        // テスト
        Map<String, Object> result = ax
                .getBeansWithAnnotation(JobComponent.class);

        // 検証
        assertNotNull(result);
        assertNotNull(result.get(beanName));
        assertEquals(bean, result.get(beanName));
    }

    /**
     * findAnnotationOnBeanメソッド<br>
     * <p>
     * (正常系)
     * </p>
     * <p>
     * 観点：
     * </p>
     * <p>
     * 入力値：<br>
     * <li>beanName : "hoge"</li>
     * <li>bean : new AnnotationTestBLogic()</li>
     * <li>beanNames : { beanName }</li>
     * <li>applicationContext : MockApplicationContext( getBeanNamesForType(Object.class) : beanNames, <br>
     * containsBean(beanName) : Boolean.TRUE, <br>
     * getBean(beanName) : bean, <br>
     * )</li>
     * </p>
     * <br>
     * <p>
     * 期待値：<br>
     * <li>return : not null</li>
     * </p>
     */
    public void testFindAnnotationOnBeanStringClassOfA001() {
        // パラメータ
        MockApplicationContext ac = new MockApplicationContext();
        String beanName = "hoge";
        Object bean = new AnnotationTestBLogic();
        String[] beanNames = { beanName };
        ac.addBeanNamesForType(Object.class, beanNames);
        ac.addContainsBeanMap(beanName, Boolean.TRUE);
        ac.addBeanMap(beanName, bean);

        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ac
                .getBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(bean.getClass().getName());
        bf.registerBeanDefinition(beanName, beanDefinition);

        // 生成
        GenericBeanFactoryAccessorEx ax = new GenericBeanFactoryAccessorEx(ac);

        // テスト
        JobComponent result = ax.findAnnotationOnBean(beanName,
                JobComponent.class);

        // 検証
        assertNotNull(result);
    }

    /**
     * findAnnotationOnBeanメソッド<br>
     * <p>
     * (正常系)
     * </p>
     * <p>
     * 観点：
     * </p>
     * <p>
     * 入力値：<br>
     * <li>beanName : "hoge"</li>
     * <li>bean : new AnnotationTestBLogic() ※AOPプロキシでラップしたもの</li>
     * <li>beanNames : { beanName }</li>
     * <li>applicationContext : MockApplicationContext( getBeanNamesForType(Object.class) : beanNames, <br>
     * containsBean(beanName) : Boolean.TRUE, <br>
     * getBean(beanName) : bean, <br>
     * )</li>
     * </p>
     * <br>
     * <p>
     * 期待値：<br>
     * <li>return : not null</li>
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void testFindAnnotationOnBeanStringClassOfA002() {
        // パラメータ
        MockApplicationContext ac = new MockApplicationContext();
        String beanName = "hoge";

        Object bean = null;
        AdvisedSupport advised = new AdvisedSupport();
        Class[] proxiedInterfaces = AopProxyUtils
                .completeProxiedInterfaces(advised);
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args)
                                                                            throws Throwable {
                return AnnotationTestBLogic.class;
            }
        };
        // プロキシオブジェクト生成
        bean = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                proxiedInterfaces, handler);

        String[] beanNames = { beanName };
        ac.addBeanNamesForType(Object.class, beanNames);
        ac.addContainsBeanMap(beanName, Boolean.TRUE);
        ac.addBeanMap(beanName, bean);

        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ac
                .getBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(AnnotationTestBLogic.class.getName());
        bf.registerBeanDefinition(beanName, beanDefinition);

        // 生成
        GenericBeanFactoryAccessorEx ax = new GenericBeanFactoryAccessorEx(ac);

        // テスト
        JobComponent result = ax.findAnnotationOnBean(beanName,
                JobComponent.class);

        // 検証
        assertNotNull(result);
    }

    /**
     * findAnnotationOnBeanメソッド<br>
     * <p>
     * (正常系)
     * </p>
     * <p>
     * 観点：
     * </p>
     * <p>
     * 入力値：<br>
     * <li>beanName : "hoge"</li>
     * <li>bean : new AnnotationTestBLogic()</li>
     * <li>beanNames : { beanName }</li>
     * <li>applicationContext : 階層化された MockApplicationContext( getBeanNamesForType(Object.class) : beanNames, <br>
     * containsBean(beanName) : Boolean.TRUE, <br>
     * getBean(beanName) : bean, <br>
     * )</li>
     * </p>
     * <br>
     * <p>
     * 期待値：<br>
     * <li>return : not null</li>
     * </p>
     */
    public void testFindAnnotationOnBeanStringClassOfA003() {
        // パラメータ
        MockApplicationContext ac1 = new MockApplicationContext();
        MockApplicationContext ac = new MockApplicationContext();

        String beanName = "hoge";
        Object bean = new AnnotationTestBLogic();
        Object beanDummy = new Object();
        String[] beanNames = { beanName };

        ac1.addBeanNamesForType(Object.class, beanNames);
        ac1.addContainsBeanMap(beanName, Boolean.TRUE);
        ac1.addBeanMap(beanName, beanDummy);
        ac1.addContainsBeanDefinitionMap(beanName, Boolean.TRUE);
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(AnnotationTestBLogic.class);
        ac1.addRergedBeanDefinitionMap(beanName, bd);
        ac1.setParent(ac);

        ac.addBeanNamesForType(Object.class, beanNames);
        ac.addContainsBeanMap(beanName, Boolean.TRUE);
        ac.addBeanMap(beanName, bean);

        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ac1
                .getBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(bean.getClass().getName());
        bf.registerBeanDefinition(beanName, beanDefinition);

        // 生成
        GenericBeanFactoryAccessorEx ax = new GenericBeanFactoryAccessorEx(ac1);

        // テスト
        JobComponent result = ax.findAnnotationOnBean(beanName,
                JobComponent.class);

        // 検証
        assertNotNull(result);
    }

}
