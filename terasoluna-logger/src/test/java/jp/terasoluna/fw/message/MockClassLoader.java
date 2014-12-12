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

public class MockClassLoader extends URLClassLoader {
    protected final Map<String, List<URL>> urlMapping = new ConcurrentHashMap<String, List<URL>>();
    
    public MockClassLoader() {
        super(new URL[]{});
    }

    public void addMapping(String name, URL... urls) {
        List<URL> mapped = urlMapping.get(name);
        if (mapped == null) {
            mapped = new ArrayList<URL>();
            urlMapping.put(name, mapped);
        }
        for (URL url : urls) {
            mapped.add(url);
        }
    }
    
    @Override
    public URL getResource(String name) {
        List<URL> mapped = urlMapping.get(name);
        if (mapped.isEmpty()) {
            return super.getResource(name);
        }
        return mapped.get(0);
    }
    
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> mapped = urlMapping.get(name);
        if (mapped.isEmpty()) {
            return super.getResources(name);
        }
        return new IteratorEnumeration<URL>(mapped.iterator());
    }
    
    static class IteratorEnumeration<T> implements Enumeration<T> {
        private Iterator<T> iterator;

        public IteratorEnumeration() {
        }

        public IteratorEnumeration(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        public T nextElement() {
            return iterator.next();
        }

        public Iterator<T> getIterator() {
            return iterator;
        }

        public void setIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }
    }
}
