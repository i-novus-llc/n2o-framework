package net.n2oapp.framework.api.metadata.global.view.widget.tree;

import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.io.Serializable;

@Deprecated
public class InheritanceNodes implements Serializable {
    private String parentFieldId;
    private String labelFieldId;
    private String hasChildrenFieldId;
    private String queryId;
    private String iconFieldId;
    private String valueFieldId;
    private String masterFieldId;
    private String detailFieldId;
    private String searchFilterId;
    private String enabledFieldId;
    private N2oPreFilter[] preFilters;

    public N2oPreFilter[] getPreFilters() {
        return preFilters;
    }

    public void setPreFilters(N2oPreFilter[] preFilters) {
        this.preFilters = preFilters;
    }

    public String getParentFieldId() {
        return parentFieldId;
    }

    public void setParentFieldId(String parentFieldId) {
        this.parentFieldId = parentFieldId;
    }

    public String getLabelFieldId() {
        return labelFieldId;
    }

    public void setLabelFieldId(String labelFieldId) {
        this.labelFieldId = labelFieldId;
    }

    public String getHasChildrenFieldId() {
        return hasChildrenFieldId;
    }

    public void setHasChildrenFieldId(String hasChildrenFieldId) {
        this.hasChildrenFieldId = hasChildrenFieldId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getIconFieldId() {
        return iconFieldId;
    }

    public void setIconFieldId(String iconFieldId) {
        this.iconFieldId = iconFieldId;
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

    public String getSearchFilterId() {
        return searchFilterId;
    }

    public void setSearchFilterId(String searchFilterId) {
        this.searchFilterId = searchFilterId;
    }

    @Deprecated
    public String getCanResolvedFieldId() {
        return enabledFieldId;
    }

    @Deprecated
    public void setCanResolvedFieldId(String canResolvedFieldId) {
        this.enabledFieldId = canResolvedFieldId;
    }

    public String getEnabledFieldId() {
        return enabledFieldId;
    }

    public void setEnabledFieldId(String enabledFieldId) {
        this.enabledFieldId = enabledFieldId;
    }

    public boolean isEmpty() {
        return parentFieldId == null && labelFieldId == null && hasChildrenFieldId == null &&
                queryId == null && iconFieldId == null && valueFieldId == null &&
                masterFieldId == null && detailFieldId == null && searchFilterId == null &&
                enabledFieldId == null && (preFilters == null || preFilters.length < 1);
    }
}