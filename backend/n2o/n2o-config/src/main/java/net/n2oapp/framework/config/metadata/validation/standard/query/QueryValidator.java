package net.n2oapp.framework.config.metadata.validation.standard.query;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Валидатор выборки
 */
@Component
public class QueryValidator implements SourceValidator<N2oQuery>, SourceClassAware {

    @Override
    public void validate(N2oQuery n2oQuery, SourceProcessor p) {
        if (n2oQuery.getObjectId() != null)
            checkForExistsObject(n2oQuery.getId(), n2oQuery.getObjectId(), p);
        if (n2oQuery.getFields() != null) {
            p.safeStreamOf(n2oQuery.getFields()).forEach(field -> checkFieldIdExistence(field, n2oQuery.getId(), p));
            checkForUniqueFields(n2oQuery.getFields(), n2oQuery.getId(), p);
        }
        if (n2oQuery.getFilters() != null) {
            checkForUniqueFilterFields(n2oQuery.getFilters(), n2oQuery.getId());
            checkForExistsFiltersInSelections(n2oQuery);
        }
        checkInvocations(n2oQuery, p);
    }

    /**
     * Проверка существования Объекта
     *
     * @param queryId  Идентификатор выборки
     * @param objectId Идентификатор объекта
     * @param p        Процессор исходных метаданных
     */
    private void checkForExistsObject(String queryId, String objectId, SourceProcessor p) {
        p.checkForExists(objectId, N2oObject.class,
                String.format("Выборка %s ссылается на несуществующий объект %s",
                        ValidationUtils.getIdOrEmptyString(queryId),
                        ValidationUtils.getIdOrEmptyString(objectId)));
    }

    /**
     * Проверка полей на наличие id
     *
     * @param field   Исходная metadata поля
     * @param p       Процессор исходных метаданных
     */
    public static void checkFieldIdExistence(AbstractField field, String queryId, SourceProcessor p) {
        if (field instanceof QueryReferenceField)
            p.safeStreamOf(((QueryReferenceField) field).getFields()).forEach(childField -> checkFieldIdExistence(childField, queryId, p));
        p.checkIdExistence(field,  String.format("Одно из полей выборки %s не имеет 'id'", ValidationUtils.getIdOrEmptyString(queryId)));
    }

    /**
     * Проверка, что поля в выборке не повторяются
     *
     * @param fields  Поля выборки
     * @param queryId Идентификатор выборки
     * @param p       Процессор исходных метаданных
     */
    private void checkForUniqueFields(AbstractField[] fields, String queryId, SourceProcessor p) {
        p.checkIdsUnique(fields, String.format("Поле '%s' встречается более чем один раз в выборке '%s'", "%s", queryId));
    }

    /**
     * Проверка, что фильтруемые поля в выборке не повторяются
     *
     * @param filters  Фильтры выборки
     * @param queryId  Идентификатор выборки
     */
    private void checkForUniqueFilterFields(N2oQuery.Filter[] filters, String queryId) {
        Set<String> filterFields = new HashSet<>();
        for (N2oQuery.Filter filter : filters) {
            if (filter.getFilterId() != null && !filterFields.add(filter.getFilterId())) {
                throw new N2oMetadataValidationException(String.format("Фильтр %s в выборке %s повторяется",
                        ValidationUtils.getIdOrEmptyString(filter.getFilterId()),
                        ValidationUtils.getIdOrEmptyString(queryId)));
            }
        }
    }

    /**
     * Проверка, что фильтры указанные в <list>, <unique>, <count> существуют
     *
     * @param query Выборка
     */
    private void checkForExistsFiltersInSelections(N2oQuery query) {
        Set<String> filterFields = Arrays.stream(query.getFilters()).map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
        checkFiltersExistInSelectionType(query.getLists(), filterFields, "list", query.getId());
        checkFiltersExistInSelectionType(query.getUniques(), filterFields, "unique", query.getId());
        checkFiltersExistInSelectionType(query.getCounts(), filterFields, "count", query.getId());
    }

    private void checkFiltersExistInSelectionType(N2oQuery.Selection[] selections, Set<String> filterFields, String selectionType, String queryId) {
        if (selections != null) {
            for (N2oQuery.Selection s : selections) {
                if (s.getFilters() != null) {
                    for (String filter : s.getFilters()) {
                        if (!filterFields.contains(filter))
                            throw new N2oMetadataValidationException(String.format("<%s> ссылается на несуществующий фильтр %s в выборке %s",
                                    selectionType,
                                    ValidationUtils.getIdOrEmptyString(filter),
                                    ValidationUtils.getIdOrEmptyString(queryId)));
                    }
                }
            }
        }
    }

    /**
     * Проверка вызовов провайдеров данных
     *
     * @param query Выборка
     * @param p     Процессор исходных метаданных
     */
    private void checkInvocations(N2oQuery query, SourceProcessor p) {
        if (query.getLists() != null)
            validateInvocations(query.getLists(), query.getId(), p);
        if (query.getCounts() != null)
            validateInvocations(query.getCounts(), query.getId(), p);
        if (query.getUniques() != null)
            validateInvocations(query.getUniques(), query.getId(), p);
    }

    /**
     * Валидирование вызовов провайдеров данных
     *
     * @param selections Массив selection элементов в выборке
     * @param p          Процессор исходных метаданных
     */
    private void validateInvocations(N2oQuery.Selection[] selections, String queryId, SourceProcessor p) {
        InvocationScope invocationScope = new InvocationScope();
        invocationScope.setQueryId(queryId);
        Arrays.stream(selections)
                .map(N2oQuery.Selection::getInvocation)
                .filter(Objects::nonNull)
                .forEach(invocation -> p.validate(invocation, invocationScope));
    }


    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }
}
