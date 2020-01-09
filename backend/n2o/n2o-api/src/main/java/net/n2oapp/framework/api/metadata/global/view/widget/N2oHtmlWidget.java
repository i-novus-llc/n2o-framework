package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oHtmlWidget extends N2oWidget {
    private String url;
    private Boolean dummyObject;

    public N2oHtmlWidget() {
        dummyObject = true;
    }

    @Override
    public void setObjectId(String objectId) {
        if(objectId != null) {
            super.setObjectId(objectId);
            dummyObject = false;
        }
    }
}
