package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

/**
 * Валидатор меню
 */
public class SimpleMenuValidator extends TypedMetadataValidator<N2oSimpleMenu> {

    @Override
    public Class<N2oSimpleMenu> getSourceClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public void validate(N2oSimpleMenu simpleMenu, SourceProcessor p) {
        if (simpleMenu.getMenuItems() == null) {
            return;
        }

        p.safeStreamOf(simpleMenu.getMenuItems())
                .filter(N2oSimpleMenu.MenuItem.class::isInstance)
                .map(N2oSimpleMenu.MenuItem.class::cast).forEach(this::validateMenuItem);

        p.safeStreamOf(simpleMenu.getMenuItems())
                .filter(N2oSimpleMenu.DropdownMenuItem.class::isInstance)
                .map(N2oSimpleMenu.DropdownMenuItem.class::cast).forEach(dropdownMenu -> {

                    if (dropdownMenu.getName() == null)
                        throw new N2oMetadataValidationException("Unspecified label for dropdown-menu");

                    p.safeStreamOf(dropdownMenu.getMenuItems()).forEach(this::validateMenuItem);
                });

    }

    private void validateMenuItem(N2oSimpleMenu.MenuItem menuItem) {
        if (menuItem.getAction() == null) {
            throw new N2oMetadataValidationException("Unspecified page or href for menu-item");
        }

        if (menuItem.getAction() instanceof N2oAnchor && menuItem.getName() == null) {
            throw new N2oMetadataValidationException(String.format("Unspecified label for %s", menuItem.getId()));
        }
    }

}