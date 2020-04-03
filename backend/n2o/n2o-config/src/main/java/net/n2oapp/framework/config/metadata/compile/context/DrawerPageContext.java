package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawerPageContext extends PageContext {

    public DrawerPageContext(String pageId, String route) {
        super(pageId, route);
    }
}
