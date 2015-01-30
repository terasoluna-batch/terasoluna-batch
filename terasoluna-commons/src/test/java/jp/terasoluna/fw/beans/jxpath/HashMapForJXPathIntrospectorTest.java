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

package jp.terasoluna.fw.beans.jxpath;

import java.util.HashMap;

import junit.framework.TestCase;

/**
 * {@link jp.terasoluna.fw.beans.jxpath.HashMapForJXPathIntrospector} クラスのブラックボックステスト。
 * 
 * <p>
 * <h4>【クラスの概要】</h4>
 * commons-JXPathのバグ(JXPATH-152)回避用HashMap。<br>
 * <p>
 * 
 * @see jp.terasoluna.fw.beans.jxpath.HashMapForJXPathIntrospector
 */
public class HashMapForJXPathIntrospectorTest extends TestCase {


    /**
     * 初期化処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 終了処理を行う。
     * 
     * @throws Exception このメソッドで発生した例外
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * testHashMapForJXPathIntrospector01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) HashMap<br>
     *                {
     *                  "aaa"="xxx",
     *                  "bbb"="yyy"
     *                }<br>
     *         
     * <br>
     * 期待値：(戻り値) HashMapForJXPathIntrospector<br>
     *                {
     *                  "aaa"="xxx",
     *                  "bbb"="yyy"
     *                }<br>
     *         
     * <br>
     * コンストラクタ引数に与えたMapのエントリーをすべて含むインスタンスがつくられることの確認。
     * (スーパークラスへの委譲確認。)
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testHashMapForJXPathIntrospector01() throws Exception {
        // 前処理
        HashMap<String, String> srcMap = new HashMap<String, String>();
        srcMap.put("aaa", "xxx");
        srcMap.put("bbb", "yyy");

        // テスト実施
        HashMapForJXPathIntrospector<String, String> map = new HashMapForJXPathIntrospector<String, String>(srcMap);

        // 判定
        assertEquals(2, map.size());
        assertEquals("xxx", map.get("aaa"));
        assertEquals("yyy", map.get("bbb"));
    }

    /**
     * testGet01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) "aaa"<br>
     *         (状態) {
     *                  "aaa"="xxx"
     *                }<br>
     *         (状態) putに2秒かかるキーオブジェクトでput中<br>
     *         
     * <br>
     * 期待値：(戻り値) "xxx"<br>
     *         (状態変化) getが即座には終了しない<br>
     *         
     * <br>
     * put中はgetが待たされることの確認。
     * (戻り値は、スーパークラスへの委譲確認。)
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGet01() throws Exception {
        // 前処理
        final HashMapForJXPathIntrospector<Object, String> map = new HashMapForJXPathIntrospector<Object, String>(new HashMap<Object, String>());
        map.put("aaa", "xxx");
        new Thread() {

            @Override
            public void run() {
                map.put(new WaitHashObject(2000), "wait");
            }
            
        }.start();
        Thread.sleep(500);

        // テスト実施
        long start = System.currentTimeMillis();
        String ret = map.get("aaa");
        long time = System.currentTimeMillis() - start;

        // 判定
        assertTrue(time > 1000); // 約1500ミリ秒
        assertEquals("xxx", ret);
    }

    /**
     * testGet02()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) "aaa"<br>
     *         (状態) {
     *                  "aaa"="xxx"
     *                  getに2秒かかるキーオブジェクト="wait"
     *                }<br>
     *         (状態) getに2秒かかるキーオブジェクトでget中<br>
     *         
     * <br>
     * 期待値：(戻り値) "xxx"<br>
     *         (状態変化) getは即座に終了する<br>
     *         
     * <br>
     * put中でなければgetは同時に複数スレッドで使用可能であることの確認。
     * (戻り値は、スーパークラスへの委譲確認。)
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testGet02() throws Exception {
        // 前処理
        final HashMapForJXPathIntrospector<Object, String> map = new HashMapForJXPathIntrospector<Object, String>(new HashMap<Object, String>());
        map.put("aaa", "xxx");
        final WaitHashObject waitKey = new WaitHashObject(2000);
        map.put(waitKey, "wait");
        new Thread() {

            @Override
            public void run() {
                map.get(waitKey);
            }

        }.start();
        Thread.sleep(500);

        // テスト実施
        long start = System.currentTimeMillis();
        String ret = map.get("aaa");
        long time = System.currentTimeMillis() - start;

        // 判定
        assertTrue(time < 500); // ほぼ0ミリ秒
        assertEquals("xxx", ret);
    }

    /**
     * testPut01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) key:"aaa"<br>
     *         (引数) value:"xxx"<br>
     *         (状態) {
     *                  (空)
     *                }<br>
     *         (状態) putに2秒かかるキーオブジェクトでput中<br>
     *         
     * <br>
     * 期待値：(状態変化) エントリー"aaa"="xxx"が追加される<br>
     *         (状態変化) putが即座には終了しない<br>
     *         
     * <br>
     * put中はputが待たされることの確認。
     * (エントリー追加は、スーパークラスへの委譲確認。)
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testPut01() throws Exception {
        // 前処理
        final HashMapForJXPathIntrospector<Object, String> map = new HashMapForJXPathIntrospector<Object, String>(new HashMap<Object, String>());
        new Thread() {

            @Override
            public void run() {
                map.put(new WaitHashObject(2000), "wait");
            }
            
        }.start();
        Thread.sleep(500);

        // テスト実施
        long start = System.currentTimeMillis();
        map.put("aaa", "xxx");
        long time = System.currentTimeMillis() - start;

        // 判定
        assertTrue(time > 1000); // 約1500ミリ秒
        assertEquals("xxx", map.get("aaa"));
    }

    /**
     * testPut01()
     * <br><br>
     * 
     * (正常系)
     * <br>
     * 観点：A
     * <br><br>
     * 入力値：(引数) key:"aaa"<br>
     *         (引数) value:"xxx"<br>
     *         (状態) {
     *                  getに2秒かかるキーオブジェクト="wait"
     *                }<br>
     *         (状態) getに2秒かかるキーオブジェクトでget中<br>
     *         
     * <br>
     * 期待値：(状態変化) エントリー"aaa"="xxx"が追加される<br>
     *         (状態変化) putが即座には終了しない<br>
     *         
     * <br>
     * get中はputが待たされることの確認。
     * (エントリー追加は、スーパークラスへの委譲確認。)
     * <br>
     * 
     * @throws Exception このメソッドで発生した例外
     */
    public void testPut02() throws Exception {
        // 前処理
        final HashMapForJXPathIntrospector<Object, String> map = new HashMapForJXPathIntrospector<Object, String>(new HashMap<Object, String>());
        final WaitHashObject waitKey = new WaitHashObject(2000);
        map.put(waitKey, "wait");
        new Thread() {

            @Override
            public void run() {
                map.get(waitKey);
            }

        }.start();
        Thread.sleep(500);

        // テスト実施
        long start = System.currentTimeMillis();
        map.put("aaa", "xxx");
        long time = System.currentTimeMillis() - start;

        // 判定
        assertTrue(time > 1000); // 約1500ミリ秒
        assertEquals("xxx", map.get("aaa"));
    }

    /**
     * HashMapへのputやgetに時間がかかるキーオブジェクト。
     * putやgetの際に利用されるhashCodeメソッドに、スリープを入れている。
     * スリープ時間はコンストラクタで指定する。
     */
    private static class WaitHashObject {
        private long sleepMillis;
        public WaitHashObject(long sleepMillis) {
            this.sleepMillis = sleepMillis;
            
        }
        @Override
        public int hashCode() {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
            }
            return super.hashCode();
        }
        
    }
}
