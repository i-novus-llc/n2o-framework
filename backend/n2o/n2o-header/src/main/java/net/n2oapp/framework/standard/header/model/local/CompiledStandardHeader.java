package net.n2oapp.framework.standard.header.model.local;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import net.n2oapp.framework.api.metadata.local.view.header.CompiledHeader;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;
import net.n2oapp.framework.standard.header.model.global.context.UserContextStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.arrayToMap;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * User: operhod
 * Date: 13.01.14
 * Time: 15:45
 */
public class CompiledStandardHeader extends CompiledHeader<N2oStandardHeader> {

    private static Logger log = LoggerFactory.getLogger(CompiledStandardHeader.class);
    private String mainPage;
    private String sourceMainPage;
    private String srcNavigation;
    protected Map<String, ModuleGroup> moduleGroups = new LinkedHashMap<>();
    protected CompiledUserMenu userMenu;
    @JsonIgnore
    private int moduleOrder;
    @JsonIgnore
    private int groupOrder;
    @JsonIgnore
    private UserContextStructure userContextStructure;
    @JsonIgnore
    private UserContext userContext;
    private Map<Integer, Map<String, Object>> contextActionParameters;

    public String getMainPage() {
        return mainPage;
    }

    @Override
    public void compile(N2oStandardHeader n2oHeader, final N2oCompiler compiler, CompileContext context) {
        super.compile(n2oHeader, compiler, context);
        srcNavigation = castDefault(n2oHeader.getSrcNavigation(), "n2o/plugins/top-menu/template/vertModulesList");
        initGroups(n2oHeader, compiler);
        this.sourceMainPage = n2oHeader.getMainPage();
        initMainPage();
        initUserMenu(n2oHeader, compiler);
        if (n2oHeader.getUserContextStructure() != null) {
            initContext(compiler, n2oHeader.getUserContextStructure());
        }
    }

    private void initMainPage() {
        if (sourceMainPage != null) {
            mainPage = sourceMainPage;
            return;
        }
        for (ModuleGroup moduleGroup : moduleGroups.values()) {
            for (CompiledHeaderModule module : moduleGroup.getModules()) {
                if (!module.isOuterModule()) {
                    this.mainPage = module.getMainPage();
                    return;
                }
            }
        }
        mainPage = null;
    }

    public Map<String, CompiledHeaderModule.Page> getPageMap() {
        //todo нужно кешировать! (учесть методы removePage)
        Map<String, CompiledHeaderModule.Page> result = new HashMap<>();
        for (CompiledHeaderModule.Page page : getAllPages()) {
            result.put(page.getId(), page);
        }
        for (CompiledHeaderModule module : getAllModules()) {
            if (!module.isOuterModule() && !result.containsKey(module.getMainPage())) {
                result.put(module.getMainPage(), new CompiledHeaderModule.Page(module.getMainPage(), module.getMainPage(), null, module.getSourceId(), module.getId()));
            }
        }
        return result;
    }

    protected void initContext(N2oCompiler compiler, UserContextStructure contextStructure) {
        this.userContextStructure = compiler.copy(contextStructure);
        this.userContextStructure.setSrc(castDefault(this.userContextStructure.getSrc(), "n2o/plugins/top-menu/template/treeContextMenu"));//listContextMenu
        this.userContextStructure.getPosition().setUnits(this.userContextStructure.getUnits());
//        for (UserContextStructure.Unit unit : this.userContextStructure.getUnits()) {
//            initContextUnit(unit);
//        }
    }

//    private void initContextUnit(UserContextStructure.Unit unit) {
//        if (unit == null) return;
//        for (UserContextStructure.Unit u : unit.getUnits()) {
//            initContextUnit(u);
//        }
//    }

    private void initUserMenu(N2oStandardHeader n2oHeader, final N2oCompiler compiler) {
        if (n2oHeader.getUserMenu() != null) {
            N2oStandardHeader.UserMenu copiedUserMenu = compiler.copy(n2oHeader.getUserMenu());
            userMenu = new CompiledUserMenu(copiedUserMenu.getSrc(), copiedUserMenu.getUsernameContext(), "");
        }
    }

    private void initGroups(N2oStandardHeader header, final N2oCompiler compiler) {
        groupOrder = 0;
        arrayToMap(header.getModuleGroups(),
                new CompileUtil.Callback<ModuleGroup, N2oStandardHeader.ModuleGroup>() {

                    @Override
                    public ModuleGroup compile(N2oStandardHeader.ModuleGroup moduleGroup) {
                        Map<String, CompiledHeaderModule> moduleMap = new LinkedHashMap<>();
                        String groupId = castDefault(moduleGroup.getId(), "g" + groupOrder++);
                        initModules(groupId, moduleGroup.getModules(), compiler, moduleMap);
                        if (!moduleMap.isEmpty()) {
                            return new ModuleGroup(groupId, moduleGroup.getName(), moduleMap);
                        } else {
                            return null;
                        }
                    }
                }, this.moduleGroups
        );
        if (header.getModules() == null || header.getModules().length == 0) return;
        Map<String, CompiledHeaderModule> moduleMap = new LinkedHashMap<>();
        String groupId = String.valueOf("main");
        initModules(groupId, header.getModules(), compiler, moduleMap);
        this.moduleGroups.put(groupId, new ModuleGroup(groupId, null, moduleMap));
    }

    private void initModules(final String groupId, N2oHeaderModule[] modules, final N2oCompiler compiler,
                             Map<String, CompiledHeaderModule> moduleMap) {
        moduleOrder = 0;
        arrayToMap(modules, n2oBaseHeaderModule -> {
            moduleOrder++;
            CompiledHeaderModule headerModule = null;
            try {
                headerModule = compiler
                        .registerAndCompile(new HeaderModuleContext(getCompileContext(), n2oBaseHeaderModule,
                                        getId(), groupId, moduleOrder)
                        );
            } catch (ReferentialIntegrityViolationException e) {
                log.debug("module not found", e);
                return null;
            } catch (Exception e) {
                log.warn("module cannot load", e);
                return null;
            }
            return headerModule;
        }, moduleMap);
    }


    @Override
    public String getSrc() {
        return "n2o/plugins/top-menu/template/header.menu";
    }

    @Override
    public Class<N2oStandardHeader> getSourceClass() {
        return N2oStandardHeader.class;
    }

    public CompiledHeaderModule.Page get(String pageId) {
        return getPageMap().get(pageId);
    }

    public CompiledHeaderModule getModule(String moduleId) {
        for (ModuleGroup moduleGroup : moduleGroups.values()) {
            for (CompiledHeaderModule module : moduleGroup.getModules()) {
                if (module.getSourceId().equals(moduleId))
                    return module;
            }
        }
        return null;
    }


    public static class ModuleGroup implements IdAware, Serializable {
        private String id;
        private String name;
        private Map<String, CompiledHeaderModule> modules = new LinkedHashMap<>();

        public ModuleGroup(String id, String name,
                           Map<String, CompiledHeaderModule> modules) {
            this.id = id;
            this.name = name;
            this.modules = modules;
        }

        @JsonIgnore
        public Collection<CompiledHeaderModule> getModules() {
            return modules.values();
        }

        public Map<String, CompiledHeaderModule> getModulesMap() {
            return modules;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }


    }

    @JsonIgnore
    public Collection<CompiledHeaderModule> getAllModules() {
        List<CompiledHeaderModule> res = new ArrayList<>();
        for (ModuleGroup moduleGroup : getModuleGroups()) {
            res.addAll(moduleGroup.getModules());
        }
        return res;
    }

    @JsonIgnore
    public Collection<ModuleGroup> getModuleGroups() {
        return moduleGroups.values();
    }

    public Map<String, ModuleGroup> getModuleGroupsMap() {
        return moduleGroups;
    }

    public CompiledUserMenu getUserMenu() {
        return userMenu;
    }

    private List<CompiledHeaderModule.Page> getAllPages() {
        List<CompiledHeaderModule.Page> res = new ArrayList<>();
        for (CompiledHeaderModule compiledHeaderModule : getAllModules()) {
            res.addAll(compiledHeaderModule.getAllPages());
        }
        return res;
    }

    @Override
    public void removePage(String pageId) {
        List<CompiledHeaderModule> forRemove = new ArrayList<>();
        for (CompiledHeaderModule compiledHeaderModule : getAllModules()) {
            compiledHeaderModule.removePage(pageId);
            if (compiledHeaderModule.getAllPages().size() < 1 && !compiledHeaderModule.isOuterModule())
                forRemove.add(compiledHeaderModule);
        }
        for (CompiledHeaderModule compiledHeaderModule : forRemove) {
            removeModule(compiledHeaderModule.getId());
        }
        if (pageId.equals(sourceMainPage)) {
            sourceMainPage = null;
        }
        if (pageId.equals(mainPage)) {
            initMainPage();
        }
    }

    @Override
    public Set<String> getAllPageIds() {
        Set<String> ids = new HashSet<>();
        if (mainPage != null) {
            ids.add(mainPage);
        }
        if (moduleGroups != null) {
            moduleGroups.values().stream().filter(mg -> mg.getModules()!= null).forEach(mg -> {
                mg.getModules().forEach(m -> ids.addAll(m.getAllPages().stream().map(p -> p.getId()).collect(Collectors.toSet())));
            });
        }
        return ids;
    }

    public void removeModule(String moduleId) {
        List<ModuleGroup> forRemove = new ArrayList<>();
        for (ModuleGroup moduleGroup : getModuleGroups()) {
            moduleGroup.getModulesMap().remove(moduleId);
            if (moduleGroup.getModulesMap().size() < 1) forRemove.add(moduleGroup);
        }
        for (ModuleGroup moduleGroup : forRemove) {
            removeModuleGroup(moduleGroup.getId());
        }
        initMainPage();
    }

    public void removeModuleGroup(String groupId) {
        getModuleGroupsMap().remove(groupId);
        initMainPage();
    }

    public UserContextStructure getUserContextStructure() {
        return userContextStructure;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public Map<Integer, Map<String, Object>> getContextActionParameters() {
        return contextActionParameters;
    }

    public String getSrcNavigation() {
        return srcNavigation;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
        if (userContext != null && userContext.getContextList() != null) {
            contextActionParameters = new HashMap<>();
            for (RootContextNode node : userContext.getContextList()) {
                if (node.getNodes() != null) for (ContextNode subNode : node.getNodes()) {
                    contextActionParameters.put(subNode.getId(), subNode.getActionParameters());
                }
            }
        }
    }

    public static class UserContext implements Serializable {
        private List<RootContextNode> contextList;
        private List<ContextLabelNode> contextLabel;

        public UserContext(List<RootContextNode> contextList, List<ContextLabelNode> contextLabel) {
            this.contextList = contextList;
            this.contextLabel = contextLabel;
        }

        public List<RootContextNode> getContextList() {
            return contextList;
        }

        public List<ContextLabelNode> getContextLabel() {
            return contextLabel;
        }

        public List<ContextLabelNode> getReverseContextLabel() {
            ArrayList<ContextLabelNode> reverseList = new ArrayList<>(contextLabel);
            Collections.reverse(reverseList);
            return reverseList;
        }
    }

    public static class ContextLabelNode implements Serializable{
        private String label;

        public ContextLabelNode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class RootContextNode extends ContextNode {
        private String hint;
        private List<ContextNode> nodes;

        public RootContextNode() {
            setLevel(0);
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public List<ContextNode> getNodes() {
            return nodes;
        }

        public void setNodes(List<ContextNode> nodes) {
            this.nodes = nodes;
        }
    }

    public static class ContextNode implements Serializable{
        private int id;
        private String unitName;
        private int level;
        private Map<String, Object> actionParameters;
        private boolean on;

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Map<String, Object> getActionParameters() {
            return actionParameters;
        }

        public void setActionParameters(Map<String, Object> actionParameters) {
            this.actionParameters = actionParameters;
        }
    }

    public static class CompiledUserMenu extends N2oStandardHeader.UserMenu {
        private String username;

        public CompiledUserMenu(String src, String usernameContext, String username) {
            super(src, usernameContext);
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}



