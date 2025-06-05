package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись источника данных, получающего данные из другого источника данных
 */
@Component
public class InheritedDatasourceIO extends BaseDatasourceIO<N2oInheritedDatasource> {

    @Override
    public void io(Element e, N2oInheritedDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "source-datasource", ds::getSourceDatasource, ds::setSourceDatasource);
        p.attributeEnum(e, "source-model", ds::getSourceModel, ds::setSourceModel, ReduxModelEnum.class);
        p.attribute(e, "source-field-id", ds::getSourceFieldId, ds::setSourceFieldId);
        p.childrenText(e, "fetch-value", ds::getFetchValue, ds::setFetchValue);
        p.child(e, null, "submit", ds::getSubmit, ds::setSubmit, N2oInheritedDatasource.Submit::new, this::submit);
        p.childrenByEnum(e, "filters", ds::getFilters, ds::setFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterTypeEnum.class, this::filters);
    }

    private void submit(Element e, N2oInheritedDatasource.Submit t, IOProcessor p) {
        p.attributeBoolean(e, "auto", t::getAuto, t::setAuto);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModelEnum.class);
        p.attribute(e, "target-datasource", t::getTargetDatasource, t::setTargetDatasource);
        p.attributeEnum(e, "target-model", t::getTargetModel, t::setTargetModel, ReduxModelEnum.class);
        p.attribute(e, "target-field-id", t::getTargetFieldId, t::setTargetFieldId);
        p.childrenText(e, "submit-value", t::getSubmitValue, t::setSubmitValue);
    }

    @Override
    public Class<N2oInheritedDatasource> getElementClass() {
        return N2oInheritedDatasource.class;
    }

    @Override
    public String getElementName() {
        return "inherited-datasource";
    }
}
