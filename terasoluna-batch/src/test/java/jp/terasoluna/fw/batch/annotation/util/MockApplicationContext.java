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

package jp.terasoluna.fw.batch.annotation.util;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.security.AccessControlContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

public class MockApplicationContext extends
                                    AbstractRefreshableApplicationContext
                                    implements ApplicationContext,
                                    HierarchicalBeanFactory,
                                    ConfigurableBeanFactory {

    private Map<String, Boolean> containsBeanMap = new HashMap<String, Boolean>();

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    private Map<Object, Object> beansOfTypeMap = new HashMap<Object, Object>();

    @SuppressWarnings("rawtypes")
    private Map<Class, String[]> beanNamesForType = new HashMap<Class, String[]>();

    private Map<String, Boolean> isSingletonMap = new HashMap<String, Boolean>();

    private ApplicationContext parent = null;

    private Map<String, Boolean> containsBeanDefinitionMap = new HashMap<String, Boolean>();

    private Map<String, Scope> registeredScopeMap = new HashMap<String, Scope>();

    private Map<String, BeanDefinition> rergedBeanDefinitionMap = new HashMap<String, BeanDefinition>();

    public MockApplicationContext() {
        refreshBeanFactory();
    }

    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {

        return null;
    }

    public String getDisplayName() {

        return null;
    }

    public String getId() {

        return null;
    }

    public ApplicationContext getParent() {

        return this.parent;
    }

    public long getStartupDate() {

        return 0;
    }

    public boolean containsBeanDefinition(String key) {
        Boolean value = this.containsBeanDefinitionMap.get(key);
        if (value != null) {
            return this.containsBeanDefinitionMap.get(key);
        }
        return false;
    }

    public int getBeanDefinitionCount() {

        return 0;
    }

    public String[] getBeanDefinitionNames() {

        return null;
    }

    public String[] getBeanNamesForType(Class<?> key) {
        return beanNamesForType.get(key);
    }

    public String[] getBeanNamesForType(Class<?> arg0, boolean arg1,
            boolean arg2) {

        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getBeansOfType(
            @SuppressWarnings("rawtypes") Class arg0) throws BeansException {

        return this.beansOfTypeMap;
    }

    @SuppressWarnings("unchecked")
    public Map<?, ?> getBeansOfType(@SuppressWarnings("rawtypes") Class arg0,
            boolean arg1, boolean arg2) throws BeansException {

        return null;
    }

    public boolean containsBean(String key) {
        Boolean value = containsBeanMap.get(key);
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }

    public String[] getAliases(String arg0) {

        return null;
    }

    public Object getBean(String key) throws BeansException {

        return beanMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public Object getBean(String key,
            @SuppressWarnings("rawtypes") Class arg1) throws BeansException {
        Object obj = beanMap.get(key);
        if (obj == null) {
            throw new NoSuchBeanDefinitionException(key);
        } else if (!arg1.isAssignableFrom(obj.getClass())) {
            throw new BeanNotOfRequiredTypeException(key, arg1, obj.getClass());
        }

        return obj;
    }

    public Object getBean(String key, Object... arg1) throws BeansException {

        return beanMap.get(key);
    }

    public Class<?> getType(String arg0) throws NoSuchBeanDefinitionException {

        return null;
    }

    public boolean isPrototype(
            String arg0) throws NoSuchBeanDefinitionException {

        return false;
    }

    public boolean isSingleton(
            String key) throws NoSuchBeanDefinitionException {
        Boolean singleton = this.isSingletonMap.get(key);
        if (singleton != null) {
            return singleton;
        }
        return false;
    }

    public boolean isTypeMatch(String arg0,
            Class<?> arg1) throws NoSuchBeanDefinitionException {

        return false;
    }

    public boolean containsLocalBean(String arg0) {

        return false;
    }

    public BeanFactory getParentBeanFactory() {

        return this.parent;
    }

    public String getMessage(MessageSourceResolvable arg0,
            Locale arg1) throws NoSuchMessageException {

        return null;
    }

    public String getMessage(String arg0, Object[] arg1,
            Locale arg2) throws NoSuchMessageException {

        return null;
    }

    public String getMessage(String arg0, Object[] arg1, String arg2,
            Locale arg3) {

        return null;
    }

    public void publishEvent(ApplicationEvent arg0) {

    }

    public Resource[] getResources(String arg0) throws IOException {

        return null;
    }

    public ClassLoader getClassLoader() {

        return null;
    }

    public Resource getResource(String arg0) {

        return null;
    }

    public void addContainsBeanMap(String key, Boolean value) {
        containsBeanMap.put(key, value);
    }

    public void addBeanMap(String key, Object value) {
        beanMap.put(key, value);
    }

    public void addBeansOfTypeMap(Object key, Object value) {
        beansOfTypeMap.put(key, value);
    }

    public void addBeanNamesForType(Class<?> key, String[] value) {
        beanNamesForType.put(key, value);
    }

    public void addContainsBeanDefinitionMap(String key, Boolean value) {
        this.containsBeanDefinitionMap.put(key, value);
    }

    public void addRergedBeanDefinitionMap(String key, BeanDefinition value) {
        this.rergedBeanDefinitionMap.put(key, value);
    }

    // public ServletContext getServletContext() {
    // return null;
    // }

    public void addIsSingletonMap(String key, Boolean value) {
        this.isSingletonMap.put(key, value);
    }

    public void addRegisteredScopeMap(String key, Scope scope) {
        this.registeredScopeMap.put(key, scope);
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

    }

    public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {

    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {

    }

    public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {

    }

    public void destroyBean(String beanName, Object beanInstance) {

    }

    public void destroyScopedBean(String beanName) {

    }

    public void destroySingletons() {

    }

    public ClassLoader getBeanClassLoader() {
        return null;
    }

    public int getBeanPostProcessorCount() {
        return 0;
    }

    public String[] getDependenciesForBean(String beanName) {
        return null;
    }

    public String[] getDependentBeans(String beanName) {
        return null;
    }

    public BeanDefinition getMergedBeanDefinition(
            String beanName) throws NoSuchBeanDefinitionException {
        return this.rergedBeanDefinitionMap.get(beanName);
    }

    public Scope getRegisteredScope(String scopeName) {
        return this.registeredScopeMap.get(scopeName);
    }

    public String[] getRegisteredScopeNames() {
        return null;
    }

    public ClassLoader getTempClassLoader() {
        return null;
    }

    public TypeConverter getTypeConverter() {
        return null;
    }

    public boolean isCacheBeanMetadata() {
        return false;
    }

    public boolean isCurrentlyInCreation(String beanName) {
        return false;
    }

    public boolean isFactoryBean(
            String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    public void registerAlias(String beanName,
            String alias) throws BeanDefinitionStoreException {

    }

    public void registerCustomEditor(
            @SuppressWarnings("rawtypes") Class requiredType,
            @SuppressWarnings("rawtypes") Class propertyEditorClass) {

    }

    public void registerCustomEditor(
            @SuppressWarnings("rawtypes") Class requiredType,
            PropertyEditor propertyEditor) {

    }

    public void registerDependentBean(String beanName,
            String dependentBeanName) {

    }

    public void registerScope(String scopeName, Scope scope) {

    }

    public void resolveAliases(StringValueResolver valueResolver) {

    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {

    }

    public void setCacheBeanMetadata(boolean cacheBeanMetadata) {

    }

    public void setParentBeanFactory(
            BeanFactory parentBeanFactory) throws IllegalStateException {

    }

    public void setTempClassLoader(ClassLoader tempClassLoader) {

    }

    public void setTypeConverter(TypeConverter typeConverter) {

    }

    public boolean containsSingleton(String beanName) {
        return false;
    }

    public Object getSingleton(String beanName) {
        return null;
    }

    public int getSingletonCount() {
        return 0;
    }

    public String[] getSingletonNames() {
        return null;
    }

    public void registerSingleton(String beanName, Object singletonObject) {

    }

    @Override
    protected void loadBeanDefinitions(
            DefaultListableBeanFactory beanFactory) throws IOException, BeansException {

    }

    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {

    }

    public AccessControlContext getAccessControlContext() {
        return null;
    }

    public BeanExpressionResolver getBeanExpressionResolver() {
        return null;
    }

    public ConversionService getConversionService() {
        return null;
    }

    public String resolveEmbeddedValue(String value) {
        return null;
    }

    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {

    }

    public void setConversionService(ConversionService conversionService) {

    }

    public void setCurrentlyInCreation(String beanName, boolean inCreation) {

    }

    @Override
    public Object getSingletonMutex() {
        return null;
    }

}
