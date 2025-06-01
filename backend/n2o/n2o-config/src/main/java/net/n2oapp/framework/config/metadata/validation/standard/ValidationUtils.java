package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.action.N2oOnFailAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.config.util.StylesResolver.camelToSnake;

/**
 * Утилиты проверки метаданных
 */
public final class ValidationUtils {

    private static final SimpleDateFormat SIMPLE_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private ValidationUtils() {
    }

    /**
     * Проверить идентификаторы метаданных по соглашениям об именовании
     *
     * @param items Метаданные
     * @param p     Процессор исходных метаданных
     */
    public static void checkIds(NamespaceUriAware[] items, SourceProcessor p) {
        if (items != null)
            for (NamespaceUriAware item : items)
                checkId(item, p);
    }

    /**
     * Проверить идентификатор метаданной по соглашениям об именовании
     *
     * @param item Метаданная
     * @param p    Процессор исходных метаданных
     */
    public static void checkId(NamespaceUriAware item, SourceProcessor p) {
        if (item instanceof IdAware idAware)
            p.checkId(idAware, "Идентификатор поля '%s' является запрещенным именем");
    }

    /**
     * Проверка наличия источника данных по указанному идентификатору
     *
     * @param dsId Идентификатор проверямого источника данных
     * @param p    Процессор исходных метаданных
     * @param msg  Сообщение об ошибке
     */
    //fixme упразднить этот метод с удалением лишних скоупов datasource
    public static void checkDatasourceExistence(String dsId, SourceProcessor p, String msg) {
        ValidatorDatasourceIdsScope datasourceIdsScope = p.getScope(ValidatorDatasourceIdsScope.class);
        ValidatorDataSourcesScope dataSourcesScope = p.getScope(ValidatorDataSourcesScope.class);

        if ((datasourceIdsScope == null || !datasourceIdsScope.contains(dsId))
                && (dataSourcesScope == null || !dataSourcesScope.containsKey(dsId))) {
            throw new N2oMetadataValidationException(msg);
        }
    }

    /**
     * Проверка наличия источника данных по указанному идентификатору
     *
     * @param dsId Идентификатор проверяемого источника данных
     * @param p    Процессор исходных метаданных
     * @param tag  Название тега, использующего атрибут datasource
     */
    public static void checkDatasourceExistenceInTag(String dsId, SourceProcessor p, @Nonnull String tag) {
        if (dsId != null) {
            checkDatasourceExistence(dsId, p,
                    String.format("Тег %s в атрибуте 'datasource' ссылается на несуществующий источник данных %s",
                            tag, dsId));
        }
    }

    /**
     * Проверка существования объекта
     *
     * @param objectId         Идентификатор объекта
     * @param messageFirstPart Первая часть сообщения об ошибке
     * @param p                Процессор исходных метаданных
     */
    public static void checkForExistsObject(String objectId, String messageFirstPart, SourceProcessor p) {
        p.checkForExists(objectId, N2oObject.class,
                String.format("%s ссылается на несуществующий объект %s",
                        messageFirstPart,
                        getIdOrEmptyString(objectId)));
    }

    /**
     * Проверка сущестования выборки
     *
     * @param queryId          Идентификатор источника данных
     * @param messageFirstPart Первая часть сообщения об ошибке
     * @return Метаданная выборки если она существует, иначе null
     */
    public static N2oQuery checkQueryExists(String queryId, String messageFirstPart, SourceProcessor p) {
        if (queryId != null) {
            p.checkForExists(queryId, N2oQuery.class,
                    String.format("%s ссылается на несуществующую выборку %s",
                            messageFirstPart,
                            getIdOrEmptyString(queryId)));
            return p.getOrThrow(queryId, N2oQuery.class);
        }
        return null;
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
        checkDatasourceExistenceInTag(((N2oIfBranchAction) operator.getFirst()).getDatasourceId(), p, "<if>");
        Optional<N2oElseIfBranchAction> elseIfBranch = findFirstByInstance(operator, N2oElseIfBranchAction.class);
        Optional<N2oElseBranchAction> elseBranch = findFirstByInstance(operator, N2oElseBranchAction.class);

        if (elseIfBranch.isPresent() && elseBranch.isPresent() &&
                (operator.indexOf(elseIfBranch.get()) > operator.indexOf(elseBranch.get())))
            throw new N2oMetadataValidationException("Неверный порядок тегов <else-if> и <else> в условном операторе if-else");

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
        return metadataId != null ? StringUtils.quote(metadataId) : "";
    }

    /**
     * Получение идентификатора метаданной для сообщения исключений
     *
     * @param metadataId Идентификатор метаданной
     * @return Пробел + идентификатор метаданной в случае существования идентификатора, иначе пустая строка
     */
    public static String getSpaceWithIdOrEmptyString(String metadataId) {
        return metadataId != null ? " " + StringUtils.quote(metadataId) : "";
    }

    /**
     * Проверка зависимостей на пустое тело
     *
     * @param dependency зависимость
     * @param message    сообщение при ошибке
     */
    public static void checkEmptyDependency(N2oDependency dependency, String message) {
        if (!StringUtils.hasText(dependency.getValue()))
            throw new N2oMetadataValidationException(message);
    }

    public static void checkInteger(String text, String message) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            throw new N2oMetadataValidationException(message);
        }
    }

    public static void checkShort(String text, String message) {
        try {
            Short.parseShort(text);
        } catch (NumberFormatException nfe) {
            throw new N2oMetadataValidationException(message);
        }
    }

    public static void checkByte(String text, String message) {
        try {
            Byte.parseByte(text);
        } catch (NumberFormatException nfe) {
            throw new N2oMetadataValidationException(message);
        }
    }

    public static void checkDouble(String text, String message) {
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException nfe) {
            throw new N2oMetadataValidationException(message);
        }
    }

    public static void checkDate(String date, String message) {
        if (!(isValidDateByFormat(date, SIMPLE_DATETIME_FORMAT) || isValidDateByFormat(date, SIMPLE_DATE_FORMAT))) {
            throw new N2oMetadataValidationException(message);
        }
    }

    public static void checkOnFailActionNotExist(N2oAction[] actions, String componentName) {
        if (actions != null && Stream.of(actions).anyMatch(N2oOnFailAction.class::isInstance))
            throw new N2oMetadataValidationException(String.format("Действие <on-fail> нельзя использовать в %s", componentName));
    }

    public static void checkOnFailAction(N2oAction[] actions) {
        if (actions == null || actions.length == 0) return;
        List<N2oAction> onFailActions = Stream.of(actions).filter(N2oOnFailAction.class::isInstance).toList();
        if (CollectionUtils.isNotEmpty(onFailActions)) {
            if (onFailActions.size() > 1) {
                throw new N2oMetadataValidationException("Не может быть более одного элемента <on-fail>");
            } else if (onFailActions.size() == 1) {
                if (!(actions[actions.length - 1] instanceof N2oOnFailAction)) {
                    throw new N2oMetadataValidationException("Действие <on-fail> должно быть последним в списке действий");
                }
                if (Stream.of(actions).noneMatch(N2oInvokeAction.class::isInstance)) {
                    throw new N2oMetadataValidationException("Задано действие <on-fail> при отсутствующем действии <invoke>");
                }
            }
        }
    }

    public static boolean isInvalidColor(String color) {
        return color != null
                && !isLink(color)
                && !EnumUtils.isValidEnum(ColorEnum.class, camelToSnake(color));
    }

    private static boolean isValidDateByFormat(String date, SimpleDateFormat dateFormat) {
        try {
            dateFormat.parseObject(date);
        } catch (ParseException ex) {
            return false;
        }
        return true;
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
