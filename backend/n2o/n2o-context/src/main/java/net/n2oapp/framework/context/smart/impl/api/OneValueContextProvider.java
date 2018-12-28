package net.n2oapp.framework.context.smart.impl.api;

import net.n2oapp.framework.api.context.Context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  Поставщик одного значения контекста
 */
public interface OneValueContextProvider extends ContextProvider {


    /**
     * Получить значение параметра контекста
     * @param context контекст
     */
    Object getValue(Context context);

    /**
     * Получить параемтр контекста
     */
    String getParam();

    @Override
    default Map<String, Object> get(Context ctx) {
        return Collections.singletonMap(getParam(), getValue(ctx));
    }

    @Override
    default Set<String> getParams() {
        Set<String> res = new HashSet<>();
        res.add(getParam());
        return res;
    }

}
