package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeBoolean;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChildren;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

/**
 * Считывает меню таблицы (action-menu) версии 1.0
 */
public class ActionMenuReaderV1 {
    public final static Namespace DEFAULT_EVENT_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-event-1.0");
    private NamespaceReaderFactory readerFactory;

    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    public N2oToolbar[] read(Element element) {
        N2oToolbar toolbar = new N2oToolbar();
        Boolean inheritDefault = getAttributeBoolean(element, "inherit-default");
        if (inheritDefault != null && inheritDefault) {
            String[] generate = new String[1];
            generate[0] = GenerateType.crud.name();
            toolbar.setGenerate(generate);
        }
        List<Element> popupMenus = element.getChildren();
        List<ToolbarItem> defaultToolbarItems = new ArrayList<>();
        for (Element popupMenu : popupMenus) {
            if (popupMenu.getName().equals("menu-item")) {
                defaultToolbarItems.add(getToolbarItem(popupMenu, popupMenu.getNamespace()));
            } else if (popupMenu.getName().equals("group")) {
                defaultToolbarItems.add(getGroup(popupMenu));
            }
        }
        if (!defaultToolbarItems.isEmpty()) {
            ToolbarItem[] toolbarItems = new ToolbarItem[defaultToolbarItems.size()];
            toolbar.setItems(defaultToolbarItems.toArray(toolbarItems));
        }
        return new N2oToolbar[]{toolbar};
    }

    private GroupItem getToolbarItem(Element popupMenu, Namespace namespace) {
        N2oSubmenu subMenuItem = getSubMenuItem(popupMenu, namespace);
        if (subMenuItem != null) {
            return subMenuItem;
        }
        return getButton(popupMenu, namespace);
    }

    private N2oGroup getGroup(Element groupElement) {
        N2oGroup group = new N2oGroup();
        List<Element> popupMenus = groupElement.getChildren();
        GroupItem[] items = new GroupItem[popupMenus.size()];
        int i = 0;
        for (Element popupMenu : popupMenus) {
            items[i] = getToolbarItem(popupMenu, popupMenu.getNamespace());
            i++;
        }
        group.setItems(items);
        return group;
    }

    private N2oButton getMenuItem(Element popupMenu, Namespace namespace) {
        N2oButton menuItem = new N2oButton();
        readMenuItem(popupMenu, menuItem, namespace);
        return menuItem;
    }

    private Element getEventElement(Element menu, Namespace namespace) {
        if (menu.getChildren() == null || menu.getChildren().size() == 0)
            return null;
        Element event = menu.getChild("invoke-action", namespace);
        if (event == null) {
            event = menu.getChild("show-modal", namespace);
            if (event == null) {
                event = menu.getChild("show-modal-form", namespace);
                if (event == null) {
                    event = menu.getChild("a", namespace);
                    if (event == null) {
                        event = menu.getChild("open-page", namespace);
                        if (event == null) {
                            event = menu.getChild("edit", namespace);
                            if (event == null) {
                                event = menu.getChild("go-edit", namespace);
                            }
                        }
                    }
                }
            }
        }
        return event;
    }


    private N2oButton getButton(Element popupMenu, Namespace namespace) {
        N2oButton button = new N2oButton();
        readMenuItem(popupMenu, button, namespace);
        return button;
    }

    private N2oSubmenu getSubMenuItem(Element popupMenu, Namespace namespace) {
        Element subMenuElement = popupMenu.getChild("sub-menu", namespace);
        if (subMenuElement == null) return null;
        List<Element> popupMenus = subMenuElement.getChildren();
        N2oSubmenu subMenu = new N2oSubmenu();
        subMenu.setId(getAttributeString(popupMenu, "id"));
        subMenu.setLabel(getAttributeString(subMenuElement, "label"));
        subMenu.setType(getAttributeEnum(subMenuElement, "type", LabelType.class));
        subMenu.setIcon(getAttributeString(subMenuElement, "icon"));
        subMenu.setColor(getAttributeString(subMenuElement, "color"));
        subMenu.setDescription(getElementString(subMenuElement, "description"));
        N2oButton[] subMenuItems = new N2oButton[popupMenus.size()];
        int i = 0;
        for (Element subPopupMenu : popupMenus) {
            subMenuItems[i] = getMenuItem(subPopupMenu, namespace);
            i++;
        }
        subMenu.setMenuItems(subMenuItems);
        return subMenu;
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
        Element eventElement = getEventElement(popupMenu, namespace);
        if (eventElement != null) {
            N2oAbstractAction action = (N2oAbstractAction) readerFactory.produce(eventElement,
                    popupMenu.getNamespace(), DEFAULT_EVENT_NAMESPACE_URI).read(eventElement);
            menuItem.setAction(action);

        }
        Boolean context = getAttributeBoolean(popupMenu, "context");
        if (context == null || context){
            menuItem.setModel(ReduxModel.RESOLVE);
        } else {
            menuItem.setModel(ReduxModel.FILTER);
        }
        menuItem.setEnablingConditions(getChildren(popupMenu, "conditions", "enabling-condition",
                MenuItemConditionReader.getInstance()));
        menuItem.setVisibilityConditions(getChildren(popupMenu, "conditions", "visibility-condition",
                MenuItemConditionReader.getInstance()));
    }

}
