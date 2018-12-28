package net.n2oapp.framework.config.audit.service;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * Список веток
 */
public class BranchCriteria extends Criteria implements IdAware {
    private String id;
    private String name;
    private Boolean isRemote;

    @Override
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

    public Boolean getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(Boolean isRemote) {
        this.isRemote = isRemote;
    }
}
