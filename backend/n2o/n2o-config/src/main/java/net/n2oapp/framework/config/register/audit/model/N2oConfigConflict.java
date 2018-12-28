package net.n2oapp.framework.config.register.audit.model;

/**
 * @author dfirstov
 * @since 12.08.2015
 */
public class N2oConfigConflict extends N2oConfigCommit {
    private String conflictContent;
    private String mergeContent;
    private String parentContent;

    public String getConflictContent() {
        return conflictContent;
    }

    public void setConflictContent(String conflictContent) {
        this.conflictContent = conflictContent;
    }

    public String getMergeContent() {
        return mergeContent;
    }

    public void setMergeContent(String mergeContent) {
        this.mergeContent = mergeContent;
    }

    public String getParentContent() {
        return parentContent;
    }

    public void setParentContent(String parentContent) {
        this.parentContent = parentContent;
    }
}
