package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 17:02
 */
public class SerializedQuery implements Serializable {
    private String itemsQueryTemplate;
    private String countQueryTemplate;
    private String idsQueryTemplate;
    private String alias;
    private CriteriaConstructorResult criteriaResult;

    public String getItemsQueryTemplate() {
        return itemsQueryTemplate;
    }

    public void setItemsQueryTemplate(String itemsQueryTemplate) {
        this.itemsQueryTemplate = itemsQueryTemplate;
    }

    public String getCountQueryTemplate() {
        return countQueryTemplate;
    }

    public void setCountQueryTemplate(String countQueryTemplate) {
        this.countQueryTemplate = countQueryTemplate;
    }

    public String getIdsQueryTemplate() {
        return idsQueryTemplate;
    }

    public void setIdsQueryTemplate(String idsQueryTemplate) {
        this.idsQueryTemplate = idsQueryTemplate;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CriteriaConstructorResult getCriteriaResult() {
        return criteriaResult;
    }

    public void setCriteriaResult(CriteriaConstructorResult criteriaResult) {
        this.criteriaResult = criteriaResult;
    }
}
