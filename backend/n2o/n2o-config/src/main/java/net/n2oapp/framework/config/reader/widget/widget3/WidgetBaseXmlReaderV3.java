package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.reader.tools.CssClassAwareReader;
import net.n2oapp.framework.config.reader.tools.MenuItemConditionReader;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

public abstract class WidgetBaseXmlReaderV3<E extends N2oWidget> extends AbstractFactoredReader<E> {

    private static final String DEFAULT_NAMESPACE_URI = "http://n2oapp.net/framework/config/schema/n2o-widget-3.0";
    private static final Namespace DEFAULT_EVENT_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-event-1.0");

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        super.setReaderFactory(readerFactory);
    }

    protected void readWidgetDefinition(Element widget, Namespace namespace, N2oWidget n2oWidget) {
        n2oWidget.setMasterFieldId(getAttributeString(widget, "master-field-id"));
        n2oWidget.setDetailFieldId(getAttributeString(widget, "detail-field-id"));
        n2oWidget.setName(getElementString(widget, "name"));
        n2oWidget.setSrc(getAttributeString(widget, "src"));
        n2oWidget.setCustomize(getAttributeString(widget, "customize"));
        n2oWidget.setQueryId(getElementString(widget, "query-id"));
        n2oWidget.setDefaultValuesQueryId(getElementString(widget, "default-values-query-id"));
        n2oWidget.setObjectId(getElementString(widget, "object-id"));
        n2oWidget.setSize(getElementInteger(widget, "size"));
        n2oWidget.setBorder(getElementBoolean(widget, "border"));
        n2oWidget.setNamespaceUri(namespace.getURI());
        CssClassAwareReader.getInstance().read(n2oWidget, widget);
        initToolbars(widget, n2oWidget, namespace);
    }

    protected void readRef(Element element, N2oWidget widget) {
        readWidgetDefinition(element, element.getNamespace(), widget);
        String refId = getAttributeString(element, "ref-id");
        if (refId != null) {
            widget.setRefId(refId);
            widget.setId(refId);
        }
    }

    @Override
    public String getNamespaceUri() {
        return DEFAULT_NAMESPACE_URI;
    }

    private void initToolbars(Element widget, N2oWidget n2oWidget, Namespace namespace) {
        Element actionMenuElement = widget.getChild("action-menu", namespace);
        if (actionMenuElement == null) return;
        List<N2oToolbar> toolbars = new ArrayList<>();

        N2oToolbar defaultToolbar = new N2oToolbar();
        Boolean inheritDefault = getAttributeBoolean(actionMenuElement, "inherit-default");
        if (inheritDefault != null && inheritDefault) {
            String[] generate = new String[1];
            generate[0] = GenerateType.crud.name();
            defaultToolbar.setGenerate(generate);
        }
        List<Element> popupMenus = actionMenuElement.getChildren();
        List<ToolbarItem> defaultToolbarItems = new ArrayList<>();
        for (Element popupMenu : popupMenus) {
            if (popupMenu.getName().equals("menu-item")) {
                N2oButton button = new N2oButton();
                readMenuItem(popupMenu, button, namespace);
                defaultToolbarItems.add(button);
            } else if (popupMenu.getName().equals("sub-menu")) {
                N2oSubmenu mi = getSubMenu(popupMenu, namespace);
                defaultToolbarItems.add(mi);
            } else if (popupMenu.getName().equals("group")) {
                String place = getAttributeString(popupMenu, "place");
                if (place == null) {
                    N2oGroup group = new N2oGroup();
                    readGroup(popupMenu, group, namespace);
                    defaultToolbarItems.add(group);
                } else {
                    N2oToolbar toolbar = new N2oToolbar();
                    toolbar.setPlace(place);
                    N2oGroup group = new N2oGroup();
                    readGroup(popupMenu, group, namespace);
                    toolbar.setItems(new ToolbarItem[]{group});
                    toolbars.add(toolbar);
                }
            }
        }
        if (!defaultToolbarItems.isEmpty()) {
            ToolbarItem[] toolbarItems = new ToolbarItem[defaultToolbarItems.size()];
            defaultToolbar.setItems(defaultToolbarItems.toArray(toolbarItems));
            toolbars.add(0, defaultToolbar);
        }
        N2oToolbar[] toolbarArray = new N2oToolbar[toolbars.size()];
        n2oWidget.setToolbars(toolbars.toArray(toolbarArray));
    }

    private void readGroup(Element groupElement, N2oGroup group, Namespace namespace) {
        List<Element> popupMenus = groupElement.getChildren();
        GroupItem[] items = new GroupItem[popupMenus.size()];
        int i = 0;
        for (Element popupMenu : popupMenus) {
            if (popupMenu.getName().equals("menu-item")) {
                N2oButton button = new N2oButton();
                readMenuItem(popupMenu, button, namespace);
                items[i] = button;
            } else if (popupMenu.getName().equals("sub-menu")) {
                N2oSubmenu mi = getSubMenu(popupMenu, namespace);
                items[i] = mi;
            }
            i++;
        }
        group.setItems(items);
    }

    private void readMenuItem(Element popupMenu, N2oButton menuItem, Namespace namespace) {
        menuItem.setId(getAttributeString(popupMenu, "id"));
        menuItem.setLabel(getAttributeString(popupMenu, "label"));
        menuItem.setType(getAttributeEnum(popupMenu, "type", LabelType.class));
        menuItem.setIcon(getAttributeString(popupMenu, "icon"));
        menuItem.setColor(getAttributeString(popupMenu, "color"));
        menuItem.setDescription(getElementString(popupMenu, "description"));
        menuItem.setVisible(getAttributeString(popupMenu, "visible"));
        menuItem.setValidate(getAttributeBoolean(popupMenu, "validate"));
        Element eventElement = popupMenu.getChild("event", namespace);
        if (eventElement != null && eventElement.getChildren() != null && !eventElement.getChildren().isEmpty()) {
            N2oAbstractAction action = (N2oAbstractAction) readerFactory.produce((Element) eventElement.getChildren().get(0),
                    popupMenu.getNamespace(), DEFAULT_EVENT_NAMESPACE_URI).read((Element) eventElement.getChildren().get(0));
            menuItem.setAction(action);
        }
        Boolean context = getAttributeBoolean(popupMenu, "context");
        if (context == null || context ){
            menuItem.setModel(ReduxModel.RESOLVE);
        } else {
            menuItem.setModel(ReduxModel.FILTER);
        }
        menuItem.setEnablingConditions(getChildren(popupMenu, "conditions", "enabling-condition",
                MenuItemConditionReader.getInstance()));
        menuItem.setVisibilityConditions(getChildren(popupMenu, "conditions", "visibility-condition",
                MenuItemConditionReader.getInstance()));
    }

    private N2oSubmenu getSubMenu(Element subMenuElement, Namespace namespace) {
        if (subMenuElement == null) return null;
        List<Element> popupMenus = subMenuElement.getChildren();
        N2oSubmenu subMenu = new N2oSubmenu();
        subMenu.setId(getAttributeString(subMenuElement, "id"));
        subMenu.setLabel(getAttributeString(subMenuElement, "label"));
        subMenu.setType(getAttributeEnum(subMenuElement, "type", LabelType.class));
        subMenu.setIcon(getAttributeString(subMenuElement, "icon"));
        subMenu.setColor(getAttributeString(subMenuElement, "color"));
        subMenu.setDescription(getElementString(subMenuElement, "description"));
        N2oButton[] subMenuItems = new N2oButton[popupMenus.size()];
        int i = 0;
        for (Element subPopupMenu : popupMenus) {
            N2oButton menuItem = new N2oButton();
            readMenuItem(subPopupMenu, menuItem, namespace);
            subMenuItems[i] = menuItem;
            i++;
        }
        subMenu.setMenuItems(subMenuItems);
        return subMenu;
    }

}
