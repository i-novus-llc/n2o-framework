package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

public interface LeftRightPage extends Page {
    Regions left();
    Regions right();
}
