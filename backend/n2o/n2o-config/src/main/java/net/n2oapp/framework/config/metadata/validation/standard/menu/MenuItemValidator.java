package net.n2oapp.framework.config.metadata.validation.standard.menu;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.menu.*;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkCloseInMultiAction;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

/**
 * Валидация элементов меню
 */
@Component
public class MenuItemValidator implements SourceValidator<N2oAbstractMenuItem>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractMenuItem.class;
    }

    @Override
    public void validate(N2oAbstractMenuItem source, SourceProcessor p) {
        if (source instanceof N2oMenuItem item) {
            checkDatasource(item, p, "menu-item");
            checkOnFailAction(item.getActions());
            checkCloseInMultiAction(item.getActions());
            checkMenuItem(item);
        } else if (source instanceof N2oLinkMenuItem item) {
            checkDatasource(item, p, "link");
            checkLinkMenuItem(item);
        } else if (source instanceof N2oGroupMenuItem item) {
            checkDatasource(item, p, "group");
            p.safeStreamOf(item.getMenuItems()).forEach(p::validate);
        } else if (source instanceof N2oDropdownMenuItem item) {
            checkDatasource(item, p, "dropdown-menu");
            p.safeStreamOf(item.getMenuItems()).forEach(p::validate);
        }
    }

    private void checkMenuItem(N2oMenuItem item) {
        if (item.getBadgeColor() != null && !StringUtils.isLink(item.getBadgeColor()) &&
                !EnumUtils.isValidEnum(ColorEnum.class, item.getBadgeColor()))
            throw new N2oMetadataValidationException(
                    String.format("<menu-item label=%s> использует недопустимое значение атрибута badge-color=\"%s\"",
                            ValidationUtils.getIdOrEmptyString(item.getLabel()),
                            item.getBadgeColor()));
    }

    private void checkLinkMenuItem(N2oLinkMenuItem item) {
        if (item.getHref() == null)
            throw new N2oMetadataValidationException("Для элемента меню <link> не задан `href`");

        if (item.getTarget() != null && item.getTarget().equals(TargetEnum.APPLICATION)
                && item.getHref().startsWith("http")) {
            throw new N2oMetadataValidationException(
                    "В элементе меню <link> при абсолютном пути (http\\https) не может быть задан target=\"application\"");
        }
    }

    private static void checkDatasource(N2oAbstractMenuItem source, SourceProcessor p, String element) {
        if (source.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("<%s label=%s> ссылается на несуществующий источник данных %s",
                            element,
                            ValidationUtils.getIdOrEmptyString(source.getLabel()),
                            ValidationUtils.getIdOrEmptyString(source.getDatasourceId())));
    }
}
