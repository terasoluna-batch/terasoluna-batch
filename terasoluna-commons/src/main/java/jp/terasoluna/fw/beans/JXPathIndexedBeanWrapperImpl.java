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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.terasoluna.fw.beans.jxpath.BeanPointerFactoryEx;
import jp.terasoluna.fw.beans.jxpath.DynamicPointerFactoryEx;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JXPathを用いたIndexBeanWrapperの実装。
 * 
 * <p>JavaBean、Map、DynaBeanからプロパティ名を指定することにより、
 * 属性値を取得することができる。
 * 属性が配列・List型の場合、該当する属性値を全て取得する。
 * <h5>取得できる属性の型</h5>
 * <ul>
 *   <li>プリミティブ型</li>
 *   <li>プリミティブ型の配列</li>
 *   <li>JavaBean</li>
 *   <li>JavaBeanの配列・List型</li>
 *   <li>Map型</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Mapオブジェクト、またはMap型属性を使用する場合、
 * 以下の文字はMapキーに使用できない。
 * <ul>
 *   <li>/ …スラッシュ</li>
 *   <li>[ …角かっこ（開く）</li>
 *   <li>] …角かっこ（閉じ）</li>
 *   <li>. …ドット</li>
 *   <li>' …シングルクォート</li>
 *   <li>" …ダブルクォート</li>
 *   <li>( …かっこ（開く）</li>
 *   <li>) …かっこ（閉じ）</li>
 * </ul>
 * </p>
 * 
 * <hr>
 *
 * <h4>簡単な使用例</h4>
 *
 * <p>以下のようなEmployeeオブジェクトのfirstName属性にアクセスする。
 * <pre>
 * public class Employee {
 *     private String firstName;
 *
 *     public void setFirstName(String firstName) {
 *         this.firstName = firstName;
 *     }
 *     public String getFirstName() {
 *         return firstName;
 *     }
 * }
 * </pre>
 * </p>
 *
 * <p><u>１．コンストラクタでアクセス対象のJavaBeanをラップする。</u>
 * <pre>
 * // アクセス対象となるEmployeeオブジェクト
 * Employee emp = new Employee();
 * emp.setFirstName("めい");
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(emp);
 * </pre>
 * </p>
 *
 * <p><u>２．firstName属性にアクセスする。</u>
 * 引数のStringには属性名を指定する。
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>firstName</strong>");
 * </pre>
 * 
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * firstName:めい
 * </pre>
 * </p>
 * 
 * <hr>
 *
 * <h4>配列属性へのアクセス</h4>
 *
 * <p>以下のようなAddressオブジェクトの配列型属性numbersにアクセスする。
 * <pre>
 * public class Address {
 *     private int[] numbers;
 *
 *     public void setNumbers(int[] numbers) {
 *         this.numbers = numbers;
 *     }
 *     public int[] getNumbers() {
 *         return numbers;
 *     }
 * }
 * </pre>
 * </p>
 *
 * <p><u>１．コンストラクタでアクセス対象のJavaBeanをラップする。</u>
 * <pre>
 * // Employeeの属性となるAddressオブジェクト
 * Address address = new Address();
 * address.setNumbers(new int[]{1, 2, 3});
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(address);
 * </pre>
 * </p>
 *
 * <p><u>２．numbers属性にアクセスする。</u>
 * <em>'numbers[]'のように配列記号を付ける必要はなく、
 * 属性名を指定すればよいことに注意すること。</em>
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>numbers</strong>");
 * </pre>
 * </p>
 *
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * numbers[0]:1
 * numbers[1]:2
 * numbers[2]:3
 * </pre>
 * List型のオブジェクトに対しても、同様の方法で値が取得できる。
 * </p>
 * 
 * <hr>
 *
 * <h4>ネストした属性へのアクセス</h4>
 *
 * <p>下記のようなEmployeeオブジェクトから、
 * ネストされたAddressクラスのstreetNumber属性にアクセスする。
 * <pre>
 * public class Employee {
 *     private Address homeAddress;
 *
 *     public void setHomeAddress(Address homeAddress) {
 *         this.homeAddress = homeAddress;
 *     }
 *     public Address getHomeAddress() {
 *         return homeAddress;
 *     }
 * }
 * public class Address {
 *     private String streetNumber;
 *
 *     public void setStreetNumber(String streetNumber) {
 *         this.streetNumber = streetNumber;
 *     }
 *     public String getStreetNumber() {
 *         return streetNumber;
 *     }
 * }
 * </pre>
 * </p>
 * 
 * <p><u>１．コンストラクタでアクセス対象のJavaBeanをラップする。</u>
 * <pre>
 * // Employeeの属性となるAddressオブジェクト
 * Address address = new Address();
 * address.setStreetNumber("住所");
 * 
 * // Employee
 * Employee emp = new Employee();
 * emp.setHomeAddress(address);
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(emp);
 * </pre>
 * </p>
 *
 * <p><u>２．EmployeeオブジェクトのhomeAddress属性にネストされた、
 * streetNumber属性にアクセスする。</u>
 * ネストした属性を指定する場合、以下のコードのように'.'（ドット）で
 * 属性名を連結する。
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>homeAddress.streetNumber</strong>");
 * </pre>
 * </p>
 * 
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * homeAddress.streetNumber:住所
 * </pre>
 * ネストした属性が配列・List型であっても、値を取得することができる。
 * </p>
 * 
 * <hr>
 *
 * <h4>Map型属性へのアクセス</h4>
 *
 * <p>下記のようなEmployeeオブジェクトのMap属性addressMapにアクセスする。
 * <pre>
 * public class Employee {
 *     private Map addressMap;
 *
 *     public void setAddressMap(Map addressMap) {
 *         this.addressMap = addressMap;
 *     }
 *     public Map getAddressMap() {
 *         return addressMap;
 *     }
 * }
 * </pre>
 * </p>
 * 
 * <p><u>１．コンストラクタでアクセス対象のJavaBeanをラップする。</u>
 * <pre>
 * // Employeeの属性となるMap
 * Map addressMap = new HashMap();
 * addressMap.put("home", "address1");
 * 
 * // Employee
 * Employee emp = new Employee();
 * emp.setAddressMap(addressMap);
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(emp);
 * </pre>
 * </p>
 *
 * <p><u>２．EmployeeのaddressMap属性中にセットしたhomeキーにアクセスする。</u>
 * Map型属性のキーを指定する場合、以下のコードのようにかっこでキー名を連結する。
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>addressMap(home)</strong>");
 * </pre>
 * </p>
 * 
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * addressMap(home):address1
 * </pre>
 * Map型属性のキー名は()（括弧）で囲われることに注意すること。
 * </p>
 *
 * <hr>
 *
 * <h4>Mapオブジェクトへのアクセス</h4>
 *
 * <p>本クラスはJavaBeanだけではなく、Mapオブジェクトへのアクセスが可能である。
 * 
 * <p><u>１．コンストラクタでアクセス対象のMapをラップする。</u>
 * <pre>
 * // Employeeの属性となるMap
 * Map addressMap = new HashMap();
 * addressMap.put("home", "address1");
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(addressMap);
 * </pre>
 * </p>
 *
 * <p><u>２．addressMapにセットしたhomeキーにアクセスする。</u>
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>home</strong>");
 * </pre>
 * </p>
 * 
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * home:address1
 * </pre>
 * Mapオブジェクトに対しても、配列・List型属性、
 * ネストした属性の取得が可能である。
 * </p>
 * 
 * <hr>
 *
 * <h4>DynaBeanへのアクセス</h4>
 *
 * <p>本クラスはJavaBeanだけではなく、DynaBeanへのアクセスが可能である。
 * 
 * <p><u>１．コンストラクタでアクセス対象のDynaBeanをラップする。</u>
 * <pre>
 * // DynaBeanにラップされるJavaBean
 * Address address = new Address();
 * address.setStreetNumber("住所");
 * 
 * // DynaBean
 * DynaBean dynaBean = new WrapDynaBean(address);
 * 
 * IndexedBeanWrapper bw = new JXPathIndexedBeanWrapperImpl(dynaBean);
 *     
 * --------------------------------------------------------
 * 上記のコードで使用しているAddressオブジェクトは以下のようなクラスである。
 * 
 * public class Address {
 *     private String streetNumber;
 *
 *     public void setStreetNumber(String streetNumber) {
 *         this.streetNumber = streetNumber;
 *     }
 *     public String getStreetNumber() {
 *         return streetNumber;
 *     }
 * }
 * </pre>
 * 
 * </p>
 *
 * <p><u>２．DynaBeanのstreetNumber属性にアクセスする。</u>
 * <pre>
 * Map&lt;String, Object&gt; map = bw.getIndexedPropertyValues("<strong>streetNumber</strong>");
 * </pre>
 * </p>
 * 
 * キーがプロパティ名、値が属性値のMapインスタンスが返される。
 * 以下のコードでは全ての要素をコンソールに出力している。
 * <pre>
 * System.out.println("Mapのキー：Mapの値");
 * System.out.println("========================");
 * Set&lt;String&gt; keyset = map.keySet();
 * for (String key : keyset) {
 *     System.out.print(key + ":");
 *     System.out.println(map.get(key).toString());
 * }
 * </pre>
 * 結果は以下のように出力される。
 * <pre>
 * Mapのキー：Mapの値
 * ========================
 * streetNumber:住所
 * </pre>
 * </p>
 * 
 */
public class JXPathIndexedBeanWrapperImpl implements IndexedBeanWrapper {
    /**
     * ログクラス。
     */
    private static Log log 
        = LogFactory.getLog(JXPathIndexedBeanWrapperImpl.class);
    
    /**
     * JXPathコンテキスト。
     */
    protected JXPathContext context = null;
    
    /**
     * 初期化処理。
     * 
     * <p>拡張したNodePointerファクトリを追加する。
     * NodePointerファクトリはstaticメソッドで、一度だけ呼び出す。
     * 実行中にNodePointerファクトリ追加を行なうと、
     * マルチスレッド環境にてNullPointerExceptionが発生する可能性がある。</p>
     */
    static {
    	JXPathContextReferenceImpl.addNodePointerFactory(
                new BeanPointerFactoryEx());
        JXPathContextReferenceImpl.addNodePointerFactory(
                new DynamicPointerFactoryEx());
    }
    
    /**
     * コンストラクタ。
     * @param target 対象のオブジェクト
     */
    public JXPathIndexedBeanWrapperImpl(Object target) {
        // ターゲットとなるJavaBeanがNullの場合は例外
        if (target == null) {
            log.error("TargetBean is null!");
            throw new IllegalArgumentException("TargetBean is null!");
        }
        context = JXPathContext.newContext(target);
    }
    
    /**
     * 指定したプロパティ名に一致する属性値を返す。
     *
     * @param propertyName プロパティ名
     * @return プロパティ名に一致する属性値を格納するMap（位置情報、属性値）
     */
    public Map<String, Object> getIndexedPropertyValues(String propertyName) {
        
        // プロパティ名がNull・空文字
        if (StringUtils.isEmpty(propertyName)) {
            String message = "PropertyName is empty!";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        
        // プロパティ名に不正な文字
        if (StringUtils.indexOfAny(propertyName,
                new char[]{'/', '"', '\''}) != -1) { 
            String message = "Invalid character has found within property name."
                + " '" + propertyName + "' " + "Cannot use [ / \" ' ]";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        
        // 配列の[]以外に[]が使われている
        String stringIndex = extractIndex(propertyName);
        if (stringIndex.length() > 0) {
            try {
                Integer.parseInt(stringIndex);
            } catch (NumberFormatException e) {
                String message = "Invalid character has found within property name."
                    + " '" + propertyName + "' " + "Cannot use [ [] ]";
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        }
        
        Map<String, Object> result
            = new LinkedHashMap<String, Object>();
        String requestXpath = toXPath(propertyName);
        
        // JXPathからプロパティ取得
        Iterator ite = null;
        try {
            ite = context.iteratePointers(requestXpath);
        } catch (JXPathException e) {
            // プロパティ名が不正
            String message = 
                "Invalid property name. "
                + "PropertyName: '" + propertyName + "'"
                + "XPath: '" + requestXpath + "'";
            log.error(message, e);
            throw new IllegalArgumentException(message, e);
        }
        
        // XPath ⇒ Java property
        while (ite.hasNext()) {
            Pointer p = (Pointer) ite.next();
            result.put(this.toPropertyName(p.asPath()), p.getValue());
        }
        return result;
    }
    
    /**
     * プロパティ形式の文字列をXPath形式の文字列に変換する。
     * @param propertyName プロパティ形式文字列
     * @return XPath形式文字列
     */
    protected String toXPath(String propertyName) {
        StringBuilder builder = new StringBuilder("/");
        String[] properties = StringUtils.split(propertyName, '.');
        
        if (properties == null || properties.length == 0) {
            String message = "Property name is null or blank.";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        
        for (String property : properties) {
            // ネスト
            if (builder.length() > 1) {
                builder.append('/');
            }
            
            // Map属性
            if (isMapProperty(property)) {
                builder.append(escapeMapProperty(property));
                
            // JavaBean または Primitive
            } else {
                builder.append(extractAttributeName(property));
            }
           
            // 配列インデックス
            builder.append(extractIncrementIndex(property));
        }
        return builder.toString();
    }
    
    /**
     * インクリメントされた添え字を取り出す。
     * @param property Javaプロパティ名。
     * @return String XPath形式の添え字。 
     */
    protected String extractIncrementIndex(String property) {
        return extractIncrementIndex(property, 1);
    }

    /**
     * インクリメントされた添え字を取り出す。
     * @param property プロパティ名。
     * @param increment インクリメントする値。
     * @return String インクリメントされた添え字。
     */
    protected String extractIncrementIndex(String property, int increment) {
        String stringIndex = extractIndex(property);
        if ("".equals(stringIndex)) {
            return "";
        }
        
        // 添え字が取得できた場合、インクリメントする
        try {
            int index = Integer.parseInt(stringIndex);
            return new StringBuilder().append('[')
                .append(index + increment).append(']').toString();
        } catch (NumberFormatException e) {
            // 配列の[]ではない
            return "";
        }
    }

    /**
     * 配列インデックスを取得する。
     * @param property プロパティ名。
     * @return 配列インデックス。
     */
    protected String extractIndex(String property) {
        int start = property.lastIndexOf('[');
        int end = property.lastIndexOf(']');
        
        // []がないので配列ではない
        if (start == -1 && end == -1) {
            return "";
        }
        
        // ']aaa[' のように[]の位置が不正、または[]のどちらかしかない
        if (start == -1 || end == -1 || start > end) {
            String message = "Cannot get Index. "
                + "Invalid property name. '" + property + "'";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return property.substring(start + 1, end);
    }
    
    /**
     * MapプロパティをXPath形式にエスケープする。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    protected String escapeMapProperty(String property) {
        // aaa(bbb) → aaa/bbb
        String mapPropertyName = extractMapPropertyName(property);
        String mapKey = extractMapPropertyKey(property);
        return mapPropertyName + "/" + mapKey;
    }

    /**
     * Map型属性のプロパティ名を取り出す。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    protected String extractMapPropertyName(String property) {
        int pos = property.indexOf('(');
        
        // '('がない場合は例外
        if (pos == -1) {
            String message = "Cannot get Map attribute. "
                + "Invalid property name. '" + property + "'";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return property.substring(0, pos);
    }

    /**
     * Map型属性のキー名を取り出す。
     * @param property Javaプロパティ名。
     * @return String XPath。 
     */
    protected String extractMapPropertyKey(String property) {
        // aaa(bbb) → bbb
        int start = property.indexOf('(');
        int end = property.indexOf(')');
        
        // '()'がない、または()の位置が不正な場合は例外
        if (start == -1 || end == -1 || start > end) {
            String message = "Cannot get Map key. "
                + "Invalid property name. '" + property + "'";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return property.substring(start + 1, end);
    }

    /**
     * Map型属性かどうか判断する。
     * @param property Javaプロパティ名。
     * @return boolean Map型属性ならばtrue、それ以外はfalseを返す。 
     */
    protected boolean isMapProperty(String property) {
        // '()'があればMap型属性
        if (property.indexOf('(') != -1 && property.indexOf(')') != -1) {
            return true;
        }
        return false;
    }
    
    /**
     * XPath形式の文字列をプロパティ形式の文字列に変換する。
     * @param xpath XPath形式文字列
     * @return プロパティ形式文字列
     */
    protected String toPropertyName(String xpath) {
        StringBuilder builder = new StringBuilder("");
        String[] nodes = StringUtils.split(xpath, '/');
        
        if (nodes == null || nodes.length == 0) {
            String message = "XPath is null or blank.";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            
            // Mapオブジェクト
            if (i == 0 && isMapObject(node)) {
                builder.append(extractMapKey(node));
                builder.append(extractDecrementIndex(node));
                continue;
            }
            
            // ネスト
            if (builder.length() > 0) {
                builder.append('.');
            }
            
            // Map属性
            if (isMapAttribute(node)) {
                builder.append(extractMapAttributeName(node));
                builder.append('(');
                builder.append(extractMapKey(node));
                builder.append(')');
                
            // JavaBean または primitive
            } else {
                builder.append(extractAttributeName(node));
            }
            
            // 配列インデックス
            builder.append(extractDecrementIndex(node));
        }
        return builder.toString();
    }

    /**
     * 属性名を取り出す。
     * 配列の場合、添え字はカットされる。
     * @param node XPathのノード。
     * @return 属性名。
     */
    protected String extractAttributeName(String node) {
        int pos = node.lastIndexOf('[');
        if (pos == -1) {
            return node;
        }
        // 配列の添え字はカット
        return node.substring(0, pos);
    }

    /**
     * Mapの属性名を取り出す。
     * @param node XPathのノード。
     * @return 属性名。
     */
    protected String extractMapAttributeName(String node) {
        // 最初の'['までの文字列をMapの属性名とする
        int pos = node.indexOf('[');
        
        // '['がない場合は例外
        if (pos == -1) {
            String message = "Cannot get Map attribute. "
                + "Invalid property name. '" + node + "'";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return node.substring(0, pos);
    }

    /**
     * Mapキーを取り出す。
     * @param node XPathのノード。
     * @return 属性名。
     */
    protected String extractMapKey(String node) {
        // aaa[@name='bbb'] → bbb 
        int start = node.indexOf('[');
        int end = node.indexOf(']');
        
        // '[]'がない、または[]の位置が不正な場合は例外
        if (start == -1 || end == -1 || start > end) {
            String message = "Cannot get Map key. "
                + "Invalid property name. '" + node + "'";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return node.substring(start + "[@name='".length(), end - "'".length());
    }

    /**
     * デクリメントした添え字を取り出す。
     * @param node XPathのノード。
     * @return 属性名。
     */
    protected String extractDecrementIndex(String node) {
        return extractIncrementIndex(node, -1);
    }

    /**
     * Map属性を持つオブジェクトかどうか判断する。
     * @param node XPathのノード。
     * @return Map属性ならばtrue、それ以外はfalseを返す。
     */
    protected boolean isMapAttribute(String node) {
        // '[@name'があればMap属性
        if (node.indexOf("[@name") != -1) {
            return true;
        }
        return false;
    }

    /**
     * Mapオブジェクトかどうか判断する。
     * @param node XPathのノード。
     * @return Mapオブジェクトならばtrue、それ以外はfalseを返す。
     */
    protected boolean isMapObject(String node) {
        // '.[@name'…で始まるならばMapオブジェクト
        if (node.startsWith(".[@name")) {
            return true;
        }
        return false;
    }
}
