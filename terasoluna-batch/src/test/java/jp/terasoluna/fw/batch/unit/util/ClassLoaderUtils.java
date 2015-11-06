package jp.terasoluna.fw.batch.unit.util;

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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;

import jp.terasoluna.fw.batch.unit.exception.UTRuntimeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class ClassLoaderUtils {
    protected volatile static ClassLoader previousClassLoader = null;
    protected static final Log LOG = LogFactory.getLog(ClassLoaderUtils.class);

    private static Method getAddURLMethod() {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL",
                    new Class<?>[] { URL.class });
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            throw new UTRuntimeException(e);
        }
    }

    /**
     * このスレッドのコンテキストのクラスローダを返します。
     * 
     * @return クラスローダ
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * このスレッドのコンテキストのクラスローダを設定します。
     * 
     * @param cl クラスローダ
     */
    public static void setClassLoader(ClassLoader cl) {
        Thread.currentThread().setContextClassLoader(cl);
    }

    /**
     * <pre>
     * このスレッドのクラスローダのクラスパスに引数のurlを追加した新しいクラスローダを作成し、
     * このスレッドのコンテキストのクラスローダを設定します。
     * 
     * このスレッドのコンテキストのクラスローダを
     * 本メソッドによって更新される前のクラスローダに戻したい場合、
     * {@link #resetClassLoader()}を使用してください。
     * </pre>
     * 
     * @param url 追加するパス
     */
    public static void addClassPath(URL url) {
        Assert.notNull(url);
        ClassLoader cl = getClassLoader();

        URLClassLoader newCl = null;
        if (cl instanceof URLClassLoader) {
            @SuppressWarnings("resource")
            URLClassLoader ucl = (URLClassLoader) cl;
            newCl = new URLClassLoader(ucl.getURLs(), cl);
        } else {
            newCl = new URLClassLoader(null, cl);
        }

        Method method = getAddURLMethod();

        try {
            method.invoke(newCl, url);
        } catch (Exception e) {
            throw new UTRuntimeException(e);
        }

        previousClassLoader = cl;
        setClassLoader(newCl);
    }

    /**
     * このスレッドのコンテキストのクラスローダをaddClassPathによって更新される一つ前のクラスローダに戻します。
     */
    public static void resetClassLoader() {
        if (previousClassLoader != null) {
            setClassLoader(previousClassLoader);
        }
    }

    /**
     * <pre>
     * このスレッドのクラスローダのクラスパスに引数のファイルを追加した新しいクラスローダを作成し、
     * このスレッドのコンテキストのクラスローダを設定します。
     * 
     * このスレッドのコンテキストのクラスローダを
     * 本メソッドによって更新される前のクラスローダに戻したい場合、
     * {@link #resetClassLoader()}を使用してください。
     * 
     * ファイルが存在しない場合は追加せず、warnログを出力します。
     * </pre>
     * 
     * @param file 追加するパス
     */
    public static void addClassPath(File file) {
        Assert.notNull(file);
        try {
            if (file.exists()) {
                addClassPath(file.toURI().toURL());
            } else {
                LOG.warn(file + " is not found.");
            }
        } catch (MalformedURLException e) {
            // ファイルチェックを行っているので発生しない。
            LOG.warn(file + " is illegal.", e);
        }
    }

    /**
     * <pre>
     * このスレッドのクラスローダのクラスパスに引数のパスを追加した新しいクラスローダを作成し、
     * このスレッドのコンテキストのクラスローダを設定します。
     * 
     * このスレッドのコンテキストのクラスローダを
     * 本メソッドによって更新される前のクラスローダに戻したい場合、
     * {@link #resetClassLoader()}を使用してください。
     * 
     * パスが存在しない場合は{@link UTRuntimeException}をスローします。
     * </pre>
     * 
     * @param path 追加するパス
     * @throws UTRuntimeException パスが存在しない場合
     */
    public static void addClassPath(String path) {
        Assert.notNull(path);
        addClassPath(new File(path));
    }

    /**
     * <pre>
     * srcPathsに含まれるパスそれぞれに対して、
     * このスレッドのコンテキストのクラスローダのクラスパス上に
     * ファイルが存在する場合、destPathsに追加します。
     * </pre>
     * 
     * @param destPaths
     * @param srcPaths
     */
    public static void addPathIfExists(List<String> destPaths,
            List<String> srcPaths) {
        Assert.notNull(destPaths);
        Assert.notNull(srcPaths);

        ClassLoader cl = getClassLoader();
        for (String path : srcPaths) {
            if (path != null) {
                URL r = cl.getResource(path);
                if (r != null) {
                    try {
                        String protocol = r.getProtocol();
                        if (protocol.equals("file")) {
                            URI uri = r.toURI();
                            File f = new File(uri);
                            if (f.isFile()) {
                                // ファイルの場合のみ追加する
                                destPaths.add(path);
                            }
                        } else {
                            // jar内等
                            URLConnection con = null;
                            try {
                                con = r.openConnection();
                                if (con.getContentLength() > 0) {
                                    // ファイルの中身がある場合に追加
                                    destPaths.add(path);
                                }
                            } catch (IOException e) {
                                // チェック済みのなので発生しない
                                LOG.warn(con + " is illegal.", e);
                            }
                        }
                    } catch (URISyntaxException e) {
                        // r != nullの段階ですでにはじかれるのでこのルートは通らない
                        LOG.warn(path + " is illegal.", e);
                    }
                } else {
                    LOG.debug(path + " is not found.");
                }
            }
        }
    }
}
