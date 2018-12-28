package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGrid;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChilds;

/**
 * @author iryabov
 * @since 12.02.2015
 */
@Component
public class N2oCheckboxGridXmlReaderV1 extends N2oStandardControlReaderV1<N2oCheckboxGrid> {

    @Override
    public String getElementName() {
        return "checkbox-grid";
    }

    @Override
    public N2oCheckboxGrid read(Element element, Namespace namespace) {
        N2oCheckboxGrid checkboxGrid = new N2oCheckboxGrid();
        getQueryFieldDefinition(element, checkboxGrid);
        checkboxGrid.setColumns(getChilds(element, element.getNamespace(), "columns", "column",
                new N2oCheckboxGridColumnReader()));

        return checkboxGrid;
    }

    @Override
    public Class<N2oCheckboxGrid> getElementClass() {
        return N2oCheckboxGrid.class;
    }

    public static class N2oCheckboxGridColumnReader implements TypedElementReader<N2oCheckboxGrid.Column> {
        @Override
        public N2oCheckboxGrid.Column read(Element element) {
            return new N2oCheckboxGrid.Column(getAttributeString(element, "name"),
                    getAttributeString(element, "column-field-id"));
        }

        @Override
        public Class<N2oCheckboxGrid.Column> getElementClass() {
            return N2oCheckboxGrid.Column.class;
        }

        @Override
        public String getElementName() {
            return "column";
        }
    }
}
