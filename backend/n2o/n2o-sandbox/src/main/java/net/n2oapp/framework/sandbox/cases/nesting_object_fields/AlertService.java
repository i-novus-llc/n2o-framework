package net.n2oapp.framework.sandbox.cases.nesting_object_fields;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlertService {

    public void createAlerts(DataList messages) {
        DataSet[] datasetArr = messages.toArray(new DataSet[0]);
        List<MessageInfo> messagesList = new ArrayList<>();
        for (DataSet dataSet : datasetArr) {
            MessageInfo message = new MessageInfo();
            message.setColor(dataSet.getDataSet("color").getString("name"));
            message.setTitle(dataSet.getString("title"));
            message.setText(dataSet.getString("text"));
            message.setPlacement(dataSet.getDataSet("placement").getString("name"));
            messagesList.add(message);
        }
        throw new SandboxAlertMessagesException(messagesList);
    }
}
