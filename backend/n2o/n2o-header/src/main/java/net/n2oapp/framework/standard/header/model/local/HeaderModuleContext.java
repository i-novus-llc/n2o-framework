package net.n2oapp.framework.standard.header.model.local;

import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import net.n2oapp.framework.api.metadata.local.context.BaseCompileContext;
import net.n2oapp.framework.api.metadata.local.context.ContextMetadataProvider;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;


/**
 * User: operhod
 * Date: 17.02.14
 * Time: 14:38
 */
public class HeaderModuleContext extends BaseCompileContext<N2oHeaderModule, CompileContext> {
    private String headerId;
    private String moduleGroupId;
    private int order;

    public HeaderModuleContext(String id) {
        super(null, id);
    }

    public HeaderModuleContext(CompileContext parentContext,
                               N2oHeaderModule metadata, String headerId, String moduleGroupId, int order) {
        super(parentContext, metadata, new HeaderModuleContextMetadataProvider(headerId, moduleGroupId, order));
        this.headerId = headerId;
        this.moduleGroupId = moduleGroupId;
        this.order = order;
    }

    @Override
    public String getIdByContext() {
        return headerId + "." + (metadataId != null ? metadataId : moduleGroupId + ".module" + order);
    }

    @Override
    public Class<N2oHeaderModule> getMetadataClassByContext() {
        return N2oHeaderModule.class;
    }


    public int getOrder() {
        return order;
    }

    public String getModuleGroupId() {
        return moduleGroupId;
    }


    public static class HeaderModuleContextMetadataProvider
            implements ContextMetadataProvider<N2oHeaderModule, CompileContext> {
        private String headerId;
        private String moduleGroupId;
        private int order;

        public HeaderModuleContextMetadataProvider(String headerId, String moduleGroupId, int order) {
            this.headerId = headerId;
            this.moduleGroupId = moduleGroupId;
            this.order = order;
        }

        @Override
        public N2oHeaderModule provide(CompileContext parentContext, GlobalMetadataProvider provider) {
            N2oStandardHeader header = provider.getGlobal(headerId, N2oStandardHeader.class);
            for (N2oStandardHeader.ModuleGroup moduleGroup : header.getModuleGroups()) {
                if (moduleGroup.getId().equals(moduleGroupId))
                    return moduleGroup.getModules()[order-1];//order-1 т.е. счет order начинается с 1
            }
            return null;
        }
    }
}
