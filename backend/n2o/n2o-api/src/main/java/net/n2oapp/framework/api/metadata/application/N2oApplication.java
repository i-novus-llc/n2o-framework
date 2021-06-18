package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
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
     * Реализация приложения
     */
    private String src;

    /**
     * Макет отображения элементов управления
     */
    private String navigationLayout;

    /**
     * Зафиксированы ли header и sidebar
     */
    private Boolean navigationLayoutFixed;

    /**
     * Ссылка на страницу, которая открывается по /
     */
    private String welcomePageId;

    /**
     * Заголовок приложения
     */
    private N2oHeader header;

    /**
     * Боковая панель приложения
     */
    private N2oSidebar sidebar;

    /**
     * Подвал приложения
     */
    private N2oFooter footer;

    @Override
    public String getPostfix() {
        return "application";
    }

    @Override
    public Class<? extends SourceMetadata> getSourceBaseClass() {
        return N2oApplication.class;
    }
}
