package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButtonCondition;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

/**
 * Считывание условий в меню и кнопках тулбара
 */
public class MenuItemConditionReader implements TypedElementReader<N2oButtonCondition> {
    private static final MenuItemConditionReader instance = new MenuItemConditionReader();

    public static MenuItemConditionReader getInstance() {
        return instance;
    }

    @Override
    public N2oButtonCondition read(Element element) {
        N2oButtonCondition condition = new N2oButtonCondition();
        condition.setExpression(getElementString(element, "expression"));
        condition.setOn(getAttributeString(element.getChild("expression", element.getNamespace()), "on"));
        condition.setTooltip(getElementString(element, "tooltip"));
        return condition;
    }

    @Override
    public Class<N2oButtonCondition> getElementClass() {
        return N2oButtonCondition.class;
    }

    @Override
    public String getElementName() {
        throw new UnsupportedOperationException("MenuItemConditionReader can't be generated with factory");
    }
}
