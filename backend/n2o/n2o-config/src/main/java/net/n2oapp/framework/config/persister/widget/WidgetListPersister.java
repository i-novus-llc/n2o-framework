package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oWidgetList;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * @author iryabov
 * @since 17.06.2015
 */

@Component
public class WidgetListPersister extends WidgetXmlPersister<N2oWidgetList> {
    @Override
    public Element getWidget(N2oWidgetList widgetList, Namespace namespace) {
        Element element = new Element(getElementName(), namespace);
        persistWidget(element, widgetList, namespace);
        setChild(element, "rows", widgetList.getColumn(), (column,n) -> {
            Element element1 = new Element("rows", namespace);
            setAttribute(element1, "label-field-id", column.getTextFieldId());
            if (column.getCell() != null) {
                Element cellElement = persisterFactory.produce(column.getCell()).persist(column.getCell(),namespace);
                PersisterJdomUtil.installPrefix(cellElement, element);
                element1.addContent(cellElement);
            }
            return element1;
        });
        return element;
    }

    @Override
    public Class<N2oWidgetList> getElementClass() {
        return N2oWidgetList.class;
    }

    @Override
    public String getElementName() {
        return "list";
    }
}
