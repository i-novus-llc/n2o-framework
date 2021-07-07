package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;

import java.util.*;

/**
 * Объект ответа на запрос за данными
 */
@Getter
@Setter
public class ResponseInfo  {
    private N2oDialog dialog;
    private List<ResponseMessage> messageList;
    private Map<String, Object> attributes;
    private boolean stackedMessages = false;
    private AlertMessageBuilder alertMessageBuilder;


    public ResponseMessage constructMessage(RequestInfo requestInfo) {
        return alertMessageBuilder.constructMessage(requestInfo);
    }

    public void addMessage(ResponseMessage message) {
        if (messageList == null)
            messageList = new ArrayList<>();
        messageList.add(message);
    }

    public void addAttribute(String name, Object value) {
        if (attributes == null)
            attributes = new LinkedHashMap<>();
        attributes.put(name, value);
    }

    public List<ResponseMessage> getMessageList() {
        return messageList != null ? messageList : Collections.emptyList();
    }


    public void prepare(DataSet dataset) {
        getMessageList().forEach(m -> {
            String msg = StringUtils.resolveLinks(m.getText(), dataset);
            m.setText(msg);
        });
    }
}
