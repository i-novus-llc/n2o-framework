package net.n2oapp.framework.engine.validation;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.engine.validation.engine.FailInfo;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.engine.validation.engine.info.ObjectValidationInfo;
import net.n2oapp.framework.engine.validation.engine.info.QueryValidationInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation.ServerMomentEnum.*;

/**
 * Процессинговый модуль валидации
 */
public class N2oValidationModule implements DataProcessing {

    private ValidationProcessor processor;
    private AlertMessageBuilder alertMessageBuilder;

    public N2oValidationModule(ValidationProcessor processor, AlertMessageBuilder alertMessageBuilder) {
        this.processor = processor;
        this.alertMessageBuilder = alertMessageBuilder;
    }

    @Override
    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), BEFORE_OPERATION);
        prepareResponse(fails, requestInfo, responseInfo);
    }

    @Override
    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet, N2oException exception) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), AFTER_FAIL_OPERATION);
        prepareResponse(fails, requestInfo, responseInfo);
        DataProcessing.super.processActionError(requestInfo, responseInfo, dataSet, exception);
    }

    @Override
    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        List<FailInfo> fails = processor.validate(buildInfo(requestInfo, dataSet), AFTER_SUCCESS_OPERATION);
        prepareResponse(fails, requestInfo, responseInfo);
    }

    @Override
    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
        if (requestInfo.isValidationEnable() && requestInfo.getSize() == 1) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), AFTER_FAIL_QUERY);
            prepareResponse(fails, requestInfo, responseInfo);
        }
        DataProcessing.super.processQueryError(requestInfo, responseInfo, exception);
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.isValidationEnable()) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, requestInfo.getData()), BEFORE_QUERY);
            prepareResponse(fails, requestInfo, responseInfo);
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        final Collection<DataSet> list = page.getCollection();
        if (requestInfo.isValidationEnable() && !list.isEmpty()) {
            List<FailInfo> fails = processor.validate(buildInfo(requestInfo, list.iterator().next()), AFTER_SUCCESS_QUERY);
            prepareResponse(fails, requestInfo, responseInfo);
        }
    }

    private ObjectValidationInfo buildInfo(ActionRequestInfo<DataSet> requestInfo, DataSet dataSet) {
        return new ObjectValidationInfo(
                requestInfo.getObject().getId(),
                requestInfo.getOperation().getValidationList(),
                dataSet,
                requestInfo.getMessagesForm()
        );
    }

    private QueryValidationInfo buildInfo(QueryRequestInfo requestInfo, DataSet inDataSet) {
        DataSet dataSet = inDataSet != null ? inDataSet : requestInfo.getData();
        DataSet result = new DataSet();
        Map<String, String> paramsMap = requestInfo.getQuery().getParamToFilterIdMap();

        for (Map.Entry<String, Object> entry : dataSet.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (paramsMap.containsKey(key)) {
                String mappedKey = paramsMap.get(key);
                if (DataSet.isSpreadKey(mappedKey) && !(value instanceof Collection)) {
                    result.put(mappedKey, Collections.singletonList(value));
                } else {
                    result.put(mappedKey, value);
                }
            } else {
                result.put(key, value);
            }
        }
        return new QueryValidationInfo(
                requestInfo.getQuery().getObject(),
                requestInfo.getQuery().getValidations(),
                result,
                requestInfo.getMessagesForm()
        );
    }

    private void prepareResponse(List<FailInfo> fails, RequestInfo requestInfo, ResponseInfo responseInfo) {
        for (FailInfo fail : fails) {
            ResponseMessage message = responseInfo.constructMessage(requestInfo, fail.getSeverity(), alertMessageBuilder);
            message.setText(fail.getMessage());
            message.setTitle(fail.getMessageTitle());
            message.setField(fail.getFieldId());
            responseInfo.addMessage(message);
        }
    }
}
