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

package jp.terasoluna.fw.file.annotation;

/**
 * パディング種別。<br>
 * <br>
 * パディングの種別(右詰/左詰/パディングなし[LEFT/RIGHT/NONE])を示す。
 */
public enum PaddingType {
    /**
     * 右詰
     */
    RIGHT,

    /**
     * 左詰
     */
    LEFT,

    /**
     * パディングなし
     */
    NONE
}
