package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import net.n2oapp.framework.engine.exception.N2oUniqueRequestNotFoundException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;

import static net.n2oapp.framework.api.criteria.N2oPreparedCriteria.simpleCriteriaOneRecord;

/**
 * Контроллер получения выборки данных
 */
@Controller
public class QueryController extends GetController {

    private static final String INSERTED_ROW = "$insertedRow";
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    public QueryController(DataProcessingStack dataProcessingStack,
                           QueryProcessor queryProcessor,
                           SubModelsProcessor subModelsProcessor,
                           AlertMessageBuilder messageBuilder,
                           MetadataEnvironment environment) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, environment);
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        try {
            CollectionPage<DataSet> collectionPage = executeQuery(requestInfo, responseInfo);
            insertSelectedRow(requestInfo, responseInfo, collectionPage);
            return new GetDataResponse(collectionPage, responseInfo, requestInfo.getSuccessAlertWidgetId());
        } catch (N2oException e) {
            String widgetId = requestInfo.getFailAlertWidgetId() == null
                    ? requestInfo.getMessagesForm()
                    : requestInfo.getFailAlertWidgetId();
            GetDataResponse response = new GetDataResponse(getMessageBuilder().buildMessages(e, requestInfo), widgetId);
            response.setStatus(e.getHttpStatus());
            logger.error("Error response " + response.getStatus() + " " + e.getSeverity(), e);
            return response;
        }
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.query;
    }


    private void insertSelectedRow(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> pageData) {
        if (requestInfo.getSelectedId() == null) return;//selectedId не пришёл, значит не нужно его вставлять
        if (requestInfo.getSize() == 1)
            return;//запрашивается выборка с одной записью, в этом случае selectedRow не можем вставлять

        Collection<DataSet> list = pageData.getCollection();
        if (findSelectedRow(requestInfo, list))
            return;//selectedRow присутствует в выборке, не нужно её искусственно вставлять
        DataSet selectedRow = executeSelectedRow(requestInfo, responseInfo);
        if (selectedRow == null) return;//если выделенная запись не была найдена по id, то ничего не вставляем

        Collection<DataSet> modifiedList = new ArrayList<>();
        modifiedList.add(selectedRow);//вставляем первой записью в выборке selectedRow
        if (!list.isEmpty()) {
            Collection<DataSet> trueList;
            if (list.size() >= requestInfo.getCriteria().getSize()) {
                trueList = new ArrayList<>(list).subList(0, requestInfo.getCriteria().getSize() - 1);//последнюю запись удаляем из выборки, чтобы size был правильным
            } else {
                trueList = list;//если выборка на последнем page, то она может быть меньше size, и удалять последний элемент в этом случае не нужно
            }
            modifiedList.addAll(trueList);//вставляем запрошенную выборку
        }
        pageData.setCollection(modifiedList);
    }

    private DataSet executeSelectedRow(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        QueryRequestInfo selectedRowReqInfo = createQueryRequestInfoForSelectedRow(requestInfo);
        CollectionPage<DataSet> selectedRowPage;
        getDataProcessingStack().processQuery(selectedRowReqInfo, responseInfo);
        try {
            selectedRowPage = getQueryProcessor().executeOneSizeQuery(selectedRowReqInfo.getQuery(), selectedRowReqInfo.getCriteria());
            getDataProcessingStack().processQueryResult(selectedRowReqInfo, responseInfo, selectedRowPage);
        } catch (N2oUniqueRequestNotFoundException e) {
            return null;
        } catch (N2oRecordNotFoundException e) {
            return null;
        } catch (N2oException e) {
            logger.error("selected-row throw exception for query [" + selectedRowReqInfo.getQuery().getId() + "]", e);
            getDataProcessingStack().processQueryError(selectedRowReqInfo, responseInfo, e);
            return null;
        }
        if (selectedRowPage.getCollection().isEmpty()) return null;//не найдена выделенная запись (selectedId)
        DataSet selectedRow = selectedRowPage.getCollection().iterator().next();
        selectedRow.put(INSERTED_ROW, true);//добавляем признак, что мы вставили запись искусственно
        return selectedRow;
    }

    private boolean findSelectedRow(QueryRequestInfo requestInfo, Collection<DataSet> list) {
        for (DataSet dataset : list) {
            if (dataset.getId() != null && dataset.getId().equals(requestInfo.getSelectedId().toString())) {
                return true;
            }
        }
        return false;
    }

    private QueryRequestInfo createQueryRequestInfoForSelectedRow(QueryRequestInfo requestInfo) {
        QueryRequestInfo selectedRowReqInfo = new QueryRequestInfo();
        selectedRowReqInfo.setUser(requestInfo.getUser());
        selectedRowReqInfo.setQuery(requestInfo.getQuery());
        selectedRowReqInfo.setData(requestInfo.getData());
        selectedRowReqInfo.setUpload(requestInfo.getUpload());
        selectedRowReqInfo.setSize(1);
        selectedRowReqInfo.setCriteria(simpleCriteriaOneRecord(N2oQuery.Field.PK, requestInfo.getSelectedId()));
        return selectedRowReqInfo;
    }


}
