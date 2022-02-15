package net.n2oapp.framework.api.metadata.control.list;

/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 19:00
 */
public class N2oClassifier extends N2oSingleListFieldAbstract {
    private String queryId;
    private String valueFieldId;
    private String labelFieldId;
    private Boolean searchAsYouType;
    private Boolean wordWrap;
    private Mode mode;

    public N2oClassifier(String id) {
        setId(id);
    }

    public N2oClassifier() {

    }

    public String getQueryId() {
        return queryId;
    }

    public String getValueFieldId() {
        return valueFieldId;
    }

    public String getLabelFieldId() {
        return labelFieldId;
    }

    public Boolean getSearchAsYouType() {
        return searchAsYouType;
    }

    public void setSearchAsYouType(Boolean searchAsYouType) {
        this.searchAsYouType = searchAsYouType;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Boolean getWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(Boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public enum Mode {
        quick, advance, combined
    }
}
