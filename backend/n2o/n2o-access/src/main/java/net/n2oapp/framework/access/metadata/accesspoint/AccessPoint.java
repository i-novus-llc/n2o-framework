package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.copy;

/**
 * Точки доступа
 */
public abstract class AccessPoint implements Source, Compiled, NamespaceUriAware {

    private String namespaceUri;

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("please implement equals() and hashCode()");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("please implement equals() and hashCode()");
    }

    protected  <T extends AccessPoint> void split(String val, BiConsumer<T, String> setter,
                                                     final List<AccessPoint> pointList) {
        if (val != null && val.contains(",")) {
            String[] split = val.replaceAll("\\s+", "").split(",");
            List<T> list = stream(split)
                    .map(str -> {
                        T point = copy((T) this);
                        setter.accept(point, str);
                        return point;
                    }).collect(Collectors.toList());
            pointList.remove(this);
            pointList.addAll(list);
        }
    }

    protected  <T extends AccessPoint, R> void collectAll(String val, Supplier<R> compiler, Function<R, Stream<String>> getter,
                                                          BiConsumer<T, String> setter, final List<AccessPoint> pointList) {
        if (val != null && val.equals("*")) {
            R r = compiler.get();
            if (r != null) {
                List<T> points = getter.apply(r)
                        .map(str -> {
                            T point = copy((T) this);
                            setter.accept(point, str);
                            return point;
                        }).collect(Collectors.toList());
                pointList.remove(this);
                pointList.addAll(points);
            }
        }
    }
}
