package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;

import java.util.List;
import java.util.Map;

public class ExportController extends AbstractController {

    private final DataController dataController;

    public ExportController(MetadataEnvironment environment,
                            DataController dataController) {
        super(environment);
        this.dataController = dataController;
    }

    public ExportController(MetadataEnvironment environment, MetadataRouter router, DataController dataController) {
        super(environment, router);
        this.dataController = dataController;
    }

    public ExportResponse export(List<DataSet> body, String format, String charset) {
        ExportResponse response = new ExportResponse();

        byte[] file = createScv(null, "export.csv");

        response.setFile(file);
        response.setCharset(charset);
        response.setFormat(format);
        response.setFileName("export.csv");
        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> parameters, UserContext user) {
        return dataController.getData(path, parameters, user);
    }

    private byte[] createScv(String body, String fileName) {
        return null;
    }
}
