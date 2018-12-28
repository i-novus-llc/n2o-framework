package net.n2oapp.framework.engine.rest;

import net.n2oapp.framework.api.exception.UserMessageAware;

/**
 * @author iryabov
 * @since 19.10.2015
 */
public class N2oRestBusinessException extends N2oRestException implements UserMessageAware {
    protected String userSummaryMessage;
    protected String userDetailedMessage;

    public N2oRestBusinessException(String userSummaryMessage, String userDetailedMessage) {
        super();
        this.userSummaryMessage = userSummaryMessage;
        this.userDetailedMessage = userDetailedMessage;
        setHttpStatus(400);
    }

    public N2oRestBusinessException(String userSummaryMessage, String userDetailedMessage, String stackTrace) {
        super(stackTrace, 400);
        this.userSummaryMessage = userSummaryMessage;
        this.userDetailedMessage = userDetailedMessage;
    }

    @Override
    public String getSummary() {
        return userSummaryMessage;
    }

    @Override
    public String getDetailedMessage() {
        return userDetailedMessage;
    }
}
