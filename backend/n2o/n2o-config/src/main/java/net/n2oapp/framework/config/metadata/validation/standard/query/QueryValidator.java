package net.n2oapp.framework.config.metadata.validation.standard.query;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Валидатор выборки
 */
@Component
public class QueryValidator implements SourceValidator<N2oQuery>, SourceClassAware {

    @Override
    public void validate(N2oQuery n2oQuery, SourceProcessor p) {
        if (nonNull(n2oQuery.getObjectId()))
            ValidationUtils.checkForExistsObject(n2oQuery.getObjectId(),
                    String.format("Выборка %s", ValidationUtils.getIdOrEmptyString(n2oQuery.getId())),p);
        if (nonNull(n2oQuery.getFields())) {
            p.safeStreamOf(n2oQuery.getFields()).forEach(field -> checkFieldIdExistence(field, n2oQuery.getId(), p));
            checkForUniqueFields(n2oQuery.getFields(), n2oQuery.getId(), p);
        }
        if (nonNull(n2oQuery.getFilters())) {
            checkForUniqueFilterFields(n2oQuery.getFilters(), n2oQuery.getId());
            checkForExistsFiltersInSelections(n2oQuery);
        }
        checkInvocations(n2oQuery, p);
        checkSwitchCase(n2oQuery.getFields());
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
        Set<String> filterFields = Arrays.stream(query.getFilters()).map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
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
            if (field instanceof QueryReferenceField)
                checkSwitchCase(((QueryReferenceField)field).getFields());
            if (field instanceof QuerySimpleField && nonNull(((QuerySimpleField) field).getN2oSwitch())) {
                N2oSwitch n2oSwitch = ((QuerySimpleField) field).getN2oSwitch();
                if (n2oSwitch.getCases().isEmpty())
                    throw new N2oMetadataValidationException(String.format("В элементе '<switch>' поля '%s' отсутствует '<case>'", field.getId()));
                if (n2oSwitch.getCases().containsKey(""))
                    throw new N2oMetadataValidationException(String.format("В '<case>' элемента '<switch>' поля '%s' атрибут 'value' пустой", field.getId()));
                if (n2oSwitch.getCases().containsValue(""))
                    throw new N2oMetadataValidationException(String.format("В '<case>' элемента '<switch>' поля '%s' отсутствует тело", field.getId()));
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }
}
