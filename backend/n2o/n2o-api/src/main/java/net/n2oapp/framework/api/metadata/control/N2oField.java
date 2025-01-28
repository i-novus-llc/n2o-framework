package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
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
public abstract class N2oField extends N2oComponent implements IdAware, FieldsetItem {
    private String id;
    private String visible;
    private String required;
    private String enabled;
    private String[] dependsOn;
    private String label;
    private String labelClass;
    private String description;
    private String help;
    private String domain;
    private String noLabel;
    private String noLabelBlock;
    private Validations validations;
    private Boolean copied;
    private String defaultValue;
    private String param;
    private ReduxModel refModel;
    private PageRef refPage;
    private String refDatasourceId;
    private String refFieldId;

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

    @Override
    public FieldsetItem[] getItems() {
        return null;
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
            System.arraycopy(d, 0, dependencies, dependencies.length - d.length, d.length);
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

    @Getter
    @Setter
    public static class EnablingDependency extends Dependency {
        private String message;
    }

    @Getter
    @Setter
    public static class VisibilityDependency extends Dependency {
        private Boolean reset;
    }

    @Getter
    @Setter
    public static class RequiringDependency extends Dependency {
        private Boolean validate;
    }

    @Getter
    @Setter
    public static class SetValueDependency extends Dependency {
        private Boolean validate;
    }

    @Getter
    @Setter
    public static class ResetDependency extends Dependency {
        private Boolean validate;
    }

    public static class FetchDependency extends Dependency {
    }

    @Getter
    @Setter
    public static class FetchValueDependency extends Dependency implements PreFiltersAware {
        private String queryId;
        private String valueFieldId;
        private N2oPreFilter[] preFilters;
        private Integer size;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }
}
