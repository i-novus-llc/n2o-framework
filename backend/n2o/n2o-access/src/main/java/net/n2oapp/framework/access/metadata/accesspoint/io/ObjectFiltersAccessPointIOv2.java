package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFiltersAccessPoint;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Реализация IO для точки доступа 'object-filters' v2.0
 */
@Component
public class ObjectFiltersAccessPointIOv2 extends AccessPointElementIOv2<N2oObjectFiltersAccessPoint> {

    @Override
    public void io(Element e, N2oObjectFiltersAccessPoint t, IOProcessor p) {
        p.attribute(e, "object-id", t::getObjectId, t::setObjectId);
        p.childrenByEnum(e, null, t::getFilters, t::setFilters, N2oObjectFilter::getType,
                N2oObjectFilter::setType, N2oObjectFilter::new, FilterType.class, this::prefilter);
    }

    @Override
    public Class<N2oObjectFiltersAccessPoint> getElementClass() {
        return N2oObjectFiltersAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "object-filters";
    }

    private void prefilter(Element e, N2oObjectFilter pf, IOProcessor p) {
        p.attribute(e, "id", pf::getId, pf::setId);
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.childrenToStringArray(e, null, "value", pf::getValueList, pf::setValueList);
    }
}
