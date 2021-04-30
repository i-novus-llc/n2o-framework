package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oFileUploadCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись ячейки с возможностью загружать файл
 */
@Component
public class FileUploadCellElementIOv2 extends AbstractCellElementIOv2<N2oFileUploadCell> {

    @Override
    public void io(Element e, N2oFileUploadCell c, IOProcessor p) {
        super.io(e, c, p);

        p.attributeBoolean(e, "multi", c::getMulti, c::setMulti);
        p.attributeBoolean(e, "ajax", c::getAjax, c::setAjax);
        p.attributeBoolean(e, "show-size", c::getShowSize, c::setShowSize);
        p.attribute(e, "upload-url", c::getUploadUrl, c::setUploadUrl);
        p.attribute(e, "delete-url", c::getDeleteUrl, c::setDeleteUrl);
        p.attribute(e, "value-field-id", c::getValueFieldId, c::setValueFieldId);
        p.attribute(e, "label-field-id", c::getLabelFieldId, c::setLabelFieldId);
        p.attribute(e, "message-field-id", c::getMessageFieldId, c::setMessageFieldId);
        p.attribute(e, "url-field-id", c::getUrlFieldId, c::setUrlFieldId);
        p.attribute(e, "request-param", c::getRequestParam, c::setRequestParam);
        p.attribute(e, "accept", c::getAccept, c::setAccept);
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "upload-icon", c::getUploadIcon, c::setUploadIcon);
        p.attribute(e, "delete-icon", c::getDeleteIcon, c::setDeleteIcon);
    }

    @Override
    public Class<N2oFileUploadCell> getElementClass() {
        return N2oFileUploadCell.class;
    }

    @Override
    public String getElementName() {
        return "file-upload";
    }
}
