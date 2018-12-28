package net.n2oapp.framework.config.reader.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oJavaMethod;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.reader.query.FilterFieldUtil.generateFilterField;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

/**
 * Считывает запрос версии 3.0 из xml элемента
 */
@Component
public class QueryElementReaderV3 extends AbstractFactoredReader<N2oQuery> {
    @Override
    public N2oQuery read(Element element, Namespace namespace) {
        N2oQuery n2oQuery = new N2oQuery();
        n2oQuery.setObjectId(getElementString(element, "object-id"));
        n2oQuery.setName(getElementString(element, "name"));
        Element executionElement = element.getChild("execution", namespace);
        ExecutionReaderUtil.readExecution(n2oQuery, executionElement);
        readFields(element, namespace, n2oQuery);
        ExecutionReaderUtil.fieldsDefault(n2oQuery, executionElement);
        return n2oQuery;
    }

    private void readFields(Element element, Namespace namespace, N2oQuery n2oQuery) {
        Element fields = element.getChild("fields", namespace);
        if (fields != null) {

            n2oQuery.setFields(new N2oQuery.Field[fields.getChildren().size()]);
            int i = 0;
            for (Element field : (List<Element>) fields.getChildren()) {
                N2oQuery.Field n2oQueryField = new N2oQuery.Field();
                n2oQueryField.setId(getElementString(field, "id"));
                n2oQueryField.setName(getElementString(field, "name"));
                n2oQueryField.setDomain(getElementString(field, "domain"));
                n2oQueryField.setExpression(getElementString(field, "expression"));
                final N2oInvocation invocation = n2oQuery.getLists() != null && n2oQuery.getLists()[0] != null && n2oQuery.getLists()[0].getInvocation() != null ?
                    n2oQuery.getLists()[0].getInvocation() : n2oQuery.getUniques() != null && n2oQuery.getUniques()[0] != null && n2oQuery.getUniques()[0].getInvocation() != null ?
                    n2oQuery.getUniques()[0].getInvocation() : null;
                if (ReaderJdomUtil.isElementExists(field, "display")) {
                    n2oQueryField.setNoDisplay(false);
                    Element display = field.getChild("display", field.getNamespace());
                    if (invocation instanceof N2oSqlDataProvider) {
                        n2oQueryField.setSelectBody(getElementString(field, "display"));
                    } else {
                        n2oQueryField.setSelectMapping(getElementString(field, "display"));
                    }
                    n2oQueryField.setSelectDefaultValue(getAttributeString(display, "default-value"));
                } else {
                    n2oQueryField.setNoDisplay(true);
                }
                if (ReaderJdomUtil.isElementExists(field, "sorting")) {
                    n2oQueryField.setNoSorting(false);
                    //значение по умолчанию для sorting mapping
                    n2oQueryField.setSortingMapping("['" + n2oQueryField.getId() + "Direction']");
                    //body сортировки теперь содержат direction, а в V3 не содержали
                    String sortingBody = getElementString(field, "sorting");
                    n2oQueryField.setSortingBody((sortingBody == null ? ":expression" : sortingBody) + " :" + n2oQueryField.getId() + "Direction");
                } else {
                    n2oQueryField.setNoSorting(true);
                }
                if (ReaderJdomUtil.isElementExists(field, "join")) {
                    n2oQueryField.setNoJoin(false);
                    n2oQueryField.setJoinBody(getElementString(field, "join"));
                } else {
                    n2oQueryField.setNoJoin(true);
                }
                Element filters = field.getChild("filters", namespace);
                if (filters != null) {
                    n2oQueryField.setFilterList(ReaderJdomUtil.getChildren(filters, null, "filter", new TypedElementReader<N2oQuery.Filter>() {
                        @Override
                        public String getElementName() {
                            return "filter";
                        }

                        @Override
                        public N2oQuery.Filter read(Element element) {
                            N2oQuery.Filter filter = new N2oQuery.Filter();
                            filter.setType(ReaderJdomUtil.getAttributeEnum(element, "type", FilterType.class));
                            filter.setDefaultValue(getAttributeString(element, "default-value"));
                            filter.setFilterField(getAttributeString(element, "filter-field"));
                            filter.setDomain(getAttributeString(element, "domain"));
                            filter.setNormalize(getAttributeString(element, "normalize"));
                            String text = ReaderJdomUtil.getText(element);
                            if (invocation instanceof N2oJavaMethod) {
                                filter.setMapping(filter.getFilterField());
                                filter.setText(text.isEmpty() ? null : text);
                            } else {
                                if (text.isEmpty()) {
                                    if (invocation instanceof N2oSqlDataProvider) {
                                        switch (filter.getType()) {
                                            case eq: {
                                                filter.setText(":expression = :" + filter.getFilterField());
                                            }
                                            break;
                                            case in: {
                                                filter.setText(":expression in (:" + filter.getFilterField() + ")");
                                            }
                                            break;
                                            case more: {
                                                filter.setText(":expression > :" + filter.getFilterField());
                                            }
                                            break;
                                            case less: {
                                                filter.setText(":expression < :" + filter.getFilterField());
                                            }
                                            break;
                                        }
                                    } else if (invocation instanceof N2oRestDataProvider) {
                                        filter.setText(filter.getFilterField() + "=" + ":" + filter.getFilterField());
                                    }
                                } else {
                                    filter.setText(text);
                                }
                                // значение по умолчанию для rest и sql
                                filter.setMapping("['" + filter.getFilterField() + "']");
                            }
                            return filter;
                        }

                        @Override
                        public Class<N2oQuery.Filter> getElementClass() {
                            return N2oQuery.Filter.class;
                        }
                    }));
                    Boolean autoGenerate = ReaderJdomUtil.getAttributeBoolean(filters, "auto-generate");
                    if (autoGenerate != null && autoGenerate) {
                        if (n2oQueryField.getFilterList() == null || n2oQueryField.getFilterList().length == 0) {
                            N2oQuery.Filter eqFilter = new N2oQuery.Filter();
                            eqFilter.setType(FilterType.eq);
                            eqFilter.setFilterField(generateFilterField(n2oQueryField.getId(), FilterType.eq));
                            eqFilter.setText(n2oQueryField.getId() + " = :" + eqFilter.getFilterField());
                            N2oQuery.Filter inFilter = new N2oQuery.Filter();
                            inFilter.setType(FilterType.in);
                            inFilter.setFilterField(generateFilterField(n2oQueryField.getId(), FilterType.in));
                            n2oQueryField.setFilterList(new N2oQuery.Filter[]{eqFilter, inFilter});
                            inFilter.setText(n2oQueryField.getId() + " in (:" + eqFilter.getFilterField() + ")");
                        }
                    }
                }
                n2oQuery.getFields()[i] = n2oQueryField;
                i++;
            }
        }
    }

    @Override
    public Class<N2oQuery> getElementClass() {
        return N2oQuery.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/query-3.0";
    }

    @Override
    public String getElementName() {
        return "query";
    }
}
