package net.n2oapp.context;

import java.util.function.Supplier;

/**
 * @author iryabov
 * @since 18.05.2016
 */
public class CacheTemplateByMapMock extends CacheTemplateByMap {

    @Override
    public <T> T get(Supplier<T> supplier, Object... keys) {
        return supplier.get();
    }
}
