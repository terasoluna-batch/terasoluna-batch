package jp.terasoluna.fw.collector.util;

import java.util.Comparator;

import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class ComparatorStub implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        Assert.isTrue(o1 instanceof Comparable,
                "The first object provided is not Comparable");
        Assert.isTrue(o2 instanceof Comparable,
                "The second object provided is not Comparable");
        return ((Comparable<Object>) o1).compareTo(o2);

    }

}
