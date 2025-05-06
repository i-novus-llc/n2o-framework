package net.n2oapp.framework.config.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetadataUtil {
    public static final Map<Class<? extends SourceMetadata>, String> XML_BY_METADATA = Map.of(
            N2oObject.class, "object.xml",
            N2oQuery.class, "query.xml",
            N2oPage.class, "page.xml",
            N2oWidget.class, "widget.xml",
            N2oFieldSet.class, "fieldset.xml",
            N2oApplication.class, "application.xml",
            N2oMenu.class, "menu.xml",
            N2oSidebar.class, "sidebar.xml"
    );
}
