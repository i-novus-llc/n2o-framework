package net.n2oapp.framework.config.metadata.validation.standard.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oField;

import java.util.ArrayList;

@Getter
@Setter
public class FieldsScope extends ArrayList<N2oField> {
    private boolean hasDependencies = false;
}
