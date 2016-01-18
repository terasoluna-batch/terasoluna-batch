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

package jp.terasoluna.fw.batch.message;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * Spring Frameworkが提供している機能を利用して、メッセージを取得するクラス。<br>
 */
public class MessageAccessorImpl extends ApplicationObjectSupport implements
                                                                 MessageAccessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(String code, Object[] args) {

        return getMessageSourceAccessor().getMessage(code, args);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(MessageSourceResolvable resolvable) {

        return getMessageSourceAccessor().getMessage(resolvable);

    }

}
