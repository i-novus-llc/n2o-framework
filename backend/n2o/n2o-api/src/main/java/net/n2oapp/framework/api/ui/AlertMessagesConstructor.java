package net.n2oapp.framework.api.ui;

import java.util.List;

public interface AlertMessagesConstructor {

    List<ResponseMessage> constructMessages(Exception exception);

    List<ResponseMessage> constructMessages(Exception exception, RequestInfo requestInfo);

}
