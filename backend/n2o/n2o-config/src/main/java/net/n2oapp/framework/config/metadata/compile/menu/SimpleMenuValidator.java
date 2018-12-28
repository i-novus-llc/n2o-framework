package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;

import java.util.stream.Stream;

/**
 * Validating simple menu items, including sub menu
 *
 * @author igafurov
 * @since 20.04.2017
 */
public class SimpleMenuValidator extends TypedMetadataValidator<N2oSimpleMenu> {

    @Override
    public Class<N2oSimpleMenu> getMetadataClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public void check(N2oSimpleMenu simpleMenu) {
        if (simpleMenu.getMenuItems() == null) {
            return;
        }

        ValidationUtil.safeStreamOf(simpleMenu.getMenuItems()).forEach(menuItem -> {
            if (menuItem.getHref() != null && menuItem.getLabel() == null) {
                throw new N2oMetadataValidationException(String.format("Unspecified label for %s", menuItem.getHref()));
            }

            if (menuItem.getPageId() == null && menuItem.getHref() == null && menuItem.getSubMenu() == null) {
                throw new N2oMetadataValidationException("Unspecified page or href for menu-item");
            }

            if (menuItem.getHref() != null && menuItem.getPageId() != null) {
                throw new N2oMetadataValidationException(String.format("Priority exception, specified page (%s) and href (%s)",
                        menuItem.getPageId(), menuItem.getHref()));
            }

            if (menuItem.getSubMenu() != null) {
                if (menuItem.getLabel() == null)
                    throw new N2oMetadataValidationException("Unspecified label for sub-menu");

                ValidationUtil.safeStreamOf(menuItem.getSubMenu()).forEach(subMenuItem -> {
                    if (subMenuItem.getSubMenu() != null)
                        throw new N2oMetadataValidationException("sub-menu in sub-menu not supported");

                    if (subMenuItem.getPageId() == null && subMenuItem.getHref() == null) {
                        throw new N2oMetadataValidationException("Unspecified page or href for sub-menu item");
                    }

                    if (subMenuItem.getHref() != null && subMenuItem.getPageId() != null) {
                        throw new N2oMetadataValidationException(String.format("Priority exception, specified page (%s) and href (%s)",
                                subMenuItem.getPageId(), subMenuItem.getHref()));
                    }

                    if (subMenuItem.getLabel() == null && subMenuItem.getHref() != null) {
                        throw new N2oMetadataValidationException(String.format("Unspecified label for %s", subMenuItem.getHref()));
                    }
                });
            }
        });
    }

}