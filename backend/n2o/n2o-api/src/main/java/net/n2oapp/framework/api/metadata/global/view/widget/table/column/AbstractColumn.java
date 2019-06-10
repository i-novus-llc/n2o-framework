package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

import java.io.Serializable;

/**
 * Абстрактный столбец таблицы
 */
@Getter
@Setter
public abstract class AbstractColumn implements IdAware, Serializable{
    private String id;
    private String textFieldId;
    private String tooltipFieldId;
    private Integer width;
    private String format;
    private String labelName;
    private String labelIcon;
    private LabelType labelType;
    private Boolean visible;
    private Boolean resizable;
    private String visibilityCondition;
    private String sortingFieldId;
    private DirectionType sortingDirection;
    private ColumnFixedPosition fixed;


    public abstract boolean isDynamic();


}
