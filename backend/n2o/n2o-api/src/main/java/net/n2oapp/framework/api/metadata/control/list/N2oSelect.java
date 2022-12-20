package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

/**
 * Компонент выбора из выпадающего списка
 */
@Getter
@Setter
@N2oComponent
public class N2oSelect extends N2oSingleListFieldAbstract {
    @N2oAttribute("Тип выбора в выпадающем списке")
    private ListType type;
    @N2oAttribute("Возможность очистки выбора")
    private Boolean cleanable;
    private String selectFormat;
    private String selectFormatOne;
    private String selectFormatFew;
    private String selectFormatMany;
    @N2oAttribute("Поле, отвечающее за описание варианта выбора")
    private String descriptionFieldId;

    @Override
    public boolean isSingle() {
        return !ListType.MULTI.equals(type) && !ListType.CHECKBOXES.equals(type);
    }
}
