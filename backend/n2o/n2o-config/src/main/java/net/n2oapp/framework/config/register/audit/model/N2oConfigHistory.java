package net.n2oapp.framework.config.register.audit.model;

import java.util.List;

/**
 * Изменение в git репозитории, в случае если заполнено previousContent и diff - это изменение одно файла
 * иначе - это информация по одному коммиту
 */
public class N2oConfigHistory extends N2oConfigCommit {
    private String localPath;
    private List<String> previousContent;
    private String diff;

    public List<String> getPreviousContent() {
        return previousContent;
    }

    public void setPreviousContent(List<String> previousContent) {
        this.previousContent = previousContent;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
