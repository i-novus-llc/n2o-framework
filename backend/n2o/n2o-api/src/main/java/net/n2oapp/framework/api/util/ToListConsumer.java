package net.n2oapp.framework.api.util;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author operehod
 * @since 06.04.2015
 */
public class ToListConsumer<T> implements Consumer<T> {

    private List<T> list;

    @Override
    public void accept(T t) {
        if (list == null)
            list = new ArrayList<>();
        list.add(t);
    }

    public List<T> toList() {
        if (list == null)
            return Collections.emptyList();
        return list;
    }

    public int size() {
        if (list == null)
            return 0;
        return list.size();
    }

    public Set<T> toSet() {
        if (list == null)
            return Collections.emptySet();
        return new HashSet<>(list);
    }

    public void clear() {
        if (list != null)
            list.clear();
    }


}
