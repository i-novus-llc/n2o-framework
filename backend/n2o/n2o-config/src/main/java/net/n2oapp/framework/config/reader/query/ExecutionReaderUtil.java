package net.n2oapp.framework.config.reader.query;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;
import net.n2oapp.framework.api.metadata.global.dao.execution.defaults.Calculation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oJavaMethod;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.RestErrorMappingReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;

import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.spel;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeInteger;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

/**
 * Утилита для считывания execution из xml файла.
 * Необходим для reader запросов старых версий(2.0 и 3.0).
 */
public class ExecutionReaderUtil {

    private static final Calculation SORTING_CALCULATION = (expression, id) -> "sortings=" + expression + "::direction";

    public static void fieldsDefault(N2oQuery n2oQuery, Element executionElement) {
        //todo generate defaults
        if (n2oQuery.getLists()[0].getInvocation() instanceof N2oSqlDataProvider) {
            String alias = ReaderJdomUtil.getElementString((Element) executionElement.getChildren().get(0), "alias");
            if (alias != null && n2oQuery.getFields() != null) {
                int i = 0;
                for (N2oQuery.Field field : n2oQuery.getFields()) {
                    if (alias != null) {
                        if (field.getExpression() == null && field.getNoDisplay() != null && !field.getNoDisplay()) {
                            field.setExpression((alias != null ? alias + "." + field.getId() : field.getId()));
                        }
                    }
                    if (field.getNoDisplay() == null || !field.getNoDisplay()) {
                        field.setSelectMapping("[" + i + "]");
                        i++;
                    }
                }
            }
        } else if (n2oQuery.getLists()[0].getInvocation() instanceof N2oRestInvocation) {
            if (n2oQuery.getFields() != null) {
                for (N2oQuery.Field field : n2oQuery.getFields()) {
                    if (field.getSortingBody() == null) {
                        field.setSortingBody(SORTING_CALCULATION.calculate(field.getExpression(), field.getId()));
                    }
                    field.setSelectMapping(field.getSelectBody());
                }
            }
        } else {
            if (n2oQuery.getFields() != null) {
                for (N2oQuery.Field field : n2oQuery.getFields()) {
                    if (field.getSelectMapping() == null) {
                        field.setSelectMapping(field.getSelectBody() == null ? field.getId() : field.getSelectBody());
                    }
                }
            }
        }
    }

    public static void readExecution(N2oQuery n2oQuery, Element executionElement) {
        if (executionElement == null) return;
        List<Element> executionElements = (List<Element>) executionElement.getChildren();
        if (executionElements.isEmpty()) MetadataReaderException.throwMissingAtLeastOneElement(executionElement);
        if (executionElements.size() > 1) MetadataReaderException.throwMoreThanOneChildElement(executionElement);
        Element execution = executionElements.get(0);
        if (execution.getName().equals("sql")) {
            readSqlExecution(n2oQuery, execution);
        } else if (execution.getName().equals("rest")) {
            readRestExecution(n2oQuery, execution);
        } else if (execution.getName().equals("java-criteria")) {
            readJavaCriteria(n2oQuery, execution);
        } else {
            MetadataReaderException.throwExpectedElement(execution, "sql, rest or java-criteria");
        }
    }

    private static void readJavaCriteria(N2oQuery n2oQuery, Element execution) {
        N2oJavaMethod javaCriteria = new N2oJavaMethod();
        javaCriteria.setBean(ReaderJdomUtil.getAttributeString(execution, "bean-name"));
        javaCriteria.setName("getCollectionPage");
        Argument argument = new Argument();
        argument.setClassName(ReaderJdomUtil.getAttributeString(execution, "criteria-class"));
        argument.setType(Argument.Type.CRITERIA);
        javaCriteria.setArguments(new Argument[]{argument});
        javaCriteria.setNamespaceUri(execution.getNamespaceURI());
        N2oQuery.Selection selection = new N2oQuery.Selection(N2oQuery.Selection.Type.list, javaCriteria);
        selection.setCountMapping("count");
        selection.setResultMapping("collection");
        n2oQuery.setLists(new N2oQuery.Selection[]{selection});
    }

    private static void readSqlExecution(N2oQuery n2oQuery, Element execution) {
        N2oSqlDataProvider sqlQuery = new N2oSqlDataProvider();
        String itemsSql = ReaderJdomUtil.getElementString(execution, "items-query");
        if (itemsSql != null) {
            itemsSql = itemsSql.replaceAll("[ \n]:where[ \n]", " :filters ");
            itemsSql = itemsSql.replaceAll("[ \n]:order[ \n]", " :sorting ");
            if (!itemsSql.contains(":limit"))
                itemsSql = itemsSql + " limit :limit";
            if (!itemsSql.contains(":offset"))
                itemsSql = itemsSql + " offset :offset";
        }
        sqlQuery.setQuery(itemsSql);
        sqlQuery.setNamespaceUri(execution.getNamespaceURI());
        sqlQuery.setRowMapper("index");
        n2oQuery.setLists(new N2oQuery.Selection[]{new N2oQuery.Selection(N2oQuery.Selection.Type.list, sqlQuery)});
        N2oSqlDataProvider countQuery = new N2oSqlDataProvider();
        String countSql = ReaderJdomUtil.getElementString(execution, "count-query");
        if (countSql != null) {
            countSql = countSql.replaceAll("[ \n]:where[ \n]", " :filters ");
            countSql = countSql.replaceAll("[ \n]:order[ \n]", " :sorting ");
        }
        countQuery.setQuery(countSql);
        countQuery.setNamespaceUri(execution.getNamespaceURI());
        countQuery.setRowMapper("index");
        N2oQuery.Selection countSelection = new N2oQuery.Selection(N2oQuery.Selection.Type.count, countQuery);
        countSelection.setCountMapping("[0][0]");
        n2oQuery.setCounts(new N2oQuery.Selection[]{countSelection});
    }

    private static void readRestExecution(N2oQuery n2oQuery, Element execution) {
        String dateFormat = getElementString(execution, "date-format");
        String resultMapping = null;
        String countMapping = null;
        Element responseMapping = execution.getChild("response-mapping", execution.getNamespace());
        if (responseMapping != null) {
            resultMapping = getAttributeString(responseMapping, "collection");
            countMapping = getAttributeString(responseMapping, "count");
        }
        String proxyHost = null;
        Integer proxyPort = null;
        Element proxyElem = execution.getChild("proxy", execution.getNamespace());
        if (proxyElem != null) {
            proxyHost = getAttributeString(proxyElem, "host");
            proxyPort = getAttributeInteger(proxyElem, "port");
        }
        RestErrorMapping errorMapping = RestErrorMappingReader.read(execution.getChild("error-mapping", execution.getNamespace()), execution.getNamespace());

        String query = getElementString(execution, "query");
        if (query != null) {
            N2oQuery.Selection listSel = new N2oQuery.Selection(N2oQuery.Selection.Type.list);
            listSel.setResultMapping(resultMapping != null ? spel(resultMapping) : spel("collection"));
            listSel.setCountMapping(countMapping != null ? spel(countMapping) : spel("count"));
            N2oRestInvocation listInv = new N2oRestInvocation();
            query = query.trim();
            listInv.setMethod("GET");
            listInv.setQuery(query);
            listInv.setDateFormat(dateFormat);
            listInv.setProxyHost(proxyHost);
            listInv.setProxyPort(proxyPort);
            listInv.setErrorMapping(errorMapping);
            listInv.setNamespaceUri(execution.getNamespaceURI());
            listSel.setInvocation(listInv);
            n2oQuery.setLists(new N2oQuery.Selection[]{listSel});
        }
        String queryById = getElementString(execution, "query-by-id");
        if (queryById != null) {
            N2oQuery.Selection uniqueSel = new N2oQuery.Selection(N2oQuery.Selection.Type.unique);
            uniqueSel.setResultMapping("#this");
            N2oRestInvocation uniqueInv = new N2oRestInvocation();
            queryById = queryById.trim();
            uniqueInv.setMethod("GET");
            uniqueInv.setQuery(queryById);
            uniqueInv.setDateFormat(dateFormat);
            uniqueInv.setProxyHost(proxyHost);
            uniqueInv.setProxyPort(proxyPort);
            uniqueInv.setErrorMapping(errorMapping);
            uniqueInv.setNamespaceUri(execution.getNamespaceURI());
            uniqueSel.setInvocation(uniqueInv);
            n2oQuery.setUniques(new N2oQuery.Selection[]{uniqueSel});
        }
    }
}
