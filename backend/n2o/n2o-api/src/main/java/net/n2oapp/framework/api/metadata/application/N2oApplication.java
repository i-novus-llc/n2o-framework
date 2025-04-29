package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.header.N2oHeader;

/**
 * Исходная модель приложения
 */
@Getter
@Setter
public class N2oApplication extends N2oMetadata {
    /**
     * Ссылка на страницу, которая открывается по /
     */
    private String welcomePageId;
    /**
     * Макет отображения элементов управления
     */
    private NavigationLayoutEnum navigationLayout;
    /**
     * Зафиксированы ли header и sidebar
     */
    private Boolean navigationLayoutFixed;

    /**
     * Шапка приложения
     */
    private N2oHeader header;
    /**
     * Боковые панели приложения
     */
    private N2oSidebar[] sidebars;
    /**
     * Подвал приложения
     */
    private N2oFooter footer;
    /**
     * Источники данных
     */
    private N2oAbstractDatasource[] datasources;
    /**
     * События
     */
    private N2oAbstractEvent[] events;

    @Override
    public String getPostfix() {
        return "application";
    }

    @Override
    public Class<? extends SourceMetadata> getSourceBaseClass() {
        return N2oApplication.class;
    }
}
