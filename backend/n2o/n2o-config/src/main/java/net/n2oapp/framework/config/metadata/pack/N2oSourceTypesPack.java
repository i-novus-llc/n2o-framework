package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.menu.N2oMenu;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;

/**
 * Стандартные типы метаданных
 */
public class N2oSourceTypesPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.types(new MetaType("object", N2oObject.class),
                new MetaType("query", N2oQuery.class),
                new MetaType("page", N2oPage.class),
                new MetaType("widget", N2oWidget.class),
                new MetaType("fieldset", N2oFieldSet.class),
                new MetaType("application", N2oApplication.class),
                new MetaType("menu", N2oMenu.class));
    }
}
