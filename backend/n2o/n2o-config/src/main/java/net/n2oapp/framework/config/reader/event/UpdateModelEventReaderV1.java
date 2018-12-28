package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.UpdateModelAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.reader.tools.PreFilterReaderV1Util;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывает мобытие update-model
 */
@Component
public class UpdateModelEventReaderV1 extends AbstractN2oEventXmlReader<UpdateModelAction>{

    @Override
    public String getElementName() {
        return "update-model";
    }

    @Override
    public Class<UpdateModelAction> getElementClass() {
        return UpdateModelAction.class;
    }

    @Override
    public UpdateModelAction read(Element element) {
        if (element == null) return null;
        UpdateModelAction res = new UpdateModelAction();
        res.setQueryId(getAttributeString(element, "query-id"));
        res.setTarget(ReaderJdomUtil.getAttributeEnum(element, "target", UpdateModelAction.Target.class));
        res.setValueFieldId(getAttributeString(element, "value-field-id"));
        res.setTargetFieldId(getAttributeString(element, "target-field-id"));
        res.setMasterFieldId(getAttributeString(element, "master-field-id"));
        res.setDetailFieldId(getAttributeString(element, "detail-field-id"));
        res.setNamespaceUri(element.getNamespaceURI());
        N2oPreFilter[] preFilters = PreFilterReaderV1Util.getControlPreFilterListDefinition(
                element.getChild("pre-filters", element.getNamespace()));
        if (preFilters != null){
            res.setPreFilters(new N2oPreFilter[preFilters.length]);
            res.setPreFilters(preFilters);
        }
        return res;
    }
}
