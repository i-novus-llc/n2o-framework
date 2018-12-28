package net.n2oapp.framework.standard.header.model.global;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.standard.header.model.global.context.UserContextStructure;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 13.01.14
 * Time: 15:17
 */
public class N2oStandardHeader extends N2oHeader {
    private String mainPage;
    private String srcNavigation;
    private N2oHeaderModule[] modules;
    private ModuleGroup[] moduleGroups;
    private UserMenu userMenu;
    private UserContextStructure userContextStructure;

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public UserContextStructure getUserContextStructure() {
        return userContextStructure;
    }

    public void setUserContextStructure(UserContextStructure userContextStructure) {
        this.userContextStructure = userContextStructure;
    }

    public UserMenu getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(UserMenu userMenu) {
        this.userMenu = userMenu;
    }

    public N2oHeaderModule[] getModules() {
        return modules;
    }

    public void setModules(N2oHeaderModule[] modules) {
        this.modules = modules;
    }

    public ModuleGroup[] getModuleGroups() {
        return moduleGroups;
    }

    public void setModuleGroups(ModuleGroup[] moduleGroups) {
        this.moduleGroups = moduleGroups;
    }

    public String getSrcNavigation() {
        return srcNavigation;
    }

    public void setSrcNavigation(String srcNavigation) {
        this.srcNavigation = srcNavigation;
    }

    public static class ModuleGroup implements IdAware, Serializable {
        private N2oHeaderModule[] modules;
        private String id;
        private String name;

        public N2oHeaderModule[] getModules() {
            return modules;
        }

        public void setModules(N2oHeaderModule[] modules) {
            this.modules = modules;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UserMenu implements Serializable {
        private String src;
        private String usernameContext;

        public UserMenu() {
        }

        public UserMenu(String src, String usernameContext) {
            this.src = src;
            this.usernameContext = usernameContext;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getUsernameContext() {
            return usernameContext;
        }

        public void setUsernameContext(String usernameContext) {
            this.usernameContext = usernameContext;
        }
    }

}
