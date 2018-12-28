package net.n2oapp.framework.api.metadata.event.action;

import net.n2oapp.framework.api.metadata.control.QueryUnit;

import java.io.Serializable;

/**
 * Событие update-model
 */
public class UpdateModelAction extends QueryUnit implements  N2oAction {
    private static final String DEFAULT_SRC = "";
    //todo:добавить src
    private String id;
    private Boolean context;
    private Target target;
    private String valueFieldId;
    private String targetFieldId;
    private String queryId;
    private String masterFieldId;
    private String detailFieldId;
    private String namespaceUri;
    private Boolean validate;
    private String src;

    public String getNamespaceUri() {
        return namespaceUri;
    }


    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public UpdateModelAction() {
        setSrc(DEFAULT_SRC);
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Boolean getContext() {
        return context;
    }

    public void setContext(Boolean context) {
        this.context = context;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getValueFieldId() {
        return valueFieldId;
    }

    public void setValueFieldId(String valueFieldId) {
        this.valueFieldId = valueFieldId;
    }

    public String getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(String targetFieldId) {
        this.targetFieldId = targetFieldId;
    }

    @Override
    public String getQueryId() {
        return queryId;
    }

    @Override
    public void setQueryId(String queryId) {
        this.queryId = queryId;
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

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public enum Target implements Serializable {
        field, form
    }

    @Override
    public String getSrc() {
        return src;
    }
}
