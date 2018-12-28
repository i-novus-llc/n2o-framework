package net.n2oapp.framework.standard.header.model.global;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.local.N2oMetadataMerger;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 17.02.14
 * Time: 9:59
 */
public class N2oBaseHeaderModule extends N2oHeaderModule<N2oBaseHeaderModule> implements IdAware {
    private String url;
    private String name;
    private String mainPage;
    private Item[] items;
    private String sourceId;
    private String refId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public String getRefId() {
        return refId;
    }

    @Override
    public N2oMetadataMerger<N2oBaseHeaderModule> getMerger() {
        return null;
    }

    public static class Item implements IdAware, Serializable {
        protected String id;
        protected String name;

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

    public static class Space extends Item {
        protected String mainPage;
        protected Page[] pages;

        public Page[] getPages() {
            return pages;
        }

        public void setPages(Page[] pages) {
            this.pages = pages;
        }

        public String getMainPage() {
            return mainPage;
        }

        public void setMainPage(String mainPage) {
            this.mainPage = mainPage;
        }
    }

    public static class Page extends Item {
    }

}
