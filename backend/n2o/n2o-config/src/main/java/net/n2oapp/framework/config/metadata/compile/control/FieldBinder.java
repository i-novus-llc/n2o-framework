package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.control.FetchValueDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

/**
 * Связывание поля для ввода с данными
 */
@Component
public class FieldBinder implements BaseMetadataBinder<Field> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Field.class;
    }

    @Override
    public Field bind(Field field, BindProcessor p) {
        if (field.getDependencies() != null) {
            field.getDependencies().stream().filter(FetchValueDependency.class::isInstance)
                    .forEach(f -> BindUtil.bindDataProvider(((FetchValueDependency) f).getDataProvider(), p));
        }
        return field;
    }
}
