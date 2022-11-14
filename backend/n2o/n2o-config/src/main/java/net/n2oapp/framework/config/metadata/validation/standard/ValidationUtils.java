package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Утилиты проверки метаданных
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Проверить идентификаторы метаданных по соглашениям об именовании
     *
     * @param items Метаданные
     * @param p     Процессор исходных метаданных
     */
    public static void checkIds(NamespaceUriAware[] items, SourceProcessor p) {
        if (items != null) {
            for (NamespaceUriAware item : items) {
                checkId(item, p);
            }
        }
    }

    /**
     * Проверить идентификатор метаданной по соглашениям об именовании
     *
     * @param item Метаданная
     * @param p    Процессор исходных метаданных
     */
    public static void checkId(NamespaceUriAware item, SourceProcessor p) {
        if (item instanceof IdAware) {
            p.checkId((IdAware) item, "Идентификатор поля {0} является запрещенным именем");
        }
    }

    /**
     * Проверка наличия источника данных по указанному идентификатору
     *
     * @param dsId               Идентификатор проверямого источника данных
     * @param datasourceIdsScope Скоуп источников данных
     * @param msg                Сообщение об ошибке
     */
    public static void checkDatasourceExistence(String dsId, DatasourceIdsScope datasourceIdsScope, String msg) {
        if (datasourceIdsScope == null || !datasourceIdsScope.contains(dsId)) {
            throw new N2oMetadataValidationException(msg);
        }
    }

    /**
     * Проверка наличия источника данных по указанному идентификатору
     *
     * @param dsId Идентификатор проверямого источника данных
     * @param p    Процессор исходных метаданных
     * @param tag  Название тега, использующего атрибут datasource
     */
    public static void checkDatasourceExistence(String dsId, SourceProcessor p, @Nonnull String tag) {
        if (dsId != null) {
            ValidationUtils.checkDatasourceExistence(dsId, p.getScope(DatasourceIdsScope.class),
                    String.format("Тег %s в атрибуте 'datasource' ссылается на несуществующий источник данных %s",
                            tag, dsId));
        }
    }

    /**
     * Проверка наличия действия в скоупе
     *
     * @param actionId     Идентификатор проверямого действия
     * @param actionsScope Скоуп действий
     * @param msg          Сообщение об ошибке
     */
    public static void checkActionExistence(@Nonnull String actionId, @Nonnull MetaActions actionsScope,
                                            @Nonnull String msg) {
        if (!actionsScope.containsKey(actionId))
            throw new N2oMetadataValidationException(msg);
    }

    /**
     * Валидация условных конструкций if-else
     *
     * @param branches Все условные конструкции компонента
     * @param p        Процессор исходных метаданных
     */
    public static void validateIfElse(Queue<N2oConditionBranch> branches, SourceProcessor p) {
        if (!(branches.element() instanceof N2oIfBranchAction))
            throw new N2oMetadataValidationException("Условный оператор if-else начинается не с тега <if>");

        LinkedList<N2oConditionBranch> operator = constructOperator(branches);
        checkDatasourceExistence(((N2oIfBranchAction) operator.getFirst()).getDatasourceId(), p, "<if>");
        Optional<N2oElseIfBranchAction> elseIfBranch = findFirstByInstance(operator, N2oElseIfBranchAction.class);
        Optional<N2oElseBranchAction> elseBranch = findFirstByInstance(operator, N2oElseBranchAction.class);

        if (elseIfBranch.isPresent() && elseBranch.isPresent()) {
            if (operator.indexOf(elseIfBranch.get()) > operator.indexOf(elseBranch.get()))
                throw new N2oMetadataValidationException("Неверный порядок тегов <else-if> и <else> в условном операторе if-else");
        }

        for (N2oConditionBranch operatorBranch : operator) {
            if (operatorBranch instanceof N2oIfBranchAction)
                checkTest(operatorBranch, p, "<if>");
            else if (operatorBranch instanceof N2oElseIfBranchAction)
                checkTest(operatorBranch, p, "<else-if>");
            p.validate(operatorBranch);
        }

        if (!branches.isEmpty())
            validateIfElse(branches, p);
    }

    /**
     * Получение идентификатора метаданной для сообщения исключений
     *
     * @param metadataId Идентификатор метаданной
     * @return Идентификатор метаданной в случае его существования, иначе пуста строка
     */
    public static String getIdOrEmptyString(String metadataId) {
        return metadataId != null ? metadataId : "";
    }

    private static void checkTest(N2oConditionBranch branch, SourceProcessor p, @Nonnull String tag) {
        p.checkNotNull(branch.getTest(),
                String.format("В теге %s условного операторе if-else не задано условие 'test'", tag));
    }

    private static <T> Optional<T> findFirstByInstance(List<? super T> list, Class<T> clazz) {
        if (list == null)
            return Optional.empty();
        return list.stream().filter(clazz::isInstance).map(clazz::cast).findFirst();
    }

    private static LinkedList<N2oConditionBranch> constructOperator(Queue<N2oConditionBranch> branches) {
        LinkedList<N2oConditionBranch> ifElseOperator = new LinkedList<>();
        ifElseOperator.add(branches.poll());
        while (!(branches.isEmpty() || (branches.element() instanceof N2oIfBranchAction)))
            ifElseOperator.add(branches.poll());
        return ifElseOperator;
    }
}
