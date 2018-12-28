package net.n2oapp.framework.api.metadata.local.view;

import net.n2oapp.framework.api.metadata.control.N2oField;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилита для работы с моделями данных
 */
public abstract class BindingsUtil {

    public static Collection<String> getBindings(Collection<N2oField> fields) {
        return fields.stream().map(f -> {
            int idx = f.getId().indexOf(".");
            if (idx > 0) {
                return f.getId().substring(0, idx);
            } else {
                return f.getId();
            }
        }).distinct().collect(Collectors.toList());
    }

}
