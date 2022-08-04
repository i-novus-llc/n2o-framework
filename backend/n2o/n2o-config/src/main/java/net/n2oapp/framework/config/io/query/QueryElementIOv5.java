package net.n2oapp.framework.config.io.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.query.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.dataprovider.DataProviderIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись запроса версии 5.0
 */
@Component
public class QueryElementIOv5 implements NamespaceIO<N2oQuery> {
    private Namespace dataProviderDefaultNamespace = DataProviderIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oQuery t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "object-id", t::getObjectId, t::setObjectId);
        p.attribute(e, "route", t::getRoute, t::setRoute);
        p.anyAttributes(e, t::getExtAttributes, t::setExtAttributes);
        p.children(e, null, "list", t::getLists, t::setLists, () -> new N2oQuery.Selection(N2oQuery.Selection.Type.list), this::selection);
        p.children(e, null, "count", t::getCounts, t::setCounts, () -> new N2oQuery.Selection(N2oQuery.Selection.Type.count), this::selection);
        p.children(e, null, "unique", t::getUniques, t::setUniques, () -> new N2oQuery.Selection(N2oQuery.Selection.Type.unique), this::selection);
        p.childrenByEnum(e, "filters", t::getFilters, t::setFilters, N2oQuery.Filter::getType,
                N2oQuery.Filter::setType, N2oQuery.Filter::new, FilterType.class, this::filter);
        p.anyChildren(e, "fields", t::getFields, t::setFields, p.oneOf(AbstractField.class)
                .add("field", SimpleField.class, this::field)
                .add("reference", ReferenceField.class, this::reference)
                .add("list", ListField.class, this::list));
    }

    private void list(Element e, ListField f, IOProcessor p) {
        reference(e, f, p);
    }

    private void reference(Element e, ReferenceField f, IOProcessor p) {
        abstractField(e, f, p);
        p.anyChildren(e, null, f::getFields, f::setFields, p.oneOf(AbstractField.class)
                .add("field", SimpleField.class, this::field)
                .add("reference", ReferenceField.class, this::reference)
                .add("list", ListField.class, this::list));
    }

    private void abstractField(Element e, AbstractField f, IOProcessor p) {
        p.attribute(e, "id", f::getId, f::setId);
        p.attribute(e, "mapping", f::getMapping, f::setMapping);
        p.attribute(e, "normalize", f::getNormalize, f::setNormalize);
    }

    private void selection(Element e, N2oQuery.Selection t, IOProcessor p) {
        p.attribute(e, "filters", t::getFilters, t::setFilters);
        p.attribute(e, "count-mapping", t::getCountMapping, t::setCountMapping);
        p.attribute(e, "result-mapping", t::getResultMapping, t::setResultMapping);
        p.anyChild(e, null, t::getInvocation, t::setInvocation, p.anyOf(N2oInvocation.class), dataProviderDefaultNamespace);
    }

    private void field(Element e, SimpleField t, IOProcessor p) {
        abstractField(e, t, p);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "sorting-expression", t::getSortingExpression, t::setSortingExpression);
        p.attribute(e, "sorting-mapping", t::getSortingMapping, t::setSortingMapping);
        p.attribute(e, "select-expression", t::getSelectExpression, t::setSelectExpression);
        p.attribute(e, "default-value", t::getDefaultValue, t::setDefaultValue);
        p.attributeBoolean(e, "select", t::getIsSelected, t::setIsSelected);
        p.attributeBoolean(e, "sorting", t::getIsSorted, t::setIsSorted);
    }

    private void filter(Element e, N2oQuery.Filter t, IOProcessor p) {
        p.attribute(e, "normalize", t::getNormalize, t::setNormalize);
        p.attribute(e, "mapping", t::getMapping, t::setMapping);
        p.attribute(e, "default-value", t::getDefaultValue, t::setDefaultValue);
        p.attribute(e, "domain", t::getDomain, t::setDomain);
        p.attribute(e, "filter-id", t::getFilterId, t::setFilterId);
        p.attribute(e, "field-id", t::getFieldId, t::setFieldId);
        p.attributeBoolean(e, "required", t::getRequired, t::setRequired);
        p.text(e, t::getText, t::setText);
    }

    @Override
    public Class<N2oQuery> getElementClass() {
        return N2oQuery.class;
    }

    @Override
    public N2oQuery newInstance(Element element) {
        return new N2oQuery();
    }

    @Override
    public String getElementName() {
        return "query";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/query-5.0";
    }

    public void setDataProviderDefaultNamespace(String dataProviderDefaultNamespace) {
        this.dataProviderDefaultNamespace = Namespace.getNamespace(dataProviderDefaultNamespace);
    }
}
