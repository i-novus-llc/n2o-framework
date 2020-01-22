package net.n2oapp.framework.autotest.factory;

import net.n2oapp.framework.autotest.N2oSelector;
import net.n2oapp.framework.autotest.component.header.StandardTableHeader;
import net.n2oapp.framework.autotest.component.header.TableHeader;

public class TableHeaders {
    public StandardTableHeader header(int index) {
        return null;
    }

    public StandardTableHeader header(N2oSelector by) {
        return null;
    }

    public <T extends TableHeader> T header(int index, Class<T> componentClass) {
        return null;
    }

    public <T extends TableHeader> T header(N2oSelector by, Class<T> componentClass) {
        return null;
    }
}
