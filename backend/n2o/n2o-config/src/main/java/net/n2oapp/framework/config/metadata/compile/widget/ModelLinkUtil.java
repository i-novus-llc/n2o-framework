package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

/**
 * Утилитные методы для компиляции внутри виджета MultiForm
 */
public class ModelLinkUtil {

    private static final String INDEX = "[index]";

    private ModelLinkUtil() {
        throw new IllegalStateException("Инстанцирование утилитного класса запрещено");
    }

    /**
     * Идёт ли компиляция внутри виджета MultiForm
     *
     * @param p Процессор сборки метаданных
     * @return true - компиляция внутри MultiForm, иначе false
     */
    public static boolean isInMultiForm(CompileProcessor p) {
        return p.getScope(MultiFormScope.class) != null;
    }

    /**
     * Создание ссылки на модель с учётом компиляции внутри виджета MultiForm.
     * Внутри MultiForm источник данных адресуется по индексу строки.
     *
     * @param p                  Процессор сборки метаданных
     * @param model              Тип модели для обычной ссылки
     * @param clientDatasourceId Клиентский идентификатор источника данных
     * @return Ссылка на модель
     */
    public static ModelLink createModelLink(CompileProcessor p, ReduxModelEnum model, String clientDatasourceId, ReduxModelEnum defaultModel) {
        if (model != null) {
            return new ModelLink(model, clientDatasourceId);
        }
        if (isInMultiForm(p)) {
            MultiFormScope multiFormScope = p.getScope(MultiFormScope.class);
            if (multiFormScope.isInner()) {
                return new ModelLink(clientDatasourceId, INDEX);
            } else {
                return new ModelLink(ReduxModelEnum.DATASOURCE, clientDatasourceId);
            }
        }
        return new ModelLink(defaultModel, clientDatasourceId);
    }

    /**
     * Получение значения для field атрибута
     *
     * @param p Процессор сборки метаданных
     * @return Значение, если компонент в мультиформе, иначе - null
     */
    public static String getField(CompileProcessor p) {
        MultiFormScope multiFormScope = p.getScope(MultiFormScope.class);
        if (multiFormScope != null && multiFormScope.isInner())
            return INDEX;
        return null;
    }

    /**
     * Получение значения для field атрибута
     *
     * @param fieldId      - Идентификатор поля
     * @param datasourceId - Источник, в котором находится поле
     * @param p            Процессор сборки метаданных
     * @return Значение атрибута в зависимости от того находится он в мультиформе или нет
     */
    public static String getField(String fieldId, String datasourceId, CompileProcessor p) {
        MultiFormScope multiFormScope = p.getScope(MultiFormScope.class);

        String prefix = getField(p);
        if (multiFormScope != null && multiFormScope.isInner())
            prefix = INDEX;

        if (prefix == null || multiFormScope == null || !datasourceId.equals(multiFormScope.getDatasourceId()))
            return fieldId;

        if (fieldId != null) {
            if (fieldId.startsWith(INDEX))
                return fieldId;
            else
                return prefix.concat(".").concat(fieldId);
        }
        return null;
    }
}
