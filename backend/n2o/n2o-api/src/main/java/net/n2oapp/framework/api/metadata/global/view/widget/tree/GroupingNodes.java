package net.n2oapp.framework.api.metadata.global.view.widget.tree;

import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dfirstov
 * @since 10.02.2015
 */
public class GroupingNodes implements Serializable {
    public List<Node> nodes = new ArrayList<>();
    private String queryId;
    private String valueFieldId;
    private String masterFieldId;
    private String detailFieldId;
    private N2oPreFilter[] preFilters;
    private String searchFieldId;

    public N2oPreFilter[] getPreFilters() {
        return preFilters;
    }

    public void setPreFilters(N2oPreFilter[] preFilters) {
        this.preFilters = preFilters;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getValueFieldId() {
        return valueFieldId;
    }

    public void setValueFieldId(String valueFieldId) {
        this.valueFieldId = valueFieldId;
    }

    public String getMasterFieldId() {
        return masterFieldId;
    }

    public void setMasterFieldId(String masterFieldId) {
        this.masterFieldId = masterFieldId;
    }

    public String getDetailFieldId() {
        return detailFieldId;
    }

    public void setDetailFieldId(String detailFieldId) {
        this.detailFieldId = detailFieldId;
    }

    public String getSearchFieldId() {
        return searchFieldId;
    }

    public void setSearchFieldId(String searchFieldId) {
        this.searchFieldId = searchFieldId;
    }

    public static class Node implements Serializable {
        private String valueFieldId;
        private String labelFieldId;
        private List<Node> nodes = new ArrayList<>();
        private String icon;
        private Boolean enabled;

        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public String getValueFieldId() {
            return valueFieldId;
        }

        public void setValueFieldId(String valueFieldId) {
            this.valueFieldId = valueFieldId;
        }

        public String getLabelFieldId() {
            return labelFieldId;
        }

        public void setLabelFieldId(String labelFieldId) {
            this.labelFieldId = labelFieldId;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Deprecated
        public Boolean getCanResolved() {
            return enabled;
        }

        @Deprecated
        public void setCanResolved(Boolean canResolved) {
            this.enabled = canResolved;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}