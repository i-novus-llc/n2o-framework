package net.n2oapp.framework.config.metadata.validation.standard.menu;

import net.n2oapp.framework.api.metadata.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

/**
 * Валидатор простого меню навигации
 */
public class SimpleMenuValidator extends TypedMetadataValidator<N2oSimpleMenu> {

    @Override
    public Class<N2oSimpleMenu> getSourceClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public void validate(N2oSimpleMenu simpleMenu, SourceProcessor p) {
        validateItems(simpleMenu.getMenuItems(), p);
    }

    private void validateDropdown(N2oSimpleMenu.DropdownMenuItem dropdownMenu, SourceProcessor p) {
        if (dropdownMenu.getName() == null)
            throw new N2oMetadataValidationException("Не задан 'name' для <dropdown-menu>");
        if (dropdownMenu.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(dropdownMenu.getDatasourceId(), p,
                    String.format("<dropdown-menu name=%s> ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(dropdownMenu.getName()),
                            ValidationUtils.getIdOrEmptyString(dropdownMenu.getDatasourceId())));
        validateItems(dropdownMenu.getMenuItems(), p);
    }

    private void validateMenuItem(N2oSimpleMenu.MenuItem menuItem, SourceProcessor p) {
        if (!(menuItem.getAction() instanceof N2oOpenPage) && menuItem.getName() == null)
            throw new N2oMetadataValidationException("Не задан 'name' для <menu-item>");

        if (menuItem.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(menuItem.getDatasourceId(), p,
                    String.format("<menu-item name=%s> ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(menuItem.getName()),
                            ValidationUtils.getIdOrEmptyString(menuItem.getDatasourceId())));
        if (isInvalidColor(menuItem.getBadgeColor())) {
            throw new N2oMetadataValidationException(
                    String.format("<menu-item name=%s> использует недопустимое значение атрибута badge-color=\"%s\"",
                            ValidationUtils.getIdOrEmptyString(menuItem.getName()),
                            menuItem.getBadgeColor()));
        }
    }

    private void validateItems(N2oSimpleMenu.AbstractMenuItem[] items, SourceProcessor p) {
        p.safeStreamOf(items)
                .forEach(i -> {
                    if (i instanceof N2oSimpleMenu.MenuItem mi)
                        validateMenuItem(mi, p);
                    else if (i instanceof N2oSimpleMenu.DropdownMenuItem mi)
                        validateDropdown(mi, p);
                });
    }
}