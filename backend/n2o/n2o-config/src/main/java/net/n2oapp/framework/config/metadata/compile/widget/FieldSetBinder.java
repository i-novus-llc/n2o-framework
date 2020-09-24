package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Связывание филдсета с данными
 */
@Component
public class FieldSetBinder implements BaseMetadataBinder<FieldSet> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return FieldSet.class;
    }

    @Override
    public FieldSet bind(FieldSet compiled, BindProcessor p) {
        if (compiled.getRows() != null)
            compiled.getRows().stream()
                    .flatMap(row -> row.getCols() != null ? row.getCols().stream(): Stream.empty()).forEach(col -> {
            if (col.getFields() != null)
                col.getFields().forEach(p::bind);
            if (col.getFieldsets() != null)
                col.getFieldsets().forEach(p::bind);
        });
        return compiled;
    }
}
