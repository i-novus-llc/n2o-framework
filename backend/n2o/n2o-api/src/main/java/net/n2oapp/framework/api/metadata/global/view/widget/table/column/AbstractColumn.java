package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Абстрактный столбец таблицы
 */
@Getter
@Setter
public abstract class AbstractColumn implements IdAware, Source, ExtensionAttributesAware {
    @N2oAttribute("Идентификатор")
    private String id;
    private String src;
    @N2oAttribute("Css класс")
    private String cssClass;
    @N2oAttribute("Стиль")
    private String style;
    @N2oAttribute("Поле, отвечающее за значение ячейки")
    private String textFieldId;
    @N2oAttribute("Поле, отвечающее за тултип ячейки")
    private String tooltipFieldId;
    @N2oAttribute("Ширина")
    private String width;
    @N2oAttribute("Имя")
    private String labelName;
    @N2oAttribute("Иконка")
    private String labelIcon;
    @N2oAttribute("Условие видимости")
    private String visible;
    @N2oAttribute("Возможность изменения ширины")
    private Boolean resizable;
    @N2oAttribute("Поле, отвечающее за сортировку столбца")
    private String sortingFieldId;
    @N2oAttribute("Направление сортировки")
    private SortingDirection sortingDirection;
    @N2oAttribute("Прилипание столбца к краю")
    private ColumnFixedPosition fixed;
    private ColumnVisibility[] columnVisibilities;
    @N2oAttribute("Скрытие ячейки при наведении на строку")
    private Boolean hideOnBlur;
    @N2oAttribute("Выравнивание заголовка")
    private Alignment alignment;
    @N2oAttribute("Выравнивание содержимого в ячейках столбца")
    private Alignment contentAlignment;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Getter
    @Setter
    public static class ColumnVisibility implements Source, DatasourceIdAware {
        private String value;
        private String datasourceId;
        private ReduxModel model;

        @Deprecated
        public String getRefWidgetId() {
            return datasourceId;
        }

        @Deprecated
        public void setRefWidgetId(String refWidgetId) {
            this.datasourceId = refWidgetId;
        }
    }
}
