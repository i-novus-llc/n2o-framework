package net.n2oapp.framework.config.audit.service;

/**
 * @author iryabov
 * @since 23.03.2017
 */
public class N2oHistoryCriteria extends N2oCommitCriteria {
    private String localPath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
