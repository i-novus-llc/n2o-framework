package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Базовая компиляция кнопки
 */
public abstract class BaseButtonCompiler<S extends N2oAbstractButton, B extends AbstractButton> implements BaseSourceCompiler<B, S, CompileContext<?, ?>> {

    protected void initItem(AbstractButton button, N2oAbstractButton source, IndexScope idx,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setId(source.getId());
        button.setProperties(p.mapAttributes(source));
        if (source.getType() != null && source.getType() == LabelType.icon) {
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType() == LabelType.text) {
            button.setLabel(source.getLabel());
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(source.getLabel());
        }

        button.setClassName(source.getCssClass());
        button.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        initColor(source, button, p);
        initHint(source, button, p);
    }

    /**
     * Задание подсказки кнопки
     *
     * @param source Исходная модель кнопки
     * @param button Клиентская модель кнопки
     * @param p      Процессор сборки метаданных
     */
    private void initHint(N2oAbstractButton source, AbstractButton button, CompileProcessor p) {
        String hint;
        if (LabelType.icon.equals(source.getType()))
            hint = p.cast(source.getDescription(), source.getLabel());
        else
            hint = source.getDescription();

        if (hint != null) {
            button.setHint(hint.trim());
            if (source.getTooltipPosition() != null) {
                button.setHintPosition(source.getTooltipPosition());
            } else {
                button.setHintPosition(
                        source instanceof N2oButton
                                ? p.resolve(property("n2o.api.button.tooltip_position"), String.class)
                                : p.resolve(property("n2o.api.menuitem.tooltip_position"), String.class)
                );
            }
        }
    }

    /**
     * Задание цвета кнопки
     *
     * @param source Исходная модель кнопки
     * @param button Клиентская модель кнопки
     * @param p      Процессор сборки метаданных
     */
    private void initColor(N2oAbstractButton source, AbstractButton button, CompileProcessor p) {
        if (source.getColor() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oCell cell = componentScope.unwrap(N2oCell.class);
                if (cell != null) {
                    button.setColor(p.resolve(property("n2o.api.cell.toolbar.button-color"), String.class));
                }
            }
        } else {
            button.setColor(source.getColor());
        }
    }
}
