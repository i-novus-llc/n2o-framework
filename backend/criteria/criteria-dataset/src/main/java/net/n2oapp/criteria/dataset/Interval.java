package net.n2oapp.criteria.dataset;

import java.util.HashMap;

/**
 * Интервал значений
 */
public class Interval<T extends Comparable> extends HashMap<String, T> {
    private static final String BEGIN = "begin";
    private static final String END = "end";

    public Interval(T begin, T end) {
        put(BEGIN, begin);
        put(END, end);
    }

    public Interval(T value) {
        put(BEGIN, value);
        put(END, value);
    }

    public Interval() {
    }

    public T getBegin() {
        return get(BEGIN);
    }

    public T getEnd() {
        return get(END);
    }

    public void setBegin(Object begin) {
        put(BEGIN, (T) begin);
    }

    public void setEnd(Object end) {
        put(END, (T) end);
    }

    public Class getDomain() {
        Object value = getBegin() != null ? getBegin() : getEnd();
        return value != null ? value.getClass() : null;
    }
}
