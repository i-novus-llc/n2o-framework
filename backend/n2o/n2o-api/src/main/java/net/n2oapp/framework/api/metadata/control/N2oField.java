package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.Arrays;


/**
 * Исходная модель поля
 */
@Getter
@Setter
public abstract class N2oField extends N2oComponent implements IdAware {
    @N2oAttribute("Идентификатор")
    private String id;
    @N2oAttribute("Условие видимости")
    private String visible;
    @N2oAttribute("Условие обязательности")
    private String required;
    @N2oAttribute("Условие доступности")
    private String enabled;
    private String[] dependsOn;
    @N2oAttribute("Заголовок")
    private String label;
    @N2oAttribute("Css класс заголовка")
    private String labelClass;
    @N2oAttribute("Описание")
    private String description;
    @N2oAttribute("Подсказка")
    private String help;
    @N2oAttribute("Тип данных")
    private String domain;
    @N2oAttribute("Отсутствие заголовка")
    private Boolean noLabel;
    @N2oAttribute("Отсутствие блока заголовка")
    private Boolean noLabelBlock;
    private Validations validations;
    private Boolean copied;
    @N2oAttribute("Значение по умолчанию")
    private String defaultValue;
    private String param;
    private ReduxModel refModel;
    private PageRef refPage;
    private String refDatasourceId;
    private String refFieldId;

    @N2oAttribute("Меню с кнопками")
    private N2oToolbar toolbar;
    private Dependency[] dependencies;

    @Deprecated
    public String getRefWidgetId() {
        return refDatasourceId;
    }

    @Deprecated
    public void setRefWidgetId(String refWidgetId) {
        this.refDatasourceId = refWidgetId;
    }

    /**
     * Добавление зависимости к списку зависимостей поля
     *
     * @param d Зависимость
     */
    public void addDependency(Dependency d) {
        if (d == null) return;
        if (dependencies == null) {
            dependencies = new Dependency[1];
            dependencies[0] = d;
        } else {
            dependencies = Arrays.copyOf(dependencies, dependencies.length + 1);
            dependencies[dependencies.length - 1] = d;
        }
    }

    /**
     * Добавление зависимостей к списку зависимостей поля
     *
     * @param d Массив зависимостей
     */
    public void addDependencies(Dependency[] d) {
        if (d == null || d.length == 0) return;

        if (dependencies == null) {
            dependencies = new Dependency[d.length];
            dependencies = Arrays.copyOfRange(d, 0, d.length);
        } else {
            dependencies = Arrays.copyOf(dependencies, dependencies.length + d.length);
            System.arraycopy(d, 0, dependencies, dependencies.length, d.length);
        }
    }

    /**
     * @param clazz - Тип зависимости
     * @return содержит ли поле зависимость типа clazz
     */
    public boolean containsDependency(Class<? extends Dependency> clazz) {
        if (dependencies == null) return false;

        for (Dependency dependency : dependencies) {
            if (dependency.getClass().equals(clazz))
                return true;
        }

        return false;
    }

    @Getter
    @Setter
    public static class Validations implements Source {
        private N2oValidation[] inlineValidations;
        private String[] whiteList;

    }

    @Getter
    @Setter
    public static class Dependency implements Source {
        private String[] on;
        private String value;
        private Boolean applyOnInit;
    }

    public static class EnablingDependency extends Dependency {
    }

    public static class RequiringDependency extends Dependency {
    }

    public static class SetValueDependency extends Dependency {
    }

    public static class FetchDependency extends Dependency {
    }

    public static class ResetDependency extends Dependency {
    }

    @Getter
    @Setter
    public static class FetchValueDependency extends Dependency implements PreFiltersAware {
        private String queryId;
        private String valueFieldId;
        private N2oPreFilter[] preFilters;
        private Integer size;
    }

    @Getter
    @Setter
    public static class VisibilityDependency extends Dependency {
        private Boolean reset;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }
}
