package jp.terasoluna.fw.collector.util.strategy;

import java.util.Comparator;

public class ComparatorStub1 implements Comparator<Object> {
    
    protected Object obj1;

    protected Object obj2;

    protected int returnValue;
    
    public int compare(Object o1, Object o2) {
        obj1 = o1;
        obj2 = o2;
        return returnValue;
    }

}
