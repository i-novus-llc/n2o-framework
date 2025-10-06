package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.menu.N2oAbstractMenuItem;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.menu.BaseMenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция абстрактного элемента меню
 */
public abstract class AbstractMenuItemCompiler<S extends N2oAbstractMenuItem, D extends BaseMenuItem> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    public void initMenuItem(S source, D compiled, CompileProcessor p) {
        IndexScope idx = castDefault(p.getScope(IndexScope.class), () -> new IndexScope(1));
        compiled.setId(castDefault(source.getId(), "mi" + idx.get()));
        source.setId(compiled.getId());
        compiled.setIcon(source.getIcon());
        compiled.setIconPosition(castDefault(source.getIconPosition(),
                () -> p.resolve(property("n2o.api.menu.item.icon_position"), PositionEnum.class)));
        compiled.setImageSrc(p.resolveJS(source.getImage()));
        compiled.setImageShape(castDefault(source.getImageShape(),
                () -> p.resolve(property("n2o.api.menu.item.image_shape"), ShapeTypeEnum.class)));
        compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        compiled.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        compiled.setClassName(p.resolveJS(source.getCssClass()));
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setModel(source.getModel());
        compiled.setDatasource(getClientDatasourceId(source.getDatasourceId(), p));
        compiled.setProperties(p.mapAttributes(source));

        if (hasLink(source.getLabel()) && (compiled.getDatasource() == null))
            throw new N2oException(
                    String.format("Меню имеет плейсхолдер 'label=%s', но при этом не указан источник данных",
                            ValidationUtils.getIdOrEmptyString(source.getLabel())));
        compiled.setLabel(p.resolveJS(source.getLabel()));
    }
}