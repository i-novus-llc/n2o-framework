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
     * Получение значения для field
     * @param p
     * @return
     */
    public static String getField(CompileProcessor p) {
        MultiFormScope multiFormScope = p.getScope(MultiFormScope.class);
        if (multiFormScope != null && multiFormScope.isInner())
            return INDEX;
        return null;
    }
}
