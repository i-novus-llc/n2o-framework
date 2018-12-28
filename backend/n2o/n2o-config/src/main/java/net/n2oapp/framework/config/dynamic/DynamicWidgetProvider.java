package net.n2oapp.framework.config.dynamic;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oEditForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Поставщик динамических виджетов
 */
public class DynamicWidgetProvider implements DynamicMetadataProvider {
    @Override
    public String getCode() {
        return "widget";
    }

    @Override
    public List<? extends N2oMetadata> read(String context) {
        //todo хорошо бы избавиться от необходимость заменять id в динамических страницах, тогда бы и не пришлось клонировать
        N2oWidget widget = CompilerHolder.get().copy(CompilerHolder.get().getGlobal(context, N2oWidget.class));
        widget.setId(getCode() + "$" + context);
        return Collections.singletonList(widget);
    }

    @Override
    public Collection<Class<? extends SourceMetadata>> getMetadataClasses() {
        return Arrays.asList(N2oForm.class, N2oEditForm.class);
    }
}
