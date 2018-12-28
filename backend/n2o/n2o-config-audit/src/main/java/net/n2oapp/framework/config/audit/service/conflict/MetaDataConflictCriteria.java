package net.n2oapp.framework.config.audit.service.conflict;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * @author dfirstov
 * @since 12.08.2015
 */
public class MetaDataConflictCriteria extends Criteria implements IdAware {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getN2oClass() {
        return n2oClass;
    }

    public void setN2oClass(String n2oClass) {
        this.n2oClass = n2oClass;
    }

    public String getConflictContent() {
        return conflictContent;
    }

    public void setConflictContent(String conflictContent) {
        this.conflictContent = conflictContent;
    }

    public String getConflictResult() {
        return conflictResult;
    }

    public void setConflictResult(String conflictResult) {
        this.conflictResult = conflictResult;
    }

    public String getConflictLeft() {
        return conflictLeft;
    }

    public void setConflictLeft(String conflictLeft) {
        this.conflictLeft = conflictLeft;
    }

    public String getConflictRight() {
        return conflictRight;
    }

    public void setConflictRight(String conflictRight) {
        this.conflictRight = conflictRight;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    private String id;
    private String code;
    private String n2oClass;
    private String conflictContent;
    private String conflictResult;
    private String conflictLeft;
    private String conflictRight;
    private String localPath;
}
