package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.menu.BaseMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.DividerMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.DropdownMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.GroupMenuItem;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Трансформатор доступа региона навигации {@code <nav>}.
 * Скрывает {@code <group>} и {@code <dropdown-menu>}, если все их дочерние элементы скрыты.
 * Обход выполняется рекурсивно для вложенных {@code <group>} и {@code <dropdown-menu>} любой глубины.
 */
@Component
public class NavRegionTransformer extends BaseAccessTransformer<NavRegion, CompileContext<?, ?>> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return NavRegion.class;
    }

    @Override
    public NavRegion transform(NavRegion compiled, CompileContext<?, ?> context, CompileProcessor p) {
        if (compiled.getContent() == null) return compiled;

        for (CompiledRegionItem item : compiled.getContent()) {
            if (item instanceof DropdownMenuItem dropdown) {
                processContainer(dropdown, dropdown.getContent());
            } else if (item instanceof GroupMenuItem group) {
                processContainer(group, group.getContent());
            }
        }
        return compiled;
    }

    private void processContainer(BaseMenuItem parent, List<BaseMenuItem> children) {
        if (children == null) return;
        for (BaseMenuItem child : children) {
            if (child instanceof DropdownMenuItem dropdown) {
                processContainer(dropdown, dropdown.getContent());
            } else if (child instanceof GroupMenuItem group) {
                processContainer(group, group.getContent());
            }
        }
        propagateToParent(parent, children);
    }

    private void propagateToParent(BaseMenuItem parent, List<BaseMenuItem> children) {
        if (children == null || children.isEmpty()) return;
        List<BaseMenuItem> relevant = children.stream()
                .filter(c -> !(c instanceof DividerMenuItem))
                .toList();
        if (relevant.isEmpty()) return;
        boolean allHaveSecurity = relevant.stream()
                .allMatch(c -> c.getProperties() != null
                        && c.getProperties().containsKey(Security.SECURITY_PROP_NAME));
        if (allHaveSecurity) {
            merge(parent, relevant);
        }
    }
}
