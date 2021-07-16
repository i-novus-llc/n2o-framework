package net.n2oapp.framework.config.reader.tools.showModal;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.tools.AfterCancel;
import net.n2oapp.framework.api.metadata.global.view.tools.AfterSubmit;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.tools.PreFilterReaderV1Util;
import org.jdom2.Element;
import org.jdom2.Namespace;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * Утилитный класс для считывания show-modal
 */
public class N2oStandardShowModalReaderUtil {

    public static void getShowModalDefinition(Element element, N2oAbstractPageAction showModal) {
        try {
            Namespace namespace = element.getNamespace();
            showModal.setPageId(getAttributeString(element, "page-id"));
            showModal.setPageName(getAttributeString(element, "page-name"));
            Element preFilters = element.getChild("pre-filters", namespace);
            N2oPreFilter[] n2oPreFilters = PreFilterReaderV1Util.getControlPreFilterListDefinition(preFilters);
            if (n2oPreFilters != null){
                showModal.setPreFilters(new N2oPreFilter[n2oPreFilters.length]);
                showModal.setPreFilters(n2oPreFilters);
            }
            showModal.setResultContainerId(getAttributeString(element, "result-container-id"));
            showModal.setContainerId(getAttributeString(element, "container-id"));
            showModal.setRefreshDependentContainer(getAttributeBoolean(element, "refresh-dependent-container"));
            showModal.setMasterFieldId(getAttributeString(element, "master-field-id"));
            showModal.setDetailFieldId(getAttributeString(element, "detail-field-id"));
            showModal.setWidth(getAttributeString(element, "width"));
            showModal.setMinWidth(getAttributeString(element, "min-width"));
            showModal.setMaxWidth(getAttributeString(element, "max-width"));
        } catch (Exception e) {
            throw new MetadataReaderException(e);
        }
    }

    public static void readEditFromActionId(Element element, N2oAbstractPageAction showModal) {
        String actionId = getAttributeString(element, "action-id");
        if (actionId == null) return;
        showModal.setSubmitOperationId(actionId);
        showModal.setCreateMore(getAttributeBoolean(element, "create-more"));
        showModal.setFocusAfterSubmit(getAttributeBoolean(element, "focus-after-submit"));
        showModal.setRefreshOnClose(getAttributeBoolean(element, "refresh-on-close"));
        AfterSubmit afterSubmit = getAttributeEnum(element, "after-submit", AfterSubmit.class);
        if (afterSubmit != null) {
            if (afterSubmit.equals(AfterSubmit.closeModal))
                showModal.setCloseAfterSubmit(true);
            else if (afterSubmit.equals(AfterSubmit.edit))
                showModal.setCloseAfterSubmit(false);
            else
                throw new UnsupportedOperationException("Unsupported since 7.0");
        }
        AfterCancel afterCancel = getAttributeEnum(element, "after-cancel", AfterCancel.class);
        if (afterCancel != null)
            throw new UnsupportedOperationException("Unsupported since 7.0");
        Boolean refreshAfterSubmit = getAttributeBoolean(element, "refresh-after-submit");
        if (refreshAfterSubmit != null && !refreshAfterSubmit)
            throw new UnsupportedOperationException("Unsupported since 7.0");
    }
}

