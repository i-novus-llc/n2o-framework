package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.header.Header;

public class HeaderContext extends BaseCompileContext<Header, N2oHeader> {

    public HeaderContext(String sourceId) {
        super(sourceId, N2oHeader.class, Header.class);
    }
}
