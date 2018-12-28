package net.n2oapp.framework.api.metadata.control;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

@Getter
@Setter
public class N2oButtonField extends N2oStandardField {

    private String title;
    private String titleFieldId;
    private String icon;
    private String iconFieldId;
    private LabelType type;

    @JsonIgnore
    private N2oAction event;
    private String eventId;
}
