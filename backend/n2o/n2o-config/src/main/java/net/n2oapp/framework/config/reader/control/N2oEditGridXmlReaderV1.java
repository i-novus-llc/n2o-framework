package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oEditGrid;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChilds;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oEditGridXmlReaderV1 extends N2oStandardControlReaderV1<N2oEditGrid> {

    @Override
    public String getElementName() {
        return "edit-grid";
    }

    @Override
    public N2oEditGrid read(Element element, Namespace namespace) {
        N2oEditGrid editGrid = new N2oEditGrid();
        editGrid.setObjectId(ReaderJdomUtil.getElementString(element, "object-id"));
        editGrid.setColumns(getChilds(element, element.getNamespace(), "columns", "column",
                new N2oCheckboxGridXmlReaderV1.N2oCheckboxGridColumnReader()));
        getControlFieldDefinition(element, editGrid);
        return editGrid;
    }

    @Override
    public Class<N2oEditGrid> getElementClass() {
        return N2oEditGrid.class;
    }
}
