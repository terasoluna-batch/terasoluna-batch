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
 *
 */

package jp.terasoluna.fw.batch.executor;

import jp.terasoluna.fw.batch.blogic.BLogic;
import jp.terasoluna.fw.batch.blogic.vo.BLogicParam;
import jp.terasoluna.fw.batch.exception.handler.ExceptionHandler;
import jp.terasoluna.fw.batch.message.MessageAccessor;
import jp.terasoluna.fw.batch.util.MessageUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;

/**
 * {@code MessageAccessor}、および業務用コンテキストとして取得される{@code ApplicationContext}を
 * それぞれ{@code MessageUtil}、{@code ThreadGroupApplicationContextHolder}に設定する。
 * 本クラスの使用上の前提として、{@code SeparateGroupThreadFactory}が併用され
 * ワーカスレッドのスレッドグループが個別に割り当てられていること。
 * なお、ポイントカット対象のメソッドは{@code BLogicExecutor#execute()}に限定されている。
 *
 * @see ThreadGroupApplicationContextHolder
 * @see jp.terasoluna.fw.batch.executor.controller.SeparateGroupThreadFactory
 * @see jp.terasoluna.fw.batch.executor.worker.BLogicExecutor#execute(ApplicationContext, BLogic, BLogicParam, ExceptionHandler)
 * @since 3.6
 */
@Aspect
public class ThreadGroupManagementAspect {

    /**
     * メッセージアクセサ.
     */
    protected MessageAccessor messageAccessor;

    /**
     * コンストラクタ.
     *
     * @param messageAccessor メッセージアクセサ
     */
    public ThreadGroupManagementAspect(MessageAccessor messageAccessor) {
        this.messageAccessor = messageAccessor;
    }

    /**
     * {@code BLogicExecutor#execute()}メソッドに対してaround-adviceとして{@code MessageAccessor}、
     * 業務用{@code ApplicationContext}を同一スレッドグループ上に設定する。
     *
     * ジョインポイントとなるメソッドの実行後、例外スローの有無にかかわらず{@code MessageAccessor}、
     * 業務用{@code ApplicationContext}のクリアを実施する。
     *
     * @param pjp ジョインポイント（{@code BLogicExecutor#execute()}）
     * @return
     * @throws Throwable
     */
    @Around("execution(* jp.terasoluna.fw.batch.executor.worker.BLogicExecutor.execute(..))")
    public Object aroundExecute(ProceedingJoinPoint pjp) throws Throwable {
        try {
            MessageUtil.setMessageAccessor(messageAccessor);
            if (pjp.getArgs().length > 0 && pjp.getArgs()[0] instanceof ApplicationContext) {
                ThreadGroupApplicationContextHolder.setApplicationContext(
                        ApplicationContext.class.cast(pjp.getArgs()[0]));
            }
            return pjp.proceed();
        } finally {
            MessageUtil.removeMessageAccessor();
            ThreadGroupApplicationContextHolder.removeApplicationContext();
        }
    }
}
