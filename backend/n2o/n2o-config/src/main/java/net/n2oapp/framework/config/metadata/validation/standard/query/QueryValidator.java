package net.n2oapp.framework.config.metadata.validation.standard.query;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkSimpleFieldSwitchCase;

/**
 * Валидатор выборки
 */
@Component
public class QueryValidator implements SourceValidator<N2oQuery>, SourceClassAware {

    @Override
    public void validate(N2oQuery n2oQuery, SourceProcessor p) {
        if (nonNull(n2oQuery.getObjectId()))
            ValidationUtils.checkForExistsObject(n2oQuery.getObjectId(),
                    String.format("Выборка %s", ValidationUtils.getIdOrEmptyString(n2oQuery.getId())), p);
        if (nonNull(n2oQuery.getFields())) {
            p.safeStreamOf(n2oQuery.getFields()).forEach(field -> checkFieldIdExistence(field, n2oQuery.getId(), p));
            checkForUniqueFields(n2oQuery.getFields(), n2oQuery.getId(), p);
        }
        if (nonNull(n2oQuery.getFilters())) {
            checkForUniqueFilterFields(n2oQuery.getFilters(), n2oQuery.getId());
            checkFilterFieldIds(n2oQuery);
        }
        checkForExistsFiltersInSelections(n2oQuery);
        checkInvocations(n2oQuery, p);
        checkSwitchCase(n2oQuery.getFields());
    }

    /**
     * Проверка полей на наличие id
     *
     * @param field Исходная metadata поля
     * @param p     Процессор исходных метаданных
     */
    public static void checkFieldIdExistence(AbstractField field, String queryId, SourceProcessor p) {
        if (field instanceof QueryReferenceField queryReferenceField)
            p.safeStreamOf(queryReferenceField.getFields()).forEach(childField -> checkFieldIdExistence(childField, queryId, p));
        p.checkIdExistence(field, String.format("Одно из полей выборки %s не имеет 'id'", ValidationUtils.getIdOrEmptyString(queryId)));
    }

    /**
     * Проверка, что поля в выборке не повторяются (включая вложенные в {@link QueryReferenceField})
     *
     * @param fields  Поля выборки
     * @param queryId Идентификатор выборки
     * @param p       Процессор исходных метаданных
     */
    private void checkForUniqueFields(AbstractField[] fields, String queryId, SourceProcessor p) {
        if (fields == null)
            return;
        p.checkIdsUnique(fields, String.format("Поле '%s' встречается более чем один раз в выборке '%s'", "%s", queryId));
        for (AbstractField field : fields) {
            if (field instanceof QueryReferenceField referenceField)
                checkForUniqueFields(referenceField.getFields(), queryId, p);
        }
    }

    /**
     * Проверка, что фильтруемые поля в выборке не повторяются
     *
     * @param filters Фильтры выборки
     * @param queryId Идентификатор выборки
     */
    private void checkForUniqueFilterFields(N2oQuery.Filter[] filters, String queryId) {
        Set<String> filterFields = new HashSet<>();
        for (N2oQuery.Filter filter : filters) {
            if (nonNull(filter.getFilterId()) && !filterFields.add(filter.getFilterId())) {
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
        N2oQuery.Filter[] filters = query.getFilters();
        Set<String> filterFields = filters == null
                ? Collections.emptySet()
                : Arrays.stream(filters)
                .map(filter -> castDefault(filter.getFilterId(), filter.getFieldId()))
                .collect(Collectors.toSet());
        checkFiltersExistInSelectionType(query.getLists(), filterFields, "list", query.getId());
        checkFiltersExistInSelectionType(query.getUniques(), filterFields, "unique", query.getId());
        checkFiltersExistInSelectionType(query.getCounts(), filterFields, "count", query.getId());
    }

    private void checkFiltersExistInSelectionType(N2oQuery.Selection[] selections, Set<String> filterFields, String selectionType, String queryId) {
        if (nonNull(selections)) {
            for (N2oQuery.Selection s : selections) {
                if (nonNull(s.getFilters())) {
                    for (String filter : s.getFilters()) {
                        if (!filterFields.contains(filter))
                            throw new N2oMetadataValidationException(String.format("\"<%s>\" ссылается на несуществующий фильтр %s в выборке %s",
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
        if (nonNull(query.getLists()))
            validateInvocations(query.getLists(), query.getId(), p);
        if (nonNull(query.getCounts()))
            validateInvocations(query.getCounts(), query.getId(), p);
        if (nonNull(query.getUniques()))
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

    private void checkSwitchCase(AbstractField[] fields) {
        if (isNull(fields))
            return;
        for (AbstractField field : fields) {
            if (field instanceof QueryReferenceField refField) {
                checkSwitchCase(refField.getFields());
            } else if (field instanceof QuerySimpleField simpleField && nonNull(simpleField.getN2oSwitch())) {
                checkSimpleFieldSwitchCase(simpleField.getN2oSwitch().getCases(), field.getId());
            }
        }
    }

    /**
     * Проверка обязательности и существования атрибута 'field-id' в фильтрах выборки.
     *
     * @param query Выборка
     */
    private void checkFilterFieldIds(N2oQuery query) {
        for (N2oQuery.Filter filter : query.getFilters()) {
            if (StringUtils.isBlank(filter.getFieldId()))
                throw new N2oMetadataValidationException(String.format(
                        "В фильтре %s выборки %s не задан обязательный атрибут 'field-id'",
                        ValidationUtils.getIdOrEmptyString(filter.getFilterId()),
                        ValidationUtils.getIdOrEmptyString(query.getId())));

            if (query.getFieldByAbsoluteId(filter.getFieldId()) == null)
                throw new N2oMetadataValidationException(String.format(
                        "Фильтр %s выборки %s ссылается на несуществующее поле %s",
                        ValidationUtils.getIdOrEmptyString(filter.getFilterId()),
                        ValidationUtils.getIdOrEmptyString(query.getId()),
                        ValidationUtils.getIdOrEmptyString(filter.getFieldId())));

        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }
}
