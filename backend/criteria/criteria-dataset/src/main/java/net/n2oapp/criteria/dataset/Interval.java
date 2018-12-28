package net.n2oapp.criteria.dataset;

import java.util.Map;

/**
 * User: operhod
 * Date: 01.11.13
 * Time: 12:16
 */
public class Interval<T extends Comparable> extends DataSet {
    private T begin;
    private T end;

    public static final Comparable MAX = Integer.MAX_VALUE;
    public static final Comparable MIN = Integer.MIN_VALUE;

    public Interval(T begin, T end) {
        this.begin = begin;
        this.end = end;
        put("begin", begin);
        put("end", end);
    }

    /**
     * вырожденный случай интервала в виде одного значения
     */
    public Interval(T value) {
        this.begin = value;
        this.end = value;
        put("begin", begin);
        put("end", end);
    }

    public Interval() {
    }

    public Interval(Interval<T> interval) {
        this.begin = interval.begin;
        this.end = interval.end;
        put("begin", begin);
        put("end", end);
    }

    public Interval(Map<? extends String, ?> m) {
        super(m);
    }

    @Override
    public Object put(String key, Object value) {
        if ("begin".equals(key)) {
            begin = (T) value;
        } else if ("end".equals(key)) {
            end = (T) value;
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (m.containsKey("begin")) {
            begin = (T) m.get("begin");
        }
        if (m.containsKey("end")) {
            end = (T) m.get("end");
        }
        super.putAll(m);
    }

    public T getBegin() {
        return begin;
    }


    public T getEnd() {
        return end;
    }

    public Class getDomain() {
        return begin.getClass();
    }

    /**
     * @param value - проверяемый интервал
     * @return входит ли проверяемый интервал в исходный
     */
    public boolean check(Interval value) {
        if (!getBegin().equals(MIN))
            if (value.getBegin().equals(MIN) || getBegin().compareTo(value.getBegin()) > 0)
                return false;
        if (!getEnd().equals(MAX))
            return !value.getEnd().equals(MAX) && getEnd().compareTo(value.getEnd()) >= 0;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + begin.hashCode();
        result = prime * result + end.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interval other = (Interval) obj;
        if (!begin.equals(other.begin))
            return false;
        return end.equals(other.end);
    }
}
