package net.n2oapp.framework.api.exception;

/**
 * Ошибка бизнес логики
 * @deprecated
 * @see N2oUserException
 */
@Deprecated
public class N2oBusinessException extends N2oException implements UserMessageAware {

    protected String userSummaryMessage;
    protected String userDetailedMessage;

    public N2oBusinessException(String userSummaryMessage) {
        this(userSummaryMessage, null, null);
    }

    public N2oBusinessException(String userSummaryMessage, String field) {
        this(userSummaryMessage, null, field);
    }

    public N2oBusinessException(String userSummaryMessage, String userDetailedMessage, String field) {
        super();
        this.userSummaryMessage = userSummaryMessage;
        this.userDetailedMessage = userDetailedMessage;
        setField(field);
    }

    public N2oBusinessException(String userSummaryMessage, String userDetailedMessage, String techMessage, String field) {
        super(techMessage);
        this.userSummaryMessage = userSummaryMessage;
        this.userDetailedMessage = userDetailedMessage;
        setField(field);
    }

    @Override
    public String getSummary() {
        return userSummaryMessage;
    }

    @Override
    public String getDetailedMessage() {
        return userDetailedMessage;
    }

    public int getHttpStatus() {
        return 400;
    }


}
