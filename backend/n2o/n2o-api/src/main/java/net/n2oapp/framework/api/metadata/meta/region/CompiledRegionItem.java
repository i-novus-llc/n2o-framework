package net.n2oapp.framework.api.metadata.meta.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Возможность сбора в список вложенных скомпилированных моделей виджетов
 */
public interface CompiledRegionItem extends Compiled {

    void collectWidgets(List<Widget<?>> compiledWidgets);

    default void collectWidgets(List<? extends CompiledRegionItem> items, List<Widget<?>> compiledWidgets) {
        if (!CollectionUtils.isEmpty(items))
            for (CompiledRegionItem c: items)
                c.collectWidgets(compiledWidgets);
    }
}
