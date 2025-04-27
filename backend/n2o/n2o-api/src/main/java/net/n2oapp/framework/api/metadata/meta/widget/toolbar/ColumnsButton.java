package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель пользовательской настройки отображения таблицы
 */
@Getter
@Setter
public class ColumnsButton extends PerformButton {
    @JsonProperty
    private String defaultColumns;
    @JsonProperty
    private String locked;

    public ColumnsButton(PerformButton performButton) {
        this.setId(performButton.getId());
        this.setSrc(performButton.getSrc());
        this.setLabel(performButton.getLabel());
        this.setIcon(performButton.getIcon());
        this.setIconPosition(performButton.getIconPosition());
        this.setHint(performButton.getHint());
        this.setHintPosition(performButton.getHintPosition());
        this.setDatasource(performButton.getDatasource());
        this.setModel(performButton.getModel());
    }
}
