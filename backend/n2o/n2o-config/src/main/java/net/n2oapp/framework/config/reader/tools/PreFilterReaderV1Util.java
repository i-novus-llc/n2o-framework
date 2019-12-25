package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import org.jdom.Element;

import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 13:17
 */
public class PreFilterReaderV1Util {
    public static N2oPreFilter[] getControlPreFilterListDefinition(Element preFilters) {
        if (preFilters != null) {
            List<Element> preFiltersList = preFilters.getChildren();
            N2oPreFilter[] n2oPreFilters = new N2oPreFilter[preFiltersList.size()];
            int i = 0;
            for (Element preFilter : preFiltersList) {
                N2oPreFilter n2oPreFilter = new N2oPreFilter();
                n2oPreFilter.setFieldId(getAttributeString(preFilter, "field-id"));
                String ref = getAttributeString(preFilter, "ref");
                if (ref != null) {
                    n2oPreFilter.setValueAttr(Placeholders.ref(ref));
                } else {
                    n2oPreFilter.setValueAttr(getAttributeString(preFilter, "value"));
                }
                n2oPreFilter.setTargetWidgetId(getAttributeString(preFilter, "container-id"));
                n2oPreFilter.setType(getAttributeEnum(preFilter, "type", FilterType.class));
                n2oPreFilter.setValueList(getElementsStringArray(preFilter, "value"));
                if ("on".equals(getAttributeString(preFilter, "reset-mode"))) {
                    n2oPreFilter.setResetOnChange(true);
                } else if ("of".equals(getAttributeString(preFilter, "reset-mode"))) {
                    n2oPreFilter.setResetOnChange(false);
                }
                n2oPreFilters[i] = n2oPreFilter;
                i++;
            }
            return n2oPreFilters;
        }
        return null;
    }
}
