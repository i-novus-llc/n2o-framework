package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Абстратная реализация контрола
 */
@Getter
@Setter
public abstract class N2oStandardField extends N2oField {
    private String placeholder;
    private Submit submit;

}
