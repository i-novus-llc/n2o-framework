package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.datasource.Submittable;


/**
 * Абстратная реализация контрола
 */
@Getter
@Setter
public abstract class N2oStandardField extends N2oField implements Submittable {
    private String placeholder;
    private Submit submit;
}
