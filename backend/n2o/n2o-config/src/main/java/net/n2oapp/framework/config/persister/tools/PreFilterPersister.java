package net.n2oapp.framework.config.persister.tools;

import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import org.jdom2.Element;
import org.jdom2.Namespace;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementString;

@Deprecated
public class PreFilterPersister {
    public static void setPreFilter(N2oPreFilter[] preFilters, Element root, Namespace namespace) {
        if (preFilters == null) return;
        Element preFiltersElement = new Element("pre-filters", namespace);
        for (N2oPreFilter preFilter : preFilters) {
            Element preFilterElement = new Element("pre-filter", namespace);
            setAttribute(preFilterElement, "field-id", preFilter.getFieldId());
            //это сделано только для поддержки старых персистеров
            if (hasLink(preFilter.getValue())) {
                String value = unwrapLink(preFilter.getValue());
                setAttribute(preFilterElement, "ref", value);
                setAttribute(preFilterElement, "value", value);
            } else {
                setAttribute(preFilterElement, "value", preFilter.getValue());
            }
            setAttribute(preFilterElement, "container-id", preFilter.getTargetWidgetId());
            setAttribute(preFilterElement, "type", preFilter.getType());
            if (preFilter.getResetOnChange() != null) {
                setAttribute(preFilterElement, "reset-mode", preFilter.getResetOnChange() ? "on" : "off");
            }
            preFiltersElement.addContent(preFilterElement);
            if (preFilter.getValues() != null) {
                for (String value : preFilter.getValues()) {
                    setElementString(preFilterElement, "value", value);
                }
            }
        }
        root.addContent(preFiltersElement);
    }
}
