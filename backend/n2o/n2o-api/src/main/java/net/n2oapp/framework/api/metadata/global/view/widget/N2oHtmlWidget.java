package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oFileAware;

@Getter
@Setter
public class N2oHtmlWidget extends N2oWidget implements N2oFileAware {
    private String html;
    private String file;
}