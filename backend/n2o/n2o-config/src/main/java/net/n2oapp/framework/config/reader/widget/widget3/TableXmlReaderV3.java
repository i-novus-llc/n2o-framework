package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeBoolean;

@Component
public class TableXmlReaderV3 extends AbstractTableXmlReaderV3<N2oTable> {

    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oTable n2oTable = new N2oTable();
        getAbstractTableDefinition(element, namespace, n2oTable, readerFactory);
        Element filtersElement = element.getChild("filters", namespace);
        if (filtersElement != null) {
            n2oTable.setFilterPosition(
                    ReaderJdomUtil.getAttributeEnum(filtersElement, "position", N2oTable.FilterPosition.class));
            n2oTable.setFilterOpened(getAttributeBoolean(filtersElement, "opened"));
            List<Element> filters = filtersElement.getChildren();
            N2oFieldSet n2oFieldSet = getControlFieldElements(filters, readerFactory, n2oTable.getFilterPosition());
            if (n2oFieldSet != null) {
                n2oTable.setFilters(new SourceComponent[]{n2oFieldSet});
            }
        }

        return n2oTable;
    }

    /**
     * virtualFieldSet - виртуальный филдсет, в который добавляются фильтры -филды, находящиеся на форме отдельно от филдсетов
     * Виртуальные филдсеты на форме разделены обычными филдсетами
     *
     * @return список филдсетов-фильтров
     */
    private static N2oFieldSet getControlFieldElements(List<Element> elementList,
                                                       NamespaceReaderFactory extensionReaderFactory, N2oTable.FilterPosition filterPosition) {
        try {
            N2oFieldSet virtualFieldSet = new N2oSetFieldSet();
            virtualFieldSet.setItems(new SourceComponent[elementList.size()]);
            int i = 0;
            for (Element element : elementList) {
                SourceComponent n2oElement = (SourceComponent) extensionReaderFactory.produce(element)
                        .read(element);
                virtualFieldSet.getItems()[i] = n2oElement;
                i++;
            }
            return virtualFieldSet;
        } catch (Exception e) {
            throw new MetadataReaderException(e);
        }
    }

    @Override
    public String getElementName() {
        return "table";
    }
}
