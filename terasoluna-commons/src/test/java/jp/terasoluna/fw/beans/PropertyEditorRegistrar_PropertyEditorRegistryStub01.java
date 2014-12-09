/*
 * Copyright (c) 2007 NTT DATA Corporation
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

package jp.terasoluna.fw.beans;

import java.beans.PropertyEditor;

import org.springframework.beans.PropertyEditorRegistry;

/**
 * PropertyEditorRegistryのスタブ。
 *
 */
public class PropertyEditorRegistrar_PropertyEditorRegistryStub01 implements
        PropertyEditorRegistry {

    protected Class clazz = null;
    protected PropertyEditor editor = null;
    
    public void registerCustomEditor(Class argClazz, PropertyEditor argEditor) {
        this.clazz = argClazz;
        this.editor = argEditor;
    }

    public void registerCustomEditor(Class arg0, String arg1,
            PropertyEditor arg2) {
    }

    public PropertyEditor findCustomEditor(Class arg0, String arg1) {
        return null;
    }

}
