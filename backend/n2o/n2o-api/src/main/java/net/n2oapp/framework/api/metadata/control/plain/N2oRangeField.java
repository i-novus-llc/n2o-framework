package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.interval.N2oBaseIntervalField;

@Getter
@Setter
public class N2oRangeField extends N2oBaseIntervalField {

    private N2oStandardField beginControl;
    private N2oStandardField endControl;


}
