package jp.terasoluna.fw.batch.executor;

import org.springframework.beans.factory.FactoryBean;

public class NullFactoryBean implements FactoryBean {

    public Object getObject() throws Exception {
        return null;
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }

}
