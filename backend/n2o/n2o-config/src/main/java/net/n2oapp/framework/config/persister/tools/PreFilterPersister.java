package net.n2oapp.framework.config.persister.tools;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import org.jdom.Element;
import org.jdom.Namespace;

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
            if (StringUtils.hasLink(preFilter.getValue())) {
                String value = preFilter.getValue().substring(1, preFilter.getValue().length() - 1);
                setAttribute(preFilterElement, "ref", value);
                setAttribute(preFilterElement, "value", value);
            } else {
                setAttribute(preFilterElement, "value", preFilter.getValue());
            }
            setAttribute(preFilterElement, "container-id", preFilter.getTargetWidgetId());
            setAttribute(preFilterElement, "type", preFilter.getType());
            setAttribute(preFilterElement, "reset-mode", preFilter.getResetMode());
            setAttribute(preFilterElement, "on-change", preFilter.getOnChange());
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
