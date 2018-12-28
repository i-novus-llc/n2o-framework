package net.n2oapp.framework.standard.header.model.local;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.n2oapp.framework.api.exception.N2oMetadataException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.local.AbstractCompiledMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.OutOfRangeException;
import net.n2oapp.framework.standard.header.context.ModulePageContext;
import net.n2oapp.framework.standard.header.model.global.N2oBaseHeaderModule;

import java.io.Serializable;
import java.util.*;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.arrayToMap;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * User: operhod
 * Date: 17.02.14
 * Time: 12:00
 */
public class CompiledHeaderModule extends AbstractCompiledMetadata<N2oBaseHeaderModule, HeaderModuleContext> implements IdAware {
    private String outerUrl;
    private String name;
    //страница, заданная главной при открытии модуля
    private String sourceMainPage;
    //страница которая будет главной по факту, если нет sourceMainPage, или она вырезана через removePage
    private String mainPage;
    private Map<String, Item> items = new LinkedHashMap<>();
    private int pageOrder;
    private int itemOrder;
    private String sourceId;
    private boolean outerModule;

    public String getSourceId() {
        return sourceId;
    }

    @Override
    public void compile(N2oBaseHeaderModule source, N2oCompiler compiler, HeaderModuleContext context) {
        super.compile(source, compiler, context);
        this.sourceId = source.getSourceId();
        if (sourceId == null && context != null) {
            this.sourceId = context.getMetadataId();
        }
        this.name = castDefault(source.getName(), getId());
        this.outerUrl = source.getUrl();
        outerModule = source.getUrl() != null;
        initItems(source.getItems(), compiler);
        initSourceMainPage(source, compiler);
        initMainPage();
    }

    private void initSourceMainPage(N2oBaseHeaderModule source, N2oCompiler compiler) {
        if (source.getMainPage() == null)
            return;
        try {
            ModulePageContext context = new ModulePageContext(getCompileContext(), getSourceId(), null, -1, source.getMainPage());
            sourceMainPage = compiler.register(context);
        } catch (OutOfRangeException e) {
            throw new IllegalStateException("it is impossible");
        }
    }

    private void initItems(N2oBaseHeaderModule.Item[] items, final N2oCompiler compiler) {
        if (items == null) return;
        itemOrder = 0;
        arrayToMap(items, item -> {
            if (item instanceof N2oBaseHeaderModule.Space)
                return createSpace((N2oBaseHeaderModule.Space) item, itemOrder++, compiler);
            else if (item instanceof N2oBaseHeaderModule.Page)
                return createPage((N2oBaseHeaderModule.Page) item, null, itemOrder++, compiler);
            return null;
        }, this.items);
    }

    private Page createPage(N2oBaseHeaderModule.Page page, String spaceId, int order, N2oCompiler compiler) {
        try {
            ModulePageContext context = new ModulePageContext(getCompileContext(), getSourceId(), spaceId, order, page.getId());
            String contextualPageId = compiler.register(context);
            return new Page(contextualPageId, page.getId(), castDefaultPageName(page.getName(), page), getSourceId(), getId());
        } catch (OutOfRangeException e) {
            throw new IllegalStateException("it is impossible");
        }
    }

    private String castDefaultPageName(String name, N2oBaseHeaderModule.Page page) {
        if (name != null)
            return name;
        else
            return compilePageName(page);

    }

    private String compilePageName(N2oBaseHeaderModule.Page page) {
        try {
            if (!CompilerHolder.get().isExists(page.getId(), N2oPage.class))
                return page.getId();
            N2oPage sourcePage = getCompiler().getGlobal(page.getId(), N2oPage.class);
            if (sourcePage.getName() != null)
                return sourcePage.getName();
            N2oObject object = sourcePage.getObjectId() != null ?
                    getCompiler().getGlobal(sourcePage.getObjectId(), N2oObject.class) : null;
            if (object != null && object.getName() != null)
                return object.getName();
            return page.getId();
        } catch (Exception e) {
            if (e instanceof N2oMetadataException) {
                SourceMetadata sourceMetadata = ((N2oMetadataException) e).getSourceMetadata();
                if (sourceMetadata instanceof NameAware)
                    return ((NameAware) sourceMetadata).getName();
            }
            return page.getId();
        }
    }

    private Item createSpace(N2oBaseHeaderModule.Space space, int order, N2oCompiler compiler) {
        pageOrder = 0;
        String spaceId = castDefault(space.getId(), "space" + order);
        HashMap<String, Page> pages = new LinkedHashMap<>();
        String[] contextualMainPageHolder = {null};
        arrayToMap(space.getPages(), page -> {
            final Page compilePage = createPage(page, spaceId, pageOrder++, compiler);
            if (page.getId() != null && page.getId().equals(space.getMainPage())) {
                contextualMainPageHolder[0] = compilePage.getId();
            }
            return compilePage;
        }, pages);
        String spaceMainPage = castDefault(contextualMainPageHolder[0], pages.values().iterator().next().getId());
        return new Space(castDefault(space.getId(), spaceId), castDefault(space.getName(),
                spaceId), spaceMainPage, pages);
    }


    @Override
    public Class<N2oBaseHeaderModule> getSourceClass() {
        return N2oBaseHeaderModule.class;
    }

    @Override
    public final Class<? extends CompiledMetadata> getCompiledBaseClass() {
        return CompiledHeaderModule.class;
    }

    public String getUrl() {
        return outerModule ? outerUrl : "#" + mainPage;
    }

    public String getName() {
        return name;
    }

    public String getMainPage() {
        return mainPage;
    }

    protected void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public boolean isOuterModule() {
        return outerModule;
    }

    public Map<String, Item> getItemsMap() {
        return items;
    }

    @JsonIgnore
    public Collection<Item> getItems() {
        return items.values();
    }

    @JsonIgnore
    public Collection<Page> getAllPages() {
        List<Page> res = new ArrayList<>();
        for (Item item : getItems()) {
            if (item instanceof Page) {
                res.add((Page) item);
            } else if (item instanceof Space) {
                res.addAll(((Space) item).getPages());
            }
        }
        return res;
    }


    public void removePage(String pageId) {
        for (Item item : new ArrayList<>(getItems())) {
            if (item instanceof Space) {
                Space space = (Space) item;
                space.removePage(pageId);
                if (space.getPages().size() == 0) getItemsMap().remove(item.getId());
            } else if (pageId.equals(item.getId())) {
                getItemsMap().remove(item.getId());
            }
        }
        if (pageId.equals(sourceMainPage))
            sourceMainPage = null;
        if (pageId.equals(mainPage)) {
            mainPage = null;
            initMainPage();
        }
    }

    private void initMainPage() {
        if (isOuterModule())
            return;//у внешнего модуля нет n2o главной страницы
        if (sourceMainPage != null) {
            //если есть заданная главная n2o страница, то она и будет фактической
            setMainPage(sourceMainPage);
            return;
        }
        if (getItems().isEmpty())
            return; //нет ни одной страницы в модуле, модуль будет вырезан в дальнейшем
        //вычисляем первую попавшуюся главную страницу модуля
        Item item = getItems().iterator().next();
        if (item instanceof Space) {
            String mainPage = ((Space) item).getMainPage();
            setMainPage(mainPage);
        } else {
            setMainPage(item.getId());
        }
    }

    public abstract static class Item extends N2oBaseHeaderModule.Item implements Serializable {
        public abstract String getType();
    }

    public static class Space extends Item {
        private String mainPage;
        private Map<String, Page> pages;

        public Space(String id, String name, String mainPage,
                     Map<String, Page> pages) {
            this.id = id;
            this.name = name;
            this.mainPage = mainPage;
            if (this.mainPage == null)
                setMainPage();
            this.pages = pages;
        }

        private void setMainPage() {
            if (pages != null)
                for (Page page : pages.values()) {
                    mainPage = page.getId();
                }
        }

        public Map<String, Page> getPageMap() {
            return pages;
        }

        @JsonIgnore
        public Collection<Page> getPages() {
            return pages.values();
        }

        public void removePage(String pageId) {
            pages.remove(pageId);
            if (pageId.equals(mainPage))
                setMainPage();
        }

        public String getMainPage() {
            return mainPage;
        }

        public void setMainPage(String mainPage) {
            this.mainPage = mainPage;
        }

        @Override
        public String getType() {
            return "space";
        }
    }

    public static class Page extends Item {
        private String sourcePageId;
        private String moduleId;
        private String contextualModuleId;

        public Page(String id, String sourcePageId, String name, String sourceModuleId, String contextualModuleId) {
            this.id = id;
            this.sourcePageId = sourcePageId;
            this.name = name;
            this.moduleId = sourceModuleId;
            this.contextualModuleId = contextualModuleId;
        }

        public String getModuleId() {
            return moduleId;
        }

        public String getContextualModuleId() {
            return contextualModuleId;
        }

        public String getUrl() {
            return "#" + getId();
        }

        public String getSourcePageId() {
            return sourcePageId;
        }

        @Override
        public String getType() {
            return "page";
        }
    }

}
