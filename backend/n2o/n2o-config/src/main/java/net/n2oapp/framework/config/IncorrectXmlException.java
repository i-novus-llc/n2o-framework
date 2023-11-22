package net.n2oapp.framework.config;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.menu.N2oMenu;

import java.util.Map;

public abstract class IncorrectXmlException extends EngineNotFoundException {

    protected static final Map<? extends Class<? extends SourceMetadata>, String> xml = Map.of(
            N2oObject.class, "object.xml",
            N2oQuery.class, "query.xml",
            N2oPage.class, "page.xml",
            N2oWidget.class, "widget.xml",
            N2oFieldSet.class, "fieldset.xml",
            N2oApplication.class, "application.xml",
            N2oMenu.class, "menu.xml",
            N2oSidebar.class, "sidebar.xml"
    );

    public IncorrectXmlException(Object type, String message) {
        super(type, message);
    }
}
