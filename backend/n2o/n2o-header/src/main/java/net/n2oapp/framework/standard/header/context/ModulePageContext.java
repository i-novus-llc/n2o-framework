package net.n2oapp.framework.standard.header.context;

import net.n2oapp.framework.api.metadata.local.context.OutOfRangeException;
import net.n2oapp.framework.api.metadata.local.context.PageContext;
import net.n2oapp.framework.standard.header.model.local.HeaderModuleContext;


/**
 * @author iryabov
 * @since 19.03.2015
 */
public class ModulePageContext extends PageContext<HeaderModuleContext> {
    private String moduleId;
    private String spaceId;     //id спейса
    private String pageId;     //id пейджа
    private Integer pageOrder;  //порядковый номер страницы в спейсе или в меню; если -1, то это mainPage

    /**
     * @param parentContext - parent
     * @param moduleId      - модуль, в котором находится page
     * @param spaceId       - id space, если page находится в space
     * @param pageOrder     - порядковый номер page в space, если page находится в space; или порядковый номер страницы
     * @param pageId        - id страницы
     * @throws OutOfRangeException
     */
    public ModulePageContext(HeaderModuleContext parentContext,
                             String moduleId,
                             String spaceId,
                             Integer pageOrder,
                             String pageId) throws OutOfRangeException {
        super(parentContext, pageId);
        this.moduleId = moduleId;
        this.pageId = pageId;
        this.spaceId = spaceId;
        this.pageOrder = pageOrder;
    }


    @Override
    public String getIdByContext() {
        return moduleId + "." + pageId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public Integer getPageOrder() {
        return pageOrder;
    }
}
