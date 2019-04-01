package net.n2oapp.framework.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.data.streaming.converter.Converter;
import net.n2oapp.data.streaming.converter.EncodingAware;
import net.n2oapp.data.streaming.reader.Reader;
import net.n2oapp.data.streaming.stream.DataInputStream;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.export.format.*;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;
import net.n2oapp.framework.ui.controller.AbstractController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.export.streaming.ClientCollectionPageReader;
import net.n2oapp.framework.export.streaming.LargeCollectionPageReader;
import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import net.n2oapp.framework.ui.exception.UnsupportedFormatException;
import net.n2oapp.framework.ui.servlet.ServletUtil;
import net.n2oapp.properties.StaticProperties;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Контроллер экспорта данных в таблице
 */
public class ExportController extends AbstractController {
    private static final String FILE_NAME = "export_data_";

    private QueryController queryController;

    public ExportController(ObjectMapper objectMapper,
                            MetadataRouter router,
                            ReadCompileBindTerminalPipeline pipeline,
                            DomainProcessor domainProcessor,
                            QueryController queryController) {
        super(objectMapper, router, pipeline, domainProcessor);
        this.queryController = queryController;
    }

    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = ServletUtil.getRequestParamRequired(request, "contentType");
        setFileName(response, FILE_NAME, contentType);
        response.setContentType(MimeTypeConstants.getMimeType(contentType));

        QueryRequestInfo requestInfo = createQueryRequestInfo(request);
        Reader<DataSet> reader = pickupDataSetReader(requestInfo);
        List<N2oDataStreamingUtil.Field> queryFields = getExportFields(request, requestInfo);

        //для excel файлов не врубаем data-streaming
        if (contentType.equals("xlsx")) {
            ExcelUtil.writeAsExcel(reader, queryFields, response.getOutputStream());
        } else {
            Converter<DataSet> converter = findConverter(contentType, queryFields);
            String encoding = ServletUtil.getRequestParam(request, "encoding");
            if (converter instanceof EncodingAware)
                ((EncodingAware) converter).setEncoding(encoding);
            try (DataInputStream<DataSet> inputStream = new DataInputStream<>(converter, reader)) {
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }
    }

    private List<N2oDataStreamingUtil.Field> getExportFields(HttpServletRequest request, QueryRequestInfo requestInfo) {
//todo
        //        if (requestInfo.getWidget().isPresent()) {
//            Widget widget = requestInfo.getWidget().get();
//            if (!(widget instanceof AbstractTable))
//                throw new N2oException("Export is possible only with table widget, but this is " + widget.getClass());
//            AbstractTable table = (AbstractTable) widget;
//            return exportFields(getColumns(request), requestInfo.getQuery().getDisplayFields(),
//                    table.getComponent().getCells(), table.getComponent().getHeaders());
//        } else {
//            return exportFieldsFromQuery(getColumns(request), requestInfo.getQuery().getDisplayFields());
//        }
        return N2oDataStreamingUtil.exportFieldsFromQuery(getColumns(request), requestInfo.getQuery().getDisplayFields());
    }

    private List<String> getColumns(HttpServletRequest request) {
        return null;//todo
    }

    private Reader<DataSet> pickupDataSetReader(QueryRequestInfo requestInfo) {
        QueryResponseInfo responseInfo = new QueryResponseInfo();
        Reader<DataSet> res;
        int exportPage = StaticProperties.getInt("n2o.ui.export.page");
        if (requestInfo.getCriteria().getSize() > exportPage || requestInfo.getCriteria().getSize() == -1) {
            res = new LargeCollectionPageReader(
                    requestInfo,
                    info -> new LinkedList<>(queryController.executeQuery(info, responseInfo).getData().getCollection()),
                    exportPage);
        } else {
            CollectionPage<DataSet> collectionPage = queryController.executeQuery(requestInfo, new QueryResponseInfo()).getData();
            res = new ClientCollectionPageReader(collectionPage);
        }
        return res;
    }

    private Converter<DataSet> findConverter(String fileExt, List<N2oDataStreamingUtil.Field> exportFields)
            throws UnsupportedFormatException, ControllerArgumentException {
        Converter<DataSet> converter;
        switch (fileExt) {
            case "dbf": {
                converter = new DbfConverter(0, exportFields);//todo не понял что значит первый параметр
                break;
            }
            case "xml":
                converter = new XmlConverter(exportFields);
                break;
            case "csv":
                converter = new CsvConverter(exportFields);
                break;
            case "txt":
                converter = new TxtConverter(exportFields);
                break;
            default:
                throw new UnsupportedFormatException("'" + fileExt + "' is unsupported format");
        }
        return converter;
    }

    private void setFileName(HttpServletResponse res, String fileName, String fileExt) {
        res.setHeader("Content-disposition",
                "attachment; filename=" + fileName + "_" + System.currentTimeMillis() + "." + fileExt);
    }

}
