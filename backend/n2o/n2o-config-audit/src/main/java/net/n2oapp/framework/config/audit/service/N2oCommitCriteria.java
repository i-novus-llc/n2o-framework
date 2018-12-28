package net.n2oapp.framework.config.audit.service;

import net.n2oapp.criteria.api.Criteria;

import java.util.Date;
import java.util.List;

/**
 * История изменения конфигураций
 */
public class N2oCommitCriteria extends Criteria {
    private String id;
    private Date dateBegin;
    private Date dateEnd;
    private String message;
    private List<String> changeType;
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getChangeType() {
        return changeType;
    }

    public void setChangeType(List<String> changeType) {
        this.changeType = changeType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
