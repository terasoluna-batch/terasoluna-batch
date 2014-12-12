package jp.terasoluna.fw.message;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BadClassLoader extends URLClassLoader {
    protected final Map<String, List<URL>> urlMapping = new ConcurrentHashMap<String, List<URL>>();
    
    public BadClassLoader() {
        super(new URL[]{});
    }

    @Override
    public URL getResource(String name) {
        throw new RuntimeException("hoge");
    }

}
