package net.n2oapp.framework.standard.header.validation;

import net.n2oapp.framework.api.metadata.validation.Level;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.standard.header.model.global.N2oBaseHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;

import java.util.HashSet;
import java.util.Set;


/**
 * @author rgalina
 * @since 19.01.2016
 */
public class ModuleIdValidator extends TypedMetadataValidator<N2oStandardHeader> {
    @Override
    public Class<N2oStandardHeader> getMetadataClass() {
        return N2oStandardHeader.class;
    }

    @Override
    public void check(N2oStandardHeader header) {
        if(header.getModuleGroups() != null && header.getModuleGroups().length>0) {
            Set<String> moduleIds = new HashSet<>();
            for(N2oStandardHeader.ModuleGroup moduleGroup : header.getModuleGroups()) {
                if(moduleGroup.getModules() != null && moduleGroup.getModules().length>0) {
                    for (N2oHeaderModule module : moduleGroup.getModules()) {
                        String id = module.getRefId() == null ? ((N2oBaseHeaderModule) module).getSourceId()
                                : module.getRefId();
                        if (id == null) {
                            throw new N2oMetadataValidationException("n2o.validation.dataNotFilled").addData(module.getName());
                        }
                        if (moduleIds.contains(id)) {
                            throw new N2oMetadataValidationException("n2o.validation.identicalId").addData(id);
                        }
                        moduleIds.add(id);
                    }
                }
            }
        }
    }

}
